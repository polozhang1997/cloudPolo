package top.zb.gatewayservice.config;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import reactor.core.publisher.Mono;
import top.zb.gatewayservice.util.JsonUtils;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * 读取nacos网关配置发布，并监听网关配置文件动态发布
 * @Author: polo
 * @Date: 2021/10/22 9:40
 */
@Slf4j
@ConditionalOnProperty(value = "spring.cloud.nacos.config.enabled", matchIfMissing = true)
public class DynamicRouteListener {

    @Autowired
    ConfigService configService;

    @Autowired
    RouteDefinitionWriter routeDefinitionWriter;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;


    @EventListener(ApplicationReadyEvent.class)
    public void createListener(){
        if (configService == null){
            log.error("【configService】为空");
            return;
        }
        try {
            //通过configService使用配置id和group以及监听器拿到上下文
            String route =configService.getConfigAndSignListener(GatewayConfig.GATEWAY_CONFIG_DATA_ID,GatewayConfig.GATEWAY_CONFIG_GROUP,10000, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                //监听器监听到配置更新
                @Override
                public void receiveConfigInfo(String s) {
                    log.info("【路由更新】监听到路由更新：{}", s);
                    List<RouteDefinition> list = JsonUtils.toList(s, RouteDefinition.class);
                    list.forEach(definition->{
                        //监听器更新
                        updateDefinition(definition);
                    });
                }
            });
            //拿到目前的路由并发布
            JsonUtils.toList(route,RouteDefinition.class).forEach( f ->{
                updateDefinition(f);
            });
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }
    private void updateDefinition(RouteDefinition routeDefinition){
        //删除原路由
        try {
            routeDefinitionWriter.delete(Mono.just(routeDefinition.getId()));
        } catch (Exception e) {
            log.error("删除原路由失败");
        }
        //保存并发布新路由
        try {
            routeDefinitionWriter.save((Mono<RouteDefinition>) Mono.just(routeDefinition).subscribe());
            this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
        } catch (Exception e) {
            log.error("发布路由失败");
        }
    }

}

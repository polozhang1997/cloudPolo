package top.zb.gatewayservice.config;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import top.zb.gatewayservice.util.JsonUtils;
import java.util.List;
import java.util.concurrent.Executor;



/**
 * 读取nacos网关配置发布，并监听网关配置文件动态发布
 * 动态路由：
 * 1.初始化ConfigService（当然也可以创建一个配置类然后再自动注入）
 * 2.通过初始化configService.getConfigAndSignListener(id,group,timeout,listener)拿到当前配置和监听器（@EventListener(ApplicationReadyEvent.class(事件源.class))）
 * 2.1。监听器监听nacos配置，并转化为RouteDefinition，并更新发布
 * 3.拿到当前当前文本路由进行发布更新
 * @author polo
 */
@Slf4j
@ConditionalOnProperty(value = "spring.cloud.nacos.config.enabled", matchIfMissing = true)
@Component
public class DynamicRouteListener {

    @Autowired
    ConfigService configService;

    @Autowired
    RouteDefinitionWriter routeDefinitionWriter;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;


    @EventListener(ApplicationReadyEvent.class)
    public void createListener(){
        if ((configService ) == null){
            log.error("【configService】为空");
            return;
        }
        try {
            //通过configService使用配置id和group拿到路由文本以及编写监听器事件
            String route =configService.getConfigAndSignListener(DynamicGatewayConfig.GATEWAY_CONFIG_DATA_ID,DynamicGatewayConfig.GATEWAY_CONFIG_GROUP,10000, new Listener() {
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
            log.info("现有路由：{}",route);
            //拿到目前的路由并发布
            JsonUtils.toList(route,RouteDefinition.class).forEach(this::updateDefinition);
        } catch (NacosException e) {
            log.warn("请在nacos编写dataId为：{}，group为：{}的相关配置文件",DynamicGatewayConfig.GATEWAY_CONFIG_DATA_ID,DynamicGatewayConfig.GATEWAY_CONFIG_GROUP);
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
            routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
            this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
        } catch (Exception e) {
            log.error("发布路由失败");
            e.printStackTrace();
        }
    }



}

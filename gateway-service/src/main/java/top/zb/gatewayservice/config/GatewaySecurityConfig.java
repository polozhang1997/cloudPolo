package top.zb.gatewayservice.config;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;
import top.zb.gatewayservice.util.JsonUtils;


/**
 * @Author: polo
 * @Date: 2021/10/26 10:26
 */
@Slf4j
@Configuration
@ConditionalOnProperty(value = "polo.security.service",havingValue = "gateway")
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class GatewaySecurityConfig {

    @Autowired
    ConfigService configService;



    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity){


        //读取白名单api，如果nacos没有，后续修改也不能注入到security
        try {
            String config = configService.getConfig(WhiteApiConfig.DATA_ID, WhiteApiConfig.GROUP, 100000);
            White white = JSONObject.parseObject(config, White.class);
            WhiteApiConfig.whiteApiList = white.getWhiteApiListNacos();
            log.info("security白名单api有：{}", Convert.toStr(WhiteApiConfig.whiteApiList));
        } catch (NacosException e) {
            e.printStackTrace();
        }
        ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec = httpSecurity.authorizeExchange();
        //添加白名单api
        WhiteApiConfig.whiteApiList.forEach(
                o -> {
                    authorizeExchangeSpec.pathMatchers(o).permitAll(); });
       httpSecurity
                //资源共享
                .cors().and()
                //防止post异常
                .csrf().disable()
                //取消登录页面
                .formLogin().disable()
                //取消base加密
                .httpBasic().disable()
                //权限不够异常拦截处理
                .exceptionHandling().accessDeniedHandler((exchange,e) ->{
                    log.info("403 Unauthorized");
                    byte[] text = JsonUtils.writeValueAsBytes("权限不足！");
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.FORBIDDEN);
                    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
                    return response.writeWith(Mono.just(response.bufferFactory().wrap(text)));
                }).and()
                //未登录认证拦截处理
                .exceptionHandling().authenticationEntryPoint((exchange,e) ->{
                    log.info("401 Authenticated");
                    byte[] text = JsonUtils.writeValueAsBytes("请求未认证！");
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
                    return response.writeWith(Mono.just(response.bufferFactory().wrap(text)));

                }).and()
                //登出
                .logout().logoutSuccessHandler((exchange,e) -> {
                    byte[] text = JsonUtils.writeValueAsBytes("登出成功！");
                    ServerHttpResponse response = exchange.getExchange().getResponse();
                    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
                    return response.writeWith(Mono.just(response.bufferFactory().wrap(text)));
                });
       return httpSecurity.build();

    }
}

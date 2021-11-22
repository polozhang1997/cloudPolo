package top.zb.gatewayservice.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;
import top.zb.gatewayservice.config.WhiteApiConfig;
import top.zb.gatewayservice.config.matcher.JwtMatcher;
import top.zb.gatewayservice.constant.AuthConstant;
import top.zb.gatewayservice.util.JsonUtils;



/**
 * security核心配置
 * @Author: polo
 * @Date: 2021/10/26 10:26
 */
@Slf4j
@Configuration
@ConditionalOnProperty(value = "polo.security.service",havingValue = "gateway")
@EnableWebFluxSecurity
//@EnableReactiveMethodSecurity  //全局方法安全管理
public class GatewaySecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {

        //白名单放行
       // ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec = httpSecurity.authorizeExchange();

     //   WhiteApiConfig.whiteApiList.forEach(o -> authorizeExchangeSpec.pathMatchers(o).permitAll());
        httpSecurity
                .cors().and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .anyExchange().permitAll()
                //.matchers(new JwtMatcher()).permitAll()
                .and()
                //权限不够异常拦截处理
                .exceptionHandling().accessDeniedHandler((exchange, e) -> {
                    log.info("403 Unauthorized");
                    byte[] text = JsonUtils.writeValueAsBytes("权限不足！");
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.FORBIDDEN);
                    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, AuthConstant.APPLICATION_JSON_UTF8_VALUE);
                    return response.writeWith(Mono.just(response.bufferFactory().wrap(text)));
                }).and()
                //未登录认证拦截处理
                .exceptionHandling().authenticationEntryPoint((exchange, e) -> {
                    log.info("401 Authenticated");
                    byte[] text = JsonUtils.writeValueAsBytes("请求未认证！或请重新登录。");
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, AuthConstant.APPLICATION_JSON_UTF8_VALUE);
                    return response.writeWith(Mono.just(response.bufferFactory().wrap(text)));

                }).and()
                //登出
                .logout().logoutSuccessHandler((exchange, e) -> {
                    byte[] text = JsonUtils.writeValueAsBytes("登出成功！");
                    ServerHttpResponse response = exchange.getExchange().getResponse();
                    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, AuthConstant.APPLICATION_JSON_UTF8_VALUE);
                    return response.writeWith(Mono.just(response.bufferFactory().wrap(text)));
                });
        return httpSecurity.build();

    }


}

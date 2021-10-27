package top.zb.gatewayservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.zb.gatewayservice.config.WhiteApiConfig;
import top.zb.gatewayservice.constant.AuthConstant;
import top.zb.gatewayservice.util.JsonUtils;
import top.zb.gatewayservice.util.NetworkUtils;

/**
 * 网关拦截器
 * @Author: polo
 * @Date: 2021/10/22 15:36
 */
@Slf4j
@Configuration

public class GatewayFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //白名单api直接放行
        if (WhiteApiConfig.whiteIpList.contains(NetworkUtils.getIpAddress(request))){
            return chain.filter(exchange);
        }
        //白名单路由直接放行
        if (WhiteApiConfig.whiteApiList.contains(request.getURI().getPath())){
            return chain.filter(exchange);
        }
        String authStr = request.getHeaders().getFirst(AuthConstant.AUTHORIZATION);
        //未携带token
        if (StringUtils.isEmpty(authStr)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
            byte[] text = JsonUtils.writeValueAsBytes("GATEWAY ERROR 用户未认证，请尝试登出并重新登录！");
            return response.writeWith(Mono.just(response.bufferFactory().wrap(text)));
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

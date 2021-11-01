package top.zb.gatewayservice.config.matcher;

import org.springframework.http.HttpHeaders;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.zb.gatewayservice.constant.AuthConstant;
import top.zb.gatewayservice.util.TokenUtils;


/**
 * 校验header是否有token及是否超时
 * @Author: polo
 * @Date: 2021/11/1 16:00
 */
public class JwtMatcher implements ServerWebExchangeMatcher {
    @Override
    public Mono<MatchResult> matches(ServerWebExchange serverWebExchange) {
        HttpHeaders headers = serverWebExchange.getRequest().getHeaders();
        String token = headers.getFirst(AuthConstant.TOKEN_HEADER);
        if (StringUtils.isEmpty(token) || !token.startsWith(AuthConstant.BEARER)){
            return MatchResult.notMatch();
        }
        if (TokenUtils.isJwtTokenExpired(StringUtils.delete(token,AuthConstant.BEARER))){
            return MatchResult.notMatch();
        }
        return MatchResult.match();
    }
}

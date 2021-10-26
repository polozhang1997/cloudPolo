package top.zb.gatewayservice.util;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;
import top.zb.gatewayservice.filter.AuthUser;

/**
 * @Author: polo
 * @Date: 2021/10/25 18:23
 */
public class UserUtil {

    /**
     * 禁止实例化
     */
    private UserUtil() {}

    public static Mono<Authentication> currentAuthentication() {
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication);
    }

    /**
     * 当前登录用户
     * @return AuthUser
     */
    public static Mono<AuthUser> currentUser() {
        return currentAuthentication()
                .map(Authentication::getPrincipal)
                .cast(AuthUser.class);
    }


}

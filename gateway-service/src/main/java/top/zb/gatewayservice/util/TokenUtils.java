package top.zb.gatewayservice.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import top.poloo.common.com.AuthUser;

import java.io.IOException;
import java.util.Base64;

/**
 * @Author: polo
 * @Date: 2021/11/1 11:32
 */
@Slf4j
public class TokenUtils {
    private static ObjectMapper mapper = new ObjectMapper();

    /***
     * 解析 token 信息
     * @param token jwttoken
     * @return claims对象
     */
    public static AuthUser getAuthUserFromToken(String token) {
        String[] parts = token.split("\\.");

        if (parts.length != 3) {
            log.warn("jwt-token不符合规范。");
            return null;
        }

        try {
            return mapper.readValue(parts[1], AuthUser.class);
        } catch (IOException e) {
            log.warn("", e);
        }

        return null;
    }

    /**
     * 判断jwttoken是否过期
     * @param token jwt
     * @return true or false
     */
    public static boolean isJwtTokenExpired(String token) {
        if (token == null) {
            log.warn("token值null");
            return true;
        }

        String[] parts = token.split("\\.");

        if (parts.length != 3) {
            log.warn("jwt-token不符合规范。");
            return true;
        }

        try {
            byte[] claim = Base64.getUrlDecoder().decode(parts[1]);
            JsonNode node = mapper.readTree(claim);
            long exp = node.findValue("exp").asLong();

            if (exp * 1000 > System.currentTimeMillis()) {
                return false;
            }

            return true;
        } catch (IOException e) {
            log.warn("", e);
        }

        return true;
    }

}

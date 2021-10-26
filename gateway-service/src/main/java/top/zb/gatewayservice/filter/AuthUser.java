package top.zb.gatewayservice.filter;

import lombok.Data;

/**
 * @Author: polo
 * @Date: 2021/10/25 18:15
 */
@Data
public class AuthUser {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名
     */
    private String nickName;
    /**
     * token
     */
    private String jwtToken;
    /**
     * 过期时间
     */
    private Long outTime;

    /**
     * 用户类型
     */
    private String type;
}

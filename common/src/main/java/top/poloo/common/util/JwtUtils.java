package top.poloo.common.util;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import top.poloo.common.constant.TokenConstant;
import java.util.Date;
import java.util.Map;

/**
 * @Author: polo
 * @Date: 2021/10/29 15:51
 */
public class JwtUtils {
    /**
     * @description 生成jwt token
     */
    public static String generateToken(Map<String, Object> claims, String subject, long expiration) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
        // 生成签名密钥
        final Date createdDate = new Date();
        final Date expirationDate = calculateExpirationDate(createdDate, expiration);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(signatureAlgorithm, TokenConstant.SECERT)
                .compact();
    }
    /**
     * 解密Jwt内容
     *
     * @param jwt jwt
     * @return Claims
     */
    public static Claims parseJwtRsa256(String jwt) {
        return Jwts.parser()
                .setSigningKey(TokenConstant.SECERT)
                .parseClaimsJws(jwt).getBody();
    }
    private static Date calculateExpirationDate(Date createdDate, long expiration) {
        return new Date(createdDate.getTime() + expiration);
    }
}

package ybl.lib.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类，负责签发和校验 token。
 *
 * <p>token 中携带三类信息：
 * <ul>
 *   <li>用户 id —— 存放于标准声明 {@code sub}（subject）</li>
 *   <li>用户名 —— 自定义声明 {@code name}</li>
 *   <li>角色 —— 自定义声明 {@code role}</li>
 * </ul>
 *
 * <p>把用户名和角色一并放进 token，是为了让后续的登录拦截器在验签通过后
 * 无需回查数据库就能拿到当前用户的身份信息。
 *
 * <p>密钥与有效期从 {@code application.yml} 的 {@code jwt.secret}、
 * {@code jwt.expire-minutes} 读取。
 */
@Component
public class JwtUtil {

    /** 用户名的声明名 */
    public static final String CLAIM_NAME = "name";

    /** 角色的声明名 */
    public static final String CLAIM_ROLE = "role";

    private final SecretKey key;

    private final long expireMillis;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expire-minutes}") long expireMinutes) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expireMillis = expireMinutes * 60_000L;
    }

    /**
     * 生成 token。
     *
     * @param userId 用户 id，写入标准声明 subject
     * @param name   用户名
     * @param role   角色，READER 或 ADMIN
     * @return 签名后的 token 字符串
     */
    public String generateToken(Long userId, String name, String role) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim(CLAIM_NAME, name)
                .claim(CLAIM_ROLE, role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expireMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 校验 token 并取出其中的声明。
     *
     * <p>校验内容包含签名是否正确、是否已过期、格式是否合法。任一项不通过都会抛异常，
     * 由调用方（通常是登录拦截器）捕获后决定如何响应。
     *
     * <p>用户 id 通过 {@code claims.getSubject()} 取得，它是字符串，需自行转为 Long。
     *
     * @param token 待校验的 token
     * @return token 中的声明
     * @throws io.jsonwebtoken.ExpiredJwtException         token 已过期
     * @throws io.jsonwebtoken.security.SignatureException 签名不匹配，token 被篡改或密钥不一致
     * @throws io.jsonwebtoken.MalformedJwtException       token 格式不合法
     * @throws io.jsonwebtoken.UnsupportedJwtException     token 类型不受支持
     * @throws IllegalArgumentException                    token 为 null 或空字符串
     */
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

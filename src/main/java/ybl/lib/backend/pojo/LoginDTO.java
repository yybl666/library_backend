package ybl.lib.backend.pojo;

import lombok.Data;

/**
 * 登录请求体，对应 {@code POST /users/login} 的 JSON body。
 */
@Data
public class LoginDTO {

    /** 用户名 */
    private String name;

    /** 密码明文，与库中 MD5 哈希比对 */
    private String password;
}

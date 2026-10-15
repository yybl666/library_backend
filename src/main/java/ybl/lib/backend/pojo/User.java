package ybl.lib.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 用户实体，对应数据库表 {@code user}。
 *
 * <p>字段与表列一一对应：{@code id / name / password / email / role}，
 * 列名本身没有下划线，因此不依赖驼峰映射。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    /** 用户ID */
    private Long id;

    /** 用户姓名 */
    private String name;

    /** 密码哈希值 */
    @ToString.Exclude
    private String password;

    /** 邮箱 */
    private String email;

    /** 角色：READER 借书者，ADMIN 图书管理员 */
    private String role;
}

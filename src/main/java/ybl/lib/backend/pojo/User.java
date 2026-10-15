package ybl.lib.backend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    /**
     * 密码哈希值。
     *
     * <p>WRITE_ONLY 表示该字段允许反序列化写入（前端注册时可以传进来），
     * 但序列化成 JSON 时会被忽略，保证任何接口都不会把密码返回给前端。
     *
     * <p>注意 @ToString.Exclude 只管 toString()，与 JSON 序列化是两条独立的路径。
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ToString.Exclude
    private String password;

    /** 邮箱 */
    private String email;

    /** 角色：READER 借书者，ADMIN 图书管理员 */
    private String role;
}

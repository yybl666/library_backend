package ybl.lib.backend.pojo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 注册请求体，对应 {@code POST /users} 的 JSON body。
 *
 * <p>字段上的校验注解在 Controller 收到请求时由 Bean Validation 自动执行，
 * 不通过会抛 MethodArgumentNotValidException，由 GlobalExceptionHandler 转成 400 响应。
 *
 * <p>注意此处没有 role 字段：角色由服务端固定写为 READER，
 * 若接受前端传值，任何人都能把自己注册成图书管理员。
 */
@Data
public class RegisterDTO {

    @NotBlank(message = "用户名不能为空")
    @Size(max = 50, message = "用户名长度不能超过 50")
    private String name;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度需为 6-20 位")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过 100")
    private String email;
}

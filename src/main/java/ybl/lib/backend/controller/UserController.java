package ybl.lib.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ybl.lib.backend.common.Result;
import ybl.lib.backend.pojo.LoginDTO;
import ybl.lib.backend.pojo.RegisterDTO;
import ybl.lib.backend.pojo.User;
import ybl.lib.backend.service.UserService;

/**
 * 用户模块接口。
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 登录。
     *
     * <p>POST /users/login
     *
     * @param dto 请求体 {@code { "name": "...", "password": "..." }}
     * @return 成功时 data 为 token 字符串；用户名不存在返回 404，密码错误返回 400
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginDTO dto) {
        return userService.login(dto);
    }

    /**
     * 注册。
     *
     * <p>POST /users
     *
     * <p>@Valid 触发 RegisterDTO 上的校验注解，任何一项不通过都会抛
     * MethodArgumentNotValidException，由 GlobalExceptionHandler 转成 400 响应。
     *
     * @param dto 请求体 {@code { "name": "...", "password": "...", "email": "..." }}
     * @return 成功时 data 为 null；用户名或邮箱重复返回 409
     */
    @PostMapping
    public Result<Void> register(@Valid @RequestBody RegisterDTO dto) {
        return userService.register(dto);
    }

    /**
     * 查询当前登录用户信息。
     *
     * <p>GET /users/me
     *
     * <p>不需要任何参数：用户 id 由登录拦截器从 token 中解析后存入 ThreadLocal，
     * Service 层直接读取。因此调用方无法指定查询谁，也就不存在越权查他人信息的可能。
     *
     * @return data 为当前用户对象，其中 password 字段不会出现在 JSON 中
     */
    @GetMapping("/me")
    public Result<User> me() {
        return userService.me();
    }
}

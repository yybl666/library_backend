package ybl.lib.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ybl.lib.backend.common.Result;
import ybl.lib.backend.pojo.LoginDTO;
import ybl.lib.backend.pojo.RegisterDTO;
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
}

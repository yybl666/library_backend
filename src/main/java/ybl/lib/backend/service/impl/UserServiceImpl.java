package ybl.lib.backend.service.impl;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ybl.lib.backend.common.Result;
import ybl.lib.backend.mapper.UserMapper;
import ybl.lib.backend.pojo.LoginDTO;
import ybl.lib.backend.pojo.RegisterDTO;
import ybl.lib.backend.pojo.User;
import ybl.lib.backend.service.UserService;
import ybl.lib.backend.util.JwtUtil;
import ybl.lib.backend.util.MD5Utils;
import ybl.lib.backend.util.ThreadLocalUtil;

/**
 * {@link UserService} 的实现。
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    /** 注册用户的默认角色。写死在服务端，不接受前端传值 */
    private static final String DEFAULT_ROLE = "READER";

    private final UserMapper userMapper;

    private final JwtUtil jwtUtil;

    @Override
    public Result<String> login(LoginDTO dto) {
        if (isBlank(dto.getName()) || isBlank(dto.getPassword())) {
            return Result.error(400, "用户名和密码不能为空");
        }

        // 第一步：判断用户名是否存在
        User user = userMapper.findByName(dto.getName());
        if (user == null) {
            return Result.error(404, "用户名不存在");
        }

        // 第二步：把前端传来的明文做同样的 MD5，与库中哈希比对
        if (!user.getPassword().equals(MD5Utils.md5(dto.getPassword()))) {
            return Result.error(400, "密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getName(), user.getRole());
        return Result.success(token);
    }

    @Override
    public Result<Void> register(RegisterDTO dto) {
        // 非空、长度、格式校验已由 @Valid 完成，走到这里说明这些都已通过

        // 去掉首尾空格，避免 "张三" 和 "张三 " 被当成两个不同的用户名
        String name = dto.getName().trim();
        String email = dto.getEmail().trim();

        // 先查重再插入。查重是为了给出「用户名还是邮箱重复」这种具体提示，
        // 表的唯一约束则是并发情况下的最后一道防线。
        if (userMapper.countByName(name) > 0) {
            return Result.error(409, "用户名已存在");
        }
        if (userMapper.countByEmail(email) > 0) {
            return Result.error(409, "邮箱已被占用");
        }

        User user = new User();
        user.setName(name);
        // 密码只存哈希，明文不落库
        user.setPassword(MD5Utils.md5(dto.getPassword()));
        user.setEmail(email);
        user.setRole(DEFAULT_ROLE);

        userMapper.insert(user);
        return Result.success();
    }

    @Override
    public Result<User> me() {
        // /users/me 已被登录拦截器保护，正常情况下这里必然有值。
        // 保留这层判断是为了在拦截器放行规则被改动时返回 401，而不是抛 NullPointerException
        Claims claims = ThreadLocalUtil.get();
        if (claims == null) {
            return Result.error(401, "未登录，请先登录");
        }

        User user = userMapper.findById(Long.valueOf(claims.getSubject()));
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        return Result.success(user);
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}

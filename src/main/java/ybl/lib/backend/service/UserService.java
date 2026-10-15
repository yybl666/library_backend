package ybl.lib.backend.service;

import ybl.lib.backend.common.Result;
import ybl.lib.backend.pojo.LoginDTO;
import ybl.lib.backend.pojo.RegisterDTO;

/**
 * 用户相关的业务接口。
 */
public interface UserService {

    /**
     * 登录：先判断用户名是否存在，再比对密码，通过后签发 token。
     *
     * <p>校验失败不抛异常，直接返回带对应状态码的 {@link Result}，由 Controller 原样透传。
     *
     * @param dto 登录请求，包含用户名和密码明文
     * @return 成功时 data 为 token 字符串；失败时 data 为 null，
     *         code 为 400（参数为空或密码错误）或 404（用户名不存在）
     */
    Result<String> login(LoginDTO dto);

    /**
     * 注册：借书者自助注册，角色由服务端固定写为 READER。
     *
     * <p>参数的非空、长度、格式校验由 Bean Validation 在进入本方法前完成，
     * 本方法只负责用户名与邮箱的查重和落库。
     *
     * @param dto 注册请求，包含用户名、密码明文、邮箱
     * @return 成功时 data 为 null；失败时 code 为 409（用户名或邮箱已被占用）
     */
    Result<Void> register(RegisterDTO dto);
}

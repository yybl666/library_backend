package ybl.lib.backend.service;

import ybl.lib.backend.common.Result;
import ybl.lib.backend.pojo.LoginDTO;
import ybl.lib.backend.pojo.RegisterDTO;
import ybl.lib.backend.pojo.UpdatePasswordDTO;
import ybl.lib.backend.pojo.User;

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

    /**
     * 查询当前登录用户的信息。
     *
     * <p>用户 id 取自 {@link ybl.lib.backend.util.ThreadLocalUtil}，由登录拦截器在
     * 校验 token 通过后写入，因此本方法不需要参数，也就不存在查别人信息的可能。
     *
     * @return 成功时 data 为当前用户对象（password 字段不参与 JSON 序列化）；
     *         用户已被删除时 code 为 404
     */
    Result<User> me();

    /**
     * 修改当前登录用户的密码。
     *
     * <p>要改的是谁由 token 决定，从 {@link ybl.lib.backend.util.ThreadLocalUtil} 取，
     * 不从请求体接收用户 id，因此不存在改他人密码的可能。
     *
     * <p>会先校验原密码，再判断新密码是否与原密码相同。
     *
     * @param dto 修改密码请求，包含原密码和新密码明文
     * @return 成功时 data 为 null；失败时 code 为 400（原密码错误或新密码不合规）、
     *         401（未登录）、404（用户不存在）
     */
    Result<Void> changePassword(UpdatePasswordDTO dto);
}

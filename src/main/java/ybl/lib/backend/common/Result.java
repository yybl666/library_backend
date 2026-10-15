package ybl.lib.backend.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应结果包装类。
 *
 * <p>Controller 一律返回该类型，保证前端拿到的 JSON 结构恒定：
 * <pre>
 * { "code": 200, "msg": "操作成功", "data": {...} }
 * </pre>
 *
 * <p>泛型 {@code T} 用于标注 data 的实际类型，调用处无需强制转换，
 * 例如 {@code Result<User> r = Result.success(user);}。
 *
 * @param <T> 业务数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    /** 状态码：200 成功，500 失败 */
    private Integer code;

    /** 提示信息 */
    private String msg;

    /** 业务数据，失败时通常为 null */
    private T data;

    /** 成功，不带数据，用于删除、修改这类没有返回值的操作 */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    /** 成功，携带数据，用于查询操作 */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    /** 失败 */
    public static <T> Result<T> error() {
        return new Result<>(500, "操作失败", null);
    }
//添加具体信息接口：-
// 200：请求处理成功
//- 400：参数错误、请求不合法
//- 404：资源不存在（比如查询一个不存在的 ID）
//- 500：服务器内部错误
    public static <T> Result<T> error(Integer code,String msg) {
        return new Result<>(code, msg, null);
    }
}

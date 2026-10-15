package ybl.lib.backend.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器。
 *
 * <p>把抛出的异常统一转成 {@link Result} 结构，保证任何情况下前端收到的 JSON 形状一致。
 * 没有这一层的话，校验失败会返回 Spring 默认的错误体，结构与成功响应完全不同。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Bean Validation 校验失败。
     *
     * <p>多个字段同时不通过时只返回第一条，避免把整份错误清单抛给前端。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("请求参数不合法");
        return Result.error(400, msg);
    }

    /** 请求体缺失或 JSON 格式错误，例如请求体为空、字段类型对不上 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleNotReadableException(HttpMessageNotReadableException e) {
        return Result.error(400, "请求体格式不正确");
    }

    /**
     * 兜底处理：未预料到的异常一律返回 500。
     *
     * <p>异常堆栈只写日志，不放进响应体，避免把表名、SQL、类路径等信息暴露给前端。
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("未处理的异常", e);
        return Result.error(500, "服务器内部错误");
    }
}

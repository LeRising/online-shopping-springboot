package com.mall.common.exception;

import com.mall.common.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * <p>统一捕获并处理所有异常，返回标准的 {@link R} 响应格式。</p>
 *
 * <p>处理的异常类型：</p>
 * <ul>
 *   <li>{@link BusinessException} - 业务异常，返回对应的 code 和 message</li>
 *   <li>{@link MethodArgumentNotValidException} - 参数校验失败（@Valid 注解）</li>
 *   <li>{@link BindException} - 参数绑定失败</li>
 *   <li>{@link Exception} - 兜底处理，返回系统内部错误</li>
 * </ul>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     *
     * @param e 业务异常
     * @return 包含错误码和错误信息的响应
     */
    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return R.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验失败（@Valid 注解触发）
     *
     * @param e 参数校验异常
     * @return 包含所有字段错误信息的响应（多个字段用 ; 连接）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<Void> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("参数校验失败");
        return R.fail(400, message);
    }

    /**
     * 处理参数绑定失败
     *
     * @param e 参数绑定异常
     * @return 包含字段错误信息的响应
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<Void> handleBindException(BindException e) {
        String message = e.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("参数绑定失败");
        return R.fail(400, message);
    }

    /**
     * 兜底处理所有未捕获的异常
     *
     * @param e 系统异常
     * @return 包含"系统内部错误"的响应
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<Void> handleException(Exception e) {
        log.error("系统异常: ", e);
        return R.fail("系统内部错误");
    }
}

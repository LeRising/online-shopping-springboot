package com.mall.common.exception;

import lombok.Getter;

/**
 * 自定义业务异常类
 *
 * <p>用于在业务逻辑中抛出异常，由 {@link GlobalExceptionHandler} 统一捕获并返回标准响应。</p>
 *
 * <p>使用示例：</p>
 * <pre>
 * if (user == null) {
 *     throw new BusinessException("用户不存在");
 * }
 *
 * if (stock < 0) {
 *     throw new BusinessException(400, "库存不足");
 * }
 * </pre>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Getter
public class BusinessException extends RuntimeException {

    /** 错误状态码 */
    private final int code;

    /**
     * 构造业务异常（默认状态码 500）
     *
     * @param message 错误信息
     */
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    /**
     * 构造业务异常（自定义状态码）
     *
     * @param code    错误状态码
     * @param message 错误信息
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}

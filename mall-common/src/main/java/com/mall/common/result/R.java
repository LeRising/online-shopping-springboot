package com.mall.common.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应封装
 *
 * <p>所有接口返回数据的标准格式，包含状态码、提示信息和响应数据。</p>
 *
 * <p>使用示例：</p>
 * <pre>
 * // 成功响应
 * return R.ok(userDTO);
 *
 * // 失败响应
 * return R.fail("用户不存在");
 *
 * // 自定义状态码
 * return R.fail(401, "请先登录");
 * </pre>
 *
 * @param <T> 响应数据类型
 * @author risinglee
 * @since 1.0.0
 */
@Data
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 状态码：200-成功，其他-失败 */
    private int code;

    /** 提示信息 */
    private String msg;

    /** 响应数据 */
    private T data;

    /**
     * 返回成功响应（无数据）
     *
     * @param <T> 数据类型
     * @return 成功响应
     */
    public static <T> R<T> ok() {
        return ok(null);
    }

    /**
     * 返回成功响应（带数据）
     *
     * @param data 响应数据
     * @param <T>  数据类型
     * @return 成功响应
     */
    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMsg("success");
        r.setData(data);
        return r;
    }

    /**
     * 返回失败响应（默认状态码 500）
     *
     * @param msg 错误信息
     * @param <T> 数据类型
     * @return 失败响应
     */
    public static <T> R<T> fail(String msg) {
        return fail(500, msg);
    }

    /**
     * 返回失败响应（自定义状态码）
     *
     * @param code 状态码
     * @param msg  错误信息
     * @param <T>  数据类型
     * @return 失败响应
     */
    public static <T> R<T> fail(int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }
}

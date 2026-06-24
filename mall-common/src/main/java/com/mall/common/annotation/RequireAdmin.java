package com.mall.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 管理员权限注解
 *
 * <p>标注在 Controller 方法上，表示该接口需要管理员权限。</p>
 *
 * <p>使用示例：</p>
 * <pre>
 * {@literal @}RequireAdmin
 * {@literal @}GetMapping("/admin/users")
 * public R&lt;List&lt;UserDTO&gt;&gt; listUsers() {
 *     // 只有管理员可以访问
 * }
 * </pre>
 *
 * @author risinglee
 * @since 1.0.0
 * @see com.mall.common.config.AdminInterceptor
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireAdmin {
}

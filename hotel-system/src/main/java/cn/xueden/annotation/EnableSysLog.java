package cn.xueden.annotation;

import java.lang.annotation.*;

/**功能描述:系统日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableSysLog {
    String value() default "";
}

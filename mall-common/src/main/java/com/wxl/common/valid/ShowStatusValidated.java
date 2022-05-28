package com.wxl.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * JSR303自定义校验注解: showStatus逻辑删除字段校验
 * validatedBy可以指定多个校验器, 自动适配
 *
 * @author wangxl
 * @since 2022/5/28 15:22
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {ShowStatusValidator.class})
public @interface ShowStatusValidated {
    /*******************以下三个默认copy默认注解*********************/
    String message() default "{com.wxl.common.valid.ShowStatusValidated.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 属性值, 默认只能取: 0, 1
     */
    int[] value() default {};
}

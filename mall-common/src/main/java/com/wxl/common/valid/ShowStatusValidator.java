package com.wxl.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * Validator
 *
 * @author wangxl
 * @since 2022/5/28 15:32
 */
public class ShowStatusValidator implements ConstraintValidator<ShowStatusValidated, Integer> {

    private final Set<Integer> set = new HashSet<>();

    /**
     * 初始化方法, 会将该该注解使用的实例返回给我们
     *
     * @param constraintAnnotation constraintAnnotation
     */
    @Override
    public void initialize(ShowStatusValidated constraintAnnotation) {
        int[] value = constraintAnnotation.value();
        for (int i : value) {
            set.add(i);
        }
    }

    /**
     * 判断是否校验成功
     *
     * @param value   目标注解实例提交的需要校验的值
     * @param context 上下文环境信息
     * @return boolean值
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return set.contains(value);
    }
}

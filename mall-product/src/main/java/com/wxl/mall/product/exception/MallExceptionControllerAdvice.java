package com.wxl.mall.product.exception;

import com.wxl.common.exception.BizCodeEnum;
import com.wxl.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * feature: 集中处理所有异常
 *
 * ControllerAdvice:作用就是用来集中处理异常的
 * 1、basePackages说明用来处理哪些Controller异常的
 * 2、@RestControllerAdvice = @ControllerAdvice + @ResponseBody
 *
 * @author wangxl
 * @since 2022/5/28 14:32
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.wxl.mall.product.controller")
public class MallExceptionControllerAdvice {

    /**
     * 处理JSR303校验不过的异常
     *
     * @param e MethodArgumentNotValidException
     * @return message
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题: {}, 异常类型: {}", e.getMessage(), e.getClass());

        BindingResult bindingResult = e.getBindingResult();

        Map<String, String> map = new HashMap<>();
        // 获取校验的错误结果
        bindingResult.getFieldErrors().forEach(fieldError -> {
            // 1.错误提示
            String message = fieldError.getDefaultMessage();
            // 2.错误的属性的名字
            String fieldName = fieldError.getField();
            map.put(fieldName, message);
        });

        return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(), BizCodeEnum.VALID_EXCEPTION.getMsg()).put("data", map);
    }


//    /**
//     * 统一兜底处理所有异常
//     *
//     * @param throwable allException
//     * @return message
//     */
//    @ExceptionHandler(value = Throwable.class)
//    public R handleException(Throwable throwable) {
//        log.error("异常处理: {}", throwable.getMessage());
//
//        return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), BizCodeEnum.UNKNOWN_EXCEPTION.getMsg());
//    }
}

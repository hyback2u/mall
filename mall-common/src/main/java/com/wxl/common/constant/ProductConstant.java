package com.wxl.common.constant;

/**
 * 商品相关的常量
 *
 * @author wangxl
 * @since 2022/5/31 20:20
 */
public class ProductConstant {

    /**
     * 这里销售属性, 和基本属性是共用一张表的, 只是通过一个字段值区分, 这里定义枚举表示
     */
    public enum AttrEnum {
        ATTR_TYPE_BASE(1, "基本属性"),
        ATTR_TYPE_SALE(0, "销售属性");

        // todo 这里自动装箱操作, 定义成int合适还是Integer合适
        private final Integer code;
        private final String msg;

        AttrEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}

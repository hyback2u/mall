package com.wxl.mall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Spu基本属性VO
 *
 * @author wangxl
 * @since 2022/6/16 23:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SpuItemAttrGroupVO {
    private String groupName;
//    private List<SpuBaseAttrVO> attrs;
    private List<Attr> attrs;
}

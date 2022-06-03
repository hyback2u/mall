package com.wxl.mall.product.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Auto-generated: 2022-06-03 11:2:17
 *
 * @author bejson.com (i@bejson.com)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SPUSaveVO {
    private String spuName;
    private String spuDescription;
    private Long catalogId;
    private Long brandId;
    private BigDecimal weight;
    private int publishStatus;
    private List<String> decript;
    private List<String> images;
    private Bounds bounds;
    private List<BaseAttrs> baseAttrs;
    private List<Skus> skus;
}

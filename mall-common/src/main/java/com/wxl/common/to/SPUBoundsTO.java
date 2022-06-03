package com.wxl.common.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 保存SPU成长值、积分的TO
 *
 * @author wangxl
 * @since 2022/6/3 14:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SPUBoundsTO {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}

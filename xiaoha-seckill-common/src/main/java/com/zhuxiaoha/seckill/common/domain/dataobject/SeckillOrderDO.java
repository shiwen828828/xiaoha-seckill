package com.zhuxiaoha.seckill.common.domain.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * 秒杀订单表 DO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeckillOrderDO {
    private Long id;

    private Long userId;

    private Long activityId;

    private Long goodsId;

    private String orderNo;

    private BigDecimal seckillPrice;

    private String goodsName;

    private String goodsImg;

    private Integer status;

    private LocalDateTime expireTime;

    private Integer isDeleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
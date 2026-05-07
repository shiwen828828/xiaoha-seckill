package com.zhuxiaoha.seckill.common.domain.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * @Author: 猪小哈
 * @Date: 2026/5/6 10:00
 * @Version: v1.0.0
 * @Description: 商品轮播图表 DO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodsDetailDO {
    private Long id;

    private Long goodsId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String detailContent;

}
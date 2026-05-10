package com.zhuxiaoha.seckill.order.service;

import com.zhuxiaoha.seckill.common.utils.Response;
import com.zhuxiaoha.seckill.order.model.vo.DoSeckillReqVO;
import com.zhuxiaoha.seckill.order.model.vo.DoSeckillRspVO;

/**
 * @Author: 猪小哈
 * @Date: 2026-05-10
 * @Version: v1.0.0
 * @Description: 订单模块业务
 **/
public interface OrderService {

    /**
     * 秒杀下单
     *
     * @param reqVO
     * @return
     */
    Response<DoSeckillRspVO> doSeckill(DoSeckillReqVO reqVO);
}

package com.zhuxiaoha.seckill.order.controller;

import com.zhuxiaoha.seckill.common.aspect.ApiOperationLog;
import com.zhuxiaoha.seckill.common.utils.Response;
import com.zhuxiaoha.seckill.order.model.vo.DoSeckillReqVO;
import com.zhuxiaoha.seckill.order.model.vo.DoSeckillRspVO;
import com.zhuxiaoha.seckill.order.service.OrderService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 猪小哈
 * @Date: 2026-05-10
 * @Version: v1.0.0
 * @Description: 订单模块
 **/
@RestController
@RequestMapping("/seckill/order")
@Slf4j
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 秒杀下单
     *
     * @param reqVO
     * @return
     */
    @PostMapping
    @ApiOperationLog(description = "秒杀下单")
    public Response<DoSeckillRspVO> doSeckill(@RequestBody @Validated DoSeckillReqVO reqVO) {
        return orderService.doSeckill(reqVO);
    }
}

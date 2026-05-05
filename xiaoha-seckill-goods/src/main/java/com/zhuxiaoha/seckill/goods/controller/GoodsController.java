package com.zhuxiaoha.seckill.goods.controller;

import com.zhuxiaoha.seckill.common.aspect.ApiOperationLog;
import com.zhuxiaoha.seckill.common.utils.Response;
import com.zhuxiaoha.seckill.goods.model.vo.FindSeckillGoodsListReqVO;
import com.zhuxiaoha.seckill.goods.model.vo.FindSeckillGoodsListRspVO;
import com.zhuxiaoha.seckill.goods.service.GoodsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: 猪小哈
 * @Date: 2026-05-06
 * @Version: v1.0.0
 * @Description: 商品模块
 **/
@RestController
@RequestMapping("/seckill/goods")
@Slf4j
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    /**
     * 查询秒杀商品列表
     *
     * @param reqVO
     * @return
     */
    @PostMapping("/list")
    @ApiOperationLog(description = "查询秒杀商品列表")
    public Response<List<FindSeckillGoodsListRspVO>> getSeckillGoodsList(@RequestBody @Validated FindSeckillGoodsListReqVO reqVO) {
        return goodsService.findSeckillGoodsList(reqVO);
    }
}

package com.zhuxiaoha.seckill.goods.service;

import com.zhuxiaoha.seckill.common.utils.Response;
import com.zhuxiaoha.seckill.goods.model.vo.FindSeckillGoodsListReqVO;
import com.zhuxiaoha.seckill.goods.model.vo.FindSeckillGoodsListRspVO;

import java.util.List;

/**
 * @Author: 猪小哈
 * @Date: 2026-05-06
 * @Version: v1.0.0
 * @Description: 商品模块业务
 **/
public interface GoodsService {
    /**
     * 查询秒杀商品列表
     *
     * @param reqVO
     * @return
     */
    Response<List<FindSeckillGoodsListRspVO>> findSeckillGoodsList(FindSeckillGoodsListReqVO reqVO);
}

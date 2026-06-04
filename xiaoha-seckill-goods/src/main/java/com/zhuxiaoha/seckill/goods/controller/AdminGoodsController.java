package com.zhuxiaoha.seckill.goods.controller;

import com.zhuxiaoha.seckill.common.aspect.ApiOperationLog;
import com.zhuxiaoha.seckill.common.utils.Response;
import com.zhuxiaoha.seckill.goods.model.vo.PreheatActivityCacheReqVO;
import com.zhuxiaoha.seckill.goods.service.GoodsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 猪小哈
 * @Date: 2026-06-04
 * @Version: v1.0.0
 * @Description: 管理端 - 商品管理
 **/
@RestController
@RequestMapping("/admin/seckill/goods")
@Slf4j
public class AdminGoodsController {

    @Resource
    private GoodsService goodsService;

    @PostMapping("/cache/preheat")
    @ApiOperationLog(description = "手动预热商品缓存")
    public Response<?> preheatCache(@RequestBody @Validated PreheatActivityCacheReqVO reqVO) {
        return goodsService.preheatActivityGoods(reqVO.getActivityId());
    }

}

package com.zhuxiaoha.seckill.goods.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.zhuxiaoha.seckill.common.constant.RedisKeyConstants;
import com.zhuxiaoha.seckill.common.domain.dataobject.*;
import com.zhuxiaoha.seckill.common.domain.mapper.*;
import com.zhuxiaoha.seckill.common.enums.ActivityStatusEnum;
import com.zhuxiaoha.seckill.common.enums.ResponseCodeEnum;
import com.zhuxiaoha.seckill.common.exception.BizException;
import com.zhuxiaoha.seckill.common.utils.JsonUtils;
import com.zhuxiaoha.seckill.common.utils.Response;
import com.zhuxiaoha.seckill.goods.model.vo.FindSeckillGoodsDetailReqVO;
import com.zhuxiaoha.seckill.goods.model.vo.FindSeckillGoodsDetailRspVO;
import com.zhuxiaoha.seckill.goods.model.vo.FindSeckillGoodsListReqVO;
import com.zhuxiaoha.seckill.goods.model.vo.FindSeckillGoodsListRspVO;
import com.zhuxiaoha.seckill.goods.service.GoodsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: 猪小哈
 * @Date: 2026-05-06
 * @Version: v1.0.0
 * @Description: 商品模块业务
 **/
@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private SeckillGoodsDOMapper seckillGoodsDOMapper;

    @Resource
    private SeckillActivityDOMapper seckillActivityDOMapper;

    @Resource
    private GoodsDOMapper goodsDOMapper;

    @Resource
    private GoodsImgDOMapper goodsImgDOMapper;

    @Resource
    private GoodsDetailDOMapper goodsDetailDOMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 查询秒杀商品列表
     *
     * @param reqVO
     * @return
     */
    @Override
    public Response<List<FindSeckillGoodsListRspVO>> findSeckillGoodsList(FindSeckillGoodsListReqVO reqVO) {
        // 活动 ID
        Long activityId = reqVO.getActivityId();
        log.info("==> 查询秒杀商品列表, activityId: {}", activityId);

        // 构建 Redis 缓存 Key
        String redisKey = RedisKeyConstants.GOODS_LIST_PREFIX + activityId;

        // 先查 Redis 缓存
        String redisJsonValue = stringRedisTemplate.opsForValue().get(redisKey);

        // 若缓存不为空
        if (StrUtil.isNotEmpty(redisJsonValue)) {
            log.info("==> 命中商品列表缓存, redisKey: {}", redisKey);

            // 缓存命中
            // 手动将 String 字符串，反序列化为商品列表
            List<FindSeckillGoodsListRspVO> cachedList = JsonUtils.parseArray(redisJsonValue, FindSeckillGoodsListRspVO.class);

            // 设置库存字段值（因为库存变化频繁，需要从数据库查最新的）
            supplementStock(cachedList, activityId);

            // 实时重新计算活动状态
            FindSeckillGoodsListRspVO first = cachedList.get(0);
            ActivityStatusEnum activityStatusEnum = calculateActivityStatus(first.getBeginTime(), first.getEndTime());
            cachedList.forEach(item -> item.setActivityStatus(activityStatusEnum.getStatus()));

            return Response.success(cachedList);
        }
        // 1. 查询活动信息
        SeckillActivityDO activityDO = seckillActivityDOMapper.selectByPrimaryKey(activityId);
        if (Objects.isNull(activityDO)) {
            throw new BizException(ResponseCodeEnum.SECKILL_ACTIVITY_NOT_EXIST);
        }

        // 2. 根据活动 ID 查询该活动下的所有秒杀商品
        List<SeckillGoodsDO> seckillGoodsDOS = seckillGoodsDOMapper.selectByActivityId(activityId);
        if (CollUtil.isEmpty(seckillGoodsDOS)) {
            log.info("==> 该活动下暂无秒杀商品, activityId: {}", activityId);
            return Response.success(Collections.emptyList());
        }

        // 3. 批量查询关联的商品信息（目的是为了获取对应秒杀商品的原价）
        List<Long> goodsIds = seckillGoodsDOS.stream().map(SeckillGoodsDO::getGoodsId).collect(Collectors.toList());

        // 一次性批量查询所有商品，提升查询性能
        List<GoodsDO> goodsDOS = goodsDOMapper.selectByIds(goodsIds);

        // 将商品 ID 和商品信息映射为 Map，方便后续查找
        Map<Long, GoodsDO> goodsMap = goodsDOS.stream().collect(Collectors.toMap(GoodsDO::getId, goodsDO -> goodsDO));

        // 4. 计算活动状态（基于当前时间动态判断）
        ActivityStatusEnum activityStatusEnum = calculateActivityStatus(activityDO);

        // 5. 组装响应数据
        List<FindSeckillGoodsListRspVO> rspVOS = new ArrayList<>();
        for (SeckillGoodsDO seckillGoodsDO : seckillGoodsDOS) {
            FindSeckillGoodsListRspVO rspVO = new FindSeckillGoodsListRspVO();
            rspVO.setId(seckillGoodsDO.getId());
            rspVO.setGoodsId(seckillGoodsDO.getGoodsId());  // 设置商品 ID
            rspVO.setActivityId(seckillGoodsDO.getActivityId());
            rspVO.setSeckillTitle(seckillGoodsDO.getSeckillTitle());
            rspVO.setSeckillImg(seckillGoodsDO.getSeckillImg());
            rspVO.setSeckillPrice(seckillGoodsDO.getSeckillPrice());
            rspVO.setSeckillTotal(seckillGoodsDO.getSeckillTotal());
            rspVO.setSeckillStock(seckillGoodsDO.getSeckillStock());
            rspVO.setActivityStatus(activityStatusEnum.getStatus());
            rspVO.setBeginTime(activityDO.getBeginTime());
            rspVO.setEndTime(activityDO.getEndTime());

            // 设置商品原价
            GoodsDO goodsDO = goodsMap.get(seckillGoodsDO.getGoodsId());
            if (Objects.nonNull(goodsDO)) {
                rspVO.setGoodsPrice(goodsDO.getGoodsPrice());
            }

            rspVOS.add(rspVO);
        }

        // 将商品列表写入 Redis 缓存
        log.info("==> 商品列表缓存未命中，将数据写入 Redis, redisKey: {}", redisKey);
        stringRedisTemplate.opsForValue().set(redisKey, JsonUtils.toJsonString(rspVOS), RedisKeyConstants.GOODS_LIST_TTL_MINUTES, TimeUnit.MINUTES);

        return Response.success(rspVOS);
    }

    /**
     * 查询秒杀商品详情
     *
     * @param reqVO
     * @return
     */
    @Override
    public Response<FindSeckillGoodsDetailRspVO> findSeckillGoodsDetail(FindSeckillGoodsDetailReqVO reqVO) {
        // 商品 ID
        Long goodsId = reqVO.getGoodsId();
        // 活动 ID
        Long activityId = reqVO.getActivityId();
        log.info("==> 查询秒杀商品详情, goodsId: {}, activityId: {}", goodsId, activityId);

        // 1. 根据活动 ID 查询活动信息，校验活动是否存在
        SeckillActivityDO activityDO = seckillActivityDOMapper.selectByPrimaryKey(activityId);
        if (Objects.isNull(activityDO)) {
            throw new BizException(ResponseCodeEnum.SECKILL_ACTIVITY_NOT_EXIST);
        }

        // 2. 根据活动 ID 和商品 ID 查询秒杀商品
        SeckillGoodsDO seckillGoodsDO = seckillGoodsDOMapper.selectByActivityIdAndGoodsId(activityId, goodsId);
        if (Objects.isNull(seckillGoodsDO)) {
            throw new BizException(ResponseCodeEnum.SECKILL_GOODS_NOT_EXIST);
        }

        // 3. 根据 goodsId 查询商品基本信息, 如商品名称、原价
        GoodsDO goodsDO = goodsDOMapper.selectByPrimaryKey(goodsId);

        // 4. 根据 goodsId 查询商品轮播图列表
        List<GoodsImgDO> goodsImgDOS = goodsImgDOMapper.selectByGoodsId(goodsId);

        List<String> goodsImgs = null;
        if (CollUtil.isNotEmpty(goodsImgDOS)) {
            goodsImgs = goodsImgDOS.stream().map(GoodsImgDO::getImgUrl).toList();
        }

        // 5. 根据 goodsId 查询商品详情 HTML
        GoodsDetailDO goodsDetailDO = goodsDetailDOMapper.selectByGoodsId(goodsId);

        // 6. 计算活动状态
        ActivityStatusEnum activityStatusEnum = calculateActivityStatus(activityDO);

        // 7. 组装响应数据
        FindSeckillGoodsDetailRspVO rspVO = FindSeckillGoodsDetailRspVO.builder().id(seckillGoodsDO.getId()).goodsId(goodsDO.getId()).activityId(seckillGoodsDO.getActivityId()).seckillPrice(seckillGoodsDO.getSeckillPrice()).seckillTotal(seckillGoodsDO.getSeckillTotal()).seckillStock(seckillGoodsDO.getSeckillStock()).activityStatus(activityStatusEnum.getStatus()).beginTime(activityDO.getBeginTime()).endTime(activityDO.getEndTime()).goodsImgs(goodsImgs).build();

        // 设置商品基本信息
        if (Objects.nonNull(goodsDO)) {
            rspVO.setGoodsName(goodsDO.getGoodsName());
            rspVO.setGoodsPrice(goodsDO.getGoodsPrice());
        }

        // 设置商品详情 HTML
        if (Objects.nonNull(goodsDetailDO)) {
            rspVO.setDetailContent(goodsDetailDO.getDetailContent());
        }

        return Response.success(rspVO);
    }

    /**
     * 根据当前时间动态计算活动状态
     *
     * @param activityDO
     * @return
     */
    private ActivityStatusEnum calculateActivityStatus(SeckillActivityDO activityDO) {
        return calculateActivityStatus(activityDO.getBeginTime(), activityDO.getEndTime());
    }

    /**
     * 根据当前时间动态计算活动状态 (重载方法)
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    private ActivityStatusEnum calculateActivityStatus(LocalDateTime beginTime, LocalDateTime endTime) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(beginTime)) { // 当前时间早于活动开始时间，则活动未开始
            return ActivityStatusEnum.NOT_STARTED;
        } else if (now.isAfter(endTime)) { // 当前时间晚于活动结束时间，则活动已结束
            return ActivityStatusEnum.ENDED;
        } else { // 活动进行中
            return ActivityStatusEnum.ING;
        }
    }

    /**
     * 实时补充库存字段（库存变化频繁，每次从数据库实时查询）
     *
     * @param goodsList  缓存中的商品列表
     * @param activityId 活动 ID
     */
    private void supplementStock(List<FindSeckillGoodsListRspVO> goodsList, Long activityId) {
        // 根据活动 ID 查询秒杀商品的实时库存（仅查 id 和 seckill_stock 字段，减少 IO 开销）
        List<SeckillGoodsDO> seckillGoodsDOS = seckillGoodsDOMapper.selectStockByActivityId(activityId);

        // 构建 ID -> 库存的映射
        Map<Long, Integer> stockMap = seckillGoodsDOS.stream().collect(Collectors.toMap(SeckillGoodsDO::getId, SeckillGoodsDO::getSeckillStock));

        // 补充库存到缓存中的商品列表
        for (FindSeckillGoodsListRspVO rspVO : goodsList) {
            Integer stock = stockMap.get(rspVO.getId());
            if (Objects.nonNull(stock)) {
                rspVO.setSeckillStock(stock);
            }
        }
    }
}

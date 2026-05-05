package com.zhuxiaoha.seckill.common.domain.mapper;

import com.zhuxiaoha.seckill.common.domain.dataobject.SeckillActivityDO;


public interface SeckillActivityDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SeckillActivityDO record);

    int insertSelective(SeckillActivityDO record);

    SeckillActivityDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SeckillActivityDO record);

    int updateByPrimaryKey(SeckillActivityDO record);

}
package com.zhuxiaoha.seckill.app;

import com.zhuxiaoha.seckill.common.domain.dataobject.UserDO;
import com.zhuxiaoha.seckill.common.domain.mapper.UserDOMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

/**
 * @Author: 猪小哈
 * @Date: 2026-04-17
 * @Version: v1.0.0
 * @Description: 用户测试类
 **/
@SpringBootTest
public class UserTests {

    @Resource
    private UserDOMapper userDOMapper;


    /**
     * 想
     * 添加一条用户记录
     */
    @Test
    void testInsertUser() {
        userDOMapper.insert(UserDO.builder().nickname("猪小哈").password("123456").mobile("18019988888").status(1).createTime(LocalDateTime.now()).updateTime(LocalDateTime.now()).build());
    }


}

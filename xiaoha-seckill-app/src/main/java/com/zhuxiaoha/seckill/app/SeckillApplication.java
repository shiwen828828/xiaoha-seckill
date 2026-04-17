package com.zhuxiaoha.seckill.app;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan({"com.zhuxiaoha.seckill.*"}) // 多模块项目中，必需手动指定扫描 com.quanxiaoha.seckill 包下面的所有类
@MapperScan("com.zhuxiaoha.seckill.common.domain.mapper")
public class SeckillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
    }
}

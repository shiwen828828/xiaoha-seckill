package com.zhuxiaoha.seckill.user.controller;

import com.zhuxiaoha.seckill.common.aspect.ApiOperationLog;
import com.zhuxiaoha.seckill.common.utils.Response;
import com.zhuxiaoha.seckill.user.model.vo.RegisterUserReqVO;
import com.zhuxiaoha.seckill.user.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 猪小哈
 * @Date: 2026-04-18
 * @Version: v1.0.0
 * @Description: 用户接口
 **/
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @ApiOperationLog(description = "用户注册")
    public Response<?> register(@Validated @RequestBody RegisterUserReqVO registerUserReqVO) {
        return userService.register(registerUserReqVO);
    }

}


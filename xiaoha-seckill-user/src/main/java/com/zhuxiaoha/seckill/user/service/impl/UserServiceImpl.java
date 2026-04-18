package com.zhuxiaoha.seckill.user.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.zhuxiaoha.seckill.common.domain.dataobject.UserDO;
import com.zhuxiaoha.seckill.common.domain.mapper.UserDOMapper;
import com.zhuxiaoha.seckill.common.enums.ResponseCodeEnum;
import com.zhuxiaoha.seckill.common.exception.BizException;
import com.zhuxiaoha.seckill.common.utils.Response;
import com.zhuxiaoha.seckill.user.enums.UserStatusEnum;
import com.zhuxiaoha.seckill.user.model.vo.RegisterUserReqVO;
import com.zhuxiaoha.seckill.user.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Author: 猪小哈
 * @Date: 2026-04-18
 * @Version: v1.0.0
 * @Description: 用户业务实现类
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserDOMapper userDOMapper;

    /**
     * 用户注册
     *
     * @param registerUserReqVO
     * @return
     */
    @Override
    public Response<?> register(RegisterUserReqVO registerUserReqVO) {
        String mobile = registerUserReqVO.getMobile();
        String password = registerUserReqVO.getPassword();
        String verifyCode = registerUserReqVO.getVerifyCode();

        // 1. 校验验证码
        // TODO: 验证码先写死 123456，后续开发验证码发送接口，再重构这里
        if (!"123456".equals(verifyCode)) {
            throw new BizException(ResponseCodeEnum.USER_VERIFY_CODE_ERROR);
        }

        // 2. 校验手机号是否已注册
        Long existUserId = userDOMapper.selectIdByMobile(mobile);
        if (Objects.nonNull(existUserId)) {
            throw new BizException(ResponseCodeEnum.USER_MOBILE_EXISTS);
        }

        // 3. 密码加密（使用 BCrypt 算法）
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);

        // 4. 构建用户实体，插入数据库
        UserDO userDO = UserDO.builder().mobile(mobile).password(encodedPassword).nickname(generateNickname()) // 生成随机用户昵称
                .status(UserStatusEnum.ENABLED.getCode()) // 默认启用
                .build();

        userDOMapper.insertSelective(userDO);

        log.info("==> 用户注册成功, mobile: {}", mobile);

        return Response.success();
    }

    /**
     * 生成随机昵称
     * 格式：用户 + 6 位随机数字，如：用户382910
     *
     * @return 昵称
     */
    private String generateNickname() {
        return "用户" + RandomUtil.randomNumbers(6);
    }
}

package com.zhuxiaoha.seckill.user.service;

import com.zhuxiaoha.seckill.common.utils.Response;
import com.zhuxiaoha.seckill.user.model.vo.LoginUserReqVO;
import com.zhuxiaoha.seckill.user.model.vo.LoginUserRspVO;
import com.zhuxiaoha.seckill.user.model.vo.RegisterUserReqVO;

/**
 * 用户业务
 */
public interface UserService {

    /**
     * 用户注册
     *
     * @param registerUserReqVO
     * @return
     */
    Response<?> register(RegisterUserReqVO registerUserReqVO);

    /**
     * 用户登录
     * @param loginUserReqVO
     * @return
     */
    Response<LoginUserRspVO> login(LoginUserReqVO loginUserReqVO);
}

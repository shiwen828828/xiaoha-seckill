package com.zhuxiaoha.seckill.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: 猪小哈
 * @Date: 2026-04-17
 * @Version: v1.0.0
 * @Description: 业务异常
 **/
@Getter
@Setter
public class BizException extends RuntimeException {
    // 异常码
    private String errorCode;
    // 错误信息
    private String errorMessage;

    public BizException(BaseExceptionInterface baseExceptionInterface) {
        this.errorCode = baseExceptionInterface.getErrorCode();
        this.errorMessage = baseExceptionInterface.getErrorMessage();
    }
}

package com.zhuxiaoha.seckill.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {


    DISABLED(0, "禁用"),

    ENABLED(1, "启用"),
    ;

    // 状态值
    private final Integer code;
    // 状态描述
    private final String description;

}

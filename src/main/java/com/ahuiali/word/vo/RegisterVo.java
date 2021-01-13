package com.ahuiali.word.vo;

import lombok.Data;

/**
 * RegisterVo
 * 注册vo
 * @author ZhengChaoHui
 * @date 2021/1/9 21:15
 */
@Data
public class RegisterVo {

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 确认密码
     */
    private String confirmPassword;
}

package com.ahuiali.word.dto;

import lombok.Data;

/**
 * LoginDto
 * 登录返回dto
 * @author ZhengChaoHui
 * @date 2020/12/26 12:58
 */
@Data
public class LoginDto {

    /**
     * 用户id
     */
    private Integer id;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 请求sessionId
     */
    private String sessionId;

    /**
     * 状态
     */
    private Integer status;


}

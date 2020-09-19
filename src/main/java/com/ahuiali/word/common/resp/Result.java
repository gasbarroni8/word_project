package com.ahuiali.word.common.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhengChaoHui
 * @Date 2020/9/19 22:28
 */
@Data
public class Result implements Serializable {
    private String code;
    private String message;
    public Result() {
        this.code = "000000";
        this.message = "success";
    }

    public Result(String code, String resultMsg) {
        this.code = code;
        this.message = resultMsg;
    }
}

package com.ahuiali.word.common.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhengChaoHui
 * @Date 2020/9/19 22:16
 */
@Data
public class Response<T> implements Serializable {

    final static String SUCCESS = "200";

    private String code;
    private String message;

    private T data;
    
    public static <T> Response<T> success(){
        Response<T> response = new Response<>();
        response.setCode("200");
        response.setMessage("success");
        return response;
    }

    public Response() {
        super();
    }

    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Response(Result result) {
        this.code = result.getCode();
        this.message = result.getMessage();
    }

    public static <T> Response<T> result(Result result) {
        return new Response<T>(result);
    }

    public static Response<?> result(Response<?> response, Result result){
        response.setCode(result.getCode());
        response.setMessage(result.getMessage());
        return response;
    }

    public static Boolean isSuccess(Response<?> response){
        return  SUCCESS.equals(response.getCode());
    }

}

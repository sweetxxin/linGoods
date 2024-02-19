package com.xxin.goods.dto;

import lombok.Data;

/**
 * @author xxin
 * @Created
 * @Date 2023/1/22 14:17
 * @Description
 */
@Data
public class ApiResponse<T> {
    private String msg;
    private String code;
    private T data;

    public static ApiResponse success(Object data){
        ApiResponse response = new ApiResponse();
        response.setData(data);
        response.setCode("0");
        return response;
    }
    public static ApiResponse fail(String msg){
        ApiResponse response = new ApiResponse();
        response.setMsg(msg);
        response.setCode("-1");
        return response;
    }
}

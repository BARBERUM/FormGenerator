package org.example.formgenerator.utils;

import lombok.Data;

@Data
public class R <T>{
    private Integer code;

    private String msg;

    private T data;

    private R(Integer code, String msg, T data)
    {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private R()
    {

    }

    //返回正确信息以及结果集
    public static <T> R<T> success(String msg, T data){
        return new R<>(200, msg, data);
    }

    //返回正确信息
    public static <T> R<T> success(String msg){
        return new R<>(200, msg, null);
    }

    //返回错误信息
    public static <T> R<T> failure(String msg){
        return new R<>(500, msg, null);
    }


    //自定义响应信息
    public static <T> R<T> error(Integer code, String msg, T data)
    {
        return new R<>(code, msg, data);
    }
}

package com.liber.sun.exception;

import com.liber.sun.enums.ResultEnum;

/**
 * Created by sunlingzhi on 2017/10/24.
 */
//extends RuntimeException 适应SpringBoot的捕获
public class MyException extends RuntimeException{

    private  Integer code;

    public MyException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}

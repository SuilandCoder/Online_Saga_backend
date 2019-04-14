package com.liber.sun.handle;


import com.liber.sun.domain.Result;
import com.liber.sun.exception.MyException;
import com.liber.sun.utils.ResultUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * Created by sunlingzhi on 2017/10/21.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value=Exception.class)
    @ResponseBody
    public Result defaultErrorHandler(Exception e) throws Exception {
        if(e instanceof MyException){
            MyException myException=(MyException) e;
            return ResultUtil.error(myException.getCode(),myException.getMessage());
        }else{
            return ResultUtil.error(-1,e.getMessage());//系统的错误
        }
    }
}

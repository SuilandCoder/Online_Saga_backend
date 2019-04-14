package com.liber.sun.aspect;


import com.liber.sun.utils.MyFileUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by sunlingzhi on 2017/10/24.
 */
@Aspect
@Component
public class ApiControlAspect {

    private final static Logger logger = LoggerFactory.getLogger(ApiControlAspect.class);



    //定义切点
    @Pointcut("execution(public * com.liber.sun.controller.ApiControl.*(..))")     //拦截TestControl的所有方法
    //@Pointcut("execution(public * com.liber.sun.controller.ApiControl.getGeoJSONData(..)) || " +
    //      "execution(public * com.liber.sun.controller.ApiControl.uploadToLocal(..))" )
    //   "execution(public * com.liber.sun.controller.ApiControl.downloadFromRemote(..)) ") //拦截若干个方法
    public void point() {
    }

    @Before("point()")
    public void doBeforePoint(JoinPoint joinPoint) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        logger.info("url={}", request.getRequestURI());
        //GET POST PUT DELETE
        logger.info("request_method={}", request.getMethod());
        logger.info("ip={}", request.getRemoteAddr());
        logger.info("class_method={}", joinPoint.getSignature().getDeclaringTypeName() + "." +
                joinPoint.getSignature().getName());
        logger.info("args={}", joinPoint.getArgs());

    }

    @After("point()")
    public void doAfterPoint() {
        logger.info("=======>After Okay！！！");
    }


    //得到Http请求返回的东西，往往是一个Object，需要toString()方法以显示内容
    @AfterReturning(pointcut = "point()", returning = "object")
    public void doAfterReturning(Object object) {
        logger.info("response={}", object.toString());
    }


}

package com.liber.sun.configuration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by sunlingzhi on 2017/12/18.
 */
@Component
public class SpringBootUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringBootUtils.applicationContext == null) {
            SpringBootUtils.applicationContext = applicationContext;
        }
        System.out.println("========" +
                "ApplicationContext配置成功,SpringBootUtils.getAppContext()\n" +
                "获取applicationContext对象,\n" +
                "applicationContext=" + SpringBootUtils.applicationContext +"\n"+
                "========");
    }

    //通过name获取 Bean.
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    //通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}

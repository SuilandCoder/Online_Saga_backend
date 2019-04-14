package com.liber.sun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by sunlingzhi on 2017/10/19.
 */
@SpringBootApplication
//@ServletComponentScan //支持@WebServlet、@WebFilter、@WebListener
public class Application  {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

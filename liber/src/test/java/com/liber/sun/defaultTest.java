package com.liber.sun;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by sunlingzhi on 2017/10/24.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class defaultTest {



    @Test
    public void findUserByIdTest(){
        String a=this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(a);
    }


}

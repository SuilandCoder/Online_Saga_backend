package com.liber.sun;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



/**
 * Created by sunlingzhi on 2017/10/21.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc//全部上下文，不启动server
public class HttpTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPostFile() throws Exception {

        File f = new File("F:"+File.separator+"1.txt");
        InputStream in = new FileInputStream(f);

        MockMultipartFile file = new MockMultipartFile("file", "or9ig.txt", null,in);

        mockMvc.perform(
                fileUpload("/upload/single").file(file)

        )
                .andExpect(status().isOk())
                .andExpect(content().string("success"))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void testPost() throws Exception {
        mockMvc.perform(
                post("/accounts/create")
                .param("username","sunlingzhi")
                .param("password","123456")

        )
                .andExpect(status().isOk())
                .andExpect(content().string("success"))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(
                get("/test/59edfb463b09363770881bfc")
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    public void testPut() throws Exception {
        mockMvc.perform(
                put("/test/59ede5c23b0936223c8ddfa7")
                        .param("id","59ede5c23b0936223c8ddfa7")
                        .param("userName","fuck")
                        .param("passWord","123")
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(
                delete("/test/59edfb463b09363770881bfc")
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }


}

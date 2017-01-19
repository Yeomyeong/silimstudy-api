package com.silimstudy.auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerLoginJUnit {
    private String TEST_ID = "aaaa";
    private String TEST_PASSWORD = "1234";
    private String TEST_EMAIL = "aaaa@silimstudy.net";

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(post("/user/join")
                .param("username", TEST_ID)
                .param("password", TEST_PASSWORD)
                .param("email", TEST_EMAIL))
                .andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    public void 정상_로그인_테스트() throws Exception {
        //given
        mockMvc.perform(post("/user/login")
                .param("username", TEST_ID)
                .param("password", TEST_PASSWORD))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

package com.silimstudy.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * Created by yeomyeongwoo on 2017. 1. 14..
 */
class UserControllerTests extends Specification {
    @Autowired private WebApplicationContext context
    private MockMvc mockMvc

    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()

    }

    def "아이디 패스워드 기입 하지 않고 로그인 시도 테스트 "() {
        given :
        def username = ""
        def password = ""

        when :
        def response = this.mockMvc.perform(get("/study/list")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))

        then :
        response.andDo(print())
                .andExpect(status().isOk())
    }
}

package com.silimstudy;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SilimstudyApiApplicationTests {
	@Autowired
	private WebApplicationContext context;
	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testEmptyStudyList() throws Exception {
		this.mockMvc.perform(get("/study/list")
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));
	}

	@Test
	public void testRegisterStudy() throws Exception {
		this.mockMvc.perform(post("/study/register")
				.param("title", "나의 첫번째 스터디")).andDo(print());

		this.mockMvc.perform(get("/study/list")
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json("[{'title' : '나의 첫번째 스터디', 'content':null}]"));
	}


}

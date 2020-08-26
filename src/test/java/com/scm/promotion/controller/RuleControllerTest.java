package com.scm.promotion.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.scm.promotion.service.rules.RuleEngine;

@RunWith(SpringRunner.class)
@WebMvcTest(value = RuleController.class)
public class RuleControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RuleEngine ruleEngine;

	@Test
	public void registerRulesSuccess() throws Exception {
		String rulesJson = "[\r\n" + 
				"    {\r\n" + 
				"        \"name\": \"Offer for A\",\r\n" + 
				"        \"conditions\": [\r\n" + 
				"            {\r\n" + 
				"                \"skuCode\": \"A\",\r\n" + 
				"                \"quantity\": 3\r\n" + 
				"            }\r\n" + 
				"        ],\r\n" + 
				"        \"outcome\": 130.0\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"        \"name\": \"Offer for C & D\",\r\n" + 
				"        \"conditions\": [\r\n" + 
				"            {\r\n" + 
				"                \"skuCode\": \"C\",\r\n" + 
				"                \"quantity\": 1\r\n" + 
				"            },\r\n" + 
				"            {\r\n" + 
				"                \"skuCode\": \"D\",\r\n" + 
				"                \"quantity\": 1\r\n" + 
				"            }\r\n" + 
				"        ],\r\n" + 
				"        \"outcome\": 30.0\r\n" + 
				"    }\r\n" + 
				"]";

		mockMvc.perform(post("/rules/add").contentType("application/json").content(rulesJson))
				.andExpect(status().isOk());

	}

	@Test
	public void registerRulesFailure() throws Exception {
		mockMvc.perform(post("/rules/add").contentType("application/json")).andExpect(status().isBadRequest());

	}

}

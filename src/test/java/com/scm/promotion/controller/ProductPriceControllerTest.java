package com.scm.promotion.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.scm.promotion.controller.ProductPriceController;
import com.scm.promotion.service.ProductService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ProductPriceController.class)
public class ProductPriceControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@Test
	public void calculatePriceSuccess() throws Exception {
		String ordersJson = "{\r\n" + 
				"    \"A\": 3,\r\n" + 
				"    \"B\": 5,\r\n" + 
				"    \"C\": 1,\r\n" + 
				"    \"D\": 1\r\n" + 
				"}";
		Mockito.when(productService.calculatePrice(Mockito.anyMap())).thenReturn(100.0);

		MvcResult result = mockMvc.perform(post("/products/price").accept(MediaType.APPLICATION_JSON)
				.content(ordersJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		assertEquals("Result does not match", "100.0", result.getResponse().getContentAsString());

	}

	@Test
	public void calculatePriceFailure() throws Exception {
		Mockito.when(productService.calculatePrice(Mockito.anyMap()))
				.thenThrow(new RuntimeException("Error in calculation"));

		mockMvc.perform(
				post("/products/price").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

	}

}

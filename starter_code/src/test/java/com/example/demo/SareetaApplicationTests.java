package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class SareetaApplicationTests {

	@Autowired
	UserController userController;

	@Autowired
	OrderController orderController;

	@Autowired
	CartController cartController;

	@Autowired
	ItemController itemController;

	@Autowired
	private JacksonTester<CreateUserRequest> json;
	@Autowired
	private MockMvc mockMvc;

	private CreateUserRequest userDummy(String name){
		CreateUserRequest request = new CreateUserRequest();
		request.setPassword("Dmmy1111");
		request.setUsername(name);
		request.setConfirmPassword("Dmmy1111");
		return request;
	}
	private CreateUserRequest userDummy(){
		return userDummy("Testington");
	}
	@Test
	public void contextLoads() {
	}

	@Test
	public void createCustomer() throws  Exception{
		mockMvc.perform(
						post(new URI("/api/user/create"))
								.content(json.write(userDummy("Create")).getJson())
								.contentType(MediaType.APPLICATION_JSON_UTF8)
								.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
	}
	@Test
	public void createDuplicateCustomer() throws  Exception{
		mockMvc.perform(
						post(new URI("/api/user/create"))
								.content(json.write(userDummy()).getJson())
								.contentType(MediaType.APPLICATION_JSON_UTF8)
								.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
		mockMvc.perform(
						post(new URI("/api/user/create"))
								.content(json.write(userDummy()).getJson())
								.contentType(MediaType.APPLICATION_JSON_UTF8)
								.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest());
	}

}

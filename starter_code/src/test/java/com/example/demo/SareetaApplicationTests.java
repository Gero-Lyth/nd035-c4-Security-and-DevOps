package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
	private JacksonTester<CreateUserRequest> userRequestJacksonTester;
	@Autowired
	private JacksonTester<ModifyCartRequest> modifyCartRequestJacksonTester;
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
								.content(userRequestJacksonTester.write(userDummy("Create")).getJson())
								.contentType(MediaType.APPLICATION_JSON_UTF8)
								.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
	}
	@Test
	public void createDuplicateCustomer() throws  Exception{
		mockMvc.perform(
						post(new URI("/api/user/create"))
								.content(userRequestJacksonTester.write(userDummy()).getJson())
								.contentType(MediaType.APPLICATION_JSON_UTF8)
								.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
		mockMvc.perform(
						post(new URI("/api/user/create"))
								.content(userRequestJacksonTester.write(userDummy()).getJson())
								.contentType(MediaType.APPLICATION_JSON_UTF8)
								.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest());
	}
	@Test
	public void testCartOperations() throws  Exception{
		String username = "Order";
		mockMvc.perform(
						post(new URI("/api/user/create"))
								.content(userRequestJacksonTester.write(userDummy(username)).getJson())
								.contentType(MediaType.APPLICATION_JSON_UTF8)
								.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
		modifyCartRequest.setUsername(username);
		modifyCartRequest.setItemId(1);
		modifyCartRequest.setQuantity(4);
		mockMvc.perform(
						post(new URI("/api/cart/addToCart"))
								.content(modifyCartRequestJacksonTester.write(modifyCartRequest).getJson())
								.contentType(MediaType.APPLICATION_JSON_UTF8)
								.accept(MediaType.APPLICATION_JSON_UTF8)
								.with(user(username)))
				.andExpect(status().isOk());
		modifyCartRequest.setQuantity(2);

		mockMvc.perform(
						post(new URI("/api/cart/removeFromCart"))
								.content(modifyCartRequestJacksonTester.write(modifyCartRequest).getJson())
								.contentType(MediaType.APPLICATION_JSON_UTF8)
								.accept(MediaType.APPLICATION_JSON_UTF8)
								.with(user(username)))
				.andExpect(status().isOk());

		mockMvc.perform(
						post(new URI("/api/order/submit/"+username))
								.content(modifyCartRequestJacksonTester.write(modifyCartRequest).getJson())
								.contentType(MediaType.APPLICATION_JSON_UTF8)
								.accept(MediaType.APPLICATION_JSON_UTF8)
								.with(user(username)))
				.andExpect(status().isOk());
		mockMvc.perform(
						get(new URI("/api/order/history/"+username))
								.with(user(username)))
				.andExpect(status().isOk());
		mockMvc.perform(
						get(new URI("/api/order/history/aaa"+username))
								.with(user(username)))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void checkItem() throws Exception {
		mockMvc.perform(
						get(new URI("/api/item/1")).with(user("Dmmyu")))
				.andExpect(status().isOk()).andReturn().getResponse();

		mockMvc.perform(
						get(new URI("/api/item/-1")).with(user("Dmmyu")))
				.andExpect(status().isNotFound());
	}
}

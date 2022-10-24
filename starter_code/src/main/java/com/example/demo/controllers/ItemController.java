package com.example.demo.controllers;

import java.util.List;

import com.example.demo.model.requests.CreateItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		return ResponseEntity.ok(itemRepository.findAll());
	}

	@PutMapping
	public ResponseEntity<Item> makeItem(@RequestBody CreateItemRequest createItemRequest, Authentication authentication) {
		authentication.getAuthorities();
		Item item = new Item();
		item.setName(createItemRequest.getName());
		item.setDescription(createItemRequest.getDescription());
		item.setPrice(createItemRequest.getPrice());
		item = itemRepository.save(item);
		return ResponseEntity.ok(item);
	}


	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		return ResponseEntity.of(itemRepository.findById(id));
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);
		return items == null || items.isEmpty() ? ResponseEntity.notFound().build()
				: ResponseEntity.ok(items);
			
	}
	
}

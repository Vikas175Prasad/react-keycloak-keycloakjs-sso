package com.vikas.keycloak;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MyController {

	@GetMapping("/data")
	public ResponseEntity<String> getData(Principal principal) {
		System.out.println(principal);
		return ResponseEntity.ok().body("Hello "+ principal.getName());
	}
}

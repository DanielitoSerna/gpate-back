package com.gpate.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@CrossOrigin("*")
public class HelpCheckRest {
	
	@GetMapping(path = "/helpcheck", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getStatusService() {
		return new ResponseEntity<>("It's up", HttpStatus.OK);
	}

}

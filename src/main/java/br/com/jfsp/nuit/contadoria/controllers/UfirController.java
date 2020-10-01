package br.com.jfsp.nuit.contadoria.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jfsp.nuit.contadoria.services.UfirService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/ufir")
public class UfirController {

	@Autowired
	private UfirService service;
	
	@GetMapping("/importa")
	public ResponseEntity<?> importaUfir() {
		service.importa();
		return ResponseEntity.ok("ok");
	}
}

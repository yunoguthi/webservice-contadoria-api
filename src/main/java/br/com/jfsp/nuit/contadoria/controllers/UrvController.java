package br.com.jfsp.nuit.contadoria.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jfsp.nuit.contadoria.models.Urv;
import br.com.jfsp.nuit.contadoria.services.UrvService;

@CrossOrigin
@RestController
@RequestMapping("/urv")
public class UrvController {
	
	@Autowired
	private UrvService service;
	
	@GetMapping("/importa")
	public ResponseEntity<?> importaUrv() {
		service.importa();
		return ResponseEntity.ok("ok");
	}

}

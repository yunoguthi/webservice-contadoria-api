package br.com.jfsp.nuit.contadoria.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jfsp.nuit.contadoria.services.SalarioMinimoService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/salarioMinimo")
public class SalarioMinimoController {
	
	@Autowired
	private SalarioMinimoService service;
	
	@GetMapping("/importa")
	public ResponseEntity<?> importaSM(){
		service.importa();
		return ResponseEntity.ok("ok");
	}

}

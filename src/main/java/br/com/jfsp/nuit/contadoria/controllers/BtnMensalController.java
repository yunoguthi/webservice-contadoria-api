package br.com.jfsp.nuit.contadoria.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.jfsp.nuit.contadoria.models.BtnMensal;
import br.com.jfsp.nuit.contadoria.services.BtnMensalService;
	
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v1/btnmensal")
public class BtnMensalController {
	
	private final HttpServletRequest request;
	private final BtnMensalService service;
	
	public BtnMensalController(HttpServletRequest request, BtnMensalService service) {
		this.request = request;
		this.service = service;
	}
	
	@GetMapping("/importa")
	public ResponseEntity<?> importaBtnMensal() {
		service.importa();
		return ResponseEntity.ok("ok");
	}
	
	@PostMapping()
	public ResponseEntity<BtnMensal> save(@RequestBody BtnMensal btnMensal) {
		btnMensal = service.save(btnMensal);
		return new ResponseEntity<BtnMensal>(btnMensal, HttpStatus.OK);
	}

	//@CrossOrigin(origins = "http://localhost:4200")
	@PutMapping()
	public @ResponseBody ResponseEntity<BtnMensal> update(@RequestBody BtnMensal btnMensal) {
		btnMensal = service.save(btnMensal);
		return new ResponseEntity<BtnMensal>(btnMensal, HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/{id}")
	public @ResponseBody ResponseEntity<String> delete(@PathVariable("id") Long id) {
		try {
			service.delete(id);
		} catch (Exception e) {
			return new ResponseEntity<String>("ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
	
	@GetMapping()
	public @ResponseBody Iterable<BtnMensal> list() {
		return service.findAll();
	}
		
	@GetMapping(path = "/{id}")
	public @ResponseBody Optional<BtnMensal> find(@PathVariable("id") Long id) {
		return service.findById(id);
	}
	
	private KeycloakSecurityContext getKeycloakSecurityContext() {
		return (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
	}

}

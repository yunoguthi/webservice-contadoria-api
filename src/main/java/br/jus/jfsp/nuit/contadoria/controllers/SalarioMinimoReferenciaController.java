package br.jus.jfsp.nuit.contadoria.controllers;

import br.jus.jfsp.nuit.contadoria.aspect.Hateoas;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.SalarioMinimo;
import br.jus.jfsp.nuit.contadoria.models.SalarioMinimoReferencia;
import br.jus.jfsp.nuit.contadoria.service.SalarioMinimoReferenciaService;
import br.jus.jfsp.nuit.contadoria.to.SalarioMinimoReferenciaTO;
import br.jus.jfsp.nuit.contadoria.to.SalarioMinimoTO;
import br.jus.jfsp.nuit.contadoria.util.controller.RestUtil;
import br.jus.jfsp.nuit.contadoria.util.converter.DirectionConverter;
import br.jus.jfsp.nuit.contadoria.util.converter.SalarioMinimoReferenciaConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/salarioMinimoReferencia")
public class SalarioMinimoReferenciaController {

	@Autowired
	private SalarioMinimoReferenciaService service;
	private final SalarioMinimoReferenciaConverter salarioMinimoReferenciaConverter;
	private final PagedResourcesAssembler assembler;

	public SalarioMinimoReferenciaController(SalarioMinimoReferenciaService service,
											 SalarioMinimoReferenciaConverter salarioMinimoReferenciaConverter,
											 PagedResourcesAssembler assembler) {
		this.service = service;
		this.salarioMinimoReferenciaConverter = salarioMinimoReferenciaConverter;
		this.assembler = assembler;
	}

	@PostMapping
	@Hateoas
	public ResponseEntity<SalarioMinimoReferenciaTO> create(@RequestBody SalarioMinimoReferenciaTO salarioMinimoReferenciaTO) throws RecordNotFoundException {
		SalarioMinimoReferencia salarioMinimoReferencia = service.create(salarioMinimoReferenciaConverter.toEntity(salarioMinimoReferenciaTO));
		SalarioMinimoReferenciaTO to = salarioMinimoReferenciaConverter.toTransferObject(salarioMinimoReferencia);
		return RestUtil.createWithLocation(getClass(), to);
	}

	@PutMapping
	@Hateoas
	public ResponseEntity<SalarioMinimoReferenciaTO> update(@RequestBody @Valid SalarioMinimoReferenciaTO salarioMinimoReferenciaTO) throws RecordNotFoundException {
		SalarioMinimoReferencia salarioMinimoReferencia = service.update(salarioMinimoReferenciaConverter.toEntity(salarioMinimoReferenciaTO));
		return ResponseEntity.ok(salarioMinimoReferenciaConverter.toTransferObject(salarioMinimoReferencia));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) throws RecordNotFoundException {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	@Hateoas
	public ResponseEntity<SalarioMinimoReferenciaTO> read(@PathVariable("id") Long id) throws RecordNotFoundException {
		return ResponseEntity.ok(salarioMinimoReferenciaConverter.toTransferObject(service.read(id)));
	}

	@GetMapping
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<SalarioMinimoReferenciaTO>>> list(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<SalarioMinimoReferenciaTO> pageResult = salarioMinimoReferenciaConverter.toTransferObject(service.findAll(pageable));
		PagedModel<EntityModel<SalarioMinimoReferenciaTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

	@GetMapping("/like/{like}")
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<SalarioMinimoReferenciaTO>>> listLike(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy,
			@PathVariable("like") String like) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<SalarioMinimoReferenciaTO> pageResult = salarioMinimoReferenciaConverter.toTransferObject(service.findLike(pageable, like));
		PagedModel<EntityModel<SalarioMinimoReferenciaTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

}
package br.jus.jfsp.nuit.contadoria.controllers;

import br.jus.jfsp.nuit.contadoria.aspect.Hateoas;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.Ipca15;
import br.jus.jfsp.nuit.contadoria.service.Ipca15Service;
import br.jus.jfsp.nuit.contadoria.to.Ipca15TO;
import br.jus.jfsp.nuit.contadoria.util.controller.RestUtil;
import br.jus.jfsp.nuit.contadoria.util.converter.DirectionConverter;
import br.jus.jfsp.nuit.contadoria.util.converter.Ipca15Converter;
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
@RequestMapping("/api/ipca15")
public class Ipca15Controller {
	
	@Autowired
	private Ipca15Service service;
	private final Ipca15Converter ipca15Converter;
	private final PagedResourcesAssembler assembler;

	public Ipca15Controller(Ipca15Service service,
						  Ipca15Converter ipca15Converter,
						  PagedResourcesAssembler assembler) {
		this.service = service;
		this.ipca15Converter = ipca15Converter;
		this.assembler = assembler;
	}

	@GetMapping("/importa")
	public ResponseEntity.BodyBuilder importaIpca15() {
		service.importa();
		return ResponseEntity.status(200);
	}

	@GetMapping("/export")
	@Hateoas
	public Iterable<Ipca15TO> listAll(
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Iterable<Ipca15TO> retorno = ipca15Converter.toTransferObject(service.findAll());
		return retorno;
	}

	@PostMapping
	@Hateoas
	public ResponseEntity<Ipca15TO> create(@RequestBody Ipca15TO ipca15TO) throws RecordNotFoundException {
		Ipca15 ipca15 = service.create(ipca15Converter.toEntity(ipca15TO));
		Ipca15TO to = ipca15Converter.toTransferObject(ipca15);
		return RestUtil.createWithLocation(getClass(), to);
	}

	@PutMapping
	@Hateoas
	public ResponseEntity<Ipca15TO> update(@RequestBody @Valid Ipca15TO ipca15TO) throws RecordNotFoundException {
		Ipca15 ipca15 = service.update(ipca15Converter.toEntity(ipca15TO));
		return ResponseEntity.ok(ipca15Converter.toTransferObject(ipca15));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) throws RecordNotFoundException {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	@Hateoas
	public ResponseEntity<Ipca15TO> read(@PathVariable("id") Long id) throws RecordNotFoundException {
		return ResponseEntity.ok(ipca15Converter.toTransferObject(service.read(id)));
	}

	@GetMapping
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<Ipca15TO>>> list(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<Ipca15TO> pageResult = ipca15Converter.toTransferObject(service.findAll(pageable));
		PagedModel<EntityModel<Ipca15TO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

	@GetMapping("/like/{like}")
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<Ipca15TO>>> listLike(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy,
			@PathVariable("like") String like) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<Ipca15TO> pageResult = ipca15Converter.toTransferObject(service.findLike(pageable, like));
		PagedModel<EntityModel<Ipca15TO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}
//	@GetMapping(path = "/{data}")
//	public @ResponseBody
//	Optional<Ipca15> find(@PathVariable("data") String data) throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = sdf.parse(data);
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		return service.findByData(data);
//	}
//
//	@GetMapping(path = "/{data}/{data2}")
//	public @ResponseBody
//	Iterable<Ipca15> find2(@PathVariable("data") String data, @PathVariable("data2") String data2) throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = sdf.parse(data);
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//
//		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
//		Date date2 = sdf.parse(data2);
//		Calendar cal2 = Calendar.getInstance();
//		cal2.setTime(date2);
//
//		return service.findByDataBetween(cal, cal2);
//	}

}

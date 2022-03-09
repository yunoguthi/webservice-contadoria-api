package br.jus.jfsp.nuit.contadoria.controllers;

import br.jus.jfsp.nuit.contadoria.aspect.Hateoas;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.AjusteMoeda;
import br.jus.jfsp.nuit.contadoria.service.AjusteMoedaService;
import br.jus.jfsp.nuit.contadoria.to.AjusteMoedaTO;
import br.jus.jfsp.nuit.contadoria.util.controller.RestUtil;
import br.jus.jfsp.nuit.contadoria.util.converter.AjusteMoedaConverter;
import br.jus.jfsp.nuit.contadoria.util.converter.DirectionConverter;
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
@RequestMapping("/api/ajusteMoeda")
public class AjusteMoedaController {
	
	//private final HttpServletRequest request;
	private final AjusteMoedaService service;
	private final AjusteMoedaConverter ajusteMoedaConverter;
	private final PagedResourcesAssembler assembler;

	public AjusteMoedaController(//HttpServletRequest request,
                                 AjusteMoedaService service,
                                 AjusteMoedaConverter ajusteMoedaConverter,
                                 PagedResourcesAssembler assembler) {
		//this.request = request;
		this.service = service;
		this.ajusteMoedaConverter = ajusteMoedaConverter;
		this.assembler = assembler;
	}

	@GetMapping("/importa")
	public ResponseEntity<?> importaAjusteMoeda() {
		service.importa();
		return ResponseEntity.ok("ok");
	}

	@PostMapping
	@Hateoas
	public ResponseEntity<AjusteMoedaTO> create(@RequestBody AjusteMoedaTO ajusteMoedaTO) throws RecordNotFoundException {
		AjusteMoeda ajusteMoeda = service.create(ajusteMoedaConverter.toEntity(ajusteMoedaTO));
		AjusteMoedaTO to = ajusteMoedaConverter.toTransferObject(ajusteMoeda);
		return RestUtil.createWithLocation(getClass(), to);
	}

	@PutMapping
	@Hateoas
	public ResponseEntity<AjusteMoedaTO> update(@RequestBody @Valid AjusteMoedaTO ajusteMoedaTO) throws RecordNotFoundException {
		AjusteMoeda ajusteMoeda = service.update(ajusteMoedaConverter.toEntity(ajusteMoedaTO));
		return ResponseEntity.ok(ajusteMoedaConverter.toTransferObject(ajusteMoeda));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) throws RecordNotFoundException {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	@Hateoas
	public ResponseEntity<AjusteMoedaTO> read(@PathVariable("id") Long id) throws RecordNotFoundException {
		return ResponseEntity.ok(ajusteMoedaConverter.toTransferObject(service.read(id)));
	}

	@GetMapping
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<AjusteMoedaTO>>> list(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<AjusteMoedaTO> pageResult = ajusteMoedaConverter.toTransferObject(service.findAll(pageable));
		PagedModel<EntityModel<AjusteMoedaTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

	@GetMapping("/like/{like}")
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<AjusteMoedaTO>>> listLike(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy,
			@PathVariable("like") String like) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<AjusteMoedaTO> pageResult = ajusteMoedaConverter.toTransferObject(service.findLike(pageable, like));
		PagedModel<EntityModel<AjusteMoedaTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

	//	@GetMapping(path = "/{data}")
//	public @ResponseBody Optional<AjusteMoeda> find(@PathVariable("data") String data) throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = sdf.parse(data);
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		return service.findByData(cal);
//	}
//
//	@GetMapping(path = "/{data}/{data2}")
//	public @ResponseBody ResponseEntity<PagedModel<EntityModel<AjusteMoedaTO>>> find2(
//			@RequestParam(value = "page", defaultValue = "0") int page,
//			@RequestParam(value = "size", defaultValue = "5") int size,
//			@RequestParam(value = "direction", defaultValue = "asc") String direction,
//			@RequestParam(value = "sort", defaultValue = "nome") String[] sortBy,
//			@PathVariable("data") String data,
//			@PathVariable("data2") String data2) throws ParseException {
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
//		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
//		Page<AjusteMoedaTO> pageResult = ajusteMoedaConverter.toTransferObject(service.findAll(pageable));
//		PagedModel<EntityModel<AjusteMoedaTO>> pagedModel = assembler.toModel(pageResult);
//		return ResponseEntity.ok(pagedModel);
//
//		//return service.findByDataBetween(cal, cal2);
//	}

}

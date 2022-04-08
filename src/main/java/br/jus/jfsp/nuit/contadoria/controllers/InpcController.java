package br.jus.jfsp.nuit.contadoria.controllers;

import br.jus.jfsp.nuit.contadoria.aspect.Hateoas;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.Inpc;
import br.jus.jfsp.nuit.contadoria.models.Inpc;
import br.jus.jfsp.nuit.contadoria.service.InpcService;
import br.jus.jfsp.nuit.contadoria.service.InpcService;
import br.jus.jfsp.nuit.contadoria.to.BtnMensalTO;
import br.jus.jfsp.nuit.contadoria.to.InpcTO;
import br.jus.jfsp.nuit.contadoria.util.controller.RestUtil;
import br.jus.jfsp.nuit.contadoria.util.converter.InpcConverter;
import br.jus.jfsp.nuit.contadoria.util.converter.DirectionConverter;
import br.jus.jfsp.nuit.contadoria.util.converter.InpcConverter;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/inpc")
public class InpcController {
	
	@Autowired
	private InpcService service;
	private final InpcConverter inpcConverter;
	private final PagedResourcesAssembler assembler;

	public InpcController(InpcService service,
						  InpcConverter inpcConverter,
						  PagedResourcesAssembler assembler) {
		this.service = service;
		this.inpcConverter = inpcConverter;
		this.assembler = assembler;
	}

	@GetMapping("/importa")
	public ResponseEntity<?> importa() {
		service.importa();		
		return ResponseEntity.ok("OK");
	}

	@GetMapping("/export")
	@Hateoas
	public Iterable<InpcTO> listAll(
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Iterable<InpcTO> retorno = inpcConverter.toTransferObject(service.findAll());
		return retorno;
	}

//	@GetMapping(path = "/{data}")
//	public @ResponseBody
//	Optional<Inpc> find(@PathVariable("data") String data) throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = sdf.parse(data);
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		return service.findByData(data);
//	}
//
//	@GetMapping(path = "/{data}/{data2}")
//	public @ResponseBody Iterable<Inpc> find2(@PathVariable("data") String data, @PathVariable("data2") String data2) throws ParseException {
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

	@PostMapping
	@Hateoas
	public ResponseEntity<InpcTO> create(@RequestBody InpcTO inpcTO) throws RecordNotFoundException {
		Inpc inpc = service.create(inpcConverter.toEntity(inpcTO));
		InpcTO to = inpcConverter.toTransferObject(inpc);
		return RestUtil.createWithLocation(getClass(), to);
	}

	@PutMapping
	@Hateoas
	public ResponseEntity<InpcTO> update(@RequestBody @Valid InpcTO inpcTO) throws RecordNotFoundException {
		Inpc inpc = service.update(inpcConverter.toEntity(inpcTO));
		return ResponseEntity.ok(inpcConverter.toTransferObject(inpc));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) throws RecordNotFoundException {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	@Hateoas
	public ResponseEntity<InpcTO> read(@PathVariable("id") Long id) throws RecordNotFoundException {
		Inpc inpc = new Inpc();
		return ResponseEntity.ok(inpcConverter.toTransferObject(service.read(id)));
	}

//	@GetMapping(path = "/{data}")
//	public @ResponseBody Optional<Inpc> find(@PathVariable("data") String data) throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = sdf.parse(data);
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		return service.findByData(cal);
//	}
//
//	@GetMapping(path = "/{data}/{data2}")
//	public @ResponseBody ResponseEntity<PagedModel<EntityModel<InpcTO>>> find2(
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
//		Page<InpcTO> pageResult = inpcConverter.toTransferObject(service.findAll(pageable));
//		PagedModel<EntityModel<InpcTO>> pagedModel = assembler.toModel(pageResult);
//		return ResponseEntity.ok(pagedModel);
//
//		//return service.findByDataBetween(cal, cal2);
//	}

	@GetMapping
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<InpcTO>>> list(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<InpcTO> pageResult = inpcConverter.toTransferObject(service.findAll(pageable));
		PagedModel<EntityModel<InpcTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

	@GetMapping("/like/{like}")
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<InpcTO>>> listLike(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy,
			@PathVariable("like") String like) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<InpcTO> pageResult = inpcConverter.toTransferObject(service.findLike(pageable, like));
		PagedModel<EntityModel<InpcTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}
	
}

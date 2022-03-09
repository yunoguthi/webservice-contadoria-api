package br.jus.jfsp.nuit.contadoria.controllers;

import br.jus.jfsp.nuit.contadoria.aspect.Hateoas;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.JurosAlt;
import br.jus.jfsp.nuit.contadoria.service.JurosAltService;
import br.jus.jfsp.nuit.contadoria.to.JurosAltTO;
import br.jus.jfsp.nuit.contadoria.util.controller.RestUtil;
import br.jus.jfsp.nuit.contadoria.util.converter.DirectionConverter;
import br.jus.jfsp.nuit.contadoria.util.converter.JurosAltConverter;
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
@RequestMapping("/api/jurosAlt")
public class JurosAltController {

	//private final HttpServletRequest request;
	private final JurosAltService service;
	private final JurosAltConverter jurosAltConverter;
	private final PagedResourcesAssembler assembler;

	public JurosAltController(//HttpServletRequest request,
                              JurosAltService service,
                              JurosAltConverter jurosAltConverter,
                              PagedResourcesAssembler assembler) {
		//this.request = request;
		this.service = service;
		this.jurosAltConverter = jurosAltConverter;
		this.assembler = assembler;
	}

	@GetMapping("/importa")
	public ResponseEntity<?> importaJurosAlt() {
		service.importa();
		return ResponseEntity.ok("ok");
	}

	@PostMapping
	@Hateoas
	public ResponseEntity<JurosAltTO> create(@RequestBody JurosAltTO jurosAltTO) throws RecordNotFoundException {
		JurosAlt jurosAlt = service.create(jurosAltConverter.toEntity(jurosAltTO));
		JurosAltTO to = jurosAltConverter.toTransferObject(jurosAlt);
		return RestUtil.createWithLocation(getClass(), to);
	}

	@PutMapping
	@Hateoas
	public ResponseEntity<JurosAltTO> update(@RequestBody @Valid JurosAltTO jurosAltTO) throws RecordNotFoundException {
		JurosAlt jurosAlt = service.update(jurosAltConverter.toEntity(jurosAltTO));
		return ResponseEntity.ok(jurosAltConverter.toTransferObject(jurosAlt));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) throws RecordNotFoundException {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	@Hateoas
	public ResponseEntity<JurosAltTO> read(@PathVariable("id") Long id) throws RecordNotFoundException {
		return ResponseEntity.ok(jurosAltConverter.toTransferObject(service.read(id)));
	}

	@GetMapping
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<JurosAltTO>>> list(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<JurosAltTO> pageResult = jurosAltConverter.toTransferObject(service.findAll(pageable));
		PagedModel<EntityModel<JurosAltTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

	@GetMapping("/like/{like}")
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<JurosAltTO>>> listLike(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy,
			@PathVariable("like") String like) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<JurosAltTO> pageResult = jurosAltConverter.toTransferObject(service.findLike(pageable, like));
		PagedModel<EntityModel<JurosAltTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

	//	@GetMapping(path = "/{data}")
//	public @ResponseBody Optional<JurosAlt> find(@PathVariable("data") String data) throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = sdf.parse(data);
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		return service.findByData(cal);
//	}
//
//	@GetMapping(path = "/{data}/{data2}")
//	public @ResponseBody ResponseEntity<PagedModel<EntityModel<JurosAltTO>>> find2(
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
//		Page<JurosAltTO> pageResult = jurosAltConverter.toTransferObject(service.findAll(pageable));
//		PagedModel<EntityModel<JurosAltTO>> pagedModel = assembler.toModel(pageResult);
//		return ResponseEntity.ok(pagedModel);
//
//		//return service.findByDataBetween(cal, cal2);
//	}

}

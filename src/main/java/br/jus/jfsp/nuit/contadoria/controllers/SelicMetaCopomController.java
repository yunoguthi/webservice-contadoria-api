package br.jus.jfsp.nuit.contadoria.controllers;

import br.jus.jfsp.nuit.contadoria.aspect.Hateoas;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.SelicMetaCopom;
import br.jus.jfsp.nuit.contadoria.service.SelicMetaCopomService;
import br.jus.jfsp.nuit.contadoria.to.SelicMetaCopomTO;
import br.jus.jfsp.nuit.contadoria.util.controller.RestUtil;
import br.jus.jfsp.nuit.contadoria.util.converter.DirectionConverter;
import br.jus.jfsp.nuit.contadoria.util.converter.SelicMetaCopomConverter;
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
@RequestMapping("/api/selicMetaCopom")
public class SelicMetaCopomController {
	
	@Autowired
	private SelicMetaCopomService service;
	private final SelicMetaCopomConverter selicMetaCopomConverter;
	private final PagedResourcesAssembler assembler;

	public SelicMetaCopomController(SelicMetaCopomService service,
						  SelicMetaCopomConverter selicMetaCopomConverter,
						  PagedResourcesAssembler assembler) {
		this.service = service;
		this.selicMetaCopomConverter = selicMetaCopomConverter;
		this.assembler = assembler;
	}
	
	@GetMapping("/importa")
	public ResponseEntity.BodyBuilder importaSelicMetaCopom(){
		service.importa();
		return ResponseEntity.status(200);
	}

	@GetMapping("/export")
	@Hateoas
	public Iterable<SelicMetaCopomTO> listAll(
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Iterable<SelicMetaCopomTO> retorno = selicMetaCopomConverter.toTransferObject(service.findAll());
		return retorno;
	}

	@PostMapping
	@Hateoas
	public ResponseEntity<SelicMetaCopomTO> create(@RequestBody SelicMetaCopomTO selicMetaCopomTO) throws RecordNotFoundException {
		SelicMetaCopom selicMetaCopom = service.create(selicMetaCopomConverter.toEntity(selicMetaCopomTO));
		SelicMetaCopomTO to = selicMetaCopomConverter.toTransferObject(selicMetaCopom);
		return RestUtil.createWithLocation(getClass(), to);
	}

	@PutMapping
	@Hateoas
	public ResponseEntity<SelicMetaCopomTO> update(@RequestBody @Valid SelicMetaCopomTO selicMetaCopomTO) throws RecordNotFoundException {
		SelicMetaCopom selicMetaCopom = service.update(selicMetaCopomConverter.toEntity(selicMetaCopomTO));
		return ResponseEntity.ok(selicMetaCopomConverter.toTransferObject(selicMetaCopom));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) throws RecordNotFoundException {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	@Hateoas
	public ResponseEntity<SelicMetaCopomTO> read(@PathVariable("id") Long id) throws RecordNotFoundException {
		return ResponseEntity.ok(selicMetaCopomConverter.toTransferObject(service.read(id)));
	}

	@GetMapping
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<SelicMetaCopomTO>>> list(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<SelicMetaCopomTO> pageResult = selicMetaCopomConverter.toTransferObject(service.findAll(pageable));
		PagedModel<EntityModel<SelicMetaCopomTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

	@GetMapping("/like/{like}")
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<SelicMetaCopomTO>>> listLike(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy,
			@PathVariable("like") String like) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<SelicMetaCopomTO> pageResult = selicMetaCopomConverter.toTransferObject(service.findLike(pageable, like));
		PagedModel<EntityModel<SelicMetaCopomTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}
//	@GetMapping(path = "/{data}")
//	public @ResponseBody
//	Optional<SelicMetaCopom> find(@PathVariable("data") String data) throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = sdf.parse(data);
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		return service.findByData(cal);
//	}
//
//	@GetMapping(path = "/{data}/{data2}")
//	public @ResponseBody
//	Iterable<SelicMetaCopom> find2(@PathVariable("data") String data, @PathVariable("data2") String data2) throws ParseException {
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

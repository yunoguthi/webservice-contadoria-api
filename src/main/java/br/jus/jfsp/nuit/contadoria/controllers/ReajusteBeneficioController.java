package br.jus.jfsp.nuit.contadoria.controllers;

import br.jus.jfsp.nuit.contadoria.aspect.Hateoas;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.ReajusteBeneficio;
import br.jus.jfsp.nuit.contadoria.service.ReajusteBeneficioService;
import br.jus.jfsp.nuit.contadoria.to.ReajusteBeneficioTO;
import br.jus.jfsp.nuit.contadoria.to.ReajusteBeneficioTO;
import br.jus.jfsp.nuit.contadoria.util.controller.RestUtil;
import br.jus.jfsp.nuit.contadoria.util.converter.ReajusteBeneficioConverter;
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
@RequestMapping("/api/reajusteBeneficio")
public class ReajusteBeneficioController {
	
	//private final HttpServletRequest request;
	private final ReajusteBeneficioService service;
	private final ReajusteBeneficioConverter reajusteBeneficioConverter;
	private final PagedResourcesAssembler assembler;

	public ReajusteBeneficioController(//HttpServletRequest request,
                                       ReajusteBeneficioService service,
                                       ReajusteBeneficioConverter reajusteBeneficioConverter,
                                       PagedResourcesAssembler assembler) {
		//this.request = request;
		this.service = service;
		this.reajusteBeneficioConverter = reajusteBeneficioConverter;
		this.assembler = assembler;
	}

	@GetMapping("/export")
	@Hateoas
	public Iterable<ReajusteBeneficioTO> listAll(
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Iterable<ReajusteBeneficioTO> retorno = reajusteBeneficioConverter.toTransferObject(service.findAll());
		return retorno;
	}

	@PostMapping
	@Hateoas
	public ResponseEntity<ReajusteBeneficioTO> create(@RequestBody ReajusteBeneficioTO reajusteBeneficioTO) throws RecordNotFoundException {
		ReajusteBeneficio reajusteBeneficio = service.create(reajusteBeneficioConverter.toEntity(reajusteBeneficioTO));
		ReajusteBeneficioTO to = reajusteBeneficioConverter.toTransferObject(reajusteBeneficio);
		return RestUtil.createWithLocation(getClass(), to);
	}

	@PutMapping
	@Hateoas
	public ResponseEntity<ReajusteBeneficioTO> update(@RequestBody @Valid ReajusteBeneficioTO reajusteBeneficioTO) throws RecordNotFoundException {
		ReajusteBeneficio reajusteBeneficio = service.update(reajusteBeneficioConverter.toEntity(reajusteBeneficioTO));
		return ResponseEntity.ok(reajusteBeneficioConverter.toTransferObject(reajusteBeneficio));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) throws RecordNotFoundException {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	@Hateoas
	public ResponseEntity<ReajusteBeneficioTO> read(@PathVariable("id") Long id) throws RecordNotFoundException {
		return ResponseEntity.ok(reajusteBeneficioConverter.toTransferObject(service.read(id)));
	}

	@GetMapping
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<ReajusteBeneficioTO>>> list(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<ReajusteBeneficioTO> pageResult = reajusteBeneficioConverter.toTransferObject(service.findAll(pageable));
		PagedModel<EntityModel<ReajusteBeneficioTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

	@GetMapping("/like/{like}")
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<ReajusteBeneficioTO>>> listLike(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy,
			@PathVariable("like") String like) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<ReajusteBeneficioTO> pageResult = reajusteBeneficioConverter.toTransferObject(service.findLike(pageable, like));
		PagedModel<EntityModel<ReajusteBeneficioTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

	//	@GetMapping(path = "/{data}")
//	public @ResponseBody Optional<ReajusteBeneficio> find(@PathVariable("data") String data) throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = sdf.parse(data);
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		return service.findByData(cal);
//	}
//
//	@GetMapping(path = "/{data}/{data2}")
//	public @ResponseBody ResponseEntity<PagedModel<EntityModel<ReajusteBeneficioTO>>> find2(
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
//		Page<ReajusteBeneficioTO> pageResult = reajusteBeneficioConverter.toTransferObject(service.findAll(pageable));
//		PagedModel<EntityModel<ReajusteBeneficioTO>> pagedModel = assembler.toModel(pageResult);
//		return ResponseEntity.ok(pagedModel);
//
//		//return service.findByDataBetween(cal, cal2);
//	}

}

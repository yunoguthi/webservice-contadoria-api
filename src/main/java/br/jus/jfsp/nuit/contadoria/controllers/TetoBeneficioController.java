package br.jus.jfsp.nuit.contadoria.controllers;

import br.jus.jfsp.nuit.contadoria.aspect.Hateoas;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.TetoBeneficio;
import br.jus.jfsp.nuit.contadoria.service.TetoBeneficioService;
import br.jus.jfsp.nuit.contadoria.to.TetoBeneficioTO;
import br.jus.jfsp.nuit.contadoria.to.TetoBeneficioTO;
import br.jus.jfsp.nuit.contadoria.util.controller.RestUtil;
import br.jus.jfsp.nuit.contadoria.util.converter.DirectionConverter;
import br.jus.jfsp.nuit.contadoria.util.converter.TetoBeneficioConverter;
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
@RequestMapping("/api/tetoBeneficio")
public class TetoBeneficioController {

	@Autowired
	private TetoBeneficioService service;
	private final TetoBeneficioConverter tetoBeneficioConverter;
	private final PagedResourcesAssembler assembler;

	public TetoBeneficioController(TetoBeneficioService service,
                                   TetoBeneficioConverter tetoBeneficioConverter,
                                   PagedResourcesAssembler assembler) {
		this.service = service;
		this.tetoBeneficioConverter = tetoBeneficioConverter;
		this.assembler = assembler;
	}

	@GetMapping("/export")
	@Hateoas
	public Iterable<TetoBeneficioTO> listAll(
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Iterable<TetoBeneficioTO> retorno = tetoBeneficioConverter.toTransferObject(service.findAll());
		return retorno;
	}

	@PostMapping
	@Hateoas
	public ResponseEntity<TetoBeneficioTO> create(@RequestBody TetoBeneficioTO tetoBeneficioTO) throws RecordNotFoundException {
		TetoBeneficio tetoBeneficio = service.create(tetoBeneficioConverter.toEntity(tetoBeneficioTO));
		TetoBeneficioTO to = tetoBeneficioConverter.toTransferObject(tetoBeneficio);
		return RestUtil.createWithLocation(getClass(), to);
	}

	@PutMapping
	@Hateoas
	public ResponseEntity<TetoBeneficioTO> update(@RequestBody @Valid TetoBeneficioTO tetoBeneficioTO) throws RecordNotFoundException {
		TetoBeneficio tetoBeneficio = service.update(tetoBeneficioConverter.toEntity(tetoBeneficioTO));
		return ResponseEntity.ok(tetoBeneficioConverter.toTransferObject(tetoBeneficio));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) throws RecordNotFoundException {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	@Hateoas
	public ResponseEntity<TetoBeneficioTO> read(@PathVariable("id") Long id) throws RecordNotFoundException {
		return ResponseEntity.ok(tetoBeneficioConverter.toTransferObject(service.read(id)));
	}

	@GetMapping
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<TetoBeneficioTO>>> list(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<TetoBeneficioTO> pageResult = tetoBeneficioConverter.toTransferObject(service.findAll(pageable));
		PagedModel<EntityModel<TetoBeneficioTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

	@GetMapping("/like/{like}")
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<TetoBeneficioTO>>> listLike(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy,
			@PathVariable("like") String like) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<TetoBeneficioTO> pageResult = tetoBeneficioConverter.toTransferObject(service.findLike(pageable, like));
		PagedModel<EntityModel<TetoBeneficioTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}
//	@GetMapping(path = "/{data}")
//	public @ResponseBody
//	Optional<TetoBeneficio> find(@PathVariable("data") String data) throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = sdf.parse(data);
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		return service.findByData(cal);
//	}
//
//	@GetMapping(path = "/{data}/{data2}")
//	public @ResponseBody Iterable<TetoBeneficio> find2(@PathVariable("data") String data, @PathVariable("data2") String data2) throws ParseException {
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

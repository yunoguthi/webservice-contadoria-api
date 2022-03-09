package br.jus.jfsp.nuit.contadoria.controllers;

import br.jus.jfsp.nuit.contadoria.aspect.Hateoas;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.AtualizacaoSalario;
import br.jus.jfsp.nuit.contadoria.service.AtualizacaoSalarioService;
import br.jus.jfsp.nuit.contadoria.to.AtualizacaoSalarioTO;
import br.jus.jfsp.nuit.contadoria.util.controller.RestUtil;
import br.jus.jfsp.nuit.contadoria.util.converter.AtualizacaoSalarioConverter;
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
@RequestMapping("/api/atualizacaoSalario")
public class AtualizacaoSalarioController {

	//private final HttpServletRequest request;
	private final AtualizacaoSalarioService service;
	private final AtualizacaoSalarioConverter atualizacaoSalarioConverter;
	private final PagedResourcesAssembler assembler;

	public AtualizacaoSalarioController(//HttpServletRequest request,
                                        AtualizacaoSalarioService service,
                                        AtualizacaoSalarioConverter atualizacaoSalarioConverter,
                                        PagedResourcesAssembler assembler) {
		//this.request = request;
		this.service = service;
		this.atualizacaoSalarioConverter = atualizacaoSalarioConverter;
		this.assembler = assembler;
	}

	@GetMapping("/importa")
	public ResponseEntity<?> importaAtualizacaoSalario() {
		service.importa();
		return ResponseEntity.ok("ok");
	}

	@PostMapping
	@Hateoas
	public ResponseEntity<AtualizacaoSalarioTO> create(@RequestBody AtualizacaoSalarioTO atualizacaoSalarioTO) throws RecordNotFoundException {
		AtualizacaoSalario atualizacaoSalario = service.create(atualizacaoSalarioConverter.toEntity(atualizacaoSalarioTO));
		AtualizacaoSalarioTO to = atualizacaoSalarioConverter.toTransferObject(atualizacaoSalario);
		return RestUtil.createWithLocation(getClass(), to);
	}

	@PutMapping
	@Hateoas
	public ResponseEntity<AtualizacaoSalarioTO> update(@RequestBody @Valid AtualizacaoSalarioTO atualizacaoSalarioTO) throws RecordNotFoundException {
		AtualizacaoSalario atualizacaoSalario = service.update(atualizacaoSalarioConverter.toEntity(atualizacaoSalarioTO));
		return ResponseEntity.ok(atualizacaoSalarioConverter.toTransferObject(atualizacaoSalario));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) throws RecordNotFoundException {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	@Hateoas
	public ResponseEntity<AtualizacaoSalarioTO> read(@PathVariable("id") Long id) throws RecordNotFoundException {
		return ResponseEntity.ok(atualizacaoSalarioConverter.toTransferObject(service.read(id)));
	}

	@GetMapping
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<AtualizacaoSalarioTO>>> list(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<AtualizacaoSalarioTO> pageResult = atualizacaoSalarioConverter.toTransferObject(service.findAll(pageable));
		PagedModel<EntityModel<AtualizacaoSalarioTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

	@GetMapping("/like/{like}")
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<AtualizacaoSalarioTO>>> listLike(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy,
			@PathVariable("like") String like) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<AtualizacaoSalarioTO> pageResult = atualizacaoSalarioConverter.toTransferObject(service.findLike(pageable, like));
		PagedModel<EntityModel<AtualizacaoSalarioTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

	//	@GetMapping(path = "/{data}")
//	public @ResponseBody Optional<AtualizacaoSalario> find(@PathVariable("data") String data) throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = sdf.parse(data);
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		return service.findByData(cal);
//	}
//
//	@GetMapping(path = "/{data}/{data2}")
//	public @ResponseBody ResponseEntity<PagedModel<EntityModel<AtualizacaoSalarioTO>>> find2(
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
//		Page<AtualizacaoSalarioTO> pageResult = atualizacaoSalarioConverter.toTransferObject(service.findAll(pageable));
//		PagedModel<EntityModel<AtualizacaoSalarioTO>> pagedModel = assembler.toModel(pageResult);
//		return ResponseEntity.ok(pagedModel);
//
//		//return service.findByDataBetween(cal, cal2);
//	}

}

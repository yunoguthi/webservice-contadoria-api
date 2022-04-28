package br.jus.jfsp.nuit.contadoria.controllers;

import br.jus.jfsp.nuit.contadoria.aspect.Hateoas;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.AtualizacaoJudicial;
import br.jus.jfsp.nuit.contadoria.service.AtualizacaoJudicialService;
import br.jus.jfsp.nuit.contadoria.to.AtualizacaoJudicialTO;
import br.jus.jfsp.nuit.contadoria.util.controller.RestUtil;
import br.jus.jfsp.nuit.contadoria.util.converter.AtualizacaoJudicialConverter;
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
@RequestMapping("/api/atualizacaoJudicial")
public class AtualizacaoJudicialController {

	//private final HttpServletRequest request;
	private final AtualizacaoJudicialService service;
	private final AtualizacaoJudicialConverter atualizacaoJudicialConverter;
	private final PagedResourcesAssembler assembler;

	public AtualizacaoJudicialController(//HttpServletRequest request,
                                         AtualizacaoJudicialService service,
                                         AtualizacaoJudicialConverter atualizacaoJudicialConverter,
                                         PagedResourcesAssembler assembler) {
		//this.request = request;
		this.service = service;
		this.atualizacaoJudicialConverter = atualizacaoJudicialConverter;
		this.assembler = assembler;
	}

	@GetMapping("/importa")
	public ResponseEntity.BodyBuilder importaAtualizacaoJudicial() {
		service.importa();
		return ResponseEntity.status(200);
	}

	@GetMapping("/export")
	@Hateoas
	public Iterable<AtualizacaoJudicialTO> listAll(
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Iterable<AtualizacaoJudicialTO> retorno = atualizacaoJudicialConverter.toTransferObject(service.findAll());
		return retorno;
	}

	@PostMapping
	@Hateoas
	public ResponseEntity<AtualizacaoJudicialTO> create(@RequestBody AtualizacaoJudicialTO atualizacaoJudicialTO) throws RecordNotFoundException {
		AtualizacaoJudicial atualizacaoJudicial = service.create(atualizacaoJudicialConverter.toEntity(atualizacaoJudicialTO));
		AtualizacaoJudicialTO to = atualizacaoJudicialConverter.toTransferObject(atualizacaoJudicial);
		return RestUtil.createWithLocation(getClass(), to);
	}

	@PutMapping
	@Hateoas
	public ResponseEntity<AtualizacaoJudicialTO> update(@RequestBody @Valid AtualizacaoJudicialTO atualizacaoJudicialTO) throws RecordNotFoundException {
		AtualizacaoJudicial atualizacaoJudicial = service.update(atualizacaoJudicialConverter.toEntity(atualizacaoJudicialTO));
		return ResponseEntity.ok(atualizacaoJudicialConverter.toTransferObject(atualizacaoJudicial));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) throws RecordNotFoundException {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	@Hateoas
	public ResponseEntity<AtualizacaoJudicialTO> read(@PathVariable("id") Long id) throws RecordNotFoundException {
		return ResponseEntity.ok(atualizacaoJudicialConverter.toTransferObject(service.read(id)));
	}

	@GetMapping
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<AtualizacaoJudicialTO>>> list(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<AtualizacaoJudicialTO> pageResult = atualizacaoJudicialConverter.toTransferObject(service.findAll(pageable));
		PagedModel<EntityModel<AtualizacaoJudicialTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

	@GetMapping("/like/{like}")
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<AtualizacaoJudicialTO>>> listLike(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy,
			@PathVariable("like") String like) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<AtualizacaoJudicialTO> pageResult = atualizacaoJudicialConverter.toTransferObject(service.findLike(pageable, like));
		PagedModel<EntityModel<AtualizacaoJudicialTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

	//	@GetMapping(path = "/{data}")
//	public @ResponseBody Optional<AtualizacaoJudicial> find(@PathVariable("data") String data) throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = sdf.parse(data);
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		return service.findByData(cal);
//	}
//
//	@GetMapping(path = "/{data}/{data2}")
//	public @ResponseBody ResponseEntity<PagedModel<EntityModel<AtualizacaoJudicialTO>>> find2(
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
//		Page<AtualizacaoJudicialTO> pageResult = atualizacaoJudicialConverter.toTransferObject(service.findAll(pageable));
//		PagedModel<EntityModel<AtualizacaoJudicialTO>> pagedModel = assembler.toModel(pageResult);
//		return ResponseEntity.ok(pagedModel);
//
//		//return service.findByDataBetween(cal, cal2);
//	}

}

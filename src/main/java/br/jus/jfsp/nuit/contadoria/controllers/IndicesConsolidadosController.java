package br.jus.jfsp.nuit.contadoria.controllers;

import br.jus.jfsp.nuit.contadoria.aspect.Hateoas;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.IndicesConsolidados;
import br.jus.jfsp.nuit.contadoria.service.IndicesConsolidadosService;
import br.jus.jfsp.nuit.contadoria.to.IndicesConsolidadosTO;
import br.jus.jfsp.nuit.contadoria.util.ManipulaArquivo;
import br.jus.jfsp.nuit.contadoria.util.controller.RestUtil;
import br.jus.jfsp.nuit.contadoria.util.converter.DirectionConverter;
import br.jus.jfsp.nuit.contadoria.util.converter.IndicesConsolidadosConverter;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
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

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/indicesConsolidados")
public class IndicesConsolidadosController {

	//private final HttpServletRequest request;
	private final IndicesConsolidadosService service;
	private final IndicesConsolidadosConverter indicesConsolidadosConverter;
	private final PagedResourcesAssembler assembler;

	public IndicesConsolidadosController(//HttpServletRequest request,
                                         IndicesConsolidadosService service,
                                         IndicesConsolidadosConverter indicesConsolidadosConverter,
                                         PagedResourcesAssembler assembler) {
		//this.request = request;
		this.service = service;
		this.indicesConsolidadosConverter = indicesConsolidadosConverter;
		this.assembler = assembler;
	}

	@GetMapping("/importa")
	public ResponseEntity.BodyBuilder importaIndicesConsolidados() {
		service.importa();
		service.mostraCSV();
		return ResponseEntity.status(200);
	}

	@GetMapping("/export")
	@Hateoas
	public Iterable<IndicesConsolidadosTO> listAll(
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Iterable<IndicesConsolidadosTO> retorno = indicesConsolidadosConverter.toTransferObject(service.findAll());
		return retorno;
	}

	@PostMapping
	@Hateoas
	public ResponseEntity<IndicesConsolidadosTO> create(@RequestBody IndicesConsolidadosTO indicesConsolidadosTO) throws RecordNotFoundException {
		IndicesConsolidados indicesConsolidados = service.create(indicesConsolidadosConverter.toEntity(indicesConsolidadosTO));
		IndicesConsolidadosTO to = indicesConsolidadosConverter.toTransferObject(indicesConsolidados);
		return RestUtil.createWithLocation(getClass(), to);
	}

	@PutMapping
	@Hateoas
	public ResponseEntity<IndicesConsolidadosTO> update(@RequestBody @Valid IndicesConsolidadosTO indicesConsolidadosTO) throws RecordNotFoundException {
		IndicesConsolidados indicesConsolidados = service.update(indicesConsolidadosConverter.toEntity(indicesConsolidadosTO));
		return ResponseEntity.ok(indicesConsolidadosConverter.toTransferObject(indicesConsolidados));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) throws RecordNotFoundException {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	@Hateoas
	public ResponseEntity<IndicesConsolidadosTO> read(@PathVariable("id") Long id) throws RecordNotFoundException {
		return ResponseEntity.ok(indicesConsolidadosConverter.toTransferObject(service.read(id)));
	}

	@GetMapping
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<IndicesConsolidadosTO>>> list(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<IndicesConsolidadosTO> pageResult = indicesConsolidadosConverter.toTransferObject(service.findAll(pageable));
		PagedModel<EntityModel<IndicesConsolidadosTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

	@GetMapping("/like/{like}")
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<IndicesConsolidadosTO>>> listLike(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy,
			@PathVariable("like") String like) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<IndicesConsolidadosTO> pageResult = indicesConsolidadosConverter.toTransferObject(service.findLike(pageable, like));
		PagedModel<EntityModel<IndicesConsolidadosTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

	@GetMapping("/download")
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {
		Resource file = ManipulaArquivo.download("", "consolidados.csv");
		Path path = file.getFile()
				.toPath();

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	//	@GetMapping(path = "/{data}")
//	public @ResponseBody Optional<IndicesConsolidados> find(@PathVariable("data") String data) throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = sdf.parse(data);
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		return service.findByData(cal);
//	}
//
//	@GetMapping(path = "/{data}/{data2}")
//	public @ResponseBody ResponseEntity<PagedModel<EntityModel<IndicesConsolidadosTO>>> find2(
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
//		Page<IndicesConsolidadosTO> pageResult = indicesConsolidadosConverter.toTransferObject(service.findAll(pageable));
//		PagedModel<EntityModel<IndicesConsolidadosTO>> pagedModel = assembler.toModel(pageResult);
//		return ResponseEntity.ok(pagedModel);
//
//		//return service.findByDataBetween(cal, cal2);
//	}

}

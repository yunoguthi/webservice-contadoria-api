package br.jus.jfsp.nuit.contadoria.controllers;

import br.jus.jfsp.nuit.contadoria.aspect.Hateoas;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.Irsm;
import br.jus.jfsp.nuit.contadoria.models.Irsm;
import br.jus.jfsp.nuit.contadoria.service.IrsmService;
import br.jus.jfsp.nuit.contadoria.service.IrsmService;
import br.jus.jfsp.nuit.contadoria.to.IrsmTO;
import br.jus.jfsp.nuit.contadoria.to.IrsmTO;
import br.jus.jfsp.nuit.contadoria.util.controller.RestUtil;
import br.jus.jfsp.nuit.contadoria.util.converter.DirectionConverter;
import br.jus.jfsp.nuit.contadoria.util.converter.IrsmConverter;
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

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/irsm")
public class IrsmController {
	
	@Autowired
	private IrsmService service;
	private final IrsmConverter irsmConverter;
	private final PagedResourcesAssembler assembler;

	public IrsmController(IrsmService service,
						  IrsmConverter irsmConverter,
						  PagedResourcesAssembler assembler) {
		this.service = service;
		this.irsmConverter = irsmConverter;
		this.assembler = assembler;
	}

	@GetMapping("/importa")
	public ResponseEntity<?> importa() {
		service.importa();		
		return ResponseEntity.ok("OK");
	}

	@GetMapping("/export")
	@Hateoas
	public Iterable<IrsmTO> listAll(
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Iterable<IrsmTO> retorno = irsmConverter.toTransferObject(service.findAll());
		return retorno;
	}
	
	@PostMapping
	@Hateoas
	public ResponseEntity<IrsmTO> create(@RequestBody IrsmTO irsmTO) throws RecordNotFoundException {
		Irsm irsm = service.create(irsmConverter.toEntity(irsmTO));
		IrsmTO to = irsmConverter.toTransferObject(irsm);
		return RestUtil.createWithLocation(getClass(), to);
	}

	@PutMapping
	@Hateoas
	public ResponseEntity<IrsmTO> update(@RequestBody @Valid IrsmTO irsmTO) throws RecordNotFoundException {
		Irsm irsm = service.update(irsmConverter.toEntity(irsmTO));
		return ResponseEntity.ok(irsmConverter.toTransferObject(irsm));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) throws RecordNotFoundException {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	@Hateoas
	public ResponseEntity<IrsmTO> read(@PathVariable("id") Long id) throws RecordNotFoundException {
		return ResponseEntity.ok(irsmConverter.toTransferObject(service.read(id)));
	}

	@GetMapping
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<IrsmTO>>> list(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<IrsmTO> pageResult = irsmConverter.toTransferObject(service.findAll(pageable));
		PagedModel<EntityModel<IrsmTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

	@GetMapping("/like/{like}")
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<IrsmTO>>> listLike(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy,
			@PathVariable("like") String like) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<IrsmTO> pageResult = irsmConverter.toTransferObject(service.findLike(pageable, like));
		PagedModel<EntityModel<IrsmTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}
//	@GetMapping(path = "/{data}")
//	public @ResponseBody
//	Optional<Irsm> find(@PathVariable("data") String data) throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = sdf.parse(data);
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		return service.findByData(data);
//	}
//
//	@GetMapping(path = "/{data}/{data2}")
//	public @ResponseBody
//	Iterable<Irsm> find2(@PathVariable("data") String data, @PathVariable("data2") String data2) throws ParseException {
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

package br.jus.jfsp.nuit.contadoria.controllers;

import br.jus.jfsp.nuit.contadoria.aspect.Hateoas;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.Ipca;
import br.jus.jfsp.nuit.contadoria.models.Ipca;
import br.jus.jfsp.nuit.contadoria.service.Ipca15Service;
import br.jus.jfsp.nuit.contadoria.service.IpcaService;
import br.jus.jfsp.nuit.contadoria.to.IpcaTO;
import br.jus.jfsp.nuit.contadoria.to.IpcaTO;
import br.jus.jfsp.nuit.contadoria.util.controller.RestUtil;
import br.jus.jfsp.nuit.contadoria.util.converter.DirectionConverter;
import br.jus.jfsp.nuit.contadoria.util.converter.Ipca15Converter;
import br.jus.jfsp.nuit.contadoria.util.converter.IpcaConverter;
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
@RequestMapping("/api/ipca")
public class IpcaController {
	
	@Autowired
	private IpcaService service;
	private final IpcaConverter ipcaConverter;
	private final PagedResourcesAssembler assembler;

	public IpcaController(IpcaService service,
							IpcaConverter ipcaConverter,
							PagedResourcesAssembler assembler) {
		this.service = service;
		this.ipcaConverter = ipcaConverter;
		this.assembler = assembler;
	}

	@GetMapping("/importa")
	public ResponseEntity<?> importa() {
		service.importa();		
		return ResponseEntity.ok("OK");
	}

	@GetMapping("/export")
	@Hateoas
	public Iterable<IpcaTO> listAll(
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Iterable<IpcaTO> retorno = ipcaConverter.toTransferObject(service.findAll());
		return retorno;
	}

	@PostMapping
	@Hateoas
	public ResponseEntity<IpcaTO> create(@RequestBody IpcaTO ipcaTO) throws RecordNotFoundException {
		Ipca ipca = service.create(ipcaConverter.toEntity(ipcaTO));
		IpcaTO to = ipcaConverter.toTransferObject(ipca);
		return RestUtil.createWithLocation(getClass(), to);
	}

	@PutMapping
	@Hateoas
	public ResponseEntity<IpcaTO> update(@RequestBody @Valid IpcaTO ipcaTO) throws RecordNotFoundException {
		Ipca ipca = service.update(ipcaConverter.toEntity(ipcaTO));
		return ResponseEntity.ok(ipcaConverter.toTransferObject(ipca));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) throws RecordNotFoundException {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	@Hateoas
	public ResponseEntity<IpcaTO> read(@PathVariable("id") Long id) throws RecordNotFoundException {
		return ResponseEntity.ok(ipcaConverter.toTransferObject(service.read(id)));
	}

	@GetMapping
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<IpcaTO>>> list(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<IpcaTO> pageResult = ipcaConverter.toTransferObject(service.findAll(pageable));
		PagedModel<EntityModel<IpcaTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

	@GetMapping("/like/{like}")
	@Hateoas
	public ResponseEntity<PagedModel<EntityModel<IpcaTO>>> listLike(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "sort", defaultValue = "data") String[] sortBy,
			@PathVariable("like") String like) throws RecordNotFoundException {
		Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
		Page<IpcaTO> pageResult = ipcaConverter.toTransferObject(service.findLike(pageable, like));
		PagedModel<EntityModel<IpcaTO>> pagedModel = assembler.toModel(pageResult);
		return ResponseEntity.ok(pagedModel);
	}

//	@GetMapping(path = "/{data}")
//	public @ResponseBody
//	Optional<Ipca> find(@PathVariable("data") String data) throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = sdf.parse(data);
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		return service.findByData(data);
//	}
//
//	@GetMapping(path = "/{data}/{data2}")
//	public @ResponseBody
//	Iterable<Ipca> find2(@PathVariable("data") String data, @PathVariable("data2") String data2) throws ParseException {
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

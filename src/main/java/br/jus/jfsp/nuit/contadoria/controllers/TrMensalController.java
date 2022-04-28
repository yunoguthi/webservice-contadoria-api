package br.jus.jfsp.nuit.contadoria.controllers;

import br.jus.jfsp.nuit.contadoria.aspect.Hateoas;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.TrMensal;
import br.jus.jfsp.nuit.contadoria.service.TrMensalService;
import br.jus.jfsp.nuit.contadoria.to.TrMensalTO;
import br.jus.jfsp.nuit.contadoria.util.controller.RestUtil;
import br.jus.jfsp.nuit.contadoria.util.converter.DirectionConverter;
import br.jus.jfsp.nuit.contadoria.util.converter.TrMensalConverter;
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
@RequestMapping("/api/trMensal")
public class TrMensalController {

    @Autowired
    private TrMensalService service;
    private final TrMensalConverter trMensalConverter;
    private final PagedResourcesAssembler assembler;

    public TrMensalController(TrMensalService service,
                          TrMensalConverter trMensalConverter,
                          PagedResourcesAssembler assembler) {
        this.service = service;
        this.trMensalConverter = trMensalConverter;
        this.assembler = assembler;
    }

    @GetMapping("/importa")
    public ResponseEntity.BodyBuilder importaTrMensal(){
        service.importa();
        return ResponseEntity.status(200);
    }

    @GetMapping("/export")
    @Hateoas
    public Iterable<TrMensalTO> listAll(
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
        Iterable<TrMensalTO> retorno = trMensalConverter.toTransferObject(service.findAll());
        return retorno;
    }
    
    @PostMapping
    @Hateoas
    public ResponseEntity<TrMensalTO> create(@RequestBody TrMensalTO trMensalTO) throws RecordNotFoundException {
        TrMensal trMensal = service.create(trMensalConverter.toEntity(trMensalTO));
        TrMensalTO to = trMensalConverter.toTransferObject(trMensal);
        return RestUtil.createWithLocation(getClass(), to);
    }

    @PutMapping
    @Hateoas
    public ResponseEntity<TrMensalTO> update(@RequestBody @Valid TrMensalTO trMensalTO) throws RecordNotFoundException {
        TrMensal trMensal = service.update(trMensalConverter.toEntity(trMensalTO));
        return ResponseEntity.ok(trMensalConverter.toTransferObject(trMensal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) throws RecordNotFoundException {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Hateoas
    public ResponseEntity<TrMensalTO> read(@PathVariable("id") Long id) throws RecordNotFoundException {
        return ResponseEntity.ok(trMensalConverter.toTransferObject(service.read(id)));
    }

    @GetMapping
    @Hateoas
    public ResponseEntity<PagedModel<EntityModel<TrMensalTO>>> list(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "sort", defaultValue = "data") String[] sortBy) throws RecordNotFoundException {
        Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
        Page<TrMensalTO> pageResult = trMensalConverter.toTransferObject(service.findAll(pageable));
        PagedModel<EntityModel<TrMensalTO>> pagedModel = assembler.toModel(pageResult);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/like/{like}")
    @Hateoas
    public ResponseEntity<PagedModel<EntityModel<TrMensalTO>>> listLike(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "sort", defaultValue = "data") String[] sortBy,
            @PathVariable("like") String like) throws RecordNotFoundException {
        Pageable pageable = PageRequest.of(page, size, DirectionConverter.from(direction), sortBy);
        Page<TrMensalTO> pageResult = trMensalConverter.toTransferObject(service.findLike(pageable, like));
        PagedModel<EntityModel<TrMensalTO>> pagedModel = assembler.toModel(pageResult);
        return ResponseEntity.ok(pagedModel);
    }

//
//    @GetMapping(path = "/{data}")
//    public @ResponseBody
//    Optional<TrMensal> find(@PathVariable("data") String data) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = sdf.parse(data);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        return service.findByData(cal);
//    }
//
//    @GetMapping(path = "/{data}/{data2}")
//    public @ResponseBody
//    Iterable<TrMensal> find2(@PathVariable("data") String data, @PathVariable("data2") String data2) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = sdf.parse(data);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//
//        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
//        Date date2 = sdf.parse(data2);
//        Calendar cal2 = Calendar.getInstance();
//        cal2.setTime(date2);
//
//        return service.findByDataBetween(cal, cal2);
//    }
}

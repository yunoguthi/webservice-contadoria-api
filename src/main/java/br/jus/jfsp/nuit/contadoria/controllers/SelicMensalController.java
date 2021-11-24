package br.jus.jfsp.nuit.contadoria.controllers;

import br.jus.jfsp.nuit.contadoria.models.SelicMensal;
import br.jus.jfsp.nuit.contadoria.service.SelicMensalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/selicMensal")
public class SelicMensalController {

    @Autowired
    private SelicMensalService service;

    @GetMapping("/importa")
    public ResponseEntity<?> importaSelicMensal(){
        service.importa();
        return ResponseEntity.ok("OK");
    }
    @GetMapping()
    public @ResponseBody
    Iterable<SelicMensal> list() {
        return service.findAll();
    }

    @GetMapping(path = "/{data}")
    public @ResponseBody
    Optional<SelicMensal> find(@PathVariable("data") String data) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(data);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return service.findByData(cal);
    }

    @GetMapping(path = "/{data}/{data2}")
    public @ResponseBody
    Iterable<SelicMensal> find2(@PathVariable("data") String data, @PathVariable("data2") String data2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(data);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = sdf.parse(data2);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return service.findByDataBetween(cal, cal2);
    }
}

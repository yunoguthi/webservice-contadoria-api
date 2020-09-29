package br.com.jfsp.nuit.contadoria.controllers;

import br.com.jfsp.nuit.contadoria.services.TrMensalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/trMensal")
public class TrMensalController {

    @Autowired
    private TrMensalService service;

    @GetMapping("/importa")
    public ResponseEntity<?> importaTrMensal(){
        service.importa();
        return ResponseEntity.ok("OK");
    }

}

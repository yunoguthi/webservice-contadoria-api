package br.com.jfsp.nuit.contadoria.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jfsp.nuit.contadoria.models.Urv;
import br.com.jfsp.nuit.contadoria.repository.UrvRepository;
import br.com.jfsp.nuit.contadoria.util.ManipulaData;

@Service
public class UrvService extends SgsBacenService{

	@Autowired
	private UrvRepository repository;
	

	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private UrlReaderService urlReaderService;
	
public void importa() {
		
		Calendar dataInicial = repository.findMaxData();
		
		String conteudoUrl = "";
		try {
			conteudoUrl = urlReaderService.getConteudo(getUrl(URV, ManipulaData.toDate(dataInicial)));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			Object[] map = jsonReader.getJsonArray(conteudoUrl);
			for (int i = 0; i < map.length; i++) {
				LinkedHashMap lMap = (LinkedHashMap) map[i];
				Date data;
				try {
					data = ManipulaData.stringToDateDiaMesAno(lMap.get("data")+"");
				} catch (ParseException e) {
					e.printStackTrace();
					continue;
				}
				Double valor = new Double(lMap.get("valor")+"");
				Urv urv = new Urv();
				urv.setData(ManipulaData.toCalendar(data));
				urv.setValor(valor);
				
				if (!repository.existsByData(ManipulaData.toCalendar(data))) {
					repository.save(urv);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}

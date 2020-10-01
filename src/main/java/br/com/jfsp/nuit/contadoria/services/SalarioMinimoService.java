package br.com.jfsp.nuit.contadoria.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jfsp.nuit.contadoria.models.SalarioMinimo;
import br.com.jfsp.nuit.contadoria.repository.SalarioMinimoRepository;
import br.com.jfsp.nuit.contadoria.util.ManipulaData;

@Service
public class SalarioMinimoService extends SgsBacenService {
	
	
	@Autowired
	private SalarioMinimoRepository repository;
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReader;
	
	public void importa() {
			
		Calendar dataInicial = repository.findMaxData();
		
		String conteudoUrl = "";
		try {
			conteudoUrl = urlReader.getConteudo(getUrl(SALARIO_MINIMO, ManipulaData.toDate(dataInicial)));
			
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
				SalarioMinimo sm = new SalarioMinimo();
				sm.setData(ManipulaData.toCalendar(data));
				sm.setValor(valor);
				if (!repository.existsByData(ManipulaData.toCalendar(data))) {
					repository.save(sm);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
}

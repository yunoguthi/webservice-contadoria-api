package br.com.jfsp.nuit.contadoria.services;

import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jfsp.nuit.contadoria.models.TrMensal;
import br.com.jfsp.nuit.contadoria.repository.TrMensalRepository;
import br.com.jfsp.nuit.contadoria.util.ManipulaData;

@Service
public class TrMensalService extends SgsBacenService {
	
	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private UrlReaderService urlReaderService;
	
	@Autowired
	private TrMensalRepository repository;
	
	public void importa() {
		
		Date dataInicial = repository.findMaxData();
		
		String conteudoUrl = "";
		
		try {
			conteudoUrl = urlReaderService.getConteudo(getUrl(TR_MENSAL_PRIMEIRO_DIA, dataInicial));
		} catch (IOException e) {
			// TODO: handle exception
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
					// TODO: handle exception
					e.printStackTrace();
					continue;
				}
				Double valor = new Double(lMap.get("valor")+"");
				TrMensal trMensal = new TrMensal();
				trMensal.setData(data);
				trMensal.setValor(valor);
				if(!repository.existsByData(data)) {
					repository.save(trMensal);
				}
			}
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		
	}

}

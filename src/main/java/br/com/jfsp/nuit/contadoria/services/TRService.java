package br.com.jfsp.nuit.contadoria.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jfsp.nuit.contadoria.models.TR;
import br.com.jfsp.nuit.contadoria.repository.TRRepository;
import br.com.jfsp.nuit.contadoria.util.ManipulaData;

@Service
public class TRService {
	
	private static final String url = "https://api.bcb.gov.br/dados/serie/bcdata.sgs.4174/dados?formato=json";
	
	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private UrlReaderService urlReaderService;
	
	@Autowired
	private TRRepository repository;
	
	private String adicionaComplementos() {
		String retorno = "";
		Date data = repository.findMaxData();
		if (data!=null) {
			retorno += "&dataInicial="+ManipulaData.dateToStringDiaMesAno(data)+"&dataFinal=" + ManipulaData.dateToStringDiaMesAno(new Date());
		}
		return retorno;
	}
	
	public void importa() {
		String conteudoUrl = "";
		try {
			conteudoUrl = urlReaderService.getConteudo(url + adicionaComplementos());
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
				TR tr = new TR();
				tr.setData(data);
				tr.setValor(valor);
				if (!repository.existsByData(data)) {
					repository.save(tr);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

package br.com.jfsp.nuit.contadoria.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jfsp.nuit.contadoria.models.Trd;
import br.com.jfsp.nuit.contadoria.repository.TrdRepository;
import br.com.jfsp.nuit.contadoria.util.ManipulaData;

@Service
public class TrdService extends SgsBacenService {
	
	//private static final String url = "https://api.bcb.gov.br/dados/serie/bcdata.sgs.4174/dados?formato=json";
	//private static final String urlCsv = "https://www3.bcb.gov.br/sgspub/pefi300/consultarTaxasJuros.do?method=downloadUC47&RN=UC47.RN01";

	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private UrlReaderService urlReaderService;
	
	@Autowired
	private TrdRepository repository;
	
	public void importa() {
		
		Date dataInicial = repository.findMaxData();
		
		String conteudoUrl = "";
		try {
			conteudoUrl = urlReaderService.getConteudo(getUrl(TRD, dataInicial));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			Object[] map = jsonReader.getJsonArray(conteudoUrl);
			for (int i = 0; i < map.length; i++) {
				LinkedHashMap lMap = (LinkedHashMap) map[i];
				Date data;
				Date dataFim;

				try {
					data = ManipulaData.stringToDateDiaMesAno(lMap.get("data")+"");
					dataFim = ManipulaData.stringToDateDiaMesAno(lMap.get("datafim")+"");

				} catch (ParseException e) {
					e.printStackTrace();
					continue;
				}
				Double valor = new Double(lMap.get("valor")+"");
				Trd tr = new Trd();
				tr.setData(data);
				tr.setDataFim(dataFim);
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
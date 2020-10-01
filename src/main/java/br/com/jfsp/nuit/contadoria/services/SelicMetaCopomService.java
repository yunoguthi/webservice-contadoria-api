package br.com.jfsp.nuit.contadoria.services;

import java.io.IOException;
import java.util.Date;
import java.util.Calendar;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jfsp.nuit.contadoria.models.SelicMetaCopom;
import br.com.jfsp.nuit.contadoria.repository.SelicMetaCopomRepository;
import br.com.jfsp.nuit.contadoria.util.ManipulaData;

@Service
public class SelicMetaCopomService extends SgsBacenService{
	
	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private UrlReaderService urlReaderService;
	
	@Autowired
	private SelicMetaCopomRepository repository;
	
	public void importa() {
		
		Calendar dataInicial = repository.findMaxData();
		
		String conteudoUrl = "";
		try {
			conteudoUrl = urlReaderService.getConteudo(getUrl(SELIC_META_COPOM, ManipulaData.toDate(dataInicial)));
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			Object[] map = jsonReader.getJsonArray(conteudoUrl);
			for (int i = 0; i < map.length; i++) {
				LinkedHashMap lMap = (LinkedHashMap) map[i];
				Date data;
				try {
					data = ManipulaData.stringToDateDiaMesAno(lMap.get("data")+"");
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					continue;
				}
				Double valor = new Double(lMap.get("valor")+"");
				SelicMetaCopom selicMetaCopom = new SelicMetaCopom();
				selicMetaCopom.setData(ManipulaData.toCalendar(data));
				selicMetaCopom.setValor(valor);
				selicMetaCopom.setUltimaAtualizacao(ManipulaData.getHoje());
				if (!repository.existsByData(ManipulaData.toCalendar(data))) {
					repository.save(selicMetaCopom);
				}
			}
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

}

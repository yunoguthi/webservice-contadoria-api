package br.com.jfsp.nuit.contadoria.services;

import java.io.IOException;
import java.util.Date;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import br.com.jfsp.nuit.contadoria.models.IpcaE;
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

	public List<SelicMetaCopom> findAll() {
		return repository.findAll();
	}

	public Optional<SelicMetaCopom> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<SelicMetaCopom> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}

}

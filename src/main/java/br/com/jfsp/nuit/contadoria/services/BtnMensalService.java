package br.com.jfsp.nuit.contadoria.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jfsp.nuit.contadoria.models.BtnMensal;
import br.com.jfsp.nuit.contadoria.repository.BtnMensalRepository;
import br.com.jfsp.nuit.contadoria.util.ManipulaData;

@Service
public class BtnMensalService extends SgsBacenService{
	
	@Autowired
	private BtnMensalRepository repository;
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReader;
	
	public void importa() {
			
		Calendar dataInicial = repository.findMaxData();
		
		String conteudoUrl = "";
		try {
			conteudoUrl = urlReader.getConteudo(getUrl(BTN_MENSAL, ManipulaData.toDate(dataInicial)));
			
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
				BtnMensal bm = new BtnMensal();
				bm.setData(ManipulaData.toCalendar(data));
				bm.setValor(valor);
				if (!repository.existsByData(ManipulaData.toCalendar(data))) {
					repository.save(bm);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	public List<BtnMensal> findAll() {
		return repository.findAll();
	}

	public Optional<BtnMensal> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<BtnMensal> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}

}

package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.models.TrMensal;
import br.jus.jfsp.nuit.contadoria.repository.TrMensalRepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
public class TrMensalService extends SgsBacenService {
	
	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private UrlReaderService urlReaderService;
	
	@Autowired
	private TrMensalRepository repository;
	
	public void importa() {
		
		Calendar dataInicial = repository.findMaxData();
		
		String conteudoUrl = "";
		
		try {
			conteudoUrl = urlReaderService.getConteudo(getUrl(TR_MENSAL_PRIMEIRO_DIA, ManipulaData.toDate(dataInicial)));
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
				trMensal.setData(ManipulaData.toCalendar(data));
				trMensal.setValor(valor);
				trMensal.setUltimaAtualizacao(ManipulaData.getHoje());
				if(!repository.existsByData(ManipulaData.toCalendar(data))) {
					repository.save(trMensal);
				}
			}
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		
	}

	public List<TrMensal> findAll() {
		return repository.findAll();
	}

	public Optional<TrMensal> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<TrMensal> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}

}

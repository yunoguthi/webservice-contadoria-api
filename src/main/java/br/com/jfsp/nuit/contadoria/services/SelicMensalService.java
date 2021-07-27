package br.com.jfsp.nuit.contadoria.services;

import br.com.jfsp.nuit.contadoria.models.IpcaE;
import br.com.jfsp.nuit.contadoria.models.SelicMensal;
import br.com.jfsp.nuit.contadoria.repository.SelicMensalRepository;
import br.com.jfsp.nuit.contadoria.util.ManipulaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;


@Service
public class SelicMensalService extends SgsBacenService{
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReaderService;

	@Autowired
	private SelicMensalRepository repository;

	public void importa(){

		Calendar dataInicial = repository.findMaxData();

		String conteudoUrl = "";

		try{
			conteudoUrl = urlReaderService.getConteudo(getUrl(SELIC_MENSAL, ManipulaData.toDate(dataInicial)));
		}catch (IOException e){
			e.printStackTrace();
		}
		try{
			Object[] map = jsonReader.getJsonArray(conteudoUrl);
			for (int i = 0; i < map.length; i++) {
				LinkedHashMap lMap = (LinkedHashMap) map[i];
				Date data;
				try{
					data = ManipulaData.stringToDateDiaMesAno(lMap.get("data")+"");
				}catch (Exception e){
					e.printStackTrace();
					continue;
				}
				Double valor = new Double(lMap.get("valor")+"");
				SelicMensal selicMensal = new SelicMensal();
				selicMensal.setData(ManipulaData.toCalendar(data));
				selicMensal.setValor(valor);
				selicMensal.setUltimaAtualizacao(ManipulaData.getHoje());
				if(!repository.existsByData(ManipulaData.toCalendar(data))){
					repository.save(selicMensal);
				}
			}
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	public List<SelicMensal> findAll() {
		return repository.findAll();
	}

	public Optional<SelicMensal> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<SelicMensal> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}

}

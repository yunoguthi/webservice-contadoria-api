package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.models.IpcaE;
import br.jus.jfsp.nuit.contadoria.repository.IpcaERepository;
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
public class IpcaEService extends SgsBacenService {
	
	@Autowired
	private IpcaERepository repository;
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReader;
	
	public void importa() {

		Calendar dataInicial = repository.findMaxData();

		String conteudoUrl = "";
		try {
			conteudoUrl = urlReader.getConteudo(getUrl(IPCA_E, ManipulaData.toDate(dataInicial)));

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
					data = ManipulaData.stringToDateDiaMesAno(lMap.get("data") + "");
				} catch (ParseException e) {
					e.printStackTrace();
					continue;
				}
				Double valor = new Double(lMap.get("valor") + "");
				IpcaE ipcae = new IpcaE();
				ipcae.setData(ManipulaData.toCalendar(data));
				ipcae.setValor(valor);
				if (!repository.existsByData(ManipulaData.toCalendar(data))) {
					repository.save(ipcae);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<IpcaE> findAll() {
		return repository.findAll();
	}

	public Optional<IpcaE> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<IpcaE> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}



}

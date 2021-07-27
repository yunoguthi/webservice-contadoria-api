package br.com.jfsp.nuit.contadoria.services;

import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import br.com.jfsp.nuit.contadoria.models.Ipca15;
import br.com.jfsp.nuit.contadoria.models.IpcaE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jfsp.nuit.contadoria.models.Ipca;
import br.com.jfsp.nuit.contadoria.repository.IpcaRepository;
import br.com.jfsp.nuit.contadoria.util.ManipulaData;

@Service
public class IpcaService extends SidraIbgeService {
	
	protected static final String 
		IPCA =  "1737",
	    IPCA_NUMERO_INDICE =  "2266",
	    IPCA_PERCENTUAL_MES =  "63",
	    PRECISAO_NUMERO_INDICE = "13",
	    PRECISAO_PERCENTUAL_MES = "2"
	    ;
	
	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private UrlReaderService urlReaderService;
	
	@Autowired
	private IpcaRepository repository;
	
	public void importa() {
		
		String dataInicial = repository.findMaxData();
		
		String conteudoUrl = "";
		try {
			String[] indices = {IPCA_NUMERO_INDICE,IPCA_PERCENTUAL_MES};
			String[] precisoes = {PRECISAO_NUMERO_INDICE, PRECISAO_PERCENTUAL_MES};
			conteudoUrl = urlReaderService.getConteudo(getUrl(IPCA, dataInicial, ManipulaData.dateToStringAnoMes(ManipulaData.getHoje()), indices, precisoes));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			Object[] map = jsonReader.getJsonArray(conteudoUrl);
			for (int i = 0; i < map.length; i++) {
				LinkedHashMap lMap = (LinkedHashMap) map[i];				
				Ipca ipca = new Ipca();
				String ano = "";
				String mes = "";
				String data = "";
				Double numeroIndice = null;
				Float variacaoMensal = null;
				
				try {
					data = (lMap.get(DATA)+"");
					if (!data.matches("[0-9]+")) {
						continue;
					}
					if (data != null && !data.equals("") && data.length() == 6) {
						ano = data.substring(0, 4);
						mes = data.substring(4, 6);
					}
					if (lMap.get(CODIGO_VARIAVEL).equals(IPCA_NUMERO_INDICE)) {
						numeroIndice = new Double(lMap.get(VALOR)+"");	
						ipca.setNumeroIndice(numeroIndice);
					} else if (lMap.get(CODIGO_VARIAVEL).equals(IPCA_PERCENTUAL_MES)) {
						try {
							variacaoMensal = new Float(lMap.get(VALOR)+"");	
							ipca.setVariacaoMensal(variacaoMensal);
						} catch (Exception e) {
							System.out.println(lMap.get(VALOR)+"");
						}
					}
					ipca.setData(data);
					ipca.setAno(ano);
					ipca.setMes(mes);
					ipca.setUltimaAtualizacao(ManipulaData.getHoje());
					save(ipca);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Ipca save(Ipca ipca) {
		if (!repository.existsByData(ipca.getData())) {
			
			return repository.save(ipca);
		} else {
			Optional<Ipca> i = repository.findByData(ipca.getData());
			ipca.setId(i.get().getId());
			ipca.setAno(i.get().getAno());
			ipca.setMes(i.get().getMes());
			if (ipca.getNumeroIndice()==null) {
				ipca.setNumeroIndice(i.get().getNumeroIndice());
			}
			if (ipca.getVariacaoMensal()==null) {
				ipca.setVariacaoMensal(i.get().getVariacaoMensal());
			}
			return repository.save(ipca);
		}
	}

	public List<Ipca> findAll() {
		return repository.findAll();
	}

	public Optional<Ipca> findByData(String data) {
		return repository.findByData(data);
	}

	public Iterable<Ipca> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}

}
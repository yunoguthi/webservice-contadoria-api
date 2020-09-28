package br.com.jfsp.nuit.contadoria.services;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jfsp.nuit.contadoria.models.Inpc;
import br.com.jfsp.nuit.contadoria.repository.InpcRepository;
import br.com.jfsp.nuit.contadoria.util.ManipulaData;

@Service
public class InpcService extends SidraIbgeService {
	
	protected static final String 
		INPC = "1736",
		INPC_NUMERO_INDICE = "2289",
		INPC_PERCENTUAL_MES = "44",
	    PRECISAO_NUMERO_INDICE = "13",
	    PRECISAO_PERCENTUAL_MES = "2"
	    ;
	
	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private UrlReaderService urlReaderService;
	
	@Autowired
	private InpcRepository repository;
	
	public void importa() {
		
		String dataInicial = repository.findMaxData();
		
		String conteudoUrl = "";
		try {
			String[] indices = {INPC_NUMERO_INDICE,INPC_PERCENTUAL_MES};
			String[] precisoes = {PRECISAO_NUMERO_INDICE, PRECISAO_PERCENTUAL_MES};
			conteudoUrl = urlReaderService.getConteudo(getUrl(INPC, dataInicial, ManipulaData.dateToStringAnoMes(ManipulaData.getHoje()), indices, precisoes));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			Object[] map = jsonReader.getJsonArray(conteudoUrl);
			for (int i = 0; i < map.length; i++) {
				LinkedHashMap lMap = (LinkedHashMap) map[i];				
				Inpc inpc = new Inpc();
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
					if (lMap.get(CODIGO_VARIAVEL).equals(INPC_NUMERO_INDICE)) {
						numeroIndice = new Double(lMap.get(VALOR)+"");	
						inpc.setNumeroIndice(numeroIndice);
					} else if (lMap.get(CODIGO_VARIAVEL).equals(INPC_PERCENTUAL_MES)) {
						try {
							variacaoMensal = new Float(lMap.get(VALOR)+"");	
							inpc.setVariacaoMensal(variacaoMensal);
						} catch (Exception e) {
							System.out.println(lMap.get(VALOR)+"");
						}
					}
					inpc.setData(data);
					inpc.setAno(ano);
					inpc.setMes(mes);
					inpc.setUltimaAtualizacao(ManipulaData.getHoje());
					save(inpc);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Inpc save(Inpc inpc) {
		if (!repository.existsByData(inpc.getData())) {
			
			return repository.save(inpc);
		} else {
			Optional<Inpc> i = repository.findByData(inpc.getData());
			inpc.setId(i.get().getId());
			inpc.setAno(i.get().getAno());
			inpc.setMes(i.get().getMes());
			if (inpc.getNumeroIndice()==null) {
				inpc.setNumeroIndice(i.get().getNumeroIndice());
			}
			if (inpc.getVariacaoMensal()==null) {
				inpc.setVariacaoMensal(i.get().getVariacaoMensal());
			}
			return repository.save(inpc);
		}
	}
	
	
}
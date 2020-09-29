package br.com.jfsp.nuit.contadoria.services;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jfsp.nuit.contadoria.models.Ipca15;
import br.com.jfsp.nuit.contadoria.repository.Ipca15Repository;
import br.com.jfsp.nuit.contadoria.util.ManipulaData;

@Service
public class Ipca15Service extends SidraIbgeService {
	
	protected static final String 	
		IPCA15 =  "1705",
		//IPCA15_PERCENTUAL_MES =  "355/c315/7169",
		IPCA15_PERCENTUAL_MES =  "355",
	    PRECISAO_PERCENTUAL_MES = "2",
	    COMPLEMENTO = "/c315/7169"
	    ;
	
	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private UrlReaderService urlReaderService;
	
	@Autowired
	private Ipca15Repository repository;
	
	@Override
	protected String getUrl(String codigo, String dataInicial, String dataFinal, String[] variaveis, String[] precisoes) {
		System.out.println(urlInicio + codigo + url2 
				+ montaVariavel(variaveis) + url3 + adicionaComplementos(dataInicial, dataFinal) +
				montaPrecisao(variaveis, precisoes) + url6);
		return urlInicio + codigo + url2 
				+ montaVariavel(variaveis) + url3 + adicionaComplementos(dataInicial, dataFinal) +
				montaPrecisao(variaveis, precisoes) + COMPLEMENTO + url6;
	}
	
	public void importa() {
		
		String dataInicial = repository.findMaxData();
		
		String conteudoUrl = "";
		try {
			String[] indices = {IPCA15_PERCENTUAL_MES};
			String[] precisoes = {PRECISAO_PERCENTUAL_MES};
			conteudoUrl = urlReaderService.getConteudo(getUrl(IPCA15, dataInicial, ManipulaData.dateToStringAnoMes(ManipulaData.getHoje()), indices, precisoes));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			Object[] map = jsonReader.getJsonArray(conteudoUrl);
			for (int i = 0; i < map.length; i++) {
				LinkedHashMap lMap = (LinkedHashMap) map[i];				
				Ipca15 ipca15 = new Ipca15();
				String ano = "";
				String mes = "";
				String data = "";
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
					if (lMap.get(CODIGO_VARIAVEL).equals(IPCA15_PERCENTUAL_MES)) {
						try {
							variacaoMensal = new Float(lMap.get(VALOR)+"");	
							ipca15.setVariacaoMensal(variacaoMensal);
						} catch (Exception e) {
							System.out.println(lMap.get(VALOR)+"");
						}
					}
					ipca15.setData(data);
					ipca15.setAno(ano);
					ipca15.setMes(mes);
					ipca15.setUltimaAtualizacao(ManipulaData.getHoje());
					save(ipca15);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Ipca15 save(Ipca15 ipca15) {
		if (!repository.existsByData(ipca15.getData())) {			
			return repository.save(ipca15);
		} else {
			Optional<Ipca15> i = repository.findByData(ipca15.getData());
			ipca15.setId(i.get().getId());
			ipca15.setAno(i.get().getAno());
			ipca15.setMes(i.get().getMes());
			if (ipca15.getVariacaoMensal()==null) {
				ipca15.setVariacaoMensal(i.get().getVariacaoMensal());
			}
			return repository.save(ipca15);
		}
	}
		
}
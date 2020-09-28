package br.com.jfsp.nuit.contadoria.services;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jfsp.nuit.contadoria.models.Irsm;
import br.com.jfsp.nuit.contadoria.repository.IrsmRepository;
import br.com.jfsp.nuit.contadoria.util.ManipulaData;

@Service
public class IrsmService extends SidraIbgeService {
	
	protected static final String 	
		IRSM = "90",
		//IRSM_PERCENTUAL_MES = "64/C73/2639",
		IRSM_PERCENTUAL_MES = "64",
	    PRECISAO_PERCENTUAL_MES = "2",
	    COMPLEMENTO = "/C73/2639"
	    ;
	
	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private UrlReaderService urlReaderService;
	
	@Autowired
	private IrsmRepository repository;
	
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
			String[] indices = {IRSM_PERCENTUAL_MES};
			String[] precisoes = {PRECISAO_PERCENTUAL_MES};
			conteudoUrl = urlReaderService.getConteudo(getUrl(IRSM, dataInicial, ManipulaData.dateToStringAnoMes(ManipulaData.getHoje()), indices, precisoes));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			Object[] map = jsonReader.getJsonArray(conteudoUrl);
			for (int i = 0; i < map.length; i++) {
				LinkedHashMap lMap = (LinkedHashMap) map[i];				
				Irsm irsm = new Irsm();
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
					if (lMap.get(CODIGO_VARIAVEL).equals(IRSM_PERCENTUAL_MES)) {
						try {
							variacaoMensal = new Float(lMap.get(VALOR)+"");	
							irsm.setVariacaoMensal(variacaoMensal);
						} catch (Exception e) {
							System.out.println(lMap.get(VALOR)+"");
						}
					}
					irsm.setData(data);
					irsm.setAno(ano);
					irsm.setMes(mes);
					irsm.setUltimaAtualizacao(ManipulaData.getHoje());
					save(irsm);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Irsm save(Irsm irsm) {
		if (!repository.existsByData(irsm.getData())) {
			
			return repository.save(irsm);
		} else {
			Optional<Irsm> i = repository.findByData(irsm.getData());
			irsm.setId(i.get().getId());
			irsm.setAno(i.get().getAno());
			irsm.setMes(i.get().getMes());
			if (irsm.getVariacaoMensal()==null) {
				irsm.setVariacaoMensal(i.get().getVariacaoMensal());
			}
			return repository.save(irsm);
		}
	}
		
}
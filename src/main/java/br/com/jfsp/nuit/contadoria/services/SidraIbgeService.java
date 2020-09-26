package br.com.jfsp.nuit.contadoria.services;

import org.springframework.stereotype.Service;

@Service
public class SidraIbgeService {
	
	protected static final String 
		INPC = "1736",
		IRSM = "90",
		//IPCA =  "1737",
		IPCA15 =  "1705",
		INPC_NUMERO_INDICE = "2289",
	    INPC_PERCENTUAL_MES = "44",
	    IRSM_PERCENTUAL_MES = "64/C73/2639",
	    //IPCA_NUMERO_INDICE =  "2266",
	    IPCA_PERCENTUAL_MES =  "63",
	    IPCA15_PERCENTUAL_MES =  "355/c315/7169",
	    
	    
	    DATA = "D3C",
	    VALOR = "V",
	    CODIGO_VARIAVEL = "D2C";

	protected static final String 
		urlInicio = "https://apisidra.ibge.gov.br/values/t/",
		url2 = "/n1/all/v/",
		url3 = "/p/",
		url4 = "/d/",
		v = "v",
		url5 = "%20",
		url6 = "?formato=json";
	
	private String montaVariavel(String[] variaveis) {
		String retorno = "";
		for (int i = 0; i < variaveis.length; i++) {
			retorno += variaveis[i] + ",";
		}
		return retorno.substring(0, retorno.length()-1);
	}
	
	private String montaPrecisao(String[] variaveis, String[] precisoes) {
		String retorno = url4;
		for (int i = 0; i < variaveis.length; i++) {
			retorno += v + variaveis[i] + url5 + precisoes[i] + ",";
		}
		return retorno.substring(0, retorno.length()-1);
	}
	
	protected String getUrl(String codigo, String dataInicial, String dataFinal, String[] variaveis, String[] precisoes) {
		System.out.println(urlInicio + codigo + url2 
				+ montaVariavel(variaveis) + url3 + adicionaComplementos(dataInicial, dataFinal) +
				montaPrecisao(variaveis, precisoes) + url6);
		return urlInicio + codigo + url2 
				+ montaVariavel(variaveis) + url3 + adicionaComplementos(dataInicial, dataFinal) +
				montaPrecisao(variaveis, precisoes) + url6;
	}
	
	protected String adicionaComplementos(String dataInicial, String dataFinal) {
		if (dataInicial != null && !dataInicial.equals("") &&
			dataFinal != null && !dataFinal.equals("")) {
			return dataInicial + "-" + dataFinal;
		}
		return "all";
	}

}

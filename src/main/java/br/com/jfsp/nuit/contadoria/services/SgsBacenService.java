package br.com.jfsp.nuit.contadoria.services;

import java.util.Date;

import org.springframework.stereotype.Service;

import br.com.jfsp.nuit.contadoria.util.ManipulaData;

@Service
public class SgsBacenService {
	
	protected static final String 
		TRD = "226",
		TR_MENSAL_PRIMEIRO_DIA = "7811",
	    SELIC_MENSAL = "4390",
	    SELIC_META_COPOM = "432",
	    SALARIO_MINIMO = "1619",
	    URV = "13",
	    BTN_MENSAL = "108",
	    IPCA_E = "10764",
	    UFIR = "108";

	protected static final String 
		urlInicio = "https://api.bcb.gov.br/dados/serie/bcdata.sgs.",
		url2 = "/dados?formato=json";
	
	protected String getUrl(String codigo, Date dataInicial) {
		return urlInicio + codigo + url2 + adicionaComplementos(dataInicial);
	}
	
	protected String adicionaComplementos(Date dataInicial) {
		if (dataInicial != null) {
			return "&dataInicial="+ManipulaData.dateToStringDiaMesAno(dataInicial)+"&dataFinal=" + ManipulaData.dateToStringDiaMesAno(new Date());
		}
		return "";
	}

}

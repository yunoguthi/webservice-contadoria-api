package br.com.jfsp.nuit.contadoria.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.springframework.stereotype.Service;

@Service
public class UrlReaderService {
	
	public String getConteudo(String urlStr) throws IOException {
		String retorno = "";
		URL url = new URL(urlStr);
	    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
	    String linha;
	    while ((linha = reader.readLine()) != null) {
	      retorno+= linha;
	    }
	    reader.close();
		return retorno;
	}

}
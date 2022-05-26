package br.jus.jfsp.nuit.contadoria.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class UrlReaderService {
	
	public String getConteudo(String urlStr) throws IOException {
		String retorno = "";
        System.out.println("******** " + urlStr);
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         conn.setRequestMethod("GET");
         conn.setRequestProperty("Accept", "application/json");
 		conn.setRequestProperty("User-Agent", "Mozilla/5.0");
         if (conn.getResponseCode() != 200) {
             throw new RuntimeException("Erro: Código de erro HTTP:"
                 + conn.getResponseCode());
         }         
         BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
         String output;
         while ((output = br.readLine()) != null) {
        	 retorno += output;
         }
         conn.disconnect();
		return retorno;
	}

}
package br.com.jfsp.nuit.contadoria.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CarregaUrl {

	private String nomeUrl = "";
	private String conteudoUrl = "";
	
	public CarregaUrl(String nomeUrl) {
		this.nomeUrl = nomeUrl;
		setConteudoUrl();
	}
	
	public void setConteudoUrl() {
		try {
			URL url = new URL(nomeUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Request-Method", "GET");
			connection.setDoInput(true);  
			connection.setDoOutput(false); 
			connection.setInstanceFollowRedirects(true);
			connection.connect();
			
			System.out.println(connection.getResponseCode());
			System.out.println(connection.getResponseMessage());
			
			boolean redirect = false;
			
			int status = connection.getResponseCode();
			if (status != HttpURLConnection.HTTP_OK) {
				if (status == HttpURLConnection.HTTP_MOVED_TEMP
					|| status == HttpURLConnection.HTTP_MOVED_PERM
						|| status == HttpURLConnection.HTTP_SEE_OTHER)
				redirect = true;
			}
			if (redirect) {
				String newUrl = connection.getHeaderField("Location");
				System.out.println(newUrl);
				connection = (HttpURLConnection) new URL(newUrl).openConnection();
				System.out.println(connection.getResponseCode());
				System.out.println(connection.getResponseMessage());
				
				redirect = false;
				
				status = connection.getResponseCode();
				if (status != HttpURLConnection.HTTP_OK) {
					if (status == HttpURLConnection.HTTP_MOVED_TEMP
						|| status == HttpURLConnection.HTTP_MOVED_PERM
							|| status == HttpURLConnection.HTTP_SEE_OTHER)
					redirect = true;
				}
			}
				
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream())); 
			System.out.println(connection.getContentType());
			System.out.println(connection.getContent());

			StringBuffer newData = new StringBuffer();    
			String s = "";
			while (null != ((s = br.readLine()))) {  
			    newData.append(s + "\n");  
			}  
			br.close();
			conteudoUrl = newData.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
		
	public String getConteudoUrl() {
		return conteudoUrl;
	}

	
}

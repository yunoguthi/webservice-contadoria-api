package br.com.jfsp.nuit.contadoria.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ManipulaData {
	
	public static String dateToStringDiaMesAno(Date data) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
		String strDate = dateFormat.format(data); 	
		return strDate;
	}
	
	public static Date stringToDateDiaMesAno(String dataStr) throws ParseException {
	    Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dataStr);  
		return date;
	}

}

package br.jus.jfsp.nuit.contadoria.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ManipulaData {
	
	public static String 
		ANO_MES_DIA = "yyyy-MM-dd", // 2016-09-27
		DIA_MES_ANO = "dd/MM/yyyy", // 27/09/2016
		ANO_MES = "yyyyMM",			// 201609
	 	ANO_MES_DIA_SEM_TRACO = "yyyyMMdd";			// 20160927


	public static Calendar toCalendar(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		return calendar;
	}
	
	public static Date toDate(Calendar calendar) {
		return calendar!=null?calendar.getTime():null;
	}

	public static String calendarToStringAnoMes(Calendar data) {
		DateFormat dateFormat = new SimpleDateFormat(ANO_MES);
		String strDate = "";
		try {
			strDate = dateFormat.format(toDate(data));
		} catch (Exception e) {
		}
		return strDate;
	}

	public static String dateToStringAnoMes(Date data) {
		DateFormat dateFormat = new SimpleDateFormat(ANO_MES);  
		String strDate = dateFormat.format(data); 	
		return strDate;
	}
	
	public static String dateToStringDiaMesAno(Date data) {
		DateFormat dateFormat = new SimpleDateFormat(DIA_MES_ANO);  
		String strDate = dateFormat.format(data); 	
		return strDate;
	}
	
	public static Date stringToDateDiaMesAno(String dataStr) throws ParseException {
	    Date date = new SimpleDateFormat(DIA_MES_ANO).parse(dataStr);  
		return date;
	}

	public static Calendar getCalendar(String dataStr, String formato) {
		Calendar cal = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		try {
			cal.setTime(sdf.parse(dataStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cal;
	}
	
	public static Date getData(String dataStr, String formato) {
		Calendar cal = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat(formato); 
		try {
			cal.setTime(sdf.parse(dataStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cal.getTime();
	}
	
	public static String getData(Date data, String formato) {
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		sdf.applyLocalizedPattern(formato);
		return sdf.format(data);
	}
	
	public static String getDataStr(String dataStr, String formato1, String formato2, Locale locale) {
		SimpleDateFormat sdf = new SimpleDateFormat(formato1, locale); 
		SimpleDateFormat sdf2 = new SimpleDateFormat(formato2, locale); 
		Date d1;
		try {
			d1 = sdf.parse(dataStr);
			String sCertDate = sdf2.format(d1);
			return sCertDate;
		} catch (ParseException e) {
			return "";
		}
	}
	
	public static Date getHoje() {
		return new Date();
	}
	
	public static int comparaData(Date data1, Date data2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(data1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(data2);
		int dia1 = cal1.get(Calendar.DAY_OF_MONTH);
		int dia2 = cal2.get(Calendar.DAY_OF_MONTH);
		int mes1 = cal1.get(Calendar.MONTH);
		int mes2 = cal2.get(Calendar.MONTH);
		int ano1 = cal1.get(Calendar.YEAR);
		int ano2 = cal2.get(Calendar.YEAR);
		
		if (ano1<ano2) {
			return -1;
		} else if (ano1>ano2) {
			return 1;
		}
		if (mes1<mes2) {
			return -1;
		} else if (mes1>mes2) {
			return 1;
		}
		if (dia1<dia2) {
			return -1;
		} else if (dia1>dia2) {
			return 1;
		}
		return 0;
	}
	
	public static int comparaDiaDaSemana(Date data1, Date data2) {
		// retorn -1 se data1 antes de data2
		// retorna 1 se data1 depois de data2
		if (getDiaSemana(data1) > getDiaSemana(data2)) {
			return 1;
		} else if (getDiaSemana(data1) < getDiaSemana(data2)) {
			return -1;
		}
		return 0;
	}
	
	public static int comparaMes(Date data1, Date data2) {
		// retorn -1 se data1 antes de data2
		// retorna 1 se data1 depois de data2
		if (getAno(data1) > getAno(data2)) {
			return 1;
		} else if (getAno(data1) < getAno(data2)) {
			return -1;
		}
		if (getMes(data1) > getMes(data2)) {
			return 1;
		}
		if (getMes(data1) < getMes(data2)) {
			return -1;
		}
		return 0;
	}
	
	public static int getDiaSemana(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	public static int getDia(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	public static int getMes(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		return cal.get(Calendar.MONTH);
	}

	public static int getMesAlterado(Date data) {
		int mes = getMes(data);
		return mes++;
	}

	public static String getMesAlteradoStr(Date data) {
		int mes = getMesAlterado(data);
		if (mes < 10) {
			return "0" + mes;
		}
		return mes + "";
	}
	
	public static int getAno(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		return cal.get(Calendar.YEAR);
	}

}
package br.jus.jfsp.nuit.contadoria.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JsonReader {
	
	public Object[] getJsonArray(String jsonArray) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Object[] retorno = objectMapper.readValue(jsonArray, Object[].class);
		return retorno;
	}

	
}
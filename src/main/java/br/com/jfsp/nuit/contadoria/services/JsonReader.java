package br.com.jfsp.nuit.contadoria.services;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JsonReader {
	
	public Object[] getJsonArray(String jsonArray) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Object[] retorno = objectMapper.readValue(jsonArray, Object[].class);
		return retorno;
	}

	
}
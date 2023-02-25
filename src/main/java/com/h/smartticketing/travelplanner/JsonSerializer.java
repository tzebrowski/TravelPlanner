package com.h.smartticketing.travelplanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

final class JsonSerializer {

	static final ObjectMapper objectMapper = new ObjectMapper() {
		private static final long serialVersionUID = 1L;

		{
			configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			enable(SerializationFeature.INDENT_OUTPUT);
		}
	};
	
	String serialize(Object obj) throws JsonProcessingException {
		return objectMapper.writeValueAsString(obj);
	}

}

package org.springdoc.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springdoc.core.SwaggerUiOAuthProperties;

public class AbstractSwaggerIndexTransformer {

	protected SwaggerUiOAuthProperties swaggerUiOAuthProperties;

	protected ObjectMapper objectMapper;

	public AbstractSwaggerIndexTransformer(SwaggerUiOAuthProperties swaggerUiOAuthProperties, ObjectMapper objectMapper) {
		this.swaggerUiOAuthProperties = swaggerUiOAuthProperties;
		this.objectMapper = objectMapper;
	}

	protected String addInitOauth(String html) throws JsonProcessingException {
		StringBuilder stringBuilder = new StringBuilder("window.ui = ui\n");
		stringBuilder.append("ui.initOAuth(\n");
		String json = objectMapper.writeValueAsString(swaggerUiOAuthProperties.getConfigParameters());
		stringBuilder.append(json);
		stringBuilder.append(")");
		return html.replace("window.ui = ui", stringBuilder.toString());
	}

	protected String readFullyAsString(InputStream inputStream)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}
		return baos.toString(StandardCharsets.UTF_8.name());
	}
}

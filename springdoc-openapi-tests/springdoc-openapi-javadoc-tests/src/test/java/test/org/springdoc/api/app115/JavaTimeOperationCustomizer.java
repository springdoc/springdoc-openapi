package test.org.springdoc.api.app115;

import java.time.Duration;
import java.util.Map;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OperationCustomizer;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

/**
 * The type Java time operation customizer.
 */
@Component
class JavaTimeOperationCustomizer implements OperationCustomizer {
	/**
	 * Customize operation.
	 *
	 * @param operation the operation 
	 * @param handlerMethod the handler method 
	 * @return the operation
	 */
	@Override
	public Operation customize(Operation operation, HandlerMethod handlerMethod) {
		if (handlerMethod.getReturnType().getParameterType().isAssignableFrom(Duration.class)) {
			for (Map.Entry<String, io.swagger.v3.oas.models.responses.ApiResponse> entry : operation.getResponses().entrySet()) {
				io.swagger.v3.oas.models.responses.ApiResponse response = entry.getValue();
				Content content = response.getContent();
				if (content.containsKey(MediaType.APPLICATION_JSON_VALUE)) {
					Schema schema = content.get(MediaType.APPLICATION_JSON_VALUE).getSchema();
					schema.getProperties().clear();
					schema.setType("string");
				}
			}
		}
		return operation;
	}
}
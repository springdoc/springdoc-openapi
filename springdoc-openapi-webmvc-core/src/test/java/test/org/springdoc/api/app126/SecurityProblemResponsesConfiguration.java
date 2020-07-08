package test.org.springdoc.api.app126;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;

import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;

import org.springframework.beans.factory.parsing.Problem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

/**
 * Configuration class defining standard OpenAPI Specification for operations
*/
@Configuration
public class SecurityProblemResponsesConfiguration {

	private static final String HTTP_401_NO_TOKEN = "http401NoToken";
	private static final String HTTP_401_BAD_TOKEN = "http401BadToken";
	private static final String HTTP_403 = "http403";
	public static final String UNAUTHORIZED_401_NO_TOKEN_RESPONSE_REF = "#/components/responses/" + HTTP_401_NO_TOKEN;
	public static final String UNAUTHORIZED_401_BAD_TOKEN_RESPONSE_REF = "#/components/responses/" + HTTP_401_BAD_TOKEN;
	public static final String FORBIDDEN_403_RESPONSE_REF = "#/components/responses/" + HTTP_403;

	@Bean
	public Map.Entry<String, ApiResponse> http401NoTokenResponse() throws IOException {
		return simpleResponse(HTTP_401_NO_TOKEN, "Invalid authentication.");
	}
	
	@Bean
	public Map.Entry<String, ApiResponse> http401BadTokenResponse() throws IOException {
		return simpleResponse(HTTP_401_BAD_TOKEN, "Invalid authentication.");
	}

	@Bean
	public Map.Entry<String, ApiResponse> http403Example() throws IOException {
		return simpleResponse(HTTP_403, "Missing authorities.");
	}

	private Map.Entry<String, ApiResponse> simpleResponse(String code, String description) throws IOException {
		ApiResponse response = new ApiResponse().description(description).content(new Content().addMediaType(
				APPLICATION_PROBLEM_JSON_VALUE,
				new MediaType()
						.schema(new Schema<Problem>().$ref("#/components/schemas/Problem"))));
		return new AbstractMap.SimpleEntry<>(code, response);
	}

}

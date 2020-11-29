package org.springdoc.core.customizers;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.PathParameter;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.util.CollectionUtils;

import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Actuator open api customiser.
 * @author bnasslahsen
 */
public class ActuatorOpenApiCustomizer implements OpenApiCustomiser {

	/**
	 * The Path pathern.
	 */
	private final Pattern pathPathern = Pattern.compile("\\{(.*?)}");

	private WebEndpointProperties webEndpointProperties;

	public ActuatorOpenApiCustomizer(WebEndpointProperties webEndpointProperties) {
		this.webEndpointProperties = webEndpointProperties;
	}

	@Override
	public void customise(OpenAPI openApi) {
		openApi.getPaths().entrySet().stream()
				.filter(stringPathItemEntry -> stringPathItemEntry.getKey().startsWith(webEndpointProperties.getBasePath()+DEFAULT_PATH_SEPARATOR))
				.forEach(stringPathItemEntry -> {
					String path = stringPathItemEntry.getKey();
					Matcher matcher = pathPathern.matcher(path);
					while (matcher.find()) {
						String pathParam = matcher.group(1);
						PathItem pathItem = stringPathItemEntry.getValue();
						pathItem.readOperations().forEach(operation -> {
							List<Parameter> existingParameters = operation.getParameters();
							Optional<Parameter> existingParam = Optional.empty();
							if (!CollectionUtils.isEmpty(existingParameters))
								existingParam = existingParameters.stream().filter(p -> pathParam.equals(p.getName())).findAny();
							if (!existingParam.isPresent())
								operation.addParametersItem(new PathParameter().name(pathParam).schema(new StringSchema()));
						});
					}
				});
	}
}

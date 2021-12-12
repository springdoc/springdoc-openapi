package org.springdoc.core.customizers;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.PathParameter;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.util.CollectionUtils;

/**
 * The type Actuator open api customiser.
 * @author bnasslahsen
 */
public class ActuatorOpenApiCustomizer extends SpecFilter implements OpenApiCustomiser {

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
		openApi.getPaths().entrySet().removeIf(path -> !path.getKey().startsWith(webEndpointProperties.getBasePath()));
		openApi.getTags().removeIf(tag -> openApi.getPaths().entrySet().stream().anyMatch(pathItemEntry -> pathItemEntry.getValue().
				readOperations().stream().noneMatch(operation -> operation.getTags().contains(tag.getName()))));

		openApi.getPaths().entrySet().stream()
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

		super.removeBrokenReferenceDefinitions(openApi);
	}
}

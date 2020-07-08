package org.springdoc.core.customizers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.PathParameter;

/**
 * The type Actuator open api customiser.
 * @author bnasslahsen
 */
public class ActuatorOpenApiCustomiser implements OpenApiCustomiser {

	private final Pattern pathPathern = Pattern.compile("\\{(.*?)}");

	@Override
	public void customise(OpenAPI openApi) {
		openApi.getPaths().entrySet().stream()
				.filter(stringPathItemEntry -> stringPathItemEntry.getKey().startsWith("/actuator/"))
				.forEach(stringPathItemEntry -> {
					String path = stringPathItemEntry.getKey();
					Matcher matcher = pathPathern.matcher(path);
					while (matcher.find()) {
						String pathParam = matcher.group(1);
						PathItem pathItem = stringPathItemEntry.getValue();
						pathItem.readOperations().forEach(operation -> operation.addParametersItem(new PathParameter().name(pathParam).schema(new StringSchema())));
					}
				});
	}
}

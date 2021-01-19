package org.springdoc.hateoas.converters;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.customizers.OpenApiCustomiser;

/**
 * The type Open api hateoas links customizer.
 * Please use {@link OpenApiHateoasLinksCustomizer}, this class kept for back compatibility,
 * will be removed in 2.0.
 * @author bnasslahsen
 */
@Deprecated
public class OpenApiHateoasLinksCustomiser extends OpenApiHateoasLinksCustomizer implements OpenApiCustomiser {

	public OpenApiHateoasLinksCustomiser(SpringDocConfigProperties springDocConfigProperties) {
		super(springDocConfigProperties);
	}

	@Override
	public void customise(OpenAPI openApi) {
		customize(openApi);
	}
}

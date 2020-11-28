package org.springdoc.webmvc.core;

import org.springdoc.core.MultipleOpenApiSupportCondition;

import org.springframework.boot.actuate.autoconfigure.web.server.ConditionalOnManagementPort;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;

import static org.springdoc.core.Constants.SPRINGDOC_SHOW_ACTUATOR;

public class MultipleOpenApiMvcSupportCondition  extends AnyNestedCondition {

	/**
	 * Instantiates a new Multiple open api support condition.
	 */
	MultipleOpenApiMvcSupportCondition() {
		super(ConfigurationPhase.REGISTER_BEAN);
	}

	/**
	 * The type On multiple open api support condition.
	 * @author bnasslahsen
	 */
	@Conditional(MultipleOpenApiSupportCondition.class)
	static class OnMultipleOpenApiSupportCondition {}

	@ConditionalOnManagementPort(ManagementPortType.DIFFERENT)
	@ConditionalOnProperty(SPRINGDOC_SHOW_ACTUATOR)
	static class OnActuatorDifferentPort{}

}
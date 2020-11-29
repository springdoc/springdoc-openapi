package org.springdoc.core;

import org.springframework.boot.actuate.autoconfigure.web.server.ConditionalOnManagementPort;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;

import static org.springdoc.core.Constants.SPRINGDOC_SHOW_ACTUATOR;

/**
 * The type Multiple open api support condition.
 * @author bnasslashen
 */
public class MultipleOpenApiSupportCondition extends AnyNestedCondition {

	/**
	 * Instantiates a new Multiple open api support condition.
	 */
	MultipleOpenApiSupportCondition() {
		super(ConfigurationPhase.REGISTER_BEAN);
	}

	/**
	 * The type On multiple open api support condition.
	 * @author bnasslahsen
	 */
	@Conditional(MultipleOpenApiGroupsCondition.class)
	static class OnMultipleOpenApiSupportCondition {}

	/**
	 * The type On actuator different port.
	 * @author bnasslashen
	 */
	@ConditionalOnManagementPort(ManagementPortType.DIFFERENT)
	@ConditionalOnProperty(SPRINGDOC_SHOW_ACTUATOR)
	static class OnActuatorDifferentPort{}

}
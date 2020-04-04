package org.springdoc.core;


import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import static org.springdoc.core.Constants.GROUP_CONFIG_FIRST_PROPERTY;

public class MultipleOpenApiSupportCondition extends AnyNestedCondition {

	MultipleOpenApiSupportCondition() {
		super(ConfigurationPhase.REGISTER_BEAN);
	}

	@ConditionalOnBean(GroupedOpenApi.class)
	static class OnGroupedOpenApiBean {}

	@ConditionalOnProperty(name = GROUP_CONFIG_FIRST_PROPERTY)
	static class OnGroupConfigProperty {}

}
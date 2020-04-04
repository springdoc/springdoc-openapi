package org.springdoc.core;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;

import static org.springdoc.core.Constants.SPRINGDOC_CACHE_DISABLED;

public class CacheOrGroupedOpenApiCondition extends AnyNestedCondition {

	CacheOrGroupedOpenApiCondition() {
		super(ConfigurationPhase.REGISTER_BEAN);
	}

	@Conditional(MultipleOpenApiSupportCondition.class)
	static class OnMultipleOpenApiSupportCondition {}

	@ConditionalOnProperty(name = SPRINGDOC_CACHE_DISABLED)
	@ConditionalOnMissingBean(GroupedOpenApi.class)
	static class OnCacheDisabled {}

}
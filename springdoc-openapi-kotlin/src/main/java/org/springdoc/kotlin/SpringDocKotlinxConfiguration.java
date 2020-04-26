package org.springdoc.kotlin;

import kotlinx.coroutines.flow.Flow;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;
import static org.springdoc.core.SpringDocUtils.getConfig;

@ConditionalOnClass(Flow.class)
@Configuration
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
class SpringDocKotlinxConfiguration {

	static {
		getConfig().addFluxWrapperToIgnore(Flow.class);
	}

}
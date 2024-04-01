package org.springdoc.core.conditions;

/**
 * @author bnasslahsen
 */

import java.util.Set;

import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SpringDocConfigProperties.GroupConfig;

import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static org.springdoc.core.utils.Constants.SPRINGDOC_PREFIX;

/**
 * The type Spec properties condition.
 *
 * @author bnasslahsen
 */
public class SpecPropertiesCondition implements Condition {
	
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		final BindResult<SpringDocConfigProperties> result = Binder.get(context.getEnvironment())
				.bind(SPRINGDOC_PREFIX, SpringDocConfigProperties.class);
		if (result.isBound()) {
			SpringDocConfigProperties springDocConfigProperties = result.get();
			if (springDocConfigProperties.getOpenApi() != null)
				return true;
			Set<GroupConfig> groupConfigs = springDocConfigProperties.getGroupConfigs();
			return groupConfigs.stream().anyMatch(config -> config.getOpenApi() != null);
		}
		return false;
	}
	
}

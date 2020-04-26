package org.springdoc.core;


import javax.money.MonetaryAmount;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;

@ConditionalOnClass(MonetaryAmount.class)
@Configuration
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocMonetaryAmountConfiguration {

	static {
		SpringDocUtils.getConfig().replaceWithClass(MonetaryAmount.class, org.springdoc.core.converters.MonetaryAmount.class);
	}

}
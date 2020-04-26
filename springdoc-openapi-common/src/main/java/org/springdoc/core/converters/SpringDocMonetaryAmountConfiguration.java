package org.springdoc.core.converters;


import javax.money.MonetaryAmount;

import org.springdoc.core.SpringDocUtils;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;

@ConditionalOnClass(MonetaryAmount.class)
@Configuration
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
class SpringDocMonetaryAmountConfiguration {

	static {
		SpringDocUtils.getConfig().replaceWithClass(MonetaryAmount.class, org.springdoc.core.converters.MonetaryAmount.class);
	}

}
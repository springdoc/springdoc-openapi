package org.springdoc.core;

import static org.springdoc.core.Constants.DEFAULT_TITLE;
import static org.springdoc.core.Constants.DEFAULT_VERSION;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.info.Info;

@Component
public class InfoBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(InfoBuilder.class);

	@Autowired
	private ApplicationContext context;

	private InfoBuilder() {
		super();
	}

	public Info build() throws ClassNotFoundException {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(OpenAPIDefinition.class));
		List<String> packagesToScan = AutoConfigurationPackages.get(context);
		OpenAPIDefinition apiDef = getApiDefClass(scanner, packagesToScan);
		Info info = null;
		if (apiDef != null) {
			info = AnnotationsUtils.getInfo(apiDef.info()).orElse(null);
		} else {
			info = new Info().title(DEFAULT_TITLE).version(DEFAULT_VERSION);
		}
		return info;
	}

	private OpenAPIDefinition getApiDefClass(ClassPathScanningCandidateComponentProvider scanner,
			List<String> packagesToScan) {
		for (String pack : packagesToScan) {
			for (BeanDefinition bd : scanner.findCandidateComponents(pack)) {
				// first one found is ok
				try {
					return AnnotationUtils.findAnnotation(Class.forName(bd.getBeanClassName()),
							OpenAPIDefinition.class);
				} catch (ClassNotFoundException e) {
					LOGGER.error("Class Not Found in classpath: " + e.getMessage());
				}
			}
		}
		return null;
	}

}

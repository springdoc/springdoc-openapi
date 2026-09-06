package org.springdoc.core.configuration;

import org.junit.jupiter.api.Test;
import org.springdoc.core.configuration.SpringDocHateoasConfiguration.HateoasPropertiesConfiguration;
import org.springdoc.core.configuration.SpringDocHateoasConfiguration.NoHateoasPropertiesConfiguration;
import org.springdoc.core.converters.CollectionModelContentConverter;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.OpenApiHateoasLinksCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.HateoasHalProvider;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.hateoas.autoconfigure.HateoasProperties;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.boot.webmvc.autoconfigure.WebMvcAutoConfiguration;
import org.springframework.hateoas.config.HateoasConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.springdoc.core.utils.Constants.LINKS_SCHEMA_CUSTOMIZER;

class SpringDocHateoasConfigurationTest {

	@Test
	void linksSchemaCustomizerShouldBeRegistered() {
		new WebApplicationContextRunner()
				.withPropertyValues(
						"springdoc.api-docs.enabled=true",
						"springdoc.enable-hateoas=true"
				)
				.withConfiguration(AutoConfigurations.of(
						WebMvcAutoConfiguration.class,
						HateoasConfiguration.class,
						SpringDocConfiguration.class,
						SpringDocConfigProperties.class,
						SpringDocHateoasConfiguration.class
				))
				.run(context -> {
					assertThat(context).getBeanNames(GlobalOpenApiCustomizer.class)
							.hasSize(2)
							.contains(LINKS_SCHEMA_CUSTOMIZER);
					assertThat(context.getBean(LINKS_SCHEMA_CUSTOMIZER)).isExactlyInstanceOf(OpenApiHateoasLinksCustomizer.class);
				});
	}

	@Test
	void linksSchemaCustomizerShouldBeRegisteredWithMultipleGlobalOpenApiCustomizer() {
		new WebApplicationContextRunner()
				.withPropertyValues(
						"springdoc.api-docs.enabled=true",
						"springdoc.enable-hateoas=true"
				)
				.withConfiguration(AutoConfigurations.of(
						WebMvcAutoConfiguration.class,
						HateoasConfiguration.class,
						SpringDocConfiguration.class,
						SpringDocConfigProperties.class,
						SpringDocHateoasConfiguration.class
				))
				.withBean("globalOpenApiCustomizer", GlobalOpenApiCustomizer.class, () -> mock(GlobalOpenApiCustomizer.class))
				.run(context -> {
					assertThat(context).getBeanNames(GlobalOpenApiCustomizer.class)
							.hasSize(2)
							.containsExactlyInAnyOrder(LINKS_SCHEMA_CUSTOMIZER, "globalOpenApiCustomizer");
				});
	}

	@Test
	void linksSchemaCustomizerShouldNotBeRegisteredIfBeanWithSameNameAlreadyExists() {
		new WebApplicationContextRunner()
				.withPropertyValues(
						"springdoc.api-docs.enabled=true",
						"springdoc.enable-hateoas=true"
				)
				.withConfiguration(AutoConfigurations.of(
						WebMvcAutoConfiguration.class,
						HateoasConfiguration.class,
						SpringDocConfiguration.class,
						SpringDocConfigProperties.class,
						SpringDocHateoasConfiguration.class
				))
				.withBean(LINKS_SCHEMA_CUSTOMIZER, GlobalOpenApiCustomizer.class, () -> mock(GlobalOpenApiCustomizer.class))
				.run(context -> {
					assertThat(context).getBeanNames(GlobalOpenApiCustomizer.class)
							.hasSize(2)
							.contains(LINKS_SCHEMA_CUSTOMIZER);
					assertThat(context.getBean(LINKS_SCHEMA_CUSTOMIZER)).isNotExactlyInstanceOf(OpenApiHateoasLinksCustomizer.class);
				});
	}

	@Test
	void halBeansShouldBeRegisteredWhenHateoasPropertiesIsPresent() {
		hateoasContextRunner()
				.run(context -> {
					assertThat(context).hasNotFailed();
					assertThat(context).hasSingleBean(HateoasHalProvider.class);
					assertThat(context).hasSingleBean(CollectionModelContentConverter.class);
					assertThat(context).hasSingleBean(HateoasPropertiesConfiguration.class);
					assertThat(context).doesNotHaveBean(NoHateoasPropertiesConfiguration.class);
				});
	}

	/**
	 * spring-hateoas can be on the classpath without the Spring Boot auto-configuration
	 * module that carries {@link HateoasProperties}, which is what happens with
	 * spring-boot-starter-data-rest alone. The HAL beans, and above all the
	 * {@code _embedded} converter, must still be registered then.
	 */
	@Test
	void halBeansShouldBeRegisteredWhenHateoasPropertiesIsAbsent() {
		hateoasContextRunner()
				.withClassLoader(new FilteredClassLoader(HateoasProperties.class))
				.run(context -> {
					assertThat(context).hasNotFailed();
					assertThat(context).hasSingleBean(HateoasHalProvider.class);
					assertThat(context).hasSingleBean(CollectionModelContentConverter.class);
					assertThat(context).hasSingleBean(NoHateoasPropertiesConfiguration.class);
					assertThat(context).doesNotHaveBean(HateoasPropertiesConfiguration.class);
				});
	}

	private WebApplicationContextRunner hateoasContextRunner() {
		return new WebApplicationContextRunner()
				.withPropertyValues(
						"springdoc.api-docs.enabled=true",
						"springdoc.enable-hateoas=true"
				)
				.withConfiguration(AutoConfigurations.of(
						WebMvcAutoConfiguration.class,
						HateoasConfiguration.class,
						SpringDocConfiguration.class,
						SpringDocConfigProperties.class,
						SpringDocHateoasConfiguration.class
				));
	}
}
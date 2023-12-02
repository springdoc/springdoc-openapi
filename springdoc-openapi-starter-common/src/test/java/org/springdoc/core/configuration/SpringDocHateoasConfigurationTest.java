package org.springdoc.core.configuration;

import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.OpenApiHateoasLinksCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.hateoas.config.HateoasConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.springdoc.core.utils.Constants.LINKS_SCHEMA_CUSTOMISER;

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
                        .hasSize(1)
                        .containsExactly(LINKS_SCHEMA_CUSTOMISER);
                    assertThat(context.getBean(LINKS_SCHEMA_CUSTOMISER)).isExactlyInstanceOf(OpenApiHateoasLinksCustomizer.class);
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
                            .containsExactlyInAnyOrder(LINKS_SCHEMA_CUSTOMISER, "globalOpenApiCustomizer");
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
                .withBean(LINKS_SCHEMA_CUSTOMISER, GlobalOpenApiCustomizer.class, () -> mock(GlobalOpenApiCustomizer.class))
                .run(context -> {
                    assertThat(context).getBeanNames(GlobalOpenApiCustomizer.class)
                            .hasSize(1)
                            .containsExactly(LINKS_SCHEMA_CUSTOMISER);
                    assertThat(context.getBean(LINKS_SCHEMA_CUSTOMISER)).isNotExactlyInstanceOf(OpenApiHateoasLinksCustomizer.class);
                });
    }
}
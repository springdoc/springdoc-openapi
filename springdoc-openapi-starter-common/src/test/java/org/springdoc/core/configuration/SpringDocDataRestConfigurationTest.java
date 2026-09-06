package org.springdoc.core.configuration;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springdoc.core.configuration.SpringDocDataRestConfiguration.DataRestHateoasPropertiesConfiguration;
import org.springdoc.core.configuration.SpringDocDataRestConfiguration.DataRestNoHateoasPropertiesConfiguration;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.DataRestHalProvider;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.hateoas.autoconfigure.HateoasProperties;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringDocDataRestConfiguration} and the way it handles the
 * optional {@link HateoasProperties} class.
 * <p>
 * There are two concerns being verified here:
 * <ul>
 *     <li><b>Structure</b> the {@code HateoasProperties}-dependent bean method is
 *     isolated in a nested configuration guarded by {@code @ConditionalOnClass}, with a
 *     {@code @ConditionalOnMissingClass} fallback, so that when {@code HateoasProperties}
 *     is absent from the classpath Spring never loads a class that references it (which
 *     would otherwise raise {@code TypeNotPresentException}).</li>
 *     <li><b>Wiring</b> depending on whether {@code HateoasProperties} is on the
 *     classpath, the matching nested configuration is selected and a working
 *     {@link DataRestHalProvider} is created.</li>
 * </ul>
 * The absence of {@code HateoasProperties} is simulated with a
 * {@link FilteredClassLoader}. A runtime property such as
 * {@code springdoc.enable-hateoas=false} cannot be used for this, because it only
 * toggles behavior at runtime and never removes the class from the classpath, so the
 * {@code @ConditionalOnMissingClass} fallback would never be selected.
 */
class SpringDocDataRestConfigurationTest {
    /**
     * Structural guarantees that keep {@code HateoasProperties} references out of any
     * class Spring might load when the type is absent.
     */
    @Nested
    class Structure {
        @Test
        void outerConfigClassHasNoBeanMethodReferencingHateoasProperties() {
            for (Method method : SpringDocDataRestConfiguration.class.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Bean.class)) {
                    for (Type type : method.getGenericParameterTypes()) {
                        assertThat(type.getTypeName())
                                .as("@Bean method %s() should not reference HateoasProperties directly", method.getName())
                                .doesNotContain(HateoasProperties.class.getSimpleName());
                    }
                }
            }
        }

        @Test
        void hateoasPropertiesConfigIsGuardedByConditionalOnClass() {
            ConditionalOnClass onClass = DataRestHateoasPropertiesConfiguration.class.getAnnotation(ConditionalOnClass.class);
            assertThat(onClass)
                    .as("%s should be guarded by @ConditionalOnClass", DataRestHateoasPropertiesConfiguration.class.getSimpleName())
                    .isNotNull();
            assertThat(Arrays.asList(onClass.name())).contains(HateoasProperties.class.getName());
        }

        @Test
        void noHateoasPropertiesConfigIsGuardedByConditionalOnMissingClass() {
            ConditionalOnMissingClass onMissingClass = DataRestNoHateoasPropertiesConfiguration.class.getAnnotation(ConditionalOnMissingClass.class);
            assertThat(onMissingClass)
                    .as("%s should be guarded by @ConditionalOnMissingClass", DataRestNoHateoasPropertiesConfiguration.class.getSimpleName())
                    .isNotNull();
            assertThat(Arrays.asList(onMissingClass.value())).contains(HateoasProperties.class.getName());
        }
    }

    /**
     * Behavioral verification that the correct nested configuration is selected and a
     * {@link DataRestHalProvider} is created in both scenarios.
     */
    @Nested
    class Wiring {
        private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
                .withBean(ObjectMapperProvider.class, () -> new ObjectMapperProvider(new SpringDocConfigProperties()))
                .withUserConfiguration(
                        DataRestHateoasPropertiesConfiguration.class,
                        DataRestNoHateoasPropertiesConfiguration.class);

        @Test
        void halProviderIsCreatedFromHateoasPropertiesConfigWhenHateoasPropertiesIsPresent() {
            contextRunner
                    .run(context -> {
                        assertThat(context).hasNotFailed();
                        assertThat(context).hasSingleBean(DataRestHalProvider.class);
                        assertThat(context).hasSingleBean(DataRestHateoasPropertiesConfiguration.class);
                        assertThat(context).doesNotHaveBean(DataRestNoHateoasPropertiesConfiguration.class);
                    });
        }

        @Test
        void halProviderIsCreatedFromFallbackConfigWhenHateoasPropertiesIsAbsent() {
            contextRunner
                    .withClassLoader(new FilteredClassLoader(HateoasProperties.class))
                    .run(context -> {
                        assertThat(context).hasNotFailed();
                        assertThat(context).hasSingleBean(DataRestHalProvider.class);
                        assertThat(context).hasSingleBean(DataRestNoHateoasPropertiesConfiguration.class);
                        assertThat(context).doesNotHaveBean(DataRestHateoasPropertiesConfiguration.class);
                    });
        }
    }
}

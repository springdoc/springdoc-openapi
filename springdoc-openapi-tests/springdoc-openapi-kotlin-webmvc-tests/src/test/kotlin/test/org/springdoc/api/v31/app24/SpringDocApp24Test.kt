package test.org.springdoc.api.v31.app24

import io.swagger.v3.core.converter.ModelConverters
import org.springdoc.core.customizers.KotlinNullablePropertyCustomizer
import org.springdoc.core.utils.Constants
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.TestPropertySource
import test.org.springdoc.api.v31.AbstractKotlinSpringDocMVCTest

@TestPropertySource(properties = [Constants.SPRINGDOC_KOTLIN_NULLABLE_PROPERTY_CUSTOMIZER_ENABLED + "=false"])
class SpringDocApp24Test : AbstractKotlinSpringDocMVCTest() {
    companion object {
        init {
            ModelConverters
                .getInstance(true)
                .getConverters()
                .filterIsInstance<KotlinNullablePropertyCustomizer>()
                .forEach { ModelConverters.getInstance(true).removeConverter(it) }
        }
    }

    @SpringBootApplication
    @ComponentScan(basePackages = ["org.springdoc", "test.org.springdoc.api.v31.app24"])
    class DemoApplication
}

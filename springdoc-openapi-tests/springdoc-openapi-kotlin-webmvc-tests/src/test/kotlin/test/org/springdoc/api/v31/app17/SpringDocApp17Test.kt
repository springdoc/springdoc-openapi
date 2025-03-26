package test.org.springdoc.api.v31.app17

import jakarta.validation.constraints.NotEmpty
import org.junit.jupiter.api.Assertions
import org.springdoc.core.customizers.DelegatingMethodParameterCustomizer
import org.springdoc.core.extractor.DelegatingMethodParameter
import org.springdoc.core.utils.Constants.SPRINGDOC_DEFAULT_FLAT_PARAM_OBJECT
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.MethodParameter
import org.springframework.test.context.TestPropertySource
import test.org.springdoc.api.v31.AbstractKotlinSpringDocMVCTest

@TestPropertySource(properties = ["$SPRINGDOC_DEFAULT_FLAT_PARAM_OBJECT=true"])
class SpringDocApp17Test : AbstractKotlinSpringDocMVCTest() {
    @SpringBootApplication
    class DemoApplication {

        @Bean
        fun optionalKotlinDelegatingMethodParameterCustomizer(): DelegatingMethodParameterCustomizer {
            return object : DelegatingMethodParameterCustomizer {
                override fun customizeList(
                    originalParameter: MethodParameter,
                    methodParameters: MutableList<MethodParameter>
                ) {
                    val fieldNameSet = mutableSetOf<String>()
                    methodParameters.forEachIndexed { index, it ->
                        if (it is DelegatingMethodParameter && it.isParameterObject && it.field != null) {
                            val field = it.field!!
                            if (fieldNameSet.contains(field.name)) {
                                val fieldAnnotations = field.annotations
                                Assertions.assertTrue(fieldAnnotations.any { it is NotEmpty })
                                // remove parent field
                                methodParameters.removeAt(index)
                            } else fieldNameSet.add(field.name)
                        }
                    }
                    if (methodParameters.size > 1) {
                        methodParameters.sortBy { it.parameterIndex }
                    }
                }

                override fun customize(
                    originalParameter: MethodParameter,
                    methodParameter: MethodParameter
                ) {
                }
            }
        }
    }
}

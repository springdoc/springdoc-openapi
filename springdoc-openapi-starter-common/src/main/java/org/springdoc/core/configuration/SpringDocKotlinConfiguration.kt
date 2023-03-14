package org.springdoc.core.configuration

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.models.media.ByteArraySchema
import org.springdoc.core.customizers.ParameterCustomizer
import org.springdoc.core.parsers.KotlinCoroutinesReturnTypeParser
import org.springdoc.core.providers.ObjectMapperProvider
import org.springdoc.core.utils.Constants
import org.springdoc.core.utils.SpringDocUtils
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.core.KotlinDetector
import org.springframework.core.MethodParameter
import org.springframework.core.annotation.AnnotatedElementUtils
import kotlin.coroutines.Continuation
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.kotlinFunction

/**
 * The type Spring doc kotlin configuration.
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = [Constants.SPRINGDOC_ENABLED], matchIfMissing = true)
@ConditionalOnExpression("\${springdoc.api-docs.enabled:true} and \${springdoc.enable-kotlin:true}")
@ConditionalOnClass(Continuation::class)
@ConditionalOnWebApplication
@ConditionalOnBean(SpringDocConfiguration::class)
open class SpringDocKotlinConfiguration(objectMapperProvider: ObjectMapperProvider) {

	/**
	 * SpringDoc Kotlin Module Configuration
	 *
	 * @param objectMapperProvider Object Mapper Provider
	 * @return the nullable Kotlin Request Parameter Customizer
	 */
	@Lazy(false)
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(KotlinModule::class)
	class SpringDocKotlinModuleConfiguration(objectMapperProvider: ObjectMapperProvider) {
		init {
			objectMapperProvider.jsonMapper()
				.registerModule(KotlinModule.Builder().build())
		}
	}

	/**
	 * Instantiates a new Spring doc kotlin configuration.
	 *
	 */
	init {
		SpringDocUtils.getConfig()
			.addRequestWrapperToIgnore(Continuation::class.java)
			.replaceWithSchema(ByteArray::class.java, ByteArraySchema())
			.addDeprecatedType(Deprecated::class.java)
		objectMapperProvider.jsonMapper().registerModule(SpringDocRequiredModule())
	}

	/**
	 * Kotlin coroutines return type parser kotlin coroutines return type parser.
	 *
	 * @return the kotlin coroutines return type parser
	 */
	@Bean
	@Lazy(false)
	@ConditionalOnMissingBean
	open fun kotlinCoroutinesReturnTypeParser(): KotlinCoroutinesReturnTypeParser {
		return KotlinCoroutinesReturnTypeParser()
	}

	/**
	 * Kotlin springdoc-openapi ParameterCustomizer
	 *
	 * @return the nullable Kotlin Request Parameter Customizer
	 */
	@Bean
	@Lazy(false)
	@ConditionalOnProperty(
		name = [Constants.SPRINGDOC_NULLABLE_REQUEST_PARAMETER_ENABLED],
		matchIfMissing = true
	)
	@ConditionalOnMissingBean
	open fun nullableKotlinRequestParameterCustomizer(): ParameterCustomizer {
		return ParameterCustomizer { parameterModel, methodParameter ->
			if (parameterModel == null) return@ParameterCustomizer null
			if (KotlinDetector.isKotlinReflectPresent()) {
				val kParameter = methodParameter.toKParameter()
				if (kParameter != null) {
					val parameterDoc = AnnotatedElementUtils.findMergedAnnotation(
						AnnotatedElementUtils.forAnnotations(*methodParameter.parameterAnnotations),
						Parameter::class.java
					)
					// Swagger @Parameter annotation takes precedence
					if (parameterDoc != null && parameterDoc.required)
						parameterModel.required = parameterDoc.required
					else
						parameterModel.required =
							kParameter.type.isMarkedNullable == false
				}
			}
			return@ParameterCustomizer parameterModel
		}
	}

	private fun MethodParameter.toKParameter(): KParameter? {
		// ignore return type, see org.springframework.core.MethodParameter.getParameterIndex
		if (parameterIndex == -1) return null
		val kotlinFunction = method?.kotlinFunction ?: return null
		// The first parameter of the kotlin function is the "this" reference and not needed here.
		// See also kotlin.reflect.KCallable.getParameters
		return kotlinFunction.parameters[parameterIndex + 1]
	}

}

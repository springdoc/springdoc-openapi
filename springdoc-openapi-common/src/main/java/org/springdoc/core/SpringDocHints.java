package org.springdoc.core;

import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.core.jackson.ApiResponsesSerializer;
import io.swagger.v3.core.jackson.PathsSerializer;
import io.swagger.v3.core.jackson.mixin.ComponentsMixin;
import io.swagger.v3.core.jackson.mixin.DateSchemaMixin;
import io.swagger.v3.core.jackson.mixin.ExampleMixin;
import io.swagger.v3.core.jackson.mixin.ExtensionsMixin;
import io.swagger.v3.core.jackson.mixin.MediaTypeMixin;
import io.swagger.v3.core.jackson.mixin.OpenAPIMixin;
import io.swagger.v3.core.jackson.mixin.OperationMixin;
import io.swagger.v3.core.jackson.mixin.SchemaMixin;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.callbacks.Callbacks;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.links.LinkParameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.BinarySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.ByteArraySchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.DateSchema;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.Discriminator;
import io.swagger.v3.oas.models.media.EmailSchema;
import io.swagger.v3.oas.models.media.EncodingProperty;
import io.swagger.v3.oas.models.media.FileSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.PasswordSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.UUIDSchema;
import io.swagger.v3.oas.models.media.XML;
import io.swagger.v3.oas.models.parameters.CookieParameter;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.PathParameter;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.servers.ServerVariables;
import org.springdoc.api.mixins.SortedOpenAPIMixin;
import org.springdoc.api.mixins.SortedSchemaMixin;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.nativex.hint.AccessBits;
import org.springframework.nativex.hint.JdkProxyHint;
import org.springframework.nativex.hint.ResourceHint;
import org.springframework.nativex.hint.TypeHint;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;
import static org.springdoc.core.Constants.SPRING_NATIVE_LISTENER;

@JdkProxyHint(typeNames = "javax.servlet.http.HttpServletRequest")
@JdkProxyHint(typeNames =  "org.springframework.web.context.request.NativeWebRequest" )

@JdkProxyHint(typeNames = { "org.springframework.web.bind.annotation.RestController", "org.springframework.core.annotation.SynthesizedAnnotation" })
@JdkProxyHint(typeNames = { "org.springframework.stereotype.Controller", "org.springframework.core.annotation.SynthesizedAnnotation" })
@JdkProxyHint(typeNames = { "org.springframework.web.bind.annotation.SessionAttribute", "org.springframework.core.annotation.SynthesizedAnnotation" })
@JdkProxyHint(typeNames = { "org.springframework.web.bind.annotation.RestControllerAdvice", "org.springframework.core.annotation.SynthesizedAnnotation" })
@JdkProxyHint(typeNames = { "org.springframework.web.bind.annotation.ResponseStatus", "org.springframework.core.annotation.SynthesizedAnnotation" })
@JdkProxyHint(typeNames = { "org.springframework.web.bind.annotation.ResponseBody", "org.springframework.core.annotation.SynthesizedAnnotation" })
@JdkProxyHint(typeNames = { "org.springframework.web.bind.annotation.RequestPart", "org.springframework.core.annotation.SynthesizedAnnotation" })
@JdkProxyHint(typeNames = { "org.springframework.web.bind.annotation.RequestPart", "org.springframework.core.annotation.SynthesizedAnnotation" })
@JdkProxyHint(typeNames = { "org.springframework.web.bind.annotation.RequestMapping", "org.springframework.core.annotation.SynthesizedAnnotation" })
@JdkProxyHint(typeNames = { "org.springframework.web.bind.annotation.GetMapping", "org.springframework.core.annotation.SynthesizedAnnotation" })
@JdkProxyHint(typeNames = { "org.springframework.web.bind.annotation.PostMapping", "org.springframework.core.annotation.SynthesizedAnnotation" })
@JdkProxyHint(typeNames = { "org.springframework.web.bind.annotation.PutMapping", "org.springframework.core.annotation.SynthesizedAnnotation" })
@JdkProxyHint(typeNames = { "org.springframework.web.bind.annotation.PatchMapping", "org.springframework.core.annotation.SynthesizedAnnotation" })
@JdkProxyHint(typeNames = { "org.springframework.web.bind.annotation.DeleteMapping", "org.springframework.core.annotation.SynthesizedAnnotation" })
@JdkProxyHint(typeNames = { "org.springframework.web.bind.annotation.ControllerAdvice", "org.springframework.core.annotation.SynthesizedAnnotation" })
@JdkProxyHint(typeNames = {"org.springframework.web.bind.annotation.RequestParam", "org.springframework.core.annotation.SynthesizedAnnotation"})
@JdkProxyHint(typeNames = {"org.springframework.web.bind.annotation.RequestHeader", "org.springframework.core.annotation.SynthesizedAnnotation"})
@JdkProxyHint(typeNames = {"org.springframework.web.bind.annotation.RequestBody", "org.springframework.core.annotation.SynthesizedAnnotation"})
@JdkProxyHint(typeNames = {"org.springframework.web.bind.annotation.PathVariable", "org.springframework.core.annotation.SynthesizedAnnotation"})
@JdkProxyHint(typeNames = {"org.springframework.web.bind.annotation.ModelAttribute", "org.springframework.core.annotation.SynthesizedAnnotation"})
@JdkProxyHint(typeNames = {"org.springframework.stereotype.Controller", "org.springframework.core.annotation.SynthesizedAnnotation"})
@JdkProxyHint(typeNames = {"org.springframework.web.bind.annotation.ControllerAdvice", "org.springframework.core.annotation.SynthesizedAnnotation"})

@TypeHint(typeNames = { "org.springdoc.core.CacheOrGroupedOpenApiCondition$OnCacheDisabled", "io.swagger.v3.oas.models.parameters.Parameter$StyleEnum",
		"io.swagger.v3.oas.models.security.SecurityScheme$In" , "io.swagger.v3.oas.models.security.SecurityScheme$Type", 
		"org.springdoc.core.CacheOrGroupedOpenApiCondition$OnMultipleOpenApiSupportCondition", "org.springdoc.core.SpringDocConfigProperties$GroupConfig" ,
		"org.springdoc.core.SpringDocConfigProperties$Cache" ,  "org.springdoc.core.SpringDocConfigProperties$GroupConfig" ,
		"org.springdoc.core.AbstractSwaggerUiConfigProperties$SwaggerUrl" ,"org.springdoc.core.AbstractSwaggerUiConfigProperties$Direction" ,
		"org.springdoc.core.AbstractSwaggerUiConfigProperties$SyntaxHighlight" ,
		"org.springdoc.core.SpringDocConfigProperties$Webjars" ,  "org.springdoc.core.SpringDocConfigProperties$ApiDocs" }, access = AccessBits.ALL)

@TypeHint(types = { Constants.class,  ModelConverter.class , ModelConverters.class})
@TypeHint(types = { SecurityRequirements.class, SecurityRequirement.class, ApiResponses.class, Callbacks.class, PropertySource.class, ExternalDocumentation.class, Hidden.class,
		Operation.class, Parameter.class, Callbacks.class, Extension.class, ExtensionProperty.class, Header.class, Link.class, LinkParameter.class,
		ArraySchema.class, Content.class, DiscriminatorMapping.class, Encoding.class, ExampleObject.class, Schema.class, RequestBody.class, ApiResponse.class,
		Info.class, Server.class, ServerVariable.class, OpenAPIDefinition.class,  Tag.class, SecuritySchemes.class, SecurityScheme.class, SecuritySchemeType.class,
		OAuthFlow.class, OAuthFlows.class, OAuthScope.class, License.class, Contact.class })

@TypeHint(types = {
		SpecFilter.class,
		MediaType.class,
		ApiResponsesSerializer.class,
		PathsSerializer.class,
		ComponentsMixin.class,
		ExtensionsMixin.class,
		OpenAPIMixin.class,
		OperationMixin.class,
		SchemaMixin.class,
		Paths.class,
		XML.class,
		UUIDSchema.class,
		PathItem.class,
		ServerVariables.class,
		OpenAPI.class,
		Components.class,
		StringSchema.class,
		DateTimeSchema.class,
		Discriminator.class,
		BooleanSchema.class,
		FileSchema.class,
		IntegerSchema.class,
		MapSchema.class,
		ObjectSchema.class,
		Scopes.class,
		DateSchema.class,
		ComposedSchema.class,
		BinarySchema.class,
		ByteArraySchema.class,
		EmailSchema.class,
		Example.class,
		EncodingProperty.class,
		NumberSchema.class,
		PasswordSchema.class,
		CookieParameter.class,
		HeaderParameter.class,
		PathParameter.class,
		QueryParameter.class,
		DateSchemaMixin.class,
		ExampleMixin.class,
		MediaTypeMixin.class,
		SortedOpenAPIMixin.class,
		SortedSchemaMixin.class,
		io.swagger.v3.oas.models.security.SecurityScheme.Type.class,
		io.swagger.v3.oas.models.security.SecurityScheme.In.class,
		io.swagger.v3.oas.models.media.Encoding.class,
		io.swagger.v3.oas.models.info.Contact.class,
		io.swagger.v3.oas.models.info.License.class,
		io.swagger.v3.oas.models.security.OAuthFlow.class, io.swagger.v3.oas.models.security.OAuthFlows.class,
		io.swagger.v3.oas.models.security.SecurityScheme.class,
		io.swagger.v3.oas.models.tags.Tag.class,
		io.swagger.v3.oas.models.servers.ServerVariable.class,
		io.swagger.v3.oas.models.servers.Server.class,
		io.swagger.v3.oas.models.security.SecurityRequirement.class,
		io.swagger.v3.oas.models.info.Info.class,
		io.swagger.v3.oas.models.parameters.RequestBody.class,
		io.swagger.v3.oas.models.media.Schema.class,
		io.swagger.v3.oas.models.media.Content.class,
		io.swagger.v3.oas.models.media.ArraySchema.class,
		io.swagger.v3.oas.models.responses.ApiResponse.class,
		io.swagger.v3.oas.models.responses.ApiResponses.class,
		io.swagger.v3.oas.models.ExternalDocumentation.class,
		io.swagger.v3.oas.models.links.LinkParameter.class,
		io.swagger.v3.oas.models.links.Link.class,
		io.swagger.v3.oas.models.parameters.Parameter.class,
		io.swagger.v3.oas.models.Operation.class,
		io.swagger.v3.oas.models.headers.Header.class
})

@ResourceHint(patterns = "springdoc.swagger-ui.config")
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
@ConditionalOnClass(name = SPRING_NATIVE_LISTENER)
public class SpringDocHints {}


/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *
 */

package org.springdoc.core.properties;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.properties.SpringDocConfigProperties.ApiDocs.OpenApiVersion;
import org.springdoc.core.utils.Constants;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;

import static org.springdoc.core.utils.Constants.DEFAULT_WEB_JARS_PREFIX_URL;
import static org.springdoc.core.utils.Constants.SPRINGDOC_ENABLED;

/**
 * The type Spring doc config properties.
 *
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = Constants.SPRINGDOC_PREFIX)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
@ConditionalOnBean(SpringDocConfiguration.class)
public class SpringDocConfigProperties {

	/**
	 * The Show actuator.
	 */
	private boolean showActuator;

	/**
	 * The Webjars.
	 */
	private Webjars webjars = new Webjars();

	/**
	 * The Api docs.
	 */
	private ApiDocs apiDocs = new ApiDocs();

	/**
	 * The Packages to scan.
	 */
	private List<String> packagesToScan;

	/**
	 * The Packages to exclude.
	 */
	private List<String> packagesToExclude;

	/**
	 * The Paths to match.
	 */
	private List<String> pathsToMatch;

	/**
	 * The Paths to exclude.
	 */
	private List<String> pathsToExclude;

	/**
	 * The Produces to match.
	 */
	private List<String> producesToMatch;

	/**
	 * The Headers to match.
	 */
	private List<String> headersToMatch;

	/**
	 * The Consumes to match.
	 */
	private List<String> consumesToMatch;

	/**
	 * The Cache.
	 */
	private Cache cache = new Cache();

	/**
	 * The Group configs.
	 */
	private Set<GroupConfig> groupConfigs = new HashSet<>();

	/**
	 * The Auto tag classes.
	 */
	private boolean autoTagClasses = true;

	/**
	 * The Model and view allowed.
	 */
	private boolean modelAndViewAllowed;

	/**
	 * The Override with generic response.
	 */
	private Boolean overrideWithGenericResponse;

	/**
	 * The Remove broken reference definitions.
	 */
	private boolean removeBrokenReferenceDefinitions = true;

	/**
	 * The Writer with default pretty printer.
	 */
	private boolean writerWithDefaultPrettyPrinter;

	/**
	 * The Writer with order by keys.
	 */
	private boolean writerWithOrderByKeys;

	/**
	 * The Default consumes media type.
	 */
	private String defaultConsumesMediaType = MediaType.APPLICATION_JSON_VALUE;

	/**
	 * The Default produces media type.
	 */
	private String defaultProducesMediaType = MediaType.ALL_VALUE;

	/**
	 * Use fully qualified name
	 */
	private boolean useFqn;

	/**
	 * The Show login endpoint.
	 */
	private boolean showLoginEndpoint;

	/**
	 * Allow for pre-loading OpenAPI
	 */
	private boolean preLoadingEnabled;

	/**
	 * locale list to pre-loading
	 */
	private List<String> preLoadingLocales;

	/**
	 * If set to true, exposes the swagger-ui on the actuator management port.
	 */
	private boolean useManagementPort;

	/**
	 * Allowed locales for i18n.
	 */
	private List<String> allowedLocales;

	/**
	 * The Disable i18n.
	 */
	private boolean disableI18n;

	/**
	 * The Show spring cloud functions.
	 */
	private boolean showSpringCloudFunctions = true;

	/**
	 * The param default flatten
	 */
	private boolean defaultFlatParamObject;

	/**
	 * The model Converters
	 */
	private ModelConverters modelConverters = new ModelConverters();

	/**
	 * The Enable groovy.
	 */
	private boolean enableGroovy = true;

	/**
	 * The Enable javadoc.
	 */
	private boolean enableJavadoc = true;

	/**
	 * The Enable spring security.
	 */
	private boolean enableSpringSecurity = true;

	/**
	 * The Enable kotlin.
	 */
	private boolean enableKotlin = true;

	/**
	 * The Enable hateoas.
	 */
	private boolean enableHateoas = true;

	/**
	 * The Enable hateoas.
	 */
	private boolean enableDataRest = true;

	/**
	 * The Enable default api docs.
	 */
	private boolean enableDefaultApiDocs = true;

	/**
	 * convert query param to form data when consumes is multipart/form-data
	 */
	private boolean defaultSupportFormData;

	/**
	 * The Show oauth 2 endpoint.
	 */
	private boolean showOauth2Endpoints;

	/**
	 * The Sort converter.
	 */
	private SortConverter sortConverter = new SortConverter();

	/**
	 * The Nullable request parameter enabled.
	 */
	private boolean nullableRequestParameterEnabled;

	/**
	 * The trim kotlin indent.
	 */
	private boolean trimKotlinIndent;

	/**
	 * The Open api.
	 */
	private OpenAPI OpenApi;

	/**
	 * The Enable extra schemas resolution.
	 */
	private boolean enableExtraSchemas;

	/**
	 * Set explicit-object-schema to true to always include type:
	 * object in the schema, or to false to omit type: object.
	 */
	private boolean explicitObjectSchema;

	/**
	 * When set to true, schemas without a defined type will be deserialized as an ArbitrarySchema (with no type),
	 * instead of an ObjectSchema with type: object.
	 */
	private boolean useArbitrarySchemas;

	/**
	 * Is enable additional schemas resolution boolean.
	 *
	 * @return the boolean
	 */
	public boolean isEnableExtraSchemas() {
		return enableExtraSchemas;
	}

	/**
	 * Sets enable additional schemas resolution.
	 *
	 * @param enableExtraSchemas the enable additional schemas resolution
	 */
	public void setEnableExtraSchemas(boolean enableExtraSchemas) {
		this.enableExtraSchemas = enableExtraSchemas;
	}

	/**
	 * Gets open api.
	 *
	 * @return the open api
	 */
	public OpenAPI getOpenApi() {
		return OpenApi;
	}

	/**
	 * Sets open api.
	 *
	 * @param openApi the open api
	 */
	public void setOpenApi(OpenAPI openApi) {
		this.OpenApi = openApi;
	}

	/**
	 * Gets trim kotlin indent.
	 *
	 * @return the trim kotlin indent.
	 */
	public boolean isTrimKotlinIndent() {
		return trimKotlinIndent;
	}

	/**
	 * Sets trim kotlin indent
	 *
	 * @param trimKotlinIndent the trim kotlin indent.
	 */
	public void setTrimKotlinIndent(boolean trimKotlinIndent) {
		this.trimKotlinIndent = trimKotlinIndent;
	}

	/**
	 * Gets override with generic response.
	 *
	 * @return the override with generic response
	 */
	public Boolean getOverrideWithGenericResponse() {
		return overrideWithGenericResponse;
	}

	/**
	 * Sets override with generic response.
	 *
	 * @param overrideWithGenericResponse the override with generic response
	 */
	public void setOverrideWithGenericResponse(Boolean overrideWithGenericResponse) {
		this.overrideWithGenericResponse = overrideWithGenericResponse;
	}

	/**
	 * Is nullable request parameter enabled boolean.
	 *
	 * @return the boolean
	 */
	public boolean isNullableRequestParameterEnabled() {
		return nullableRequestParameterEnabled;
	}

	/**
	 * Sets nullable request parameter enabled.
	 *
	 * @param nullableRequestParameterEnabled the nullable request parameter enabled
	 */
	public void setNullableRequestParameterEnabled(boolean nullableRequestParameterEnabled) {
		this.nullableRequestParameterEnabled = nullableRequestParameterEnabled;
	}

	/**
	 * Is default support form data boolean.
	 *
	 * @return the boolean
	 */
	public boolean isDefaultSupportFormData() {
		return defaultSupportFormData;
	}

	/**
	 * Sets default support form data.
	 *
	 * @param defaultSupportFormData the default support form data
	 */
	public void setDefaultSupportFormData(boolean defaultSupportFormData) {
		this.defaultSupportFormData = defaultSupportFormData;
	}

	/**
	 * Is default flat param object boolean.
	 *
	 * @return the boolean
	 */
	public boolean isDefaultFlatParamObject() {
		return defaultFlatParamObject;
	}

	/**
	 * Sets default flat param object.
	 *
	 * @param defaultFlatParamObject the default flat param object
	 */
	public void setDefaultFlatParamObject(boolean defaultFlatParamObject) {
		this.defaultFlatParamObject = defaultFlatParamObject;
	}

	/**
	 * Gets sort converter.
	 *
	 * @return the sort converter
	 */
	public SortConverter getSortConverter() {
		return sortConverter;
	}

	/**
	 * Sets sort converter.
	 *
	 * @param sortConverter the sort converter
	 */
	public void setSortConverter(SortConverter sortConverter) {
		this.sortConverter = sortConverter;
	}

	/**
	 * Is enable data rest boolean.
	 *
	 * @return the boolean
	 */
	public boolean isEnableDataRest() {
		return enableDataRest;
	}

	/**
	 * Sets enable data rest.
	 *
	 * @param enableDataRest the enable data rest
	 */
	public void setEnableDataRest(boolean enableDataRest) {
		this.enableDataRest = enableDataRest;
	}

	/**
	 * Is enable hateoas boolean.
	 *
	 * @return the boolean
	 */
	public boolean isEnableHateoas() {
		return enableHateoas;
	}

	/**
	 * Sets enable hateoas.
	 *
	 * @param enableHateoas the enable hateoas
	 */
	public void setEnableHateoas(boolean enableHateoas) {
		this.enableHateoas = enableHateoas;
	}

	/**
	 * Is enable kotlin boolean.
	 *
	 * @return the boolean
	 */
	public boolean isEnableKotlin() {
		return enableKotlin;
	}

	/**
	 * Sets enable kotlin.
	 *
	 * @param enableKotlin the enable kotlin
	 */
	public void setEnableKotlin(boolean enableKotlin) {
		this.enableKotlin = enableKotlin;
	}

	/**
	 * Is enable spring security boolean.
	 *
	 * @return the boolean
	 */
	public boolean isEnableSpringSecurity() {
		return enableSpringSecurity;
	}

	/**
	 * Sets enable spring security.
	 *
	 * @param enableSpringSecurity the enable spring security
	 */
	public void setEnableSpringSecurity(boolean enableSpringSecurity) {
		this.enableSpringSecurity = enableSpringSecurity;
	}

	/**
	 * Is enable javadoc boolean.
	 *
	 * @return the boolean
	 */
	public boolean isEnableJavadoc() {
		return enableJavadoc;
	}

	/**
	 * Sets enable javadoc.
	 *
	 * @param enableJavadoc the enable javadoc
	 */
	public void setEnableJavadoc(boolean enableJavadoc) {
		this.enableJavadoc = enableJavadoc;
	}

	/**
	 * Is enable groovy boolean.
	 *
	 * @return the boolean
	 */
	public boolean isEnableGroovy() {
		return enableGroovy;
	}

	/**
	 * Sets enable groovy.
	 *
	 * @param enableGroovy the enable groovy
	 */
	public void setEnableGroovy(boolean enableGroovy) {
		this.enableGroovy = enableGroovy;
	}

	/**
	 * Is show spring cloud functions boolean.
	 *
	 * @return the boolean
	 */
	public boolean isShowSpringCloudFunctions() {
		return showSpringCloudFunctions;
	}

	/**
	 * Sets show spring cloud functions.
	 *
	 * @param showSpringCloudFunctions the show spring cloud functions
	 */
	public void setShowSpringCloudFunctions(boolean showSpringCloudFunctions) {
		this.showSpringCloudFunctions = showSpringCloudFunctions;
	}

	/**
	 * Gets model converters.
	 *
	 * @return the model converters
	 */
	public ModelConverters getModelConverters() {
		return modelConverters;
	}

	/**
	 * Sets model converters.
	 *
	 * @param modelConverters the model converters
	 */
	public void setModelConverters(ModelConverters modelConverters) {
		this.modelConverters = modelConverters;
	}


	/**
	 * Is use management port boolean.
	 *
	 * @return the boolean
	 */
	public boolean isUseManagementPort() {
		return useManagementPort;
	}

	/**
	 * Sets use management port.
	 *
	 * @param useManagementPort the use management port
	 */
	public void setUseManagementPort(boolean useManagementPort) {
		this.useManagementPort = useManagementPort;
	}

	/**
	 * Gets produces to match.
	 *
	 * @return the produces to match
	 */
	public List<String> getProducesToMatch() {
		return producesToMatch;
	}

	/**
	 * Sets produces to match.
	 *
	 * @param producesToMatch the produces to match
	 */
	public void setProducesToMatch(List<String> producesToMatch) {
		this.producesToMatch = producesToMatch;
	}

	/**
	 * Gets headers to match.
	 *
	 * @return the headers to match
	 */
	public List<String> getHeadersToMatch() {
		return headersToMatch;
	}

	/**
	 * Sets headers to match.
	 *
	 * @param headersToMatch the headers to match
	 */
	public void setHeadersToMatch(List<String> headersToMatch) {
		this.headersToMatch = headersToMatch;
	}

	/**
	 * Gets consumes to match.
	 *
	 * @return the consumes to match
	 */
	public List<String> getConsumesToMatch() {
		return consumesToMatch;
	}

	/**
	 * Sets consumes to match.
	 *
	 * @param consumesToMatch the consumes to match
	 */
	public void setConsumesToMatch(List<String> consumesToMatch) {
		this.consumesToMatch = consumesToMatch;
	}

	/**
	 * Is use fqn boolean.
	 *
	 * @return the boolean
	 */
	public boolean isUseFqn() {
		return useFqn;
	}

	/**
	 * Sets use fqn.
	 *
	 * @param useFqn the use fqn
	 */
	public void setUseFqn(boolean useFqn) {
		this.useFqn = useFqn;
	}

	/**
	 * Is auto tag classes boolean.
	 *
	 * @return the boolean
	 */
	public boolean isAutoTagClasses() {
		return autoTagClasses;
	}

	/**
	 * Sets auto tag classes.
	 *
	 * @param autoTagClasses the auto tag classes
	 */
	public void setAutoTagClasses(boolean autoTagClasses) {
		this.autoTagClasses = autoTagClasses;
	}

	/**
	 * Is model and view allowed boolean.
	 *
	 * @return the boolean
	 */
	public boolean isModelAndViewAllowed() {
		return modelAndViewAllowed;
	}

	/**
	 * Sets model and view allowed.
	 *
	 * @param modelAndViewAllowed the model and view allowed
	 */
	public void setModelAndViewAllowed(boolean modelAndViewAllowed) {
		this.modelAndViewAllowed = modelAndViewAllowed;
	}

	/**
	 * Gets packages to exclude.
	 *
	 * @return the packages to exclude
	 */
	public List<String> getPackagesToExclude() {
		return packagesToExclude;
	}

	/**
	 * Sets packages to exclude.
	 *
	 * @param packagesToExclude the packages to exclude
	 */
	public void setPackagesToExclude(List<String> packagesToExclude) {
		this.packagesToExclude = packagesToExclude;
	}

	/**
	 * Gets paths to exclude.
	 *
	 * @return the paths to exclude
	 */
	public List<String> getPathsToExclude() {
		return pathsToExclude;
	}

	/**
	 * Sets paths to exclude.
	 *
	 * @param pathsToExclude the paths to exclude
	 */
	public void setPathsToExclude(List<String> pathsToExclude) {
		this.pathsToExclude = pathsToExclude;
	}

	/**
	 * Is show login endpoint boolean.
	 *
	 * @return the boolean
	 */
	public boolean isShowLoginEndpoint() {
		return showLoginEndpoint;
	}

	/**
	 * Sets show login endpoint.
	 *
	 * @param showLoginEndpoint the show login endpoint
	 */
	public void setShowLoginEndpoint(boolean showLoginEndpoint) {
		this.showLoginEndpoint = showLoginEndpoint;
	}

	/**
	 * Gets packages to scan.
	 *
	 * @return the packages to scan
	 */
	public List<String> getPackagesToScan() {
		return packagesToScan;
	}

	/**
	 * Sets packages to scan.
	 *
	 * @param packagesToScan the packages to scan
	 */
	public void setPackagesToScan(List<String> packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	/**
	 * Is show actuator boolean.
	 *
	 * @return the boolean
	 */
	public boolean isShowActuator() {
		return showActuator;
	}

	/**
	 * Sets show actuator.
	 *
	 * @param showActuator the show actuator
	 */
	public void setShowActuator(boolean showActuator) {
		this.showActuator = showActuator;
	}

	/**
	 * Gets webjars.
	 *
	 * @return the webjars
	 */
	public Webjars getWebjars() {
		return webjars;
	}

	/**
	 * Sets webjars.
	 *
	 * @param webjars the webjars
	 */
	public void setWebjars(Webjars webjars) {
		this.webjars = webjars;
	}

	/**
	 * Gets api docs.
	 *
	 * @return the api docs
	 */
	public ApiDocs getApiDocs() {
		return apiDocs;
	}

	/**
	 * Sets api docs.
	 *
	 * @param apiDocs the api docs
	 */
	public void setApiDocs(ApiDocs apiDocs) {
		this.apiDocs = apiDocs;
	}

	/**
	 * Gets paths to match.
	 *
	 * @return the paths to match
	 */
	public List<String> getPathsToMatch() {
		return pathsToMatch;
	}

	/**
	 * Sets paths to match.
	 *
	 * @param pathsToMatch the paths to match
	 */
	public void setPathsToMatch(List<String> pathsToMatch) {
		this.pathsToMatch = pathsToMatch;
	}

	/**
	 * Gets cache.
	 *
	 * @return the cache
	 */
	public Cache getCache() {
		return cache;
	}

	/**
	 * Sets cache.
	 *
	 * @param cache the cache
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
	}

	/**
	 * Is cache disabled boolean.
	 *
	 * @return the boolean
	 */
	public boolean isCacheDisabled() {
		return cache.isDisabled();
	}


	/**
	 * Gets group configs.
	 *
	 * @return the group configs
	 */
	public Set<GroupConfig> getGroupConfigs() {
		return groupConfigs;
	}

	/**
	 * Sets group configs.
	 *
	 * @param groupConfigs the group configs
	 */
	public void setGroupConfigs(Set<GroupConfig> groupConfigs) {
		this.groupConfigs = groupConfigs;
	}

	/**
	 * Add group config.
	 *
	 * @param groupConfigs the group configs
	 */
	public void addGroupConfig(GroupConfig groupConfigs) {
		this.groupConfigs.add(groupConfigs);
	}

	/**
	 * Gets default consumes media type.
	 *
	 * @return the default consumes media type
	 */
	public String getDefaultConsumesMediaType() {
		return defaultConsumesMediaType;
	}

	/**
	 * Sets default consumes media type.
	 *
	 * @param defaultConsumesMediaType the default consumes media type
	 */
	public void setDefaultConsumesMediaType(String defaultConsumesMediaType) {
		this.defaultConsumesMediaType = defaultConsumesMediaType;
	}

	/**
	 * Gets default produces media type.
	 *
	 * @return the default produces media type
	 */
	public String getDefaultProducesMediaType() {
		return defaultProducesMediaType;
	}

	/**
	 * Sets default produces media type.
	 *
	 * @param defaultProducesMediaType the default produces media type
	 */
	public void setDefaultProducesMediaType(String defaultProducesMediaType) {
		this.defaultProducesMediaType = defaultProducesMediaType;
	}

	/**
	 * Is override with generic response boolean.
	 *
	 * @return the boolean
	 */
	public boolean isOverrideWithGenericResponse() {
		return overrideWithGenericResponse != null && overrideWithGenericResponse;
	}

	/**
	 * Sets override with generic response.
	 *
	 * @param overrideWithGenericResponse the override with generic response
	 */
	public void setOverrideWithGenericResponse(boolean overrideWithGenericResponse) {
		this.overrideWithGenericResponse = overrideWithGenericResponse;
	}

	/**
	 * Gets default override with generic response.
	 *
	 * @return the default override with generic response
	 */
	public boolean isDefaultOverrideWithGenericResponse() {
		if (overrideWithGenericResponse == null)
			return true;
		else
			return overrideWithGenericResponse;
	}

	/**
	 * Is remove broken reference definitions boolean.
	 *
	 * @return the boolean
	 */
	public boolean isRemoveBrokenReferenceDefinitions() {
		return removeBrokenReferenceDefinitions;
	}

	/**
	 * Sets remove broken reference definitions.
	 *
	 * @param removeBrokenReferenceDefinitions the remove broken reference definitions
	 */
	public void setRemoveBrokenReferenceDefinitions(boolean removeBrokenReferenceDefinitions) {
		this.removeBrokenReferenceDefinitions = removeBrokenReferenceDefinitions;
	}

	/**
	 * Is writer wither order by keys boolean.
	 *
	 * @return the boolean
	 */
	public boolean isWriterWithOrderByKeys() {
		return writerWithOrderByKeys;
	}

	/**
	 * Sets writer wither order by keys.
	 *
	 * @param writerWithOrderByKeys the writer wither order by keys
	 */
	public void setWriterWithOrderByKeys(boolean writerWithOrderByKeys) {
		this.writerWithOrderByKeys = writerWithOrderByKeys;
	}

	/**
	 * Is writer with default pretty printer boolean.
	 *
	 * @return the boolean
	 */
	public boolean isWriterWithDefaultPrettyPrinter() {
		return writerWithDefaultPrettyPrinter;
	}

	/**
	 * Sets writer with default pretty printer.
	 *
	 * @param writerWithDefaultPrettyPrinter the writer with default pretty printer
	 */
	public void setWriterWithDefaultPrettyPrinter(boolean writerWithDefaultPrettyPrinter) {
		this.writerWithDefaultPrettyPrinter = writerWithDefaultPrettyPrinter;
	}

	/**
	 * List of allowed locales for i18n.
	 *
	 * @return the allowed locales
	 */
	public List<String> getAllowedLocales() {
		return allowedLocales;
	}

	/**
	 * Sets allowed locales for i18n.
	 *
	 * @param allowedLocales the allowed locales
	 */
	public void setAllowedLocales(List<String> allowedLocales) {
		this.allowedLocales = allowedLocales;
	}

	/**
	 * Is disable i 18 n boolean.
	 *
	 * @return the boolean
	 */
	public boolean isDisableI18n() {
		return disableI18n;
	}

	/**
	 * Sets disable i 18 n.
	 *
	 * @param disableI18n the disable i 18 n
	 */
	public void setDisableI18n(boolean disableI18n) {
		this.disableI18n = disableI18n;
	}

	/**
	 * Is pre loading enabled boolean.
	 *
	 * @return the boolean
	 */
	public boolean isPreLoadingEnabled() {
		return preLoadingEnabled;
	}

	/**
	 * locale list to pre-loading.
	 *
	 * @return the Locales
	 */
	public List<String> getPreLoadingLocales() {
		return preLoadingLocales;
	}

	/**
	 * Sets locale list to pre-loading.
	 *
	 * @param preLoadingEnabled the Locales
	 */
	public void setPreLoadingEnabled(boolean preLoadingEnabled) {
		this.preLoadingEnabled = preLoadingEnabled;
	}

	/**
	 * Sets pre loading locales.
	 *
	 * @param preLoadingLocales the pre loading locales
	 */
	public void setPreLoadingLocales(List<String> preLoadingLocales) {
		this.preLoadingLocales = preLoadingLocales;
	}

	/**
	 * The type Model converters.
	 *
	 * @author bnasslashen
	 */
	public static class ModelConverters {

		/**
		 * The Deprecating converter.
		 */
		private DeprecatingConverter deprecatingConverter = new DeprecatingConverter();

		/**
		 * The Pageable converter.
		 */
		private PageableConverter pageableConverter = new PageableConverter();

		/**
		 * The Polymorphic model converter.
		 */
		private PolymorphicConverter polymorphicConverter = new PolymorphicConverter();


		/**
		 * Gets deprecating converter.
		 *
		 * @return the deprecating converter
		 */
		public DeprecatingConverter getDeprecatingConverter() {
			return deprecatingConverter;
		}

		/**
		 * Sets deprecating converter.
		 *
		 * @param deprecatingConverter the deprecating converter
		 */
		public void setDeprecatingConverter(DeprecatingConverter deprecatingConverter) {
			this.deprecatingConverter = deprecatingConverter;
		}

		/**
		 * Gets pageable converter.
		 *
		 * @return the pageable converter
		 */
		public PageableConverter getPageableConverter() {
			return pageableConverter;
		}

		/**
		 * Sets pageable converter.
		 *
		 * @param pageableConverter the pageable converter
		 */
		public void setPageableConverter(PageableConverter pageableConverter) {
			this.pageableConverter = pageableConverter;
		}

		/**
		 * Gets polymorphic model converter.
		 *
		 * @return the polymorphic model converter
		 */
		public PolymorphicConverter getPolymorphicConverter() {
			return polymorphicConverter;
		}

		/**
		 * Sets polymorphic model converter.
		 *
		 * @param polymorphicConverter the polymorphic model converter
		 */
		public void setPolymorphicConverter(PolymorphicConverter polymorphicConverter) {
			this.polymorphicConverter = polymorphicConverter;
		}

		/**
		 * The type Deprecating converter.
		 *
		 * @author bnasslashen
		 */
		public static class DeprecatingConverter {

			/**
			 * The Enabled.
			 */
			private boolean enabled;

			/**
			 * Is enabled boolean.
			 *
			 * @return the boolean
			 */
			public boolean isEnabled() {
				return enabled;
			}

			/**
			 * Sets enabled.
			 *
			 * @param enabled the enabled
			 */
			public void setEnabled(boolean enabled) {
				this.enabled = enabled;
			}
		}

		/**
		 * The type Polymorphic model converter.
		 */
		public static class PolymorphicConverter {

			/**
			 * The Enabled.
			 */
			private boolean enabled;

			/**
			 * Is enabled boolean.
			 *
			 * @return the boolean
			 */
			public boolean isEnabled() {
				return enabled;
			}

			/**
			 * Sets enabled.
			 *
			 * @param enabled the enabled
			 */
			public void setEnabled(boolean enabled) {
				this.enabled = enabled;
			}
		}


		/**
		 * The type Pageable converter.
		 *
		 * @author bnasslashen
		 */
		public static class PageableConverter {

			/**
			 * The Enabled.
			 */
			private boolean enabled;

			/**
			 * Is enabled boolean.
			 *
			 * @return the boolean
			 */
			public boolean isEnabled() {
				return enabled;
			}

			/**
			 * Sets enabled.
			 *
			 * @param enabled the enabled
			 */
			public void setEnabled(boolean enabled) {
				this.enabled = enabled;
			}
		}
	}

	/**
	 * The type Sort converter.
	 *
	 * @author daniel -shuy
	 */
	public static class SortConverter {

		/**
		 * The Enabled.
		 */
		private boolean enabled;

		/**
		 * Is enabled boolean.
		 *
		 * @return the boolean
		 */
		public boolean isEnabled() {
			return enabled;
		}

		/**
		 * Sets enabled.
		 *
		 * @param enabled the enabled
		 */
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}

	/**
	 * The type Webjars.
	 *
	 * @author bnasslahsen
	 */
	public static class Webjars {
		/**
		 * The Prefix.
		 */
		private String prefix = DEFAULT_WEB_JARS_PREFIX_URL;

		/**
		 * Gets prefix.
		 *
		 * @return the prefix
		 */
		public String getPrefix() {
			return prefix;
		}

		/**
		 * Sets prefix.
		 *
		 * @param prefix the prefix
		 */
		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}
	}

	/**
	 * The type Api docs.
	 *
	 * @author bnasslahsen
	 */
	public static class ApiDocs {
		/**
		 * Path to the generated OpenAPI documentation. For a yaml file, append ".yaml" to the path.
		 */
		private String path = Constants.DEFAULT_API_DOCS_URL;

		/**
		 * Whether to generate and serve an OpenAPI document.
		 */
		private boolean enabled = true;

		/**
		 * The Resolve schema properties.
		 */
		private boolean resolveSchemaProperties;

		/**
		 * The Resolve extensions properties.
		 */
		private boolean resolveExtensionsProperties;

		/**
		 * The Groups.
		 */
		private Groups groups = new Groups();

		/**
		 * The OpenAPI version.
		 */
		private OpenApiVersion version = OpenApiVersion.OPENAPI_3_1;

		/**
		 * Gets path.
		 *
		 * @return the path
		 */
		public String getPath() {
			return path;
		}

		/**
		 * Sets path.
		 *
		 * @param path the path
		 */
		public void setPath(String path) {
			this.path = path;
		}

		/**
		 * Is enabled boolean.
		 *
		 * @return the boolean
		 */
		public boolean isEnabled() {
			return enabled;
		}

		/**
		 * Sets enabled.
		 *
		 * @param enabled the enabled
		 */
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		/**
		 * Gets groups.
		 *
		 * @return the groups
		 */
		public Groups getGroups() {
			return groups;
		}

		/**
		 * Sets groups.
		 *
		 * @param groups the groups
		 */
		public void setGroups(Groups groups) {
			this.groups = groups;
		}

		/**
		 * Is resolve schema properties boolean.
		 *
		 * @return the boolean
		 */
		public boolean isResolveSchemaProperties() {
			return resolveSchemaProperties;
		}

		/**
		 * Sets resolve schema properties.
		 *
		 * @param resolveSchemaProperties the resolve schema properties
		 */
		public void setResolveSchemaProperties(boolean resolveSchemaProperties) {
			this.resolveSchemaProperties = resolveSchemaProperties;
		}

		/**
		 * Gets version.
		 *
		 * @return the version
		 */
		public OpenApiVersion getVersion() {
			return version;
		}

		/**
		 * Sets version.
		 *
		 * @param version the version
		 */
		public void setVersion(OpenApiVersion version) {
			this.version = version;
		}

		/**
		 * The enum OpenApiVersion.
		 */
		public enum OpenApiVersion {
			/**
			 * Openapi 3.0.1 version.
			 */
			OPENAPI_3_0("3.0.1"),
			/**
			 * Openapi 3.1.0 version.
			 */
			OPENAPI_3_1("3.1.0");

			/**
			 * The Open api version.
			 */
			private final String version;

			/**
			 * Instantiates a new OpenApiVersion.
			 *
			 * @param openApiVersion the open api version
			 */
			OpenApiVersion(String openApiVersion) {
				this.version = openApiVersion;
			}

			/**
			 * Gets open api version.
			 *
			 * @return the open api version
			 */
			public String getVersion() {
				return version;
			}
		}

		/**
		 * Is resolve extensions properties boolean.
		 *
		 * @return the boolean
		 */
		public boolean isResolveExtensionsProperties() {
			return resolveExtensionsProperties;
		}

		/**
		 * Sets resolve extensions properties.
		 *
		 * @param resolveExtensionsProperties the resolve extensions properties
		 */
		public void setResolveExtensionsProperties(boolean resolveExtensionsProperties) {
			this.resolveExtensionsProperties = resolveExtensionsProperties;
		}
	}

	/**
	 * The type Groups.
	 *
	 * @author bnasslahsen
	 */
	public static class Groups {
		/**
		 * The Enabled.
		 */
		private boolean enabled;

		/**
		 * Is enabled boolean.
		 *
		 * @return the boolean
		 */
		public boolean isEnabled() {
			return enabled;
		}

		/**
		 * Sets enabled.
		 *
		 * @param enabled the enabled
		 */
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}


	/**
	 * The type Cache.
	 *
	 * @author bnasslahsen
	 */
	public static class Cache {
		/**
		 * The Disabled.
		 */
		private boolean disabled;

		/**
		 * Is disabled boolean.
		 *
		 * @return the boolean
		 */
		public boolean isDisabled() {
			return disabled;
		}

		/**
		 * Sets disabled.
		 *
		 * @param disabled the disabled
		 */
		public void setDisabled(boolean disabled) {
			this.disabled = disabled;
		}
	}

	/**
	 * The type Group config.
	 *
	 * @author bnasslahsen
	 */
	public static class GroupConfig {

		/**
		 * The Paths to match.
		 */
		private List<String> pathsToMatch;

		/**
		 * The Packages to scan.
		 */
		private List<String> packagesToScan;

		/**
		 * The Packages to exclude.
		 */
		private List<String> packagesToExclude;

		/**
		 * The Paths to exclude.
		 */
		private List<String> pathsToExclude;

		/**
		 * The Group.
		 */
		private String group;

		/**
		 * The Produces to match.
		 */
		private List<String> producesToMatch;

		/**
		 * The Headers to match.
		 */
		private List<String> headersToMatch;

		/**
		 * The Consumes to match.
		 */
		private List<String> consumesToMatch;

		/**
		 * The Display name.
		 */
		private String displayName;

		/**
		 * The Open api.
		 */
		private OpenAPI openApi;


		/**
		 * Instantiates a new Group config.
		 */
		public GroupConfig() {
		}

		/**
		 * Instantiates a new Group config.
		 *
		 * @param group             the group
		 * @param pathsToMatch      the paths to match
		 * @param packagesToScan    the packages to scan
		 * @param packagesToExclude the packages to exclude
		 * @param pathsToExclude    the paths to exclude
		 * @param producesToMatch   the produces to match
		 * @param consumesToMatch   the consumes to match
		 * @param headersToMatch    the headers to match
		 * @param displayName       the display name
		 */
		public GroupConfig(String group, List<String> pathsToMatch, List<String> packagesToScan,
				List<String> packagesToExclude, List<String> pathsToExclude,
				List<String> producesToMatch, List<String> consumesToMatch, List<String> headersToMatch,
				String displayName) {
			this.pathsToMatch = pathsToMatch;
			this.pathsToExclude = pathsToExclude;
			this.packagesToExclude = packagesToExclude;
			this.packagesToScan = packagesToScan;
			this.group = group;
			this.producesToMatch = producesToMatch;
			this.consumesToMatch = consumesToMatch;
			this.headersToMatch = headersToMatch;
			this.displayName = displayName;
		}

		/**
		 * Gets headers to match.
		 *
		 * @return the headers to match
		 */
		public List<String> getHeadersToMatch() {
			return headersToMatch;
		}

		/**
		 * Sets headers to match.
		 *
		 * @param headersToMatch the headers to match
		 */
		public void setHeadersToMatch(List<String> headersToMatch) {
			this.headersToMatch = headersToMatch;
		}

		/**
		 * Gets consumes to match.
		 *
		 * @return the consumes to match
		 */
		public List<String> getConsumesToMatch() {
			return consumesToMatch;
		}

		/**
		 * Sets consumes to match.
		 *
		 * @param consumesToMatch the consumes to match
		 */
		public void setConsumesToMatch(List<String> consumesToMatch) {
			this.consumesToMatch = consumesToMatch;
		}

		/**
		 * Gets paths to match.
		 *
		 * @return the paths to match
		 */
		public List<String> getPathsToMatch() {
			return pathsToMatch;
		}

		/**
		 * Sets paths to match.
		 *
		 * @param pathsToMatch the paths to match
		 */
		public void setPathsToMatch(List<String> pathsToMatch) {
			this.pathsToMatch = pathsToMatch;
		}

		/**
		 * Gets packages to scan.
		 *
		 * @return the packages to scan
		 */
		public List<String> getPackagesToScan() {
			return packagesToScan;
		}

		/**
		 * Sets packages to scan.
		 *
		 * @param packagesToScan the packages to scan
		 */
		public void setPackagesToScan(List<String> packagesToScan) {
			this.packagesToScan = packagesToScan;
		}

		/**
		 * Gets group.
		 *
		 * @return the group
		 */
		public String getGroup() {
			return group;
		}

		/**
		 * Sets group.
		 *
		 * @param group the group
		 */
		public void setGroup(String group) {
			this.group = group;
		}

		/**
		 * Gets packages to exclude.
		 *
		 * @return the packages to exclude
		 */
		public List<String> getPackagesToExclude() {
			return packagesToExclude;
		}

		/**
		 * Sets packages to exclude.
		 *
		 * @param packagesToExclude the packages to exclude
		 */
		public void setPackagesToExclude(List<String> packagesToExclude) {
			this.packagesToExclude = packagesToExclude;
		}

		/**
		 * Gets paths to exclude.
		 *
		 * @return the paths to exclude
		 */
		public List<String> getPathsToExclude() {
			return pathsToExclude;
		}

		/**
		 * Sets paths to exclude.
		 *
		 * @param pathsToExclude the paths to exclude
		 */
		public void setPathsToExclude(List<String> pathsToExclude) {
			this.pathsToExclude = pathsToExclude;
		}

		/**
		 * Gets produces to match.
		 *
		 * @return the produces to match
		 */
		public List<String> getProducesToMatch() {
			return producesToMatch;
		}

		/**
		 * Sets produces to match.
		 *
		 * @param producesToMatch the produces to match
		 */
		public void setProducesToMatch(List<String> producesToMatch) {
			this.producesToMatch = producesToMatch;
		}

		/**
		 * Gets display name.
		 *
		 * @return the display name
		 */
		public String getDisplayName() {
			return displayName;
		}

		/**
		 * Sets display name.
		 *
		 * @param displayName the display name
		 */
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		/**
		 * Gets open api.
		 *
		 * @return the open api
		 */
		public OpenAPI getOpenApi() {
			return openApi;
		}

		/**
		 * Sets open api.
		 *
		 * @param openApi the open api
		 */
		public void setOpenApi(OpenAPI openApi) {
			this.openApi = openApi;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			GroupConfig that = (GroupConfig) o;
			return Objects.equals(group, that.group);
		}

		@Override
		public int hashCode() {
			return Objects.hash(group);
		}
	}


	/**
	 * Is show oauth 2 endpoints boolean.
	 *
	 * @return the boolean
	 */
	public boolean isShowOauth2Endpoints() {
		return showOauth2Endpoints;
	}

	/**
	 * Sets show oauth 2 endpoints.
	 *
	 * @param showOauth2Endpoints the show oauth 2 endpoints
	 */
	public void setShowOauth2Endpoints(boolean showOauth2Endpoints) {
		this.showOauth2Endpoints = showOauth2Endpoints;
	}

	/**
	 * Gets spec version.
	 *
	 * @return the spec version
	 */
	public SpecVersion getSpecVersion() {
		if (apiDocs.getVersion() == OpenApiVersion.OPENAPI_3_1)
			return SpecVersion.V31;
		return SpecVersion.V30;
	}

	/**
	 * Is openapi 31 boolean.
	 *
	 * @return the boolean
	 */
	public boolean isOpenapi31() {
		if (apiDocs.getVersion() == OpenApiVersion.OPENAPI_3_1)
			return true;
		return false;
	}

	/**
	 * Is enable default api docs boolean.
	 *
	 * @return the boolean
	 */
	public boolean isEnableDefaultApiDocs() {
		return enableDefaultApiDocs;
	}

	/**
	 * Sets enable default api docs.
	 *
	 * @param enableDefaultApiDocs the enable default api docs
	 */
	public void setEnableDefaultApiDocs(boolean enableDefaultApiDocs) {
		this.enableDefaultApiDocs = enableDefaultApiDocs;
	}

	/**
	 * Is explicit object schema boolean.
	 *
	 * @return the boolean
	 */
	public boolean isExplicitObjectSchema() {
		return explicitObjectSchema;
	}

	/**
	 * Sets explicit object schema.
	 *
	 * @param explicitObjectSchema the explicit object schema
	 */
	public void setExplicitObjectSchema(boolean explicitObjectSchema) {
		this.explicitObjectSchema = explicitObjectSchema;
	}

	/**
	 * Is use arbitrary schemas boolean.
	 *
	 * @return the boolean
	 */
	public boolean isUseArbitrarySchemas() {
		return useArbitrarySchemas;
	}

	/**
	 * Sets use arbitrary schemas.
	 *
	 * @param useArbitrarySchemas the use arbitrary schemas
	 */
	public void setUseArbitrarySchemas(boolean useArbitrarySchemas) {
		this.useArbitrarySchemas = useArbitrarySchemas;
	}
}

package org.springdoc.core;


import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Please refer to the swagger
 * <a href="https://github.com/swagger-api/swagger-ui/blob/master/docs/usage/configuration.md">configuration.md</a>
 * to get the idea what each parameter does.
 */
@Configuration
@ConfigurationProperties(prefix = "springdoc.swagger-ui")
public class SwaggerUiConfigProperties {
    /**
     * The path for the Swagger UI pages to load. Will redirect to the springdoc.webjars.prefix property.
     */
    private String path = Constants.DEFAULT_SWAGGER_UI_PATH;

    /**
     * The name of a component available via the plugin system to use as the top-level layout for Swagger UI.
     */
    private String layout;
    /**
     * URL to fetch external configuration document from.
     */
    private String configUrl;
    /**
     * If set, enables filtering. The top bar will show an edit box that
     * could be used to filter the tagged operations that are shown.
     */
    private String filter;

    /**
     * Apply a sort to the operation list of each API
     */
    private String operationsSorter;
    /**
     * Apply a sort to the tag list of each API
     */
    private String tagsSorter;

    /**
     * Enables or disables deep linking for tags and operations.
     *
     * @see <a href="https://github.com/swagger-api/swagger-ui/blob/master/docs/usage/deep-linking.md">deep-linking.md</a>
     */
    private Boolean deepLinking;
    /**
     * Controls the display of operationId in operations list.
     */
    private Boolean displayOperationId;
    /**
     * The default expansion depth for models (set to -1 completely hide the models).
     */
    private Integer defaultModelsExpandDepth;
    /**
     * The default expansion depth for the model on the model-example section.
     */
    private Integer defaultModelExpandDepth;

    /**
     * Controls how the model is shown when the API is first rendered.
     */
    private String defaultModelRendering;
    /**
     * Controls the display of the request duration (in milliseconds) for Try-It-Out requests.
     */
    private Boolean displayRequestDuration;
    /**
     * Controls the default expansion setting for the operations and tags.
     */
    private String docExpansion;
    /**
     * If set, limits the number of tagged operations displayed to at most this many.
     */
    private Integer maxDisplayedTags;
    /**
     * Controls the display of vendor extension (x-) fields and values.
     */
    private Boolean showExtensions;
    /**
     * Controls the display of extensions
     */
    private Boolean showCommonExtensions;

    /**
     * The supported try it out methods
     */
    private List<String> supportedSubmitMethods;

    /**
     * OAuth redirect URL.
     */
    private String oauth2RedirectUrl;


    public Map<String, String> getConfigParameters() {
        final Map<String, String> params = new TreeMap<>();
        put("layout", layout, params);
        put("configUrl", configUrl, params);
        put("filter", filter, params);
        put("deepLinking", this.deepLinking, params);
        put("displayOperationId", displayOperationId, params);
        put("defaultModelsExpandDepth", defaultModelsExpandDepth, params);
        put("defaultModelExpandDepth", defaultModelExpandDepth, params);
        put("defaultModelRendering", defaultModelRendering, params);
        put("displayRequestDuration", displayRequestDuration, params);
        put("docExpansion", docExpansion, params);
        put("maxDisplayedTags", maxDisplayedTags, params);
        put("showExtensions", showExtensions, params);
        put("showCommonExtensions", showCommonExtensions, params);
        put("operationsSorter", operationsSorter, params);
        put("tagsSorter", tagsSorter, params);
        if (!CollectionUtils.isEmpty(supportedSubmitMethods))
            put("supportedSubmitMethods", supportedSubmitMethods.toString(), params);
        put("oauth2RedirectUrl", oauth2RedirectUrl, params);
        return params;
    }

    protected void put(final String name, final Integer value, final Map<String, String> params) {
        if (value != null) {
            params.put(name, value.toString());
        }
    }

    protected void put(final String name, final Boolean value, final Map<String, String> params) {
        if (value != null) {
            params.put(name, value.toString());
        }
    }

    protected void put(final String name, final String value, final Map<String, String> params) {
        if (!StringUtils.isEmpty(value)) {
            params.put(name, value);
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getConfigUrl() {
        return configUrl;
    }

    public void setConfigUrl(String configUrl) {
        this.configUrl = configUrl;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getOperationsSorter() {
        return operationsSorter;
    }

    public void setOperationsSorter(String operationsSorter) {
        this.operationsSorter = operationsSorter;
    }

    public String getTagsSorter() {
        return tagsSorter;
    }

    public void setTagsSorter(String tagsSorter) {
        this.tagsSorter = tagsSorter;
    }

    public Boolean getDeepLinking() {
        return deepLinking;
    }

    public void setDeepLinking(Boolean deepLinking) {
        this.deepLinking = deepLinking;
    }

    public Boolean getDisplayOperationId() {
        return displayOperationId;
    }

    public void setDisplayOperationId(Boolean displayOperationId) {
        this.displayOperationId = displayOperationId;
    }

    public Integer getDefaultModelsExpandDepth() {
        return defaultModelsExpandDepth;
    }

    public void setDefaultModelsExpandDepth(Integer defaultModelsExpandDepth) {
        this.defaultModelsExpandDepth = defaultModelsExpandDepth;
    }

    public Integer getDefaultModelExpandDepth() {
        return defaultModelExpandDepth;
    }

    public void setDefaultModelExpandDepth(Integer defaultModelExpandDepth) {
        this.defaultModelExpandDepth = defaultModelExpandDepth;
    }

    public String getDefaultModelRendering() {
        return defaultModelRendering;
    }

    public void setDefaultModelRendering(String defaultModelRendering) {
        this.defaultModelRendering = defaultModelRendering;
    }

    public Boolean getDisplayRequestDuration() {
        return displayRequestDuration;
    }

    public void setDisplayRequestDuration(Boolean displayRequestDuration) {
        this.displayRequestDuration = displayRequestDuration;
    }

    public String getDocExpansion() {
        return docExpansion;
    }

    public void setDocExpansion(String docExpansion) {
        this.docExpansion = docExpansion;
    }

    public Integer getMaxDisplayedTags() {
        return maxDisplayedTags;
    }

    public void setMaxDisplayedTags(Integer maxDisplayedTags) {
        this.maxDisplayedTags = maxDisplayedTags;
    }

    public Boolean getShowExtensions() {
        return showExtensions;
    }

    public void setShowExtensions(Boolean showExtensions) {
        this.showExtensions = showExtensions;
    }

    public Boolean getShowCommonExtensions() {
        return showCommonExtensions;
    }

    public void setShowCommonExtensions(Boolean showCommonExtensions) {
        this.showCommonExtensions = showCommonExtensions;
    }

    public List<String> getSupportedSubmitMethods() {
        return supportedSubmitMethods;
    }

    public void setSupportedSubmitMethods(List<String> supportedSubmitMethods) {
        this.supportedSubmitMethods = supportedSubmitMethods;
    }

    public String getOauth2RedirectUrl() {
        return oauth2RedirectUrl;
    }

    public void setOauth2RedirectUrl(String oauth2RedirectUrl) {
        this.oauth2RedirectUrl = oauth2RedirectUrl;
    }
}
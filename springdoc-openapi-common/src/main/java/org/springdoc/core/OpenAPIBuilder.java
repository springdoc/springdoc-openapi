package org.springdoc.core;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springdoc.core.Constants.*;

public class OpenAPIBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAPIBuilder.class);
    private final OpenAPI openAPI;
    private boolean isServersPresent = false;
    private final ApplicationContext context;
    private final SecurityParser securityParser;
    private final Map<HandlerMethod, io.swagger.v3.oas.models.tags.Tag> springdocTags = new HashMap<>();
    private String serverBaseUrl;
    private final Optional<SecurityOAuth2Provider> springSecurityOAuth2Provider;

    @SuppressWarnings("WeakerAccess")
    OpenAPIBuilder(Optional<OpenAPI> openAPI, ApplicationContext context, SecurityParser securityParser, Optional<SecurityOAuth2Provider> springSecurityOAuth2Provider) {
        if (openAPI.isPresent()) {
            this.openAPI = openAPI.get();
            if (this.openAPI.getComponents() == null)
                this.openAPI.setComponents(new Components());
            if (this.openAPI.getPaths() == null)
                this.openAPI.setPaths(new Paths());
            if (!CollectionUtils.isEmpty(this.openAPI.getServers()))
                this.isServersPresent = true;
        } else {
            this.openAPI = new OpenAPI();
            this.openAPI.setComponents(new Components());
            this.openAPI.setPaths(new Paths());
        }
        this.context = context;
        this.securityParser = securityParser;
        this.springSecurityOAuth2Provider = springSecurityOAuth2Provider;
    }

    private static String splitCamelCase(String str) {
        return str.replaceAll(
                String.format(
                        "%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"),
                "-")
                .toLowerCase(Locale.ROOT);
    }

    public OpenAPI getOpenAPI() {
        return openAPI;
    }

    public Components getComponents() {
        return openAPI.getComponents();
    }

    public Paths getPaths() {
        return openAPI.getPaths();
    }

    public void build() {
        Optional<OpenAPIDefinition> apiDef = getOpenAPIDefinition();
        if (apiDef.isPresent()) {
            buildOpenAPIWithOpenAPIDefinition(openAPI, apiDef.get());
        }
        // Set default info
        else if (openAPI.getInfo() == null) {
            Info infos = new Info().title(DEFAULT_TITLE).version(DEFAULT_VERSION);
            openAPI.setInfo(infos);
        }
        // default server value
        if (CollectionUtils.isEmpty(openAPI.getServers()) || !isServersPresent) {
            Server server = new Server().url(serverBaseUrl).description(DEFAULT_SERVER_DESCRIPTION);
            List servers = new ArrayList();
            servers.add(server);
            openAPI.setServers(servers);
        }
        // add security schemes
        this.calculateSecuritySchemes(openAPI.getComponents());
    }

    public Operation buildTags(HandlerMethod handlerMethod, Operation operation, OpenAPI openAPI) {
        // class tags
        List<Tag> classTags =
                ReflectionUtils.getRepeatableAnnotations(handlerMethod.getBeanType(), Tag.class);

        // method tags
        List<Tag> methodTags =
                ReflectionUtils.getRepeatableAnnotations(handlerMethod.getMethod(), Tag.class);

        List<Tag> allTags = new ArrayList<>();
        Set<String> tagsStr = new HashSet<>();

        if (!CollectionUtils.isEmpty(methodTags)) {
            tagsStr.addAll(methodTags.stream().map(Tag::name).collect(Collectors.toSet()));
            allTags.addAll(methodTags);
        }

        if (!CollectionUtils.isEmpty(classTags)) {
            tagsStr.addAll(classTags.stream().map(Tag::name).collect(Collectors.toSet()));
            allTags.addAll(classTags);
        }

        if (springdocTags.containsKey(handlerMethod)) {
            io.swagger.v3.oas.models.tags.Tag tag = springdocTags.get(handlerMethod);
            tagsStr.add(tag.getName());
            if(openAPI.getTags() == null || !openAPI.getTags().contains(tag)) {
                openAPI.addTagsItem(tag);
            }
        }

        Optional<Set<io.swagger.v3.oas.models.tags.Tag>> tags = AnnotationsUtils
                .getTags(allTags.toArray(new Tag[0]), true);

        if (tags.isPresent()) {
            Set<io.swagger.v3.oas.models.tags.Tag> tagsSet = tags.get();
            // Existing tags
            List<io.swagger.v3.oas.models.tags.Tag> openApiTags = openAPI.getTags();
            if (!CollectionUtils.isEmpty(openApiTags))
                tagsSet.addAll(openApiTags);
            openAPI.setTags(new ArrayList<>(tagsSet));
        }

        // Handle SecurityRequirement at operation level
        Optional<io.swagger.v3.oas.annotations.security.SecurityRequirement[]> securityRequirement = securityParser
                .getSecurityRequirements(handlerMethod);
        securityRequirement.ifPresent(securityRequirements -> securityParser.buildSecurityRequirement(securityRequirements, operation));

        if (!CollectionUtils.isEmpty(tagsStr)) {
            operation.setTags(new ArrayList<>(tagsStr));
        }

        if (CollectionUtils.isEmpty(operation.getTags())) {
            operation.addTagsItem(splitCamelCase(handlerMethod.getBeanType().getSimpleName()));
        }

        return operation;
    }

    public void setServerBaseUrl(String serverBaseUrl) {
        this.serverBaseUrl = serverBaseUrl;
    }

    private Optional<OpenAPIDefinition> getOpenAPIDefinition() {
        // Look for OpenAPIDefinition in a spring managed bean
        Map<String, Object> openAPIDefinitionMap = context.getBeansWithAnnotation(OpenAPIDefinition.class);
        OpenAPIDefinition apiDef = null;
        if (openAPIDefinitionMap.size() > 1)
            LOGGER.warn(
                    "found more than one OpenAPIDefinition class. springdoc-openapi will be using the first one found.");
        if (openAPIDefinitionMap.size() > 0) {
            Map.Entry<String, Object> entry = openAPIDefinitionMap.entrySet().iterator().next();
            Class<?> objClz = entry.getValue().getClass();
            apiDef = ReflectionUtils.getAnnotation(objClz, OpenAPIDefinition.class);
        }

        // Look for OpenAPIDefinition in the spring classpath
        else {
            ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
                    false);
            scanner.addIncludeFilter(new AnnotationTypeFilter(OpenAPIDefinition.class));
            if (AutoConfigurationPackages.has(context)) {
                List<String> packagesToScan = AutoConfigurationPackages.get(context);
                apiDef = getApiDefClass(scanner, packagesToScan);
            }

        }
        return Optional.ofNullable(apiDef);
    }

    private void buildOpenAPIWithOpenAPIDefinition(OpenAPI openAPI, OpenAPIDefinition apiDef) {
        // info
        Optional<Info> infos = AnnotationsUtils.getInfo(apiDef.info());
        if (infos.isPresent()) {
            Info info = infos.get();
            if (StringUtils.isNotBlank(info.getTitle())) {
                PropertyResolverUtils propertyResolverUtils = context.getBean(PropertyResolverUtils.class);
                info.title(propertyResolverUtils.resolve(info.getTitle()));
            }
            openAPI.setInfo(info);
        }
        // OpenApiDefinition security requirements
        securityParser.getSecurityRequirements(apiDef.security()).ifPresent(openAPI::setSecurity);
        // OpenApiDefinition external docs
        AnnotationsUtils.getExternalDocumentation(apiDef.externalDocs()).ifPresent(openAPI::setExternalDocs);
        // OpenApiDefinition tags
        AnnotationsUtils.getTags(apiDef.tags(), false).ifPresent(tags -> openAPI.setTags(new ArrayList<>(tags)));
        // OpenApiDefinition servers
        Optional<List<Server>> optionalServers = AnnotationsUtils.getServers(apiDef.servers());
        if (optionalServers.isPresent()) {
            openAPI.setServers(optionalServers.get());
            this.isServersPresent = true;
        }
        // OpenApiDefinition extensions
        if (apiDef.extensions().length > 0) {
            openAPI.setExtensions(AnnotationsUtils.getExtensions(apiDef.extensions()));
        }
    }

    private void calculateSecuritySchemes(Components components) {
        // Look for OpenAPIDefinition in a spring managed bean
        Map<String, Object> securitySchemeBeans = context
                .getBeansWithAnnotation(io.swagger.v3.oas.annotations.security.SecurityScheme.class);
        if (securitySchemeBeans.size() > 0) {
            for (Map.Entry<String, Object> entry : securitySchemeBeans.entrySet()) {
                Class<?> objClz = entry.getValue().getClass();
                List<io.swagger.v3.oas.annotations.security.SecurityScheme> apiSecurityScheme = ReflectionUtils
                        .getRepeatableAnnotations(objClz, io.swagger.v3.oas.annotations.security.SecurityScheme.class);
                this.addSecurityScheme(apiSecurityScheme, components);
            }
        }

        // Look for OpenAPIDefinition in the spring classpath
        else {
            ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
                    false);
            scanner.addIncludeFilter(
                    new AnnotationTypeFilter(io.swagger.v3.oas.annotations.security.SecurityScheme.class));
            if (AutoConfigurationPackages.has(context)) {
                List<String> packagesToScan = AutoConfigurationPackages.get(context);
                List<io.swagger.v3.oas.annotations.security.SecurityScheme> apiSecurityScheme = getSecuritySchemesClasses(
                        scanner, packagesToScan);
                this.addSecurityScheme(apiSecurityScheme, components);
            }

        }
    }

    private void addSecurityScheme(List<io.swagger.v3.oas.annotations.security.SecurityScheme> apiSecurityScheme,
                                   Components components) {
        for (io.swagger.v3.oas.annotations.security.SecurityScheme securitySchemeAnnotation : apiSecurityScheme) {
            Optional<SecuritySchemePair> securityScheme = securityParser.getSecurityScheme(securitySchemeAnnotation);
            if (securityScheme.isPresent()) {
                Map<String, SecurityScheme> securitySchemeMap = new HashMap<>();
                if (StringUtils.isNotBlank(securityScheme.get().getKey())) {
                    securitySchemeMap.put(securityScheme.get().getKey(), securityScheme.get().getSecurityScheme());
                    if (!CollectionUtils.isEmpty(components.getSecuritySchemes())) {
                        components.getSecuritySchemes().putAll(securitySchemeMap);
                    } else {
                        components.setSecuritySchemes(securitySchemeMap);
                    }
                }
            }
        }
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
                    LOGGER.error("Class Not Found in classpath : {}", e.getMessage());
                }
            }
        }
        return null;
    }

    private List<io.swagger.v3.oas.annotations.security.SecurityScheme> getSecuritySchemesClasses(
            ClassPathScanningCandidateComponentProvider scanner, List<String> packagesToScan) {
        List<io.swagger.v3.oas.annotations.security.SecurityScheme> apiSecurityScheme = new ArrayList<>();
        for (String pack : packagesToScan) {
            for (BeanDefinition bd : scanner.findCandidateComponents(pack)) {
                try {
                    apiSecurityScheme.add(AnnotationUtils.findAnnotation(Class.forName(bd.getBeanClassName()),
                            io.swagger.v3.oas.annotations.security.SecurityScheme.class));
                } catch (ClassNotFoundException e) {
                    LOGGER.error("Class Not Found in classpath : {}", e.getMessage());
                }
            }
        }
        return apiSecurityScheme;
    }

    public void addTag(Set<HandlerMethod> handlerMethods, io.swagger.v3.oas.models.tags.Tag tag) {
        handlerMethods.forEach(handlerMethod -> springdocTags.put(handlerMethod, tag));
    }

    public Map<String, Object> getRestControllersMap() {
        return context.getBeansWithAnnotation(RestController.class);
    }

    public Map<String, Object> getRequestMappingMap() {
        return context.getBeansWithAnnotation(RequestMapping.class);
    }

    public Map<String, Object> getControllerAdviceMap() {
        Map<String, Object> controllerAdviceMap = context.getBeansWithAnnotation(ControllerAdvice.class);
        return Stream.of(controllerAdviceMap).flatMap(mapEl -> mapEl.entrySet().stream()).filter(
                controller -> (AnnotationUtils.findAnnotation(controller.getValue().getClass(), Hidden.class) == null))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a1, a2) -> a1));
    }

    public Optional<SecurityOAuth2Provider> getSpringSecurityOAuth2Provider() {
        return springSecurityOAuth2Provider;
    }
}

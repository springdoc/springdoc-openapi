package org.springdoc.core;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.method.HandlerMethod;

import java.util.Map;
import java.util.Optional;

@SuppressWarnings("rawtypes")
public class RequestBodyBuilder {

    private final AbstractParameterBuilder parameterBuilder;

    public RequestBodyBuilder(AbstractParameterBuilder parameterBuilder) {
        super();
        this.parameterBuilder = parameterBuilder;
    }

    public Optional<RequestBody> buildRequestBodyFromDoc(
            io.swagger.v3.oas.annotations.parameters.RequestBody requestBody, String[] classConsumes,
            String[] methodConsumes, Components components, JsonView jsonViewAnnotation) {
        if (requestBody == null) {
            return Optional.empty();
        }
        RequestBody requestBodyObject = new RequestBody();
        boolean isEmpty = true;

        if (StringUtils.isNotBlank(requestBody.ref())) {
            requestBodyObject.set$ref(requestBody.ref());
            return Optional.of(requestBodyObject);
        }

        if (StringUtils.isNotBlank(requestBody.description())) {
            requestBodyObject.setDescription(requestBody.description());
            isEmpty = false;
        }

        if (requestBody.required()) {
            requestBodyObject.setRequired(requestBody.required());
            isEmpty = false;
        }
        if (requestBody.extensions().length > 0) {
            Map<String, Object> extensions = AnnotationsUtils.getExtensions(requestBody.extensions());
            extensions.forEach(requestBodyObject::addExtension);
            isEmpty = false;
        }

        if (requestBody.content().length > 0) {
            isEmpty = false;
        }

        if (isEmpty) {
            return Optional.empty();
        }
        AnnotationsUtils
                .getContent(requestBody.content(), classConsumes == null ? new String[0] : classConsumes,
                        methodConsumes == null ? new String[0] : methodConsumes, null, components, jsonViewAnnotation)
                .ifPresent(requestBodyObject::setContent);
        return Optional.of(requestBodyObject);
    }

    public void calculateRequestBodyInfo(Components components, HandlerMethod handlerMethod,
                                         MethodAttributes methodAttributes, int i, ParameterInfo parameterInfo, RequestBodyInfo requestBodyInfo) {
        RequestBody requestBody = requestBodyInfo.getRequestBody();

        // Get it from parameter level, if not present
        if (requestBody == null) {
            io.swagger.v3.oas.annotations.parameters.RequestBody requestBodyDoc = parameterBuilder
                    .getParameterAnnotation(handlerMethod, parameterInfo.getParameter(), i,
                            io.swagger.v3.oas.annotations.parameters.RequestBody.class);
            requestBody = this.buildRequestBodyFromDoc(requestBodyDoc, methodAttributes.getClassConsumes(),
                    methodAttributes.getMethodConsumes(), components, null).orElse(null);
        }

        RequestPart requestPart = parameterBuilder.getParameterAnnotation(handlerMethod, parameterInfo.getParameter(),
                i, RequestPart.class);
        String paramName = null;
        if (requestPart != null)
            paramName = StringUtils.defaultIfEmpty(requestPart.value(), requestPart.name());
        paramName = StringUtils.defaultIfEmpty(paramName, parameterInfo.getpName());
        parameterInfo.setpName(paramName);

        requestBody = buildRequestBody(requestBody, components, methodAttributes, parameterInfo,
                requestBodyInfo);
        requestBodyInfo.setRequestBody(requestBody);
    }

    private RequestBody buildRequestBody(RequestBody requestBody, Components components,
                                         MethodAttributes methodAttributes,
                                         ParameterInfo parameterInfo, RequestBodyInfo requestBodyInfo) {
        if (requestBody == null)
            requestBody = new RequestBody();

        if (parameterInfo.getParameterModel() != null) {
            io.swagger.v3.oas.models.parameters.Parameter parameter = parameterInfo.getParameterModel();
            if (StringUtils.isNotBlank(parameter.getDescription()))
                requestBody.setDescription(parameter.getDescription());
            if (parameter.getSchema() != null) {
                Schema<?> schema = parameterInfo.getParameterModel().getSchema();
                buildContent(requestBody, methodAttributes, schema);
            }
            requestBody.setRequired(parameter.getRequired());
        }

        if (requestBody.getContent() == null
                || (requestBody.getContent() != null && methodAttributes.isMethodOverloaded())) {
            Schema<?> schema = parameterBuilder.calculateSchema(components, parameterInfo.getParameter(),
                    parameterInfo.getpName(), requestBodyInfo,
                    methodAttributes.getJsonViewAnnotationForRequestBody());
            buildContent(requestBody, methodAttributes, schema);
        }
        return requestBody;
    }

    private void buildContent(RequestBody requestBody, MethodAttributes methodAttributes, Schema<?> schema) {
        Content content = requestBody.getContent();
        if (methodAttributes.isMethodOverloaded() && content != null) {
            for (String value : methodAttributes.getAllConsumes()) {
                setMediaTypeToContent(schema, content, value);
            }
        } else {
            content = new Content();
            for (String value : methodAttributes.getAllConsumes()) {
                setMediaTypeToContent(schema, content, value);
            }
        }
        requestBody.setContent(content);
    }


    private void setMediaTypeToContent(Schema schema, Content content, String value) {
        io.swagger.v3.oas.models.media.MediaType mediaTypeObject = new io.swagger.v3.oas.models.media.MediaType();
        mediaTypeObject.setSchema(schema);
        content.addMediaType(value, mediaTypeObject);
    }
}

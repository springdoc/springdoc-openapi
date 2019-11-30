package org.springdoc.core;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.FileSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@SuppressWarnings("rawtypes")
public abstract class AbstractParameterBuilder {

    private final LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer;
    private final IgnoredParameterAnnotations ignoredParameterAnnotations;


    public AbstractParameterBuilder(LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer, IgnoredParameterAnnotations ignoredParameterAnnotations) {
        this.localSpringDocParameterNameDiscoverer = localSpringDocParameterNameDiscoverer;
        this.ignoredParameterAnnotations = ignoredParameterAnnotations;
    }

    Parameter mergeParameter(List<Parameter> existingParamDoc, Parameter paramCalcul) {
        Parameter result = paramCalcul;
        if (paramCalcul != null && paramCalcul.getName() != null) {
            final String name = paramCalcul.getName();
            Parameter paramDoc = existingParamDoc.stream().filter(p -> name.equals(p.getName())).findAny().orElse(null);
            if (paramDoc != null) {
                mergeParameter(paramCalcul, paramDoc);
                result = paramDoc;
            } else
                existingParamDoc.add(result);
        }
        return result;
    }

    private void mergeParameter(Parameter paramCalcul, Parameter paramDoc) {
        if (StringUtils.isBlank(paramDoc.getDescription()))
            paramDoc.setDescription(paramCalcul.getDescription());

        if (StringUtils.isBlank(paramDoc.getIn()))
            paramDoc.setIn(paramCalcul.getIn());

        if (paramDoc.getExample() == null)
            paramDoc.setExample(paramCalcul.getExample());

        if (paramDoc.getDeprecated() == null)
            paramDoc.setDeprecated(paramCalcul.getDeprecated());

        if (paramDoc.getRequired() == null)
            paramDoc.setRequired(paramCalcul.getRequired());

        if (paramDoc.getAllowEmptyValue() == null)
            paramDoc.setAllowEmptyValue(paramCalcul.getAllowEmptyValue());

        if (paramDoc.getAllowReserved() == null)
            paramDoc.setAllowReserved(paramCalcul.getAllowReserved());

        if (StringUtils.isBlank(paramDoc.get$ref()))
            paramDoc.set$ref(paramDoc.get$ref());

        if (paramDoc.getSchema() == null)
            paramDoc.setSchema(paramCalcul.getSchema());

        if (paramDoc.getExamples() == null)
            paramDoc.setExamples(paramCalcul.getExamples());

        if (paramDoc.getExtensions() == null)
            paramDoc.setExtensions(paramCalcul.getExtensions());

        if (paramDoc.getStyle() == null)
            paramDoc.setStyle(paramCalcul.getStyle());

        if (paramDoc.getExplode() == null)
            paramDoc.setExplode(paramCalcul.getExplode());
    }

    Parameter buildParameterFromDoc(io.swagger.v3.oas.annotations.Parameter parameterDoc,
                                    Components components, JsonView jsonView) {
        Parameter parameter = new Parameter();
        if (StringUtils.isNotBlank(parameterDoc.description())) {
            parameter.setDescription(parameterDoc.description());
        }
        if (StringUtils.isNotBlank(parameterDoc.name())) {
            parameter.setName(parameterDoc.name());
        }
        if (StringUtils.isNotBlank(parameterDoc.in().toString())) {
            parameter.setIn(parameterDoc.in().toString());
        }
        if (StringUtils.isNotBlank(parameterDoc.example())) {
            try {
                parameter.setExample(Json.mapper().readTree(parameterDoc.example()));
            } catch (IOException e) {
                parameter.setExample(parameterDoc.example());
            }
        }
        if (parameterDoc.deprecated()) {
            parameter.setDeprecated(parameterDoc.deprecated());
        }
        if (parameterDoc.required()) {
            parameter.setRequired(parameterDoc.required());
        }
        if (parameterDoc.allowEmptyValue()) {
            parameter.setAllowEmptyValue(parameterDoc.allowEmptyValue());
        }
        if (parameterDoc.allowReserved()) {
            parameter.setAllowReserved(parameterDoc.allowReserved());
        }
        if (StringUtils.isNotBlank(parameterDoc.ref())) {
            parameter.$ref(parameterDoc.ref());
        } else {
            Schema schema = AnnotationsUtils.getSchemaFromAnnotation(parameterDoc.schema(), components, jsonView)
                    .orElse(null);
            if (schema == null) {
                if (parameterDoc.content().length > 0) {
                    if (AnnotationsUtils.hasSchemaAnnotation(parameterDoc.content()[0].schema())) {
                        schema = AnnotationsUtils.getSchemaFromAnnotation(parameterDoc.content()[0].schema(), components, jsonView).orElse(null);
                    } else if (AnnotationsUtils.hasArrayAnnotation(parameterDoc.content()[0].array())) {
                        schema = AnnotationsUtils.getArraySchema(parameterDoc.content()[0].array(), components, jsonView).orElse(null);
                    }
                } else {
                    schema = AnnotationsUtils.getArraySchema(parameterDoc.array(), components, jsonView).orElse(null);
                }
            }
            parameter.setSchema(schema);
        }

        setExamples(parameterDoc, parameter);
        setExtensions(parameterDoc, parameter);
        setParameterStyle(parameter, parameterDoc);
        setParameterExplode(parameter, parameterDoc);

        return parameter;
    }

    Schema calculateSchema(Components components, java.lang.reflect.Parameter parameter, String paramName,
                           RequestBodyInfo requestBodyInfo, JsonView jsonView) {
        Schema schemaN;
        Class<?> schemaImplementation = null;
        Type returnType = null;
        JavaType ct = null;

        if (parameter != null) {
            returnType = parameter.getParameterizedType();
            ct = constructType(parameter.getType());
            schemaImplementation = parameter.getType();
        }

        if (isFile(ct)) {
            schemaN = getFileSchema(requestBodyInfo);
            schemaN.addProperties(paramName, new FileSchema());
            return schemaN;
        }

        if (returnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) returnType;
            if (isFile(parameterizedType)) {
                return extractFileSchema(paramName, requestBodyInfo);
            }
            schemaN = calculateSchemaFromParameterizedType(components, returnType, jsonView);
        } else {
            schemaN = SpringDocAnnotationsUtils.resolveSchemaFromType(schemaImplementation, components, jsonView);
        }
        if (isRequestBodySchema(requestBodyInfo)) {
            requestBodyInfo.getMergedSchema().addProperties(paramName, schemaN);
            schemaN = requestBodyInfo.getMergedSchema();
        }

        return schemaN;
    }

    public LocalVariableTableParameterNameDiscoverer getLocalSpringDocParameterNameDiscoverer() {
        return localSpringDocParameterNameDiscoverer;
    }

    protected abstract Schema calculateSchemaFromParameterizedType(Components components, Type returnType, JsonView jsonView);

    private Schema extractFileSchema(String paramName, RequestBodyInfo requestBodyInfo) {
        Schema schemaN = getFileSchema(requestBodyInfo);
        ArraySchema schemafile = new ArraySchema();
        schemafile.items(new FileSchema());
        schemaN.addProperties(paramName, new ArraySchema().items(new FileSchema()));
        return schemaN;
    }

    private Schema getFileSchema(RequestBodyInfo requestBodyInfo) {
        Schema schemaN;
        if (isRequestBodySchema(requestBodyInfo))
            schemaN = requestBodyInfo.getMergedSchema();
        else
            schemaN = new ObjectSchema();
        return schemaN;
    }

    private boolean isRequestBodySchema(RequestBodyInfo requestBodyInfo) {
        return requestBodyInfo != null && requestBodyInfo.getMergedSchema() != null;
    }

    protected abstract boolean isFile(ParameterizedType parameterizedType);

    protected abstract boolean isFile(JavaType ct);

    <A extends Annotation> A getParameterAnnotation(HandlerMethod handlerMethod,
                                                    java.lang.reflect.Parameter parameter, int i, Class<A> annotationType) {
        A parameterDoc = AnnotationUtils.getAnnotation(parameter, annotationType);
        if (parameterDoc == null) {
            Set<Method> methods = MethodUtils.getOverrideHierarchy(handlerMethod.getMethod(),
                    ClassUtils.Interfaces.INCLUDE);
            for (Method methodOverriden : methods) {
                parameterDoc = AnnotationUtils.getAnnotation(methodOverriden.getParameters()[i], annotationType);
                if (parameterDoc != null)
                    break;
            }
        }
        return parameterDoc;
    }

    private void setExamples(io.swagger.v3.oas.annotations.Parameter parameterDoc, Parameter parameter) {
        Map<String, Example> exampleMap = new HashMap<>();
        if (parameterDoc.examples().length == 1 && StringUtils.isBlank(parameterDoc.examples()[0].name())) {
            Optional<Example> exampleOptional = AnnotationsUtils.getExample(parameterDoc.examples()[0]);
            exampleOptional.ifPresent(parameter::setExample);
        } else {
            for (ExampleObject exampleObject : parameterDoc.examples()) {
                AnnotationsUtils.getExample(exampleObject)
                        .ifPresent(example -> exampleMap.put(exampleObject.name(), example));
            }
        }
        if (exampleMap.size() > 0) {
            parameter.setExamples(exampleMap);
        }
    }

    private void setExtensions(io.swagger.v3.oas.annotations.Parameter parameterDoc, Parameter parameter) {
        if (parameterDoc.extensions().length > 0) {
            Map<String, Object> extensionMap = AnnotationsUtils.getExtensions(parameterDoc.extensions());
            extensionMap.forEach(parameter::addExtension);
        }
    }

    private void setParameterExplode(Parameter parameter, io.swagger.v3.oas.annotations.Parameter p) {
        if (isExplodable(p)) {
            if (Explode.TRUE.equals(p.explode())) {
                parameter.setExplode(Boolean.TRUE);
            } else if (Explode.FALSE.equals(p.explode())) {
                parameter.setExplode(Boolean.FALSE);
            }
        }
    }

    private void setParameterStyle(Parameter parameter, io.swagger.v3.oas.annotations.Parameter p) {
        if (StringUtils.isNotBlank(p.style().toString())) {
            parameter.setStyle(Parameter.StyleEnum.valueOf(p.style().toString().toUpperCase()));
        }
    }

    private boolean isExplodable(io.swagger.v3.oas.annotations.Parameter p) {
        io.swagger.v3.oas.annotations.media.Schema schema = p.schema();
        boolean explode = true;
        Class<?> implementation = schema.implementation();
        if (implementation == Void.class && !schema.type().equals("object") && !schema.type().equals("array")) {
            explode = false;
        }
        return explode;
    }

    private JavaType constructType(Type type) {
        return TypeFactory.defaultInstance().constructType(type);
    }

    public boolean isAnnotationToIgnore(java.lang.reflect.Parameter parameter){
        return  ignoredParameterAnnotations.isAnnotationToIgnore(parameter);
    }
}

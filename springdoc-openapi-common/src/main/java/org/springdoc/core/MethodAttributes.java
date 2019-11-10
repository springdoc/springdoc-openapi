package org.springdoc.core;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.core.util.ReflectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

public class MethodAttributes {

    private String[] classProduces;
    private String[] classConsumes;
    private String[] methodProduces = {};
    private String[] methodConsumes = {};
    private boolean methodOverloaded;
    private boolean withApiResponseDoc;
    private JsonView jsonViewAnnotation;
    private JsonView jsonViewAnnotationForRequestBody;

    public MethodAttributes() {
    }

    public MethodAttributes(String[] methodProducesNew) {
        this.methodProduces = methodProducesNew;
    }

    public String[] getClassProduces() {
        return classProduces;
    }

    public void setClassProduces(String[] classProduces) {
        this.classProduces = classProduces;
    }

    public String[] getClassConsumes() {
        return classConsumes;
    }

    public void setClassConsumes(String[] classConsumes) {
        this.classConsumes = classConsumes;
    }

    public String[] getMethodProduces() {
        return methodProduces;
    }


    public String[] getMethodConsumes() {
        return methodConsumes;
    }


    public void calculateConsumesProduces(Method method) {
        PostMapping reqPostMappringMethod = ReflectionUtils.getAnnotation(method, PostMapping.class);
        if (reqPostMappringMethod != null) {
            fillMethods(reqPostMappringMethod.produces(), reqPostMappringMethod.consumes());
            return;
        }

        GetMapping reqGetMappringMethod = ReflectionUtils.getAnnotation(method, GetMapping.class);
        if (reqGetMappringMethod != null) {
            fillMethods(reqGetMappringMethod.produces(), reqGetMappringMethod.consumes());
            return;
        }

        DeleteMapping reqDeleteMappringMethod = ReflectionUtils.getAnnotation(method, DeleteMapping.class);
        if (reqDeleteMappringMethod != null) {
            fillMethods(reqDeleteMappringMethod.produces(), reqDeleteMappringMethod.consumes());
            return;
        }

        PutMapping reqPutMappringMethod = ReflectionUtils.getAnnotation(method, PutMapping.class);
        if (reqPutMappringMethod != null) {
            fillMethods(reqPutMappringMethod.produces(), reqPutMappringMethod.consumes());
            return;
        }

        RequestMapping reqMappringMethod = ReflectionUtils.getAnnotation(method, RequestMapping.class);
        if (reqMappringMethod != null) {
            fillMethods(reqMappringMethod.produces(), reqMappringMethod.consumes());
        }

    }

    private void fillMethods(String[] produces, String[] consumes) {
        methodProduces = ArrayUtils.isNotEmpty(produces) ? produces : new String[]{MediaType.ALL_VALUE};
        methodConsumes = ArrayUtils.isNotEmpty(consumes) ? consumes : new String[]{MediaType.ALL_VALUE};
    }

    public String[] getAllConsumes() {
        return ArrayUtils.addAll(methodConsumes, classConsumes);
    }

    public String[] getAllProduces() {
        return ArrayUtils.addAll(methodProduces, classProduces);
    }

    public boolean isMethodOverloaded() {
        return methodOverloaded;
    }

    public void setMethodOverloaded(boolean overloaded) {
        methodOverloaded = overloaded;
    }

    public void setWithApiResponseDoc(boolean withApiDoc) {
        this.withApiResponseDoc = withApiDoc;
    }

    public boolean isNoApiResponseDoc() {
        return !withApiResponseDoc;
    }

    public JsonView getJsonViewAnnotation() {
        return jsonViewAnnotation;
    }

    public void setJsonViewAnnotation(JsonView jsonViewAnnotation) {
        this.jsonViewAnnotation = jsonViewAnnotation;
    }

    public JsonView getJsonViewAnnotationForRequestBody() {
        if (jsonViewAnnotationForRequestBody == null)
            return jsonViewAnnotation;
        return jsonViewAnnotationForRequestBody;
    }

    public void setJsonViewAnnotationForRequestBody(JsonView jsonViewAnnotationForRequestBody) {
        this.jsonViewAnnotationForRequestBody = jsonViewAnnotationForRequestBody;
    }

}

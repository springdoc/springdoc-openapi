package org.springdoc.core.model;

import java.lang.reflect.Method;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springdoc.core.models.MethodAttributes;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.BDDMockito.given;

public class MethodAttributesTest {

    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_XML = "application/xml";
    private static final String APPLICATION_YAML = "application/yaml";

    @Test
    void testMergeArrays() throws Exception {
        MethodAttributes methodAttributes = new MethodAttributes(APPLICATION_JSON, APPLICATION_XML, Locale.ENGLISH);

        String[] array1 = {APPLICATION_JSON, APPLICATION_XML};
        String[] array2 = {APPLICATION_XML, APPLICATION_YAML};

        String[] expected = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML};

        Method mergeArraysMethod = MethodAttributes.class.getDeclaredMethod("mergeArrays", String[].class, String[].class);
        mergeArraysMethod.setAccessible(true);
        String[] result = (String[]) mergeArraysMethod.invoke(methodAttributes, (Object) array1, (Object) array2);

        assertArrayEquals(expected, result);
    }

    @Test
    void testMergeArraysWithNullArray1() throws Exception {
        MethodAttributes methodAttributes = new MethodAttributes(APPLICATION_JSON, APPLICATION_XML, Locale.ENGLISH);

        String[] array1 = null;
        String[] array2 = {APPLICATION_XML, APPLICATION_YAML};

        String[] expected = {APPLICATION_XML, APPLICATION_YAML};

        Method mergeArraysMethod = MethodAttributes.class.getDeclaredMethod("mergeArrays", String[].class, String[].class);
        mergeArraysMethod.setAccessible(true);
        String[] result = (String[]) mergeArraysMethod.invoke(methodAttributes, (Object) array1, (Object) array2);

        assertArrayEquals(expected, result);
    }

    @Test
    void testDefaultProducesMediaType() {
        MethodAttributes methodAttributes = new MethodAttributes(APPLICATION_JSON, APPLICATION_XML, Locale.ENGLISH);

        Method method = this.getClass().getDeclaredMethods()[0];
        methodAttributes.calculateConsumesProduces(method);

        String[] expectedProduces = {APPLICATION_XML};
        String[] resultProduces = methodAttributes.getMethodProduces();

        assertArrayEquals(expectedProduces, resultProduces);
    }

    @Test
    void testDefaultConsumesMediaType() {
        MethodAttributes methodAttributes = new MethodAttributes(APPLICATION_JSON, APPLICATION_XML, Locale.ENGLISH);

        Method method = this.getClass().getDeclaredMethods()[0];
        methodAttributes.calculateConsumesProduces(method);

        String[] expectedConsumes = {APPLICATION_JSON};
        String[] resultConsumes = methodAttributes.getMethodConsumes();

        assertArrayEquals(expectedConsumes, resultConsumes);
    }

    @Test
    void methodConsumesOverridesClassConsumes() {
        MethodAttributes methodAttributes = new MethodAttributes(APPLICATION_JSON, APPLICATION_XML, Locale.ENGLISH);
        RequestMapping requestMapping = givenAnnotationHasMediaTypeAnnotations(
                new String[]{APPLICATION_JSON, APPLICATION_XML},
                new String[]{APPLICATION_JSON, APPLICATION_XML}
        );
        Method method = this.getClass().getDeclaredMethods()[0];
        try (MockedStatic<AnnotatedElementUtils> annotatedElementUtils = Mockito.mockStatic(AnnotatedElementUtils.class)) {
            annotatedElementUtils.when(() -> AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class))
                    .thenReturn(requestMapping);

            methodAttributes.setClassConsumes(new String[]{APPLICATION_YAML});
            methodAttributes.calculateConsumesProduces(method);

            String[] expectedConsumes = {APPLICATION_JSON, APPLICATION_XML};
            String[] resultConsumes = methodAttributes.getMethodConsumes();

            assertArrayEquals(expectedConsumes, resultConsumes);
        }
    }

    @Test
    void methodProducesOverridesClassProduces() {
        MethodAttributes methodAttributes = new MethodAttributes(APPLICATION_JSON, APPLICATION_XML, Locale.ENGLISH);
        RequestMapping requestMapping = givenAnnotationHasMediaTypeAnnotations(
                new String[]{APPLICATION_JSON, APPLICATION_XML},
                new String[]{APPLICATION_JSON, APPLICATION_XML}
        );
        Method method = this.getClass().getDeclaredMethods()[0];
        try (MockedStatic<AnnotatedElementUtils> annotatedElementUtils = Mockito.mockStatic(AnnotatedElementUtils.class)) {
            annotatedElementUtils.when(() -> AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class))
                    .thenReturn(requestMapping);

            methodAttributes.setClassProduces(new String[]{APPLICATION_YAML});
            methodAttributes.calculateConsumesProduces(method);

            String[] expectedProduces = {APPLICATION_JSON, APPLICATION_XML};
            String[] resultProduces = methodAttributes.getMethodProduces();

            assertArrayEquals(expectedProduces, resultProduces);
        }
    }

    @Test
    void methodConsumesIsSetToClassConsumesIfNoMethodConsumesIsDefined() {
        MethodAttributes methodAttributes = new MethodAttributes(APPLICATION_JSON, APPLICATION_XML, Locale.ENGLISH);
        RequestMapping requestMapping = givenAnnotationHasMediaTypeAnnotations(
                new String[]{APPLICATION_JSON, APPLICATION_XML},
                new String[]{}
        );
        Method method = this.getClass().getDeclaredMethods()[0];
        try (MockedStatic<AnnotatedElementUtils> annotatedElementUtils = Mockito.mockStatic(AnnotatedElementUtils.class)) {
            annotatedElementUtils.when(() -> AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class))
                    .thenReturn(requestMapping);

            String[] classConsumes = new String[]{APPLICATION_YAML};
            methodAttributes.setClassConsumes(classConsumes);
            methodAttributes.calculateConsumesProduces(method);

            String[] resultConsumes = methodAttributes.getMethodConsumes();

            assertArrayEquals(classConsumes, resultConsumes);
        }
    }

    @Test
    void methodProducesIsSetToClassProducesIfNoMethodProducesIsDefined() {
        MethodAttributes methodAttributes = new MethodAttributes(APPLICATION_JSON, APPLICATION_XML, Locale.ENGLISH);
        RequestMapping requestMapping = givenAnnotationHasMediaTypeAnnotations(
                new String[]{},
                new String[]{APPLICATION_JSON, APPLICATION_XML}
        );
        Method method = this.getClass().getDeclaredMethods()[0];
        try (MockedStatic<AnnotatedElementUtils> annotatedElementUtils = Mockito.mockStatic(AnnotatedElementUtils.class)) {
            annotatedElementUtils.when(() -> AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class))
                    .thenReturn(requestMapping);

            String[] classProduces = new String[]{APPLICATION_YAML};
            methodAttributes.setClassProduces(classProduces);
            methodAttributes.calculateConsumesProduces(method);

            String[] resultProduces = methodAttributes.getMethodProduces();

            assertArrayEquals(classProduces, resultProduces);
        }
    }

    @Test
    void methodConsumesIsSetToClassConsumesIfNoMethodConsumesIsDefinedAndClassConsumesNotSet() {
        MethodAttributes methodAttributes = new MethodAttributes(APPLICATION_JSON, APPLICATION_XML, Locale.ENGLISH);
        String[] classConsumes = new String[]{APPLICATION_YAML};
        RequestMapping requestMapping = givenAnnotationHasMediaTypeAnnotations(
                new String[]{APPLICATION_JSON, APPLICATION_XML},
                new String[]{}
        );
        RequestMapping classMapping = givenAnnotationHasMediaTypeAnnotations(
                new String[]{},
                classConsumes
        );
        Method method = this.getClass().getDeclaredMethods()[0];
        try (MockedStatic<AnnotatedElementUtils> annotatedElementUtils = Mockito.mockStatic(AnnotatedElementUtils.class)) {
            annotatedElementUtils.when(() -> AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class))
                    .thenReturn(requestMapping);
            annotatedElementUtils.when(() -> AnnotatedElementUtils.findMergedAnnotation(method.getDeclaringClass(), RequestMapping.class))
                    .thenReturn(classMapping);

            methodAttributes.calculateConsumesProduces(method);

            String[] resultConsumes = methodAttributes.getMethodConsumes();

            assertArrayEquals(classConsumes, resultConsumes);
        }
    }

    @Test
    void methodProducesIsSetToClassProducesIfNoMethodProducesIsDefinedAndClassProducesNotSet() {
        MethodAttributes methodAttributes = new MethodAttributes(APPLICATION_JSON, APPLICATION_XML, Locale.ENGLISH);
        String[] classProduces = new String[]{APPLICATION_YAML};
        RequestMapping requestMapping = givenAnnotationHasMediaTypeAnnotations(
                new String[]{},
                new String[]{APPLICATION_JSON, APPLICATION_XML}
        );
        RequestMapping classMapping = givenAnnotationHasMediaTypeAnnotations(
                classProduces,
                new String[]{}
        );
        Method method = this.getClass().getDeclaredMethods()[0];
        try (MockedStatic<AnnotatedElementUtils> annotatedElementUtils = Mockito.mockStatic(AnnotatedElementUtils.class)) {
            annotatedElementUtils.when(() -> AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class))
                    .thenReturn(requestMapping);
            annotatedElementUtils.when(() -> AnnotatedElementUtils.findMergedAnnotation(method.getDeclaringClass(), RequestMapping.class))
                    .thenReturn(classMapping);

            methodAttributes.calculateConsumesProduces(method);

            String[] resultProduces = methodAttributes.getMethodProduces();

            assertArrayEquals(classProduces, resultProduces);
        }
    }

    private RequestMapping givenAnnotationHasMediaTypeAnnotations(String[] produces, String[] consumes) {
        RequestMapping requestMapping = Mockito.mock(RequestMapping.class);
        given(requestMapping.produces()).willReturn(produces);
        given(requestMapping.consumes()).willReturn(consumes);
        return requestMapping;
    }
}
package org.springdoc.core.model;

import org.junit.jupiter.api.Test;
import org.springdoc.core.models.MethodAttributes;

import java.lang.reflect.Method;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class MethodAttributesTest {

    @Test
    public void testMergeArrays() {
        MethodAttributes methodAttributes = new MethodAttributes("application/json", "application/xml", Locale.ENGLISH);

        String[] array1 = {"application/json", "application/xml"};
        String[] array2 = {"application/xml", "application/yaml"};

        String[] expected = {"application/json", "application/xml", "application/yaml"};
        String[] result = methodAttributes.mergeArrays(array1, array2);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testMergeArraysWithNullArray1() {
        MethodAttributes methodAttributes = new MethodAttributes("application/json", "application/xml", Locale.ENGLISH);

        String[] array1 = null;
        String[] array2 = {"application/xml", "application/yaml"};

        String[] expected = {"application/xml", "application/yaml"};
        String[] result = methodAttributes.mergeArrays(array1, array2);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testDefaultProducesMediaType() {
        MethodAttributes methodAttributes = new MethodAttributes("application/json", "application/xml", Locale.ENGLISH);

        Method method = this.getClass().getDeclaredMethods()[0];
        methodAttributes.calculateConsumesProduces(method);

        String[] expectedProduces = {"application/xml"};
        String[] resultProduces = methodAttributes.getMethodProduces();

        assertArrayEquals(expectedProduces, resultProduces);
    }

    @Test
    public void testDefaultConsumesMediaType() {
        MethodAttributes methodAttributes = new MethodAttributes("application/json", "application/xml", Locale.ENGLISH);

        Method method = this.getClass().getDeclaredMethods()[0];
        methodAttributes.calculateConsumesProduces(method);

        String[] expectedConsumes = {"application/json"};
        String[] resultConsumes = methodAttributes.getMethodConsumes();

        assertArrayEquals(expectedConsumes, resultConsumes);
    }
}

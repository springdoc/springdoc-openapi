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

package org.springdoc.core.customizers;

import io.swagger.v3.oas.models.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.annotations.EnumDescription;
import org.springdoc.core.service.AbstractRequestService;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Field;

/**
 * The type Enum description operation customizer.
 * Automatically adds enum constants and descriptions to operation.
 * when fields are annotated with {@link EnumDescription}.
 *
 * @author TAEWOOKK
 */
public class EnumDescriptionOperationCustomizer implements GlobalOperationCustomizer{

    /**
     * The constant DEFAULT_FIELD_NAME.
     */
    private static final String DEFAULT_FIELD_NAME = "description";

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        if (operation == null || handlerMethod == null) {
            return operation;
        }

        for (MethodParameter methodParameter : handlerMethod.getMethodParameters()) {
            Class<?> dtoClass = methodParameter.getParameterType();

            if (!isValidDtoClass(dtoClass)) {
                continue;
            }

            String enumDescriptions = extractEnumDescriptionsFromDto(dtoClass);
            if (StringUtils.isNotBlank(enumDescriptions)) {
                String existingDescription = operation.getDescription();
                String newDescription = StringUtils.isNotBlank(existingDescription)
                        ? existingDescription + "\n\n" + enumDescriptions
                        : enumDescriptions;
                operation.setDescription(newDescription);
            }
        }

        return operation;
    }

    /**
     * Is valid dto class boolean.
     *
     * @param clazz the clazz
     * @return the boolean
     */
    private boolean isValidDtoClass(Class<?> clazz) {
        return !clazz.isPrimitive()
                && !clazz.isEnum()
                && !clazz.isArray()
                && !clazz.isInterface()
                && !clazz.getName().startsWith("java.")
                && !AbstractRequestService.isRequestTypeToIgnore(clazz);
    }

    /**
     * Extract enum descriptions from dto string.
     *
     * @param dtoClass the dto class
     * @return the string
     */
    private String extractEnumDescriptionsFromDto(Class<?> dtoClass) {
        if (dtoClass == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        Class<?> currentClass = dtoClass;

        while (currentClass != null && !currentClass.getName().startsWith("java.")) {
            for (Field field : currentClass.getDeclaredFields()) {
                EnumDescription annotation = field.getAnnotation(EnumDescription.class);

                if (annotation != null && field.getType().isEnum()) {
                    String enumDescriptionText = extractEnumDescription(
                            field.getType(),
                            annotation.fieldName(),
                            field.getName()
                    );

                    if (StringUtils.isNotBlank(enumDescriptionText)) {
                        if (!result.isEmpty()) {
                            result.append("\n\n");
                        }
                        result.append(enumDescriptionText);
                    }
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        return !result.isEmpty() ? result.toString() : null;
    }

    /**
     * Extract enum description string.
     *
     * @param enumClass        the enum class
     * @param fieldName        the field name
     * @param fieldDisplayName the field display name
     * @return the string
     */
    private String extractEnumDescription(Class<?> enumClass,
                                          String fieldName,
                                          String fieldDisplayName) {
        Object[] enumConstants = enumClass.getEnumConstants();
        if (enumConstants == null || enumConstants.length == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("**").append(fieldDisplayName).append("**\n");

        for (Object enumConstant : enumConstants) {
            String enumKey = ((Enum<?>) enumConstant).name();
            String description = getEnumDescription(enumConstant, fieldName);

            sb.append("- `").append(enumKey).append("`");
            if (StringUtils.isNotBlank(description)) {
                sb.append(": ").append(description);
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Gets enum description string.
     *
     * @param enumConstant the enum constant
     * @param fieldName    the field name
     * @return the string
     */
    private String getEnumDescription(Object enumConstant, String fieldName) {
        if (enumConstant == null) {
            return "";
        }

        String targetFieldName = StringUtils.isNotBlank(fieldName) ? fieldName : DEFAULT_FIELD_NAME;

        try {
            Field field = enumConstant.getClass().getDeclaredField(targetFieldName);
            field.setAccessible(true);
            Object value = field.get(enumConstant);
            return value != null ? value.toString() : "";
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return "";
        }
    }
}

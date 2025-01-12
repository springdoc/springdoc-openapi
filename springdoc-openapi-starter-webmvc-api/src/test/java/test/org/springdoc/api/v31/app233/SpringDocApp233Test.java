/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2024 the original author or authors.
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

package test.org.springdoc.api.v31.app233;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author bnasslahsen, michael.clarke
 */
class SpringDocApp233Test extends AbstractSpringDocTest {

    @CsvSource({"requiredNotNullParameterObject.requiredNotNullField, true",
            "requiredNotNullParameterObject.requiredNoValidationField, true",
            "requiredNotNullParameterObject.notRequiredNotNullField, false",
            "requiredNotNullParameterObject.notRequiredNoValidationField, false",
            "requiredNotNullParameterObject.noSchemaNotNullField, true",
            "requiredNotNullParameterObject.noSchemaNoValidationField, false",
            "requiredNoValidationParameterObject.requiredNotNullField, true",
            "requiredNoValidationParameterObject.requiredNoValidationField, true",
            "requiredNoValidationParameterObject.notRequiredNotNullField, false",
            "requiredNoValidationParameterObject.notRequiredNoValidationField, false",
            "requiredNoValidationParameterObject.noSchemaNotNullField, true",
            "requiredNoValidationParameterObject.noSchemaNoValidationField, false",
            "notRequiredNotNullParameterObject.requiredNotNullField, true",
            "notRequiredNotNullParameterObject.requiredNoValidationField, true",
            "notRequiredNotNullParameterObject.notRequiredNotNullField, false",
            "notRequiredNotNullParameterObject.notRequiredNoValidationField, false",
            "notRequiredNotNullParameterObject.noSchemaNotNullField, false",
            "notRequiredNotNullParameterObject.noSchemaNoValidationField, false",
            "notRequiredNoValidationParameterObject.requiredNotNullField, true",
            "notRequiredNoValidationParameterObject.requiredNoValidationField, true",
            "notRequiredNoValidationParameterObject.notRequiredNotNullField, false",
            "notRequiredNoValidationParameterObject.notRequiredNoValidationField, false",
            "notRequiredNoValidationParameterObject.noSchemaNotNullField, false",
            "notRequiredNoValidationParameterObject.noSchemaNoValidationField, false",
            "noSchemaNotNullParameterObject.requiredNotNullField, true",
            "noSchemaNotNullParameterObject.requiredNoValidationField, true",
            "noSchemaNotNullParameterObject.notRequiredNotNullField, false",
            "noSchemaNotNullParameterObject.notRequiredNoValidationField, false",
            "noSchemaNotNullParameterObject.noSchemaNotNullField, true",
            "noSchemaNotNullParameterObject.noSchemaNoValidationField, false",
            "noSchemaNoValidationParameterObject.requiredNotNullField, true",
            "noSchemaNoValidationParameterObject.requiredNoValidationField, true",
            "noSchemaNoValidationParameterObject.notRequiredNotNullField, false",
            "noSchemaNoValidationParameterObject.notRequiredNoValidationField, false",
            "noSchemaNoValidationParameterObject.noSchemaNotNullField, false",
            "noSchemaNoValidationParameterObject.noSchemaNoValidationField, false"})
    @ParameterizedTest
    void shouldHaveCorrectRequireStatus(String field, String required) throws Exception {
        MvcResult mockMvcResult = mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL)).andExpect(status().isOk()).andReturn();
        String result = mockMvcResult.getResponse().getContentAsString();

        String requiredMode = ((JSONArray) JsonPath.parse(result).read("$.paths.['/optional-parent'].get.parameters[?(@.name == '" + field + "')].required")).get(0).toString();
        assertThat(requiredMode).isEqualTo(required);
    }

    @Test
    void shouldMatchSwaggerFieldRequirementsMatchJavaValidation() throws Exception {
        MvcResult mockMvcResult = mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL)).andExpect(status().isOk()).andReturn();
        String result = mockMvcResult.getResponse().getContentAsString();

        JSONArray allFieldsJsonArray = JsonPath.parse(result).read("$.paths.['/optional-parent'].get.parameters[*].name");
        List<String> allFields = allFieldsJsonArray.stream().map(Object::toString).toList();

        // check we get no validation failures when all mandatory fields are present
        verifySwaggerFieldRequirementsMatchJavaValidation(allFields, List.of());

        JSONArray mandatoryFieldsJsonArray = JsonPath.parse(result).read("$.paths.['/optional-parent'].get.parameters[?(@.required == true)].name");
        List<String> mandatoryFields = mandatoryFieldsJsonArray.stream().map(Object::toString).toList();

        // check validation failures when each individual mandatory field is missing
        for (String mandatoryField : mandatoryFields) {
            List<String> filteredFields = allFields.stream()
                    .filter(field -> !field.equals(mandatoryField))
                    .toList();

            List<String> expectedErrors = Stream.of(mandatoryField)
                    // Fields using Swagger annotations to drive required status but not using Java validation to enforce it so don't cause validation errors
                    .filter(field -> !field.endsWith("requiredNullableField"))
                    .filter(field -> !field.endsWith("requiredNoValidationField"))
                    // the error is returned prefixed with the query parameter name, so add it to the expected error message
                    .map(field -> "multiFieldParameterObject." + field)
                    .toList();
            verifySwaggerFieldRequirementsMatchJavaValidation(filteredFields, expectedErrors);
        }


        JSONArray nonMandatoryFieldsJsonArray = JsonPath.parse(result).read("$.paths.['/optional-parent'].get.parameters[?(@.required == false)].name");
        List<String> nonMandatoryFields = nonMandatoryFieldsJsonArray.stream().map(Object::toString).toList();

        // check validation failures for any individual non-mandatory fields being missed
        for (String nonMandatoryField : nonMandatoryFields) {
            List<String> filteredFields = allFields.stream()
                    .filter(field -> !field.equals(nonMandatoryField))
                    .toList();

            List<String> expectedErrors = Stream.of(nonMandatoryField)
                    // Fields that are mandatory but either have nullable parent fields so are excluded in swagger or are marked as not required so do cause validation errors
                    .filter(field -> field.endsWith("NotNullField"))
                    // the error is returned prefixed with the query parameter name, so add it to the expected error message
                    .map(field -> "multiFieldParameterObject." + field)
                    .toList();
            verifySwaggerFieldRequirementsMatchJavaValidation(filteredFields, expectedErrors);
        }
    }

    private void verifySwaggerFieldRequirementsMatchJavaValidation(Collection<String> requestFields, Collection<String> expectedErrorFields) throws Exception {
        MockHttpServletRequestBuilder request = get("/optional-parent");
        for (String mandatoryField : requestFields) {
            request.queryParam(mandatoryField, mandatoryField + ".value");
        }

        mockMvc.perform(request)
                .andExpect(result -> {

                    Set<String> errorFields = Optional.ofNullable(result.getResolvedException())
                            .map(MethodArgumentNotValidException.class::cast)
                            .map(MethodArgumentNotValidException::getBindingResult)
                            .map(BindingResult::getFieldErrors)
                            .stream()
                            .flatMap(Collection::stream)
                            .map(field -> field.getObjectName() + "." + field.getField())
                            .collect(Collectors.toSet());


                    assertThat(errorFields).containsExactlyElementsOf(expectedErrorFields);

                    assertThat(result.getResponse().getStatus()).isEqualTo(expectedErrorFields.isEmpty() ? 200 : 400);
                });
    }


    @SpringBootApplication
    static class SpringDocTestApp {

    }

}
/*
 *
 *  * Copyright 2019-2026 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package test.org.springdoc.api.v31.issue3136;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifies that a Spring Data REST response schema keeps composite-key values
 * while representing associations as HAL links instead of recursively
 * expanding related entities.
 *
 * @author hej090224
 */
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class SpringDocIssue3136Test {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void embeddedIdRelationsUseCompactResponseSchema() throws Exception {
		mockMvc.perform(get("/v3/api-docs")).andExpect(status().isOk())
				.andExpect(jsonPath("$.components.schemas.EntityModelGoal.properties.impactsByInitiatives.items.$ref",
						equalTo("#/components/schemas/InitiativeImpactOnGoalResponse")))
				.andExpect(jsonPath("$.components.schemas.GoalInitiativeImpactId.properties.goalId.type", equalTo("integer")))
				.andExpect(jsonPath("$.components.schemas.GoalInitiativeImpactId.properties.initiativeId.type", equalTo("integer")))
				.andExpect(jsonPath("$.components.schemas.InitiativeImpactOnGoalResponse.properties.impactLevel").exists())
				.andExpect(jsonPath("$.components.schemas.InitiativeImpactOnGoalResponse.properties.id").doesNotExist())
				.andExpect(jsonPath("$.components.schemas.InitiativeImpactOnGoalResponse.properties.goal").doesNotExist())
				.andExpect(jsonPath("$.components.schemas.InitiativeImpactOnGoalResponse.properties.initiative").doesNotExist())
				.andExpect(jsonPath("$.components.schemas.CollectionModelEntityModelGoal.properties._embedded.properties.goals.items").exists());
	}

	@SpringBootApplication
	static class SpringDocTestApp {
	}
}

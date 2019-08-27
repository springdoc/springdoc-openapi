package test.org.springdoc.api.app4;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springdoc.core.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SpringDocApp4Test {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testApp4() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL)).andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.0.1")))
				.andExpect(jsonPath("$.info.title", is("OpenAPI definition")))
				.andExpect(jsonPath("$.info.version", is("v0")))
				.andExpect(jsonPath("$.paths./values/data.post.operationId", is("list")))
				.andExpect(jsonPath("$.paths./values/data.post.requestBody.content.['*/*'].schema.['$ref']",
						is("#/components/schemas/TrackerData")))
				.andExpect(jsonPath("$.paths./values/data.post.responses.200.content.['*/*'].schema.['$ref']",
						is("#/components/schemas/TrackerData")))
				.andExpect(
						jsonPath("$.paths./values/data.post.responses.200.description", is("default response")))
				.andExpect(jsonPath("$.components.schemas.TrackerData.required")
						.value(Matchers.containsInAnyOrder("timestamp", "trackerId", "value")))
				.andExpect(jsonPath("$.components.schemas.TrackerData.type", is("object")))
				.andExpect(jsonPath("$.components.schemas.TrackerData.properties.trackerId.type", is("string")))
				.andExpect(jsonPath("$.components.schemas.TrackerData.properties.timestamp.type", is("string")))
				.andExpect(jsonPath("$.components.schemas.TrackerData.properties.value.type", is("number")))
				.andExpect(jsonPath("$.components.schemas.TrackerData.properties.timestamp.format", is("date-time")))
				.andExpect(jsonPath("$.components.schemas.TrackerData.properties.value.format", is("double")))
				.andExpect(
						jsonPath("$.components.schemas.TrackerData.properties.trackerId.example", is("the-tracker-id")))
				.andExpect(jsonPath("$.components.schemas.TrackerData.properties.timestamp.example",
						is("2018-01-01T00:00:00Z")))
				.andExpect(jsonPath("$.components.schemas.TrackerData.properties.value.example", is(19.0)));
	}

}

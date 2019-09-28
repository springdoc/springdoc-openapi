package test.org.springdoc.ui.app1;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import test.org.springdoc.ui.AbstractSpringDocTest;

public class SpringDocApp1Test extends AbstractSpringDocTest {

	@Test
	public void shouldDisplaySwaggerUiPage() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk()).andReturn();
		String contentAsString = mvcResult.getResponse().getContentAsString();
		assertTrue(contentAsString.contains("Swagger UI"));
	}
}
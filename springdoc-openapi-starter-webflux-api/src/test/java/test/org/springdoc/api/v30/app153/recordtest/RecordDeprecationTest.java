package test.org.springdoc.api.v30.app153.recordtest;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import test.org.springdoc.api.v30.AbstractSpringDocTest;
import static org.assertj.core.api.Assertions.assertThat;

public class RecordDeprecationTest extends AbstractSpringDocTest {

    @SpringBootApplication
    @ComponentScan(basePackages = { "org.springdoc", "test.org.springdoc.api.v30.app153.recordtest" })
    static class TestApp {}

    @Test
    void testRecordFieldDeprecation() throws Exception {
        String openApi = getContent();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(openApi);

        // Find the schema for DTO1 and DTO2
        JsonNode schemas = root.path("components").path("schemas");
        JsonNode dto1 = schemas.path("DTO1");
        JsonNode dto2 = schemas.path("DTO2");

        assertThat(dto1.path("properties").path("field").path("deprecated").asBoolean()).isTrue();
        assertThat(dto2.path("properties").path("field").has("deprecated")).isFalse();
    }
}

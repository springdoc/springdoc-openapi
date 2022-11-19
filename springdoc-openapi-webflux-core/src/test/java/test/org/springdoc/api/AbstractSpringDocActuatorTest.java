package test.org.springdoc.api;

import javax.annotation.PostConstruct;

import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

@TestPropertySource(properties = { "management.endpoints.enabled-by-default=true" })
public abstract class AbstractSpringDocActuatorTest extends AbstractCommonTest {

	protected WebClient webClient;

	@LocalManagementPort
	private int managementPort;

	@PostConstruct
	void init() {
		webClient = WebClient.builder().baseUrl("http://localhost:" + this.managementPort)
				.build();
	}
}

package test.org.springdoc.ui;

import javax.annotation.PostConstruct;

import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;
import org.springframework.web.reactive.function.client.WebClient;

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

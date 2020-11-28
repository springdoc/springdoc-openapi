package test.org.springdoc.ui;

import javax.annotation.PostConstruct;

import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class AbstractSpringDocActuatorTest extends  AbstractCommonTest{

	@LocalManagementPort
	private int managementPort;

	protected WebClient webClient;

	@PostConstruct
	void init(){
		webClient =	WebClient.builder().baseUrl("http://localhost:" + this.managementPort)
				.build();
	}
}

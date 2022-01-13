package test.org.springdoc.api.app1;

import org.junit.jupiter.api.Test;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestApplicationTests  {

	@SpringBootConfiguration
	static class SpringDocTestApp {}

	@Test
	void contextLoads() {}

}

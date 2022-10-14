package test.org.springdoc.api.app1;

import org.junit.jupiter.api.Test;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "springdoc.enable-native-support=true")
class TestApplicationTests  {

	@SpringBootConfiguration
	static class SpringDocTestApp {}

	@Test
	void contextLoads() {}

}

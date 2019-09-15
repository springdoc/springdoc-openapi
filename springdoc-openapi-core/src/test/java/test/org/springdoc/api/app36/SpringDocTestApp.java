package test.org.springdoc.api.app36;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:/properties/36.properties")
public class SpringDocTestApp {

	public static void main(String[] args) {
		SpringApplication.run(SpringDocTestApp.class, args);
	}
}

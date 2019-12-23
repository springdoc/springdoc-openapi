package test.org.springdoc.api.app69.customizer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CustomizedParameter {
	String addition() default "customized parameter!";
}

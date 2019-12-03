package test.org.springdoc.api.app60;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Parameters({
        @Parameter(name = "page", description = "desc page from Annotated interface"),
        @Parameter(name = "size", description = "desc page from Annotated interface")
})
public @interface QueryPaging {
}
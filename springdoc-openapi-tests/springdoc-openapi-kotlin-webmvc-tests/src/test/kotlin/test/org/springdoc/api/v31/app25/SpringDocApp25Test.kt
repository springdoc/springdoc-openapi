package test.org.springdoc.api.v31.app25

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import test.org.springdoc.api.v31.AbstractKotlinSpringDocMVCTest

/**
 * Regression test for the swagger-core `@JsonUnwrapped` null-property-key issue. When an
 * unwrapped member resolves to a `$ref`, swagger-core adds a `null`-keyed entry to the
 * enclosing schema properties; before the fix `/v3/api-docs` failed to serialize with
 * "Null key for a Map not allowed in JSON". Reproduces the same swagger path exercised by
 * Spring HATEOAS `EntityModel.getContent()` (which is `@JsonUnwrapped`) when HAL is disabled.
 */
class SpringDocApp25Test : AbstractKotlinSpringDocMVCTest() {
    @SpringBootApplication
    @ComponentScan(basePackages = ["org.springdoc", "test.org.springdoc.api.v31.app25"])
    class DemoApplication
}

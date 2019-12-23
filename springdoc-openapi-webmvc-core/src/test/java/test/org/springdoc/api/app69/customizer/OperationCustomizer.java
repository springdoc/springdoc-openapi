package test.org.springdoc.api.app69.customizer;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.oas.models.Operation;

@Component
public class OperationCustomizer implements org.springdoc.core.customizer.OperationCustomizer {
	@Override
	public Operation customize(Operation operation, HandlerMethod handlerMethod) {
		CustomizedOperation annotation = handlerMethod.getMethodAnnotation(CustomizedOperation.class);
		if(annotation != null){
			operation.description(operation.getDescription() + ", " + annotation.addition());
		}
		return operation;
	}
}

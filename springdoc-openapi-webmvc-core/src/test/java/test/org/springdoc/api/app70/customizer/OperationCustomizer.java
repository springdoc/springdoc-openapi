package test.org.springdoc.api.app70.customizer;

import io.swagger.v3.oas.models.Operation;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

@Component
public class OperationCustomizer implements org.springdoc.core.customizers.OperationCustomizer {
	@Override
	public Operation customize(Operation operation, HandlerMethod handlerMethod) {
		CustomizedOperation annotation = handlerMethod.getMethodAnnotation(CustomizedOperation.class);
		if(annotation != null){
			operation.description(operation.getDescription() + ", " + annotation.addition());
		}
		return operation;
	}
}

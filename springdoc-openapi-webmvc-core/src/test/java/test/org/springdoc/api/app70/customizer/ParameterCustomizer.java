package test.org.springdoc.api.app70.customizer;

import io.swagger.v3.oas.models.parameters.Parameter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

@Component
public class ParameterCustomizer implements org.springdoc.core.customizers.ParameterCustomizer {
    @Override
    public Parameter customize(Parameter parameterModel, java.lang.reflect.Parameter parameter, HandlerMethod handlerMethod) {
        CustomizedParameter annotation = parameter.getAnnotation(CustomizedParameter.class);
        if (annotation != null) {
            parameterModel.description(parameterModel.getDescription() + ", " + annotation.addition());
        }
        return parameterModel;
    }
}

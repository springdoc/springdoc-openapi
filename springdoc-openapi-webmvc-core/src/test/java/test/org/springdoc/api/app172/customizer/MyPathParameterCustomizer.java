package test.org.springdoc.api.app172.customizer;

import org.springdoc.core.customizers.ParameterCustomizer;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;

import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.PathParameter;
import test.org.springdoc.api.app172.annotation.MyIdPathVariable;

@Component
public class MyPathParameterCustomizer implements ParameterCustomizer {
    @Override
    public Parameter customize(Parameter parameterModel, MethodParameter methodParameter) {
        if (methodParameter.hasParameterAnnotation(MyIdPathVariable.class)) {
            Parameter alternativeParameter = new PathParameter();
            alternativeParameter.setName("objId");
            alternativeParameter.setSchema(new StringSchema());
            return alternativeParameter;
        }
        return parameterModel;
    }
}

package org.springdoc.core;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.models.Operation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class RequestBuilder extends AbstractRequestBuilder {

    public RequestBuilder(AbstractParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
                          OperationBuilder operationBuilder) {
        super(parameterBuilder, requestBodyBuilder, operationBuilder);
    }

    @Override
    protected boolean isParamTypeToIgnore(Class<?> paramType) {
        return WebRequest.class.equals(paramType) || NativeWebRequest.class.equals(paramType)
                || javax.servlet.ServletRequest.class.equals(paramType)
                || javax.servlet.ServletResponse.class.equals(paramType)
                || javax.servlet.http.HttpServletRequest.class.equals(paramType)
                || javax.servlet.http.HttpServletResponse.class.equals(paramType)
                || javax.servlet.http.HttpSession.class.equals(paramType)
                || java.security.Principal.class.equals(paramType) || HttpMethod.class.equals(paramType)
                || java.util.Locale.class.equals(paramType) || java.util.TimeZone.class.equals(paramType)
                || java.time.ZoneId.class.equals(paramType) || java.io.InputStream.class.equals(paramType)
                || java.io.Reader.class.equals(paramType) || java.io.OutputStream.class.equals(paramType)
                || java.io.Writer.class.equals(paramType) || java.util.Map.class.equals(paramType)
                || org.springframework.ui.Model.class.equals(paramType)
                || org.springframework.ui.ModelMap.class.equals(paramType) || RedirectAttributes.class.equals(paramType)
                || Errors.class.equals(paramType) || BindingResult.class.equals(paramType)
                || SessionStatus.class.equals(paramType) || UriComponentsBuilder.class.equals(paramType)
                || (AnnotationUtils.findAnnotation(paramType, Hidden.class) != null);
    }

    @Override
    protected Operation customiseOperation(Operation operation, HandlerMethod handlerMethod) {
        return operation;
    }
}

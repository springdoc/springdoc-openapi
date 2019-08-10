package org.springdoc.core;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class RequestBuilder extends AbstractRequestBuilder {

	private RequestBuilder(ParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder) {
		super(parameterBuilder, requestBodyBuilder);
	}

	boolean isParamTypeToIgnore(Class<?> paramType) {
		return WebRequest.class.equals(paramType) || NativeWebRequest.class.equals(paramType)
				|| javax.servlet.ServletRequest.class.equals(paramType)
				|| javax.servlet.ServletResponse.class.equals(paramType)
				|| javax.servlet.http.HttpSession.class.equals(paramType)
				|| java.security.Principal.class.equals(paramType) || HttpMethod.class.equals(paramType)
				|| java.util.Locale.class.equals(paramType) || java.util.TimeZone.class.equals(paramType)
				|| java.time.ZoneId.class.equals(paramType) || java.io.InputStream.class.equals(paramType)
				|| java.io.Reader.class.equals(paramType) || java.io.OutputStream.class.equals(paramType)
				|| java.io.Writer.class.equals(paramType) || java.util.Map.class.equals(paramType)
				|| org.springframework.ui.Model.class.equals(paramType)
				|| org.springframework.ui.ModelMap.class.equals(paramType) || RedirectAttributes.class.equals(paramType)
				|| Errors.class.equals(paramType) || BindingResult.class.equals(paramType)
				|| SessionStatus.class.equals(paramType) || UriComponentsBuilder.class.equals(paramType);
	}
}

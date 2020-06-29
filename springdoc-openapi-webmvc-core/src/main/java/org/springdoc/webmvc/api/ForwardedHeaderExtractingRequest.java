package org.springdoc.webmvc.api;

import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UrlPathHelper;

class ForwardedHeaderExtractingRequest {

	@Nullable
	private final String scheme;

	private final boolean secure;

	@Nullable
	private final String host;

	private final int port;

	private final ForwardedPrefixExtractor forwardedPrefixExtractor;


	ForwardedHeaderExtractingRequest(HttpServletRequest request, UrlPathHelper pathHelper) {
		HttpRequest httpRequest = new ServletServerHttpRequest(request);
		UriComponents uriComponents = UriComponentsBuilder.fromHttpRequest(httpRequest).build();
		int port = uriComponents.getPort();

		this.scheme = uriComponents.getScheme();
		this.secure = "https".equals(this.scheme);
		this.host = uriComponents.getHost();
		this.port = (port == -1 ? (this.secure ? 443 : 80) : port);

		String baseUrl = this.scheme + "://" + this.host + (port == -1 ? "" : ":" + port);
		Supplier<HttpServletRequest> delegateRequest = () -> request;
		this.forwardedPrefixExtractor = new ForwardedPrefixExtractor(delegateRequest, pathHelper, baseUrl);
	}


	@Nullable
	public String getScheme() {
		return this.scheme;
	}

	@Nullable
	public String getServerName() {
		return this.host;
	}

	public int getServerPort() {
		return this.port;
	}

	public boolean isSecure() {
		return this.secure;
	}

	public String getContextPath() {
		return this.forwardedPrefixExtractor.getContextPath();
	}

	public String getRequestURI() {
		return this.forwardedPrefixExtractor.getRequestUri();
	}

	public StringBuffer getRequestURL() {
		return this.forwardedPrefixExtractor.getRequestUrl();
	}

	public String adjustedRequestURL() {
		return String.format("%s://%s:%s%s", this.getScheme(), this.getServerName(), this.getServerPort(), this.getRequestURI());
	}
}

package org.springdoc.webmvc.api;

import java.util.Enumeration;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.lang.Nullable;
import org.springframework.web.util.UrlPathHelper;

class ForwardedPrefixExtractor {

	private final Supplier<HttpServletRequest> delegate;

	private final UrlPathHelper pathHelper;

	private final String baseUrl;

	private String actualRequestUri;

	@Nullable
	private final String forwardedPrefix;

	@Nullable
	private String requestUri;

	private String requestUrl;


	/**
	 * Constructor with required information.
	 * @param delegateRequest supplier for the current
	 * {@link HttpServletRequestWrapper#getRequest() delegate request} which
	 * may change during a forward (e.g. Tomcat.
	 * @param pathHelper the path helper instance
	 * @param baseUrl the host, scheme, and port based on forwarded headers
	 */
	public ForwardedPrefixExtractor(
			Supplier<HttpServletRequest> delegateRequest, UrlPathHelper pathHelper, String baseUrl) {

		this.delegate = delegateRequest;
		this.pathHelper = pathHelper;
		this.baseUrl = baseUrl;
		this.actualRequestUri = delegateRequest.get().getRequestURI();

		this.forwardedPrefix = initForwardedPrefix(delegateRequest.get());
		this.requestUri = initRequestUri();
		this.requestUrl = initRequestUrl(); // Keep the order: depends on requestUri
	}

	@Nullable
	private static String initForwardedPrefix(HttpServletRequest request) {
		String result = null;
		Enumeration<String> names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			if ("X-Forwarded-Prefix".equalsIgnoreCase(name)) {
				result = request.getHeader(name);
			}
		}
		if (result != null) {
			while (result.endsWith("/")) {
				result = result.substring(0, result.length() - 1);
			}
		}
		return result;
	}

	@Nullable
	private String initRequestUri() {
		if (this.forwardedPrefix != null) {
			return this.forwardedPrefix + this.pathHelper.getPathWithinApplication(this.delegate.get());
		}
		return null;
	}

	private String initRequestUrl() {
		return this.baseUrl + (this.requestUri != null ? this.requestUri : this.delegate.get().getRequestURI());
	}


	public String getContextPath() {
		return this.forwardedPrefix == null ? this.delegate.get().getContextPath() : this.forwardedPrefix;
	}

	public String getRequestUri() {
		if (this.requestUri == null) {
			return this.delegate.get().getRequestURI();
		}
		recalculatePathsIfNecessary();
		return this.requestUri;
	}

	public StringBuffer getRequestUrl() {
		recalculatePathsIfNecessary();
		return new StringBuffer(this.requestUrl);
	}

	private void recalculatePathsIfNecessary() {
		if (!this.actualRequestUri.equals(this.delegate.get().getRequestURI())) {
			// Underlying path change (e.g. Servlet FORWARD).
			this.actualRequestUri = this.delegate.get().getRequestURI();
			this.requestUri = initRequestUri();
			this.requestUrl = initRequestUrl(); // Keep the order: depends on requestUri
		}
	}
}

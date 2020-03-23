package org.springdoc.core;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Objects;
import java.util.stream.Stream;

class DelegatingMethodParameter extends MethodParameter {
	private volatile MethodParameter delegate;
	private Annotation[] additionalParameterAnnotations;
	private String parameterName;

	DelegatingMethodParameter(MethodParameter delegate, String parameterName, Annotation[] additionalParameterAnnotations) {
		super(delegate);
		this.delegate = delegate;
		this.additionalParameterAnnotations = additionalParameterAnnotations;
		this.parameterName = parameterName;
	}

	@Nullable
	static MethodParameter fromGetterOfField(Class<?> paramClass, Field field) {
		try {
			return Stream.of(Introspector.getBeanInfo(paramClass).getPropertyDescriptors())
					.filter(d -> d.getName().equals(field.getName()))
					.map(PropertyDescriptor::getReadMethod)
					.filter(Objects::nonNull)
					.findFirst()
					.map(method -> new MethodParameter(method, -1))
					.map(param -> new DelegatingMethodParameter(param, field.getName(), field.getDeclaredAnnotations()))
					.orElse(null);
		} catch (IntrospectionException e) {
			return null;
		}
	}

	@Override
	@NonNull
	public Annotation[] getParameterAnnotations() {
		return ArrayUtils.addAll(delegate.getParameterAnnotations(), additionalParameterAnnotations);
	}

	@Override
	public String getParameterName() {
		return parameterName;
	}

	@Override
	public Method getMethod() {
		return delegate.getMethod();
	}

	@Override
	public Constructor<?> getConstructor() {
		return delegate.getConstructor();
	}

	@Override
	public Class<?> getDeclaringClass() {
		return delegate.getDeclaringClass();
	}

	@Override
	public Member getMember() {
		return delegate.getMember();
	}

	@Override
	public AnnotatedElement getAnnotatedElement() {
		return delegate.getAnnotatedElement();
	}

	@Override
	public Executable getExecutable() {
		return delegate.getExecutable();
	}

	@Override
	public MethodParameter withContainingClass(Class<?> containingClass) {
		return delegate.withContainingClass(containingClass);
	}

	@Override
	public Class<?> getContainingClass() {
		return delegate.getContainingClass();
	}

	@Override
	public Class<?> getParameterType() {
		return delegate.getParameterType();
	}

	@Override
	public Type getGenericParameterType() {
		return delegate.getGenericParameterType();
	}

	@Override
	public Class<?> getNestedParameterType() {
		return delegate.getNestedParameterType();
	}

	@Override
	public Type getNestedGenericParameterType() {
		return delegate.getNestedGenericParameterType();
	}

	@Override
	public void initParameterNameDiscovery(ParameterNameDiscoverer parameterNameDiscoverer) {
		delegate.initParameterNameDiscovery(parameterNameDiscoverer);
	}
}

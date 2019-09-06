package org.springdoc.core;

import java.lang.reflect.Method;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.v3.core.util.ReflectionUtils;

public class MediaAttributes {

	private String[] classProduces;
	private String[] classConsumes;
	private String[] methodProduces = {};
	private String[] methodConsumes = {};

	public String[] getClassProduces() {
		return classProduces;
	}

	public void setClassProduces(String[] classProduces) {
		this.classProduces = classProduces;
	}

	public String[] getClassConsumes() {
		return classConsumes;
	}

	public void setClassConsumes(String[] classConsumes) {
		this.classConsumes = classConsumes;
	}

	public String[] getMethodProduces() {
		return methodProduces;
	}

	public void setMethodProduces(String[] methodProduces) {
		this.methodProduces = methodProduces;
	}

	public String[] getMethodConsumes() {
		return methodConsumes;
	}

	public void setMethodConsumes(String[] methodConsumes) {
		this.methodConsumes = methodConsumes;
	}

	public void calculateConsumesProduces(RequestMethod requestMethod, Method method) {
		PostMapping reqPostMappringMethod = ReflectionUtils.getAnnotation(method, PostMapping.class);
		if (reqPostMappringMethod != null) {
			fillMethods(reqPostMappringMethod.produces(), reqPostMappringMethod.consumes());
			return;
		}

		GetMapping reqGetMappringMethod = ReflectionUtils.getAnnotation(method, GetMapping.class);
		if (reqGetMappringMethod != null) {
			fillMethods(reqGetMappringMethod.produces(), reqGetMappringMethod.consumes());
			return;
		}

		DeleteMapping reqDeleteMappringMethod = ReflectionUtils.getAnnotation(method, DeleteMapping.class);
		if (reqDeleteMappringMethod != null) {
			fillMethods(reqDeleteMappringMethod.produces(), reqDeleteMappringMethod.consumes());
			return;
		}

		PutMapping reqPutMappringMethod = ReflectionUtils.getAnnotation(method, PutMapping.class);
		if (reqPutMappringMethod != null) {
			fillMethods(reqPutMappringMethod.produces(), reqPutMappringMethod.consumes());
			return;
		}

		RequestMapping reqMappringMethod = ReflectionUtils.getAnnotation(method, RequestMapping.class);
		if (reqMappringMethod != null) {
			fillMethods(reqMappringMethod.produces(), reqMappringMethod.consumes());
		}

	}

	private void fillMethods(String[] produces, String[] consumes) {
		methodProduces = ArrayUtils.isNotEmpty(produces) ? produces : new String[] { MediaType.ALL_VALUE };
		methodConsumes = ArrayUtils.isNotEmpty(consumes) ? consumes : new String[] { MediaType.ALL_VALUE };
	}

	public String[] getAllConsumes() {
		return ArrayUtils.addAll(methodConsumes, classConsumes);
	}

	public String[] getAllProduces() {
		return ArrayUtils.addAll(methodProduces, classProduces);
	}

}

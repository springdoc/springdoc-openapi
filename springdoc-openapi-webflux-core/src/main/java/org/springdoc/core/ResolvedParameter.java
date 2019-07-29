package org.springdoc.core;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.models.parameters.Parameter;

public class ResolvedParameter {
	List<Parameter> parameters = new ArrayList<>();
	Parameter requestBody;
	Parameter formParameter;
}

package org.springdoc.api;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;

public abstract class RequestMethodHandler {
    public abstract void execute(Operation operation, PathItem pathItemObject);
}

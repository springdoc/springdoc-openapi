package test.org.springdoc.api.v31.app227.wrapper;


import test.org.springdoc.api.v31.app227.model.Item;

import org.springframework.http.HttpStatusCode;

public class NoGenericWrapper extends ResponseEntityWrapper<String> {
    public NoGenericWrapper(Item<String> body, HttpStatusCode status) {
        super(body, status);
    }
}

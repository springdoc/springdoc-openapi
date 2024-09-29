package test.org.springdoc.api.app227.wrapper;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import test.org.springdoc.api.app227.model.Item;

public class ResponseEntityWrapper<T> extends ResponseEntity<Item<T>> {
    public ResponseEntityWrapper(Item<T> body, HttpStatusCode status) {
        super(body, status);
    }
}

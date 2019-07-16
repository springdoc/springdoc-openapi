package org.springdoc.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Optional;
@javax.annotation.Generated(value = "org.springdoc.codegen.languages.SpringCodegen", date = "2019-07-11T00:09:29.839+02:00[Europe/Paris]")

@Controller
@RequestMapping("${openapi.openAPIPetstore.base-path:/}")
public class StoreApiController implements StoreApi {

    private final StoreApiDelegate delegate;

    public StoreApiController(@org.springframework.beans.factory.annotation.Autowired(required = false) StoreApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new StoreApiDelegate() {});
    }

    @Override
    public StoreApiDelegate getDelegate() {
        return delegate;
    }

}

package test.org.springdoc.api.app68.api.pet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@javax.annotation.Generated(value = "org.springdoc.demo.app2.codegen.languages.SpringCodegen", date = "2019-07-11T00:09:29.839+02:00[Europe/Paris]")

@Controller
@RequestMapping("${openapi.openAPIPetstore.base-path:/}")
public class PetApiController implements PetApi {

    private final PetApiDelegate delegate;

    public PetApiController(@org.springframework.beans.factory.annotation.Autowired(required = false) PetApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new PetApiDelegate() {
        });
    }

    @Override
    public PetApiDelegate getDelegate() {
        return delegate;
    }

}

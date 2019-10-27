package test.org.springdoc.api.app2.api;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@javax.annotation.Generated(value = "org.springdoc.demo.app2.codegen.languages.SpringCodegen", date = "2019-07-11T00:09:29.839+02:00[Europe/Paris]")

@Controller
@RequestMapping("${openapi.openAPIPetstore.base-path:/}")
public class UserApiController implements UserApi {

    private final UserApiDelegate delegate;

    public UserApiController(@org.springframework.beans.factory.annotation.Autowired(required = false) UserApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new UserApiDelegate() {});
    }

    @Override
    public UserApiDelegate getDelegate() {
        return delegate;
    }

}

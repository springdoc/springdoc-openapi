package test.org.springdoc.api.app104;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "design")
@Controller
@RequestMapping("/design")
public class DesignController extends CrudController<Design> {


}
package test.org.springdoc.api.v31.app196;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/jsp")
public class JSPController {


	@GetMapping(produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public ModelAndView HelloJSP() {
		ModelAndView jsp = new ModelAndView();
		jsp.setViewName("index");

		return jsp;
	}
}

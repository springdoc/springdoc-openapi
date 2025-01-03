package test.org.springdoc.api.v31.app47;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class BasicController {

	@GetMapping(headers = { "foo=bar" })
	public String get1() {
		return null;
	}

	@GetMapping(headers = { "fi=ri" })
	public String get2() {
		return null;
	}

	@GetMapping(
			headers = { "User-Agent=" + "MyUserAgent" })
	public String get3() {
		return null;
	}
}

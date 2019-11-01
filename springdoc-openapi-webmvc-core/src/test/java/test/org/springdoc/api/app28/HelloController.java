package test.org.springdoc.api.app28;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class HelloController {

	@PostMapping(value = "/upload2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String upload2(@RequestPart("one") MultipartFile one, @RequestPart("two") MultipartFile two) {
		return "Ok";
	}
}

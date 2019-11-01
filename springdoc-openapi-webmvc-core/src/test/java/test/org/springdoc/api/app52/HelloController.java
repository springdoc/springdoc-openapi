package test.org.springdoc.api.app52;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class HelloController {

	@PostMapping(value = "/test1/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String createTest1(@PathVariable String username, @RequestPart("test") MyTestDto test,
			@RequestPart("image") MultipartFile imageFile) {
		return null;
	}

	@PostMapping(value = "/test2/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String createTest2(@PathVariable String username, @RequestPart("image") MultipartFile imageFile,
			@RequestPart("test") MyTestDto test) {
		return null;
	}

	@PostMapping(value = "/test3", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String createTest3(@RequestPart("test") MyTestDto test,
			@RequestPart("doc") List<MultipartFile> multipartFiles) {
		return null;
	}

	class MyTestDto {
		public String object1;
		public String object2;
		public String object3;
	}
}
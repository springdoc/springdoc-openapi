package test.org.springdoc.api.v31.app12;

import jakarta.annotation.Nullable;
import test.org.springdoc.api.v30.app12.SpringDocApp12Test.MyEnum;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bnasslahsen
 */
@RestController
public class EnumController {
	@GetMapping("/test-enum-2")
	String testEnum2(@Nullable MyEnum e) {
		return "";
	}
}
package test.org.springdoc.api.app93;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.GetMapping;

public abstract class BaseController<TClientModel extends BaseClientModel> {
	@Operation
	@GetMapping
	TClientModel get(TClientModel param) {
		return null;
	}
}

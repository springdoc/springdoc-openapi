package test.org.springdoc.api.app79;

import java.util.List;

import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public abstract class BaseController<TClientModel extends BaseClientModel> {
	@GetMapping("/test1")
	Mono<ResponseEntity<TClientModel>> get1() {
		return null;
	}

	@GetMapping("/test2")
	Mono<ResponseEntity<List<TClientModel>>> get2() {
		return null;
	}
}

package test.org.springdoc.api.app153;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TestController {

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(OrderState.class, new OrderStateMapper());
	}

	@GetMapping(value = {"/orders"})
	public Object method(
			@RequestParam(value = "state", defaultValue = "finished") OrderState orderState) {
		return null;
	}
}
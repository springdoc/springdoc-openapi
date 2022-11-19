package test.org.springdoc.api.app153;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Test controller.
 */
@RestController
class TestController {

	/**
	 * Init binder.
	 *
	 * @param dataBinder the data binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(OrderState.class, new OrderStateMapper());
	}

	/**
	 * Method object.
	 *
	 * @param orderState the order state
	 * @return the object
	 */
	@GetMapping(value = { "/orders" })
	public Object method(
			@RequestParam(value = "state", defaultValue = "finished") OrderState orderState) {
		return null;
	}
}
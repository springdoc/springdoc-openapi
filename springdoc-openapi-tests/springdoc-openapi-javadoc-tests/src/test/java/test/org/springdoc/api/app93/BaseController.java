package test.org.springdoc.api.app93;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * The type Base controller.
 *
 * @param <TClientModel>  the type parameter
 */
public abstract class BaseController<TClientModel extends BaseClientModel> {
	/**
	 * Get t client model.
	 *
	 * @param param the param 
	 * @return the t client model
	 */
	@Operation
	@GetMapping
	TClientModel get(TClientModel param) {
		return null;
	}
}

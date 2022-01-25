package test.org.springdoc.api.app153;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;

import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public class OrderStateMapper extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) {
		setValue(
				Arrays.stream(OrderState.class.getEnumConstants())
						.filter(e -> e.getValue().equals(text))
						.findFirst()
						.orElseThrow(() -> new MethodArgumentTypeMismatchException(
								text, OrderState.class, "orderState", null, null)));
	}
}
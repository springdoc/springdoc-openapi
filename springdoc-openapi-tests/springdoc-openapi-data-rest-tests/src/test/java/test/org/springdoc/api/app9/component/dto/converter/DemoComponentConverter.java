package test.org.springdoc.api.app9.component.dto.converter;



import test.org.springdoc.api.app9.component.dto.DemoComponentDto;
import test.org.springdoc.api.app9.component.model.DemoComponent;
import test.org.springdoc.api.app9.utils.Converter;

import org.springframework.stereotype.Component;

@Component
public class DemoComponentConverter implements Converter<DemoComponent, DemoComponentDto> {

	@Override
	public DemoComponentDto convert(DemoComponent source) {
		if (source == null) {
			return null;
		}

		return DemoComponentDto.builder() //
				.id(source.getId()) //
				.payload(source.getPayload()) //
				.build();
	}

}

package test.org.springdoc.api.app9.component.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class DemoComponent {

	@Id
	private String id;
	
	private String payload;

}

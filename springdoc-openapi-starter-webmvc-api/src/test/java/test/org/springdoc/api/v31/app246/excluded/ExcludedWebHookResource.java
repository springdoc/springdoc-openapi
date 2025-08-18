package test.org.springdoc.api.v31.app246.excluded;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Webhook;

import io.swagger.v3.oas.annotations.Webhooks;
import org.springframework.stereotype.Component;
import test.org.springdoc.api.v31.app246.IncludedWebHookResource;

@Component
@Webhooks({
        @Webhook(
                name = "excludedNewPet",
                operation = @Operation(
                        operationId = "excludedNewPet",
                        method = "post",
                        summary = "This webhook should be ignored"
                )
        )
})
public class ExcludedWebHookResource {
	public void excludedNewPet(IncludedWebHookResource.RequestDto requestDto) {
		// This method is intentionally left empty.
	}
}

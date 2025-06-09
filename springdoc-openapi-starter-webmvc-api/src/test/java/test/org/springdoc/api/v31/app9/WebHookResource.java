package test.org.springdoc.api.v31.app9;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Webhook;
import io.swagger.v3.oas.annotations.Webhooks;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.stereotype.Component;

@Webhooks({
		@Webhook(
				name = "newPet1",
				operation = @Operation(
						requestBody = @RequestBody(
								description = "Information about a new pet in the system",
								content = {
										@Content(
												mediaType = "application/json",
												schema = @Schema(
														description = "Webhook Pet",
														implementation = RequestDto.class
												)
										)
								}
						),
						method = "post",
						responses = @ApiResponse(
								responseCode = "200",
								description = "Return a 200 status to indicate that the data was received successfully"
						)
				)
		)
})
@Component
public class WebHookResource {

	@Webhook(
			name = "newPet",
			operation = @Operation(
					requestBody = @RequestBody(
							description = "Information about a new pet in the system",
							content = {
									@Content(
											mediaType = "application/json",
											schema = @Schema(
													description = "Webhook Pet",
													implementation = RequestDto.class
											)
									)
							}
					),
					method = "post",
					responses = @ApiResponse(
							responseCode = "200",
							description = "Return a 200 status to indicate that the data was received successfully"
					)
			)
	)
	public void newPet(RequestDto requestDto) {
		// This method is intentionally left empty.
		// The actual processing of the webhook data would be implemented here.
		System.out.println("Received new pet with personal number: " + requestDto.getPersonalNumber());
	}
	
	
}


class RequestDto {

	private String personalNumber;

	public String getPersonalNumber() {
		return personalNumber;
	}

	public void setPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
	}
}
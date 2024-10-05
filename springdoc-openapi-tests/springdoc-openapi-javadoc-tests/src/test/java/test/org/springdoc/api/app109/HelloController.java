package test.org.springdoc.api.app109;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {

	/**
	 * Gets resource.
	 *
	 * @return the resource
	 */
	@GetMapping("/api/v1/resource")
	public Resource getResource() {
		return new ByteArrayResource(new byte[] {});
	}

	/**
	 * Get byte array byte [ ].
	 *
	 * @return the byte [ ]
	 */
	@GetMapping("/api/v1/bytearray")
	@ApiResponse(content = @Content(schema = @Schema(type = "string", format = "binary")))
	public byte[] getByteArray() {
		return new byte[] {};
	}
}

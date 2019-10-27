package test.org.springdoc.api.app2.api;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

import test.org.springdoc.api.app2.model.Order;

/**
 * A delegate to be called by the {@link StoreApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@javax.annotation.Generated(value = "org.springdoc.demo.app2.codegen.languages.SpringCodegen", date = "2019-07-11T00:09:29.839+02:00[Europe/Paris]")

public interface StoreApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * @see StoreApi#deleteOrder
     */
    default ResponseEntity<Void> deleteOrder( String  orderId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * @see StoreApi#getInventory
     */
    default ResponseEntity<Map<String, Integer>> getInventory() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * @see StoreApi#getOrderById
     */
    default ResponseEntity<Order> getOrderById( Long  orderId) {
        extract();
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

	default void extract() {
		getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"petId\" : 6,  \"quantity\" : 1,  \"id\" : 0,  \"shipDate\" : \"2000-01-23T04:56:07.000+00:00\",  \"complete\" : false,  \"status\" : \"placed\"}");
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/xml"))) {
                    ApiUtil.setExampleResponse(request, "application/xml", "<Order>  <id>123456789</id>  <petId>123456789</petId>  <quantity>123</quantity>  <shipDate>2000-01-23T04:56:07.000Z</shipDate>  <status>aeiou</status>  <complete>true</complete></Order>");
                    break;
                }
            }
        });
	}

    /**
     * @see StoreApi#placeOrder
     */
    default ResponseEntity<Order> placeOrder( Order  order) {
        extract();
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}

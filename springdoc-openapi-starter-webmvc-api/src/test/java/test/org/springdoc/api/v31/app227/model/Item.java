package test.org.springdoc.api.v31.app227.model;

/**
 * Base item
 */
public record Item<T>(String id, String type, T resource) {

    /**
     * Create Item object
     */
    public static <T> Item<T> fromPayload(String id, String type, T payload) {
        return new Item<>(id, type, payload);
    }
}

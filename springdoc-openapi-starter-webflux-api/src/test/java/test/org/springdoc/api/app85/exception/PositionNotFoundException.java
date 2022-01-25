package test.org.springdoc.api.app85.exception;

public class PositionNotFoundException extends RuntimeException {

    public PositionNotFoundException(String positionId) {
        super("Position not found with id " + positionId);
    }
}

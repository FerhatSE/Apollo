package nl.saxion.cos.exceptions;

public class NonBooleanConditionException extends ValidateTypeException {

    public NonBooleanConditionException(String message) {
        super(message);
    }
}

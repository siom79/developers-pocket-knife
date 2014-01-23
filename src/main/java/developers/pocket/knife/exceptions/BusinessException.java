package developers.pocket.knife.exceptions;

public class BusinessException extends RuntimeException {

    public enum Reason {
        UnsupportedEncoding
    }

    public BusinessException(Reason reason, String message) {
        super(message);
    }
}

package developers.pocket.knife.exceptions;

public class TechnicalException extends RuntimeException {
    private Reason reason;

    public enum Reason {
        InternalError, IllegalArgument
    }

    public TechnicalException(Reason reason) {
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }
}

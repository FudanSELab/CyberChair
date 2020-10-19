package SELab.exception;

public class IllegalRatifyStatusException extends RuntimeException {
    private static final long serialVersionUID = -3306753942366869976L;

    public IllegalRatifyStatusException() {
        super("illegal ratify exception");
    }
}
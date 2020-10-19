package SELab.exception;

public class EmailHasBeenRegisteredException extends RuntimeException {
    private static final long serialVersionUID = -3012533940710869977L;

    public EmailHasBeenRegisteredException(String email) {
        super("Email " + email + " has been registered");
    }
}
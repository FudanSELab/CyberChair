package SELab.exception;

import org.springframework.security.core.AuthenticationException;

public class IllegalEmailAddressException extends AuthenticationException {
    private static final long serialVersionUID = -6074753940710869976L;

    public IllegalEmailAddressException() {
        super("illegal email address");
    }
}

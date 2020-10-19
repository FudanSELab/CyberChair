package SELab.exception;

import org.springframework.security.core.AuthenticationException;

public class IllegalUserNameException extends AuthenticationException {
    private static final long serialVersionUID = -6074753940710869976L;

    public IllegalUserNameException() {
        super("User Name is illegal!");
    }
}

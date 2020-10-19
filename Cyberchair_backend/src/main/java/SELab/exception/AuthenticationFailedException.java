package SELab.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationFailedException extends AuthenticationException {
    private static final long serialVersionUID = -6074753940710869976L;

    public AuthenticationFailedException(String errorMsg) {
        super(errorMsg);
    }
}

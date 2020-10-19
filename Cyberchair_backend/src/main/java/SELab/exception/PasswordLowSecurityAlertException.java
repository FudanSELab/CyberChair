package SELab.exception;

import org.springframework.security.core.AuthenticationException;

public class PasswordLowSecurityAlertException extends AuthenticationException {
    private static final long serialVersionUID = -6074753940710869975L;

    public PasswordLowSecurityAlertException() {
        super("Password is of low security!");
    }
}

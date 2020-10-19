package SELab.exception;

import org.springframework.security.core.AuthenticationException;

public class FullNameillegalException extends AuthenticationException {
    private static final long serialVersionUID = -6074753940710869974L;

    public FullNameillegalException(String FullName) {
        super("Full name " + FullName + " is illegal!");
    }
}

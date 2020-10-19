package SELab.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNamedidntExistException extends AuthenticationException {
    private static final long serialVersionUID = -6074753940710869973L;

    public UserNamedidntExistException(String username) {
        super("username " + username + " didn't exist!");
    }
}

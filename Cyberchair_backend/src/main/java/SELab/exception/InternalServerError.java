package SELab.exception;

import org.springframework.security.core.AuthenticationException;

public class InternalServerError extends AuthenticationException {
    private static final long serialVersionUID = -6074753940710869974L;

    public InternalServerError(String reason){
        super(reason);
    }
}

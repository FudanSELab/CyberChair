package SELab.exception.user;

import org.springframework.security.core.AuthenticationException;

public class ArticleNotFoundException extends AuthenticationException {
    private static final long serialVersionUID = -6074753940710869974L;

    public ArticleNotFoundException(String articleId){
        super("article with id " + articleId + "not found");
    }
}

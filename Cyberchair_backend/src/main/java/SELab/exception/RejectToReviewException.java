package SELab.exception;

import org.springframework.security.core.AuthenticationException;

public class RejectToReviewException extends AuthenticationException {
    private static final long serialVersionUID = -3534753914312569970L;

    public RejectToReviewException(String pcMemberName,String articleId) {
        super("User " + pcMemberName + " didn't have a ReviewRelation with " + articleId + " Or Already Reviewed");
    }
}
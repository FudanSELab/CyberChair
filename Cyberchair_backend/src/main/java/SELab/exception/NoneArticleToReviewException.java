package SELab.exception;

public class NoneArticleToReviewException extends RuntimeException {
    private static final long serialVersionUID = -3178953914310869970L;

    public NoneArticleToReviewException(String meetingName) {
        super("Meeting Name" + meetingName + " isn't avaliable to begin review because of none article");
    }
}
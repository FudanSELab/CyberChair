package SELab.exception;

public class MeetingStatusUnAvailableToReviewException extends RuntimeException {
    private static final long serialVersionUID = -3124953914310869970L;

    public MeetingStatusUnAvailableToReviewException() {
        super("Meeting Status UnAvailable To Review or Publish reviews!");
    }
}
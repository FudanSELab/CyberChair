package SELab.exception;

public class AtLeastOneTopicException extends RuntimeException {
    private static final long serialVersionUID = -1762533940710869977L;

    public AtLeastOneTopicException() {
        super("There should be at least one topic!");
    }
}
package SELab.exception;

public class AtLeastThreePCMemberException extends RuntimeException {
    private static final long serialVersionUID = -2432533940710869977L;

    public AtLeastThreePCMemberException() {
        super("There should be at least three available PCMember besides the chair!");
    }
}
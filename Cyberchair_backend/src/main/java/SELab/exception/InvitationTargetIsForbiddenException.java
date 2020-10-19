package SELab.exception;

public class InvitationTargetIsForbiddenException extends RuntimeException{
    private static final long serialVersionUID = -257323940710869970L;

    public InvitationTargetIsForbiddenException(String meetingName) {
        super("Meeting Named" + meetingName + " can't add Chair as PCMember again!");
    }
}
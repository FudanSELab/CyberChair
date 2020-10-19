package SELab.exception;

import org.springframework.security.core.AuthenticationException;

public class MeetingStatusUnavailableForPCMemberInvitationException extends RuntimeException{
    private static final long serialVersionUID = -1345853940710869970L;

    public MeetingStatusUnavailableForPCMemberInvitationException(String meetingName) {
        super("Meeting Named" + meetingName + " can't add PCmember in such Status!");
    }
}

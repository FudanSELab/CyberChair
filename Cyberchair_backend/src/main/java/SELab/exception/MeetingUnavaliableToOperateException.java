package SELab.exception;

public class MeetingUnavaliableToOperateException extends RuntimeException {
    private static final long serialVersionUID = -2374753914310869970L;

    public MeetingUnavaliableToOperateException(String meetingName) {
        super("Meeting Name" + meetingName + " isn't avaliable to do this operation for current status.(Must unproccessed when ratify,ApplyPassed when beginSubmission,SubmissionAvailable when submission and begin review)");
    }
}

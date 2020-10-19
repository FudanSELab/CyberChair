package SELab.exception;

public class MeetingNameHasbeenregisteredException extends RuntimeException {
    private static final long serialVersionUID = -6074753940710869970L;

    public MeetingNameHasbeenregisteredException(String meetingName) {
        super("Meeting Name" + meetingName + " has been registered!");
    }
}

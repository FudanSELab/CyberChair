package SELab.exception;

public class MeetingOfNoExistenceException extends RuntimeException {
    private static final long serialVersionUID = -2513753940710869970L;

    public MeetingOfNoExistenceException(String meetingName) {
        super("Meeting Named" + meetingName + " didn't exist!");
    }
}

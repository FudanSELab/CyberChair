package SELab.exception;

public class cannotAssignArticlesException extends RuntimeException{
    private static final long serialVersionUID = -1762533940710869977L;

    public cannotAssignArticlesException() {
        super("We can not assign the articles under the rules, please add some new PCMembers.");
    }
}

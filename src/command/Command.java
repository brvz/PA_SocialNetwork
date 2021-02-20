package command;

public interface Command {
    /**
     * Execute something that will generate an action.
     */
    void execute();

    /**
     * Returns back that action.
     */
    void unExecute();

}

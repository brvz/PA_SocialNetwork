package command;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Command Manager is a class that will have a stack and will give the possibility to make undo and redo of some client
 * actions.
 *
 */

public class CommandManager {

    private final Stack<Command> commandsDone;
    private final List<Command> commandMacro;
    private boolean isRecordingMacro;
    private final Stack<Command> history;


    public CommandManager() {
        this.commandsDone = new Stack<>();
        this.commandMacro = new ArrayList<>();
        this.isRecordingMacro = false;
        this.history = new Stack<>();
    }

    /**
     * Executes the commands.
     * @param cmd - Command
     */
    public void executeCommand(Command cmd){
        if (isRecordingMacro) {
            commandMacro.add(cmd);
        } else {
            cmd.execute();
            commandsDone.push(cmd);
        }
    }

    /**
     * Associated with redo actions.
     * @param cmd - Command
     */
    public void executeRedo(Command cmd){
        if(!history.isEmpty()) {
            cmd.execute();
            commandsDone.push(history.peek());
            history.pop();
        }
    }

    /**
     * Associated with undo actions.
     * @param cmd - Command
     */
    public void executeUndo(Command cmd){
        if(!commandsDone.isEmpty()) {
            cmd.execute();
            history.push(commandsDone.peek());
            commandsDone.pop();
        }
    }

    /**
     * Is the command that will make the possibility to filter by interests.
     * @param cmd - Command.
     */
    public void executeFilterInterest(Command cmd){
        if(!commandsDone.isEmpty()){
            cmd.execute();
            history.push(commandsDone.peek());
        }
    }

    /**
     * Is the command that will make the possibility to filter by date.
     * @param cmd - Command.
     */
    public void executeFilterDate(Command cmd){
        if(!commandsDone.isEmpty()){
            cmd.execute();
            history.push(commandsDone.peek());
        }
    }


}

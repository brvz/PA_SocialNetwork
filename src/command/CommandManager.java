package command;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Class that contains a stack, enabling the user to undo or redo the previous action.
 */

public class CommandManager {
    private final Stack<Command> commandsDone;
    private final List<Command> commandMacro;
    private boolean isRecording;
    private final Stack<Command> history;


    public CommandManager() {
        commandMacro = new ArrayList<>();
        commandsDone = new Stack<>();
        isRecording = false;
        history = new Stack<>();
    }

    public void executeCommand(Command command){
        if (isRecording) {
            commandMacro.add(command);
        } else {
            command.execute();
            commandsDone.push(command);
        }
    }

    public void executeUndo(Command command){
        if(!commandsDone.isEmpty()) {
            command.execute();
            history.push(commandsDone.peek());
            commandsDone.pop();
        }
    }

    public void executeFilterInterest(Command command){
        if(!commandsDone.isEmpty()){
            command.execute();
            history.push(commandsDone.peek());
        }
    }

    public void executeFilterDate(Command command) {
        if (!commandsDone.isEmpty()) {
            command.execute();
            history.push(commandsDone.peek());
        }
    }
}

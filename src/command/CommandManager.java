package command;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CommandManager {

    private final Stack<Command> commandsDone;
    private final List<Command> commandMacro;
    private boolean isRecordingMacro;

    public CommandManager() {
        this.commandsDone = new Stack<>();
        this.commandMacro = new ArrayList<>();
        this.isRecordingMacro = false;
    }

    public void undo(){
        if(!commandsDone.isEmpty()) {
            commandsDone.pop().unExecute();
        }
    }

    public void executeCommand(Command cmd){
        if (isRecordingMacro) {
            commandMacro.add(cmd);
        } else {
            cmd.execute();
            commandsDone.push(cmd);
        }
    }

}

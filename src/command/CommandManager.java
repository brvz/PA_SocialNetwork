package command;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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

    public void undo(){
        if(!commandsDone.isEmpty()) {
            history.push(commandsDone.peek());
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

    public void executeRedo(Command cmd){
        if(!history.isEmpty()) {
            cmd.execute();
            commandsDone.push(history.peek());
            history.pop();
        }
    }

    public void executeFilterInterest(Command cmd){
        if(!commandsDone.isEmpty()){
            cmd.execute();
            history.push(commandsDone.peek());
        }
    }

    public void executeFilterDate(Command cmd){
        if(!commandsDone.isEmpty()){
            cmd.execute();
            history.push(commandsDone.peek());
        }
    }


}

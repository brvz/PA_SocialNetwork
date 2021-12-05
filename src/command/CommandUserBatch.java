package command;

import model.SocialNetwork;
import model.User;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Command User is a class that will make the action of users batch insertion after a undo or a redo.
 */
public class  CommandUserBatch extends CommandNetwork {
    private final List<Integer> userId;

    public CommandUserBatch(SocialNetwork network, List<Integer> userId) {
        super(network);
        this.userId = userId;

    }

    @Override
    public void execute() {
        if(network.isRedo() || network.isUndo()){
            if(!network.getLastUsers().isEmpty()){
                network.clearLastUsers();
            }
            if(!network.getLastUsersHistory().isEmpty()){
                network.clearLastUsersHistory();
            }
            network.setUndo(false);
            network.setRedo(false);
        }
        network.readCSVBatch(userId);
    }

    @Override
    public void unExecute() {
    }

}

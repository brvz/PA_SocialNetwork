package command;

import model.SocialNetwork;
import model.User;

import java.util.Stack;

/**
 * Command User is a class that will make the action of users automatic insertion after a undo or a redo.
 */
public class CommandUser extends CommandNetwork  {
    private final int userId;

    public CommandUser(SocialNetwork network, int userId) {
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
        network.readCSVRelationshipsByUser(userId);
    }

    @Override
    public void unExecute() {
    }
}

package command;

import model.SocialNetwork;
import model.User;

import java.util.Stack;

/**
 * Command User is a class that will make the action of users automatic insertion after a undo or a redo.
 */
public class CommandUser extends CommandSocialNetwork  {

    private final int userNumber;


    public CommandUser(SocialNetwork sn, int userNumber) {
        super(sn);
        this.userNumber = userNumber;

    }

    @Override
    public void execute() {
        if(sn.isRedo() || sn.isUndo()){
            if(!sn.getLastUsers().isEmpty()){
                sn.clearLastUsers();
            }
            if(!sn.getLastUsersHistory().isEmpty()){
                sn.clearLastUsersHistory();
            }
            sn.setUndo(false);
            sn.setRedo(false);
        }
        sn.readCSVRelationshipsByUser(userNumber);
    }

    @Override
    public void unExecute() {
        /*User user = sn.getIdOfUser(userNumber);
        if(sn.isRedo()){
            if(!sn.getLastUserAdded().contains(user)){
                sn.getLastUserAdded().push(user);
                sn.setUndo(true);
            }
        }
        sn.removeUserById(userNumber);
        sn.setRedo(false);*/

    }
}

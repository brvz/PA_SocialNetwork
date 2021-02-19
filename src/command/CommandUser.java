package command;

import model.SocialNetwork;
import model.User;

import java.util.Stack;

public class CommandUser extends CommandSocialNetwork  {

    private final int userNumber;


    public CommandUser(SocialNetwork sn, int userNumber) {
        super(sn);
        this.userNumber = userNumber;

    }

    @Override
    public void execute() {
        sn.readCSVRelationshipsByUser(userNumber);
        if(!sn.getLastUsers().isEmpty()){
            sn.clearLastUsers();
        }
    }

    @Override
    public void unExecute() {
        User user = sn.getIdOfUser(userNumber);
        if(sn.isRedo()){
            if(!sn.getLastUserAdded().contains(user)){
                sn.getLastUserAdded().push(user);
                sn.setUndo(true);
            }
        }
        sn.removeUserById(userNumber);
        sn.setRedo(false);

    }
}

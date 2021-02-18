package command;

import model.SocialNetwork;

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
        sn.removeUserById(userNumber);
    }
}

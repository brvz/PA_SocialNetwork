package command;

import model.SocialNetwork;

public class CommandUser extends CommandSocialNetwork  {

    private final int userNumber;

    public CommandUser(SocialNetwork sn, int userNumber) {
        super(sn);
        this.userNumber = userNumber;
    }

    @Override
    public void execute() {
        sn.readCSVRelationshipsByUser(userNumber);
    }

    @Override
    public void unExecute() {
        sn.removeUserById(userNumber);
    }
}

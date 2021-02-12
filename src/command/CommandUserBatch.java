package command;

import model.SocialNetwork;

import java.util.ArrayList;
import java.util.List;

public class  CommandUserBatch extends CommandSocialNetwork {

    private final List<Integer> userNumber;

    public CommandUserBatch(SocialNetwork sn, List<Integer> userNumber) {
        super(sn);
        this.userNumber = userNumber;
    }

    @Override
    public void execute() {
        for (Integer integer : userNumber) {
            sn.readCSVRelationshipsByUser(integer);
        }

    }

    @Override
    public void unExecute() {
        for (Integer integer : userNumber) {
            sn.removeUserById(integer);
        }

    }
}

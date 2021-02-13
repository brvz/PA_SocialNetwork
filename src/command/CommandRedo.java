package command;

import model.SocialNetwork;
import model.User;
import view.SocialNetworkUI;

import java.util.ArrayList;
import java.util.List;

public class CommandRedo extends CommandSocialNetwork{



    public CommandRedo(SocialNetwork sn) {
        super(sn);
    }

    @Override
    public void execute() {
        if(sn.getLastUserAdded() != null){
            sn.readCSVRelationshipsByUser(sn.getLastUserAdded().getNumber());
            sn.clearLastUser();
        }else if(sn.getLastUsers().size() > 0){
            List<Integer> user = new ArrayList<>();
            for (User i : sn.getLastUsers()) {
                user.add(i.getNumber());
            }
            sn.clearLastUsers();
            sn.readCSVBatch(user);


        }

    }

    @Override
    public void unExecute() {

    }
}

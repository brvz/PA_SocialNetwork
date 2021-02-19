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
            if(sn.isUndo()){
                sn.readCSVRelationshipsByUser(sn.getLastUserAdded().peek().getNumber());
                sn.setUndo(false);

            }else{
                sn.readCSVRelationshipsByUser(sn.getLastUserAdded().pop().getNumber());
            }
            sn.setRedo(true);
        }else if(sn.getLastUsers().size() > 0){
            List<Integer> user = new ArrayList<>();
            for (User i : sn.getLastUsers().peek()) {
                user.add(i.getNumber());
            }
            sn.getLastUsers().pop();
            sn.readCSVBatch(user);


        }

    }

    @Override
    public void unExecute() {

    }
}

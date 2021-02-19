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
        /*if(sn.getLastUserAdded() != null){
            if(sn.isUndo()){
                sn.readCSVRelationshipsByUser(sn.getLastUserAdded().peek().getNumber());
                sn.setUndo(false);
            }else{
                sn.readCSVRelationshipsByUser(sn.getLastUserAdded().pop().getNumber());
            }
            sn.setRedo(true);
        }else*/
        if(sn.getLastUsers().size() > 0){
            List<Integer> user = new ArrayList<>();
            List<User> list = sn.getLastUsersHistory().peek();
            //if(sn.isUndo()){
            if(list.size() > 1) {
                for (User i : sn.getLastUsersHistory().peek()) {
                    user.add(i.getNumber());
                }
                sn.getLastUsersHistory().pop();
                sn.getLastUsers().push(list);
                sn.readCSVBatch(user);
                sn.setRedo(true);
                sn.setUndo(false);
            }else if(list.size() == 1){
                for (User i : sn.getLastUsersHistory().peek()) {
                    user.add(i.getNumber());
                    sn.readCSVRelationshipsByUser(i.getNumber());
                }
                sn.getLastUsersHistory().pop();
                sn.getLastUsers().push(list);
                sn.setRedo(true);
                sn.setUndo(false);
            }
        }


    }

    @Override
    public void unExecute() {

    }
}

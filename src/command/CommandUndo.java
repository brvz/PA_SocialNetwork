package command;

import model.SocialNetwork;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class CommandUndo extends CommandSocialNetwork{

    public CommandUndo(SocialNetwork sn) {
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
            //sn.setRedo(true);
        }else*/
        if(sn.getLastUsers().size() > 0){
            List<Integer> user = new ArrayList<>();
            List<User> list = sn.getLastUsers().peek();
            //if(sn.isRedo()){
            if(list.size() > 1){
                for (User i : list) {
                    user.add(i.getNumber());
                    sn.removeUserById(i.getNumber());
                }
                sn.getLastUsers().pop();
                sn.getLastUsersHistory().push(list);
                sn.setUndo(true);
                sn.setRedo(false);
            }else if(list.size() == 1){
                for (User i : list) {
                    user.add(i.getNumber());
                    sn.removeUserById(i.getNumber());
                }
                sn.getLastUsers().pop();
                sn.getLastUsersHistory().push(list);
                sn.setUndo(true);
                sn.setRedo(false);
            }


            //sn.readCSVBatch(user);
            //sn.setUndo(false);
           /* }else{
                for (User i : sn.getLastUsers().pop()) {
                    user.add(i.getNumber());
                }
                //sn.getLastUsers().pop();
                sn.readCSVBatch(user);
            }*/
        }

    }

    @Override
    public void unExecute() {

    }
}

package command;

import model.SocialNetwork;
import model.User;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Command User is a class that will make the action of users batch insertion after a undo or a redo.
 */
public class  CommandUserBatch extends CommandSocialNetwork {

    private final List<Integer> userNumber;



    public CommandUserBatch(SocialNetwork sn, List<Integer> userNumber) {
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
        sn.readCSVBatch(userNumber);
    }

    @Override
    public void unExecute() {
        /*List<User> userList = new ArrayList<>();
        for (Integer integer : userNumber) {
            User user = sn.getIdOfUser(integer);
            //sn.removeUserById(integer);
            userList.add(user);
        }
        if(sn.isRedo()){
            //if(!sn.getLastUsers().contains(userList)){
              //  sn.getLastUsers().push(userList);
                //sn.setUndo(true);
            //}
            if(sn.getLastUsersHistory().contains(userList)){
                sn.getLastUsersHistory().push(userList);
                sn.setUndo(true);
            }
        }
        for (Integer integer : userNumber) {
            sn.removeUserById(integer);
        }
        sn.setRedo(false);*/
    }

}

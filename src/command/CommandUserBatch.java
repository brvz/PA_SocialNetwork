package command;

import model.SocialNetwork;
import model.User;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class  CommandUserBatch extends CommandSocialNetwork {

    private final List<Integer> userNumber;



    public CommandUserBatch(SocialNetwork sn, List<Integer> userNumber) {
        super(sn);
        this.userNumber = userNumber;

    }

    @Override
    public void execute() {

        //for (Integer integer : userNumber) {
            sn.readCSVBatch(userNumber);
            if(sn.getLastUserAdded() != null){
                sn.clearLastUser();
            }


        //}

    }

    @Override
    public void unExecute() {
        List<User> userList = new ArrayList<>();
        for (Integer integer : userNumber) {
            User user = sn.getIdOfUser(integer);
            //sn.removeUserById(integer);
            userList.add(user);
        }
        if(sn.isRedo()){
            if(!sn.getLastUsers().contains(userList)){
                sn.getLastUsers().push(userList);
                sn.setUndo(true);
            }
        }
        for (Integer integer : userNumber) {
            sn.removeUserById(integer);
        }
        sn.setRedo(false);
    }

}

package command;

import model.SocialNetwork;

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
        for (Integer integer : userNumber) {
            sn.removeUserById(integer);
        }

    }

}

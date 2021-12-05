package command;

import model.SocialNetwork;
import model.User;

import java.util.ArrayList;
import java.util.List;
/**
 * Class responsible for the logic to undo the previous user action.
 */
public class CommandUndo extends CommandNetwork{

    public CommandUndo(SocialNetwork network) {
        super(network);
    }

    @Override
    public void execute() {
        if (network.getLastUsers().size() > 0) {
            List<Integer> user = new ArrayList<>();
            List<User> list = network.getLastUsers().peek();
            if (list.size() > 1) {
                for (User i : list) {
                    user.add(i.getId());
                    network.removeUserById(i.getId());
                }
                network.getLastUsers().pop();
                network.getLastUsersHistory().push(list);
                network.setUndo(true);

            } else if(list.size() == 1) {
                for (User i : list) {
                    user.add(i.getId());
                    network.removeUserById(i.getId());
                }
                network.getLastUsers().pop();
                network.getLastUsersHistory().push(list);
                network.setUndo(true);
                network.setRedo(false);
            }
        }
    }

    @Override
    public void unExecute() {

    }
}

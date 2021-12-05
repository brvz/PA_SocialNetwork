package command;

import javafx.scene.Node;
import model.Interest;
import model.User;
import smartgraph.view.graphview.SmartGraphVertex;
import smartgraph.view.graphview.SmartGraphVertexNode;
import view.SocialNetworkUI;

/**
 * This classes makes the action when a client wants to filter the Social Network by an interest.
 */
public class CommandFilterInterest extends CommandNetwork{
    private SocialNetworkUI view;

    public CommandFilterInterest(SocialNetworkUI view) {
        super(view.getSocialNetwork());
        this.view = view;
    }

    @Override
    public void execute() {
        for(Node node: view.graphContainer.getChildren()) {
            if (node instanceof SmartGraphVertexNode) {
                SmartGraphVertex n = (SmartGraphVertex) node;
                User u = (User) n.getUnderlyingVertex().element();
                if(u.getInterestList().size() > 0){
                    boolean filterCheck = false;
                    for (Interest in : u.getInterestList()) {
                        if (in.getName().equals(view.interestFilter.getSelectionModel().getSelectedItem())) {
                            filterCheck = false;
                            break;
                        }else {
                            filterCheck = true;
                        }
                    }
                    if(filterCheck) {
                        network.removeUser(u);
                    }
                }else {
                    network.removeUser(u);
                }

            }
        }
    }

    @Override
    public void unExecute() {

    }
}

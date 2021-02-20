package command;

import javafx.scene.Node;
import model.Interest;
import model.SocialNetwork;
import model.User;
import smartgraph.view.graphview.SmartGraphVertex;
import smartgraph.view.graphview.SmartGraphVertexNode;
import view.SocialNetworkUI;

/**
 * This classes makes the action when a client wants to filter the Social Network by an interest.
 */
public class CommandFilterInterest  extends CommandSocialNetwork{
    private SocialNetworkUI ui;

    public CommandFilterInterest(SocialNetworkUI ui) {
        super(ui.getSocialNetwork());
        this.ui = ui;
    }

    @Override
    public void execute() {
        //ui.interestFilter.getSelectionModel().getSelectedItem();
        for(Node node: ui.graphView.getChildren()) {

            if (node instanceof SmartGraphVertexNode) {

                SmartGraphVertex n = (SmartGraphVertex) node;
                User u = (User) n.getUnderlyingVertex().element();
                if(u.getInterestList().size() > 0){
                    boolean temp = false;
                    for (Interest in : u.getInterestList()) {
                        if (in.getHashtag().equals(ui.interestFilter.getSelectionModel().getSelectedItem())) {
                            temp = false;
                            break;
                        }else{
                            temp = true;
                        }
                    }
                    if(temp == true){
                        sn.removeUser(u);
                    }
                }else{
                    //ui.getSocialNetwork().removeUser(u);
                    sn.removeUser(u);
                    //((SmartGraphVertexNode<?>) node).setStyleClass("VertexFilteredHide");
                }

            }
        }
    }

    @Override
    public void unExecute() {

    }
}

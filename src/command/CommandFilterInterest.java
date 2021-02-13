package command;

import javafx.scene.Node;
import model.Interest;
import model.SocialNetwork;
import model.User;
import smartgraph.view.graphview.SmartGraphVertex;
import smartgraph.view.graphview.SmartGraphVertexNode;
import view.SocialNetworkUI;

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
                    for (Interest in : u.getInterestList()) {
                        if (!in.getHashtag().equals(ui.interestFilter.getSelectionModel().getSelectedItem())) {
                           // ui.getSocialNetwork().removeUser(((User) n.getUnderlyingVertex().element()));
                            //((SmartGraphVertexNode<?>) node).setStyleClass("VertexFilteredHide");
                            //((SmartGraphVertexNode<?>) node).setStyleClass("VertexFiltered");
                            ui.updateGraph();
                        }
                    }
                }else{
                    //ui.getSocialNetwork().removeUser(u);
                    ((SmartGraphVertexNode<?>) node).setStyleClass("VertexFilteredHide");
                }

            }
        }
        ui.updateGraph();
    }

    @Override
    public void unExecute() {

    }
}

package command;

import javafx.scene.Node;
import model.Relationship;
import model.User;
import smartgraph.view.graphview.SmartGraphEdge;
import smartgraph.view.graphview.SmartGraphVertex;
import smartgraph.view.graphview.SmartGraphVertexNode;
import view.SocialNetworkUI;

import java.time.format.DateTimeFormatter;

public class CommandFilterDate extends CommandSocialNetwork{

    private SocialNetworkUI ui;

    public CommandFilterDate(SocialNetworkUI ui) {
        super(ui.getSocialNetwork());
        this.ui = ui;
    }

    @Override
    public void execute() {
        for(Node node: ui.graphView.getChildren()) {

            if (node instanceof SmartGraphVertexNode) {

                SmartGraphVertex n = (SmartGraphVertex) node;
                User u = (User) n.getUnderlyingVertex().element();
                String date = ui.d.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                if (!u.getDate().equals(date)) {
                    //((SmartGraphVertexNode<?>) node).setStyleClass("VertexFiltered");
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

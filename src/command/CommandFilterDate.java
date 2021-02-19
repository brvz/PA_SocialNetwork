package command;

import javafx.scene.Node;
import model.User;
import smartgraph.view.graphview.SmartGraphVertex;
import smartgraph.view.graphview.SmartGraphVertexNode;
import view.SocialNetworkUI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
                String date = ui.dateFilter.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                try {
                    Date dateFilter = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                    Date dateUser = new SimpleDateFormat("yyyy-MM-dd").parse(u.getDate());
                    if (dateUser.after(dateFilter)) {
                        //((SmartGraphVertexNode<?>) node).setStyleClass("VertexFiltered");
                        sn.removeUser(u);
                        //((SmartGraphVertexNode<?>) node).setStyleClass("VertexFilteredHide");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    @Override
    public void unExecute() {

    }
}

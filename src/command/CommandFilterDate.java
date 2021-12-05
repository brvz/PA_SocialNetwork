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

/**
 * Class responsible for the logic of a filter by interest name, requested by a user.
 */
public class CommandFilterDate extends CommandNetwork{

    private SocialNetworkUI view;

    public CommandFilterDate(SocialNetworkUI view) {
        super(view.getSocialNetwork());
        this.view = view;
    }

    @Override
    public void execute() {
        for(Node node: view.graphContainer.getChildren()) {

            if (node instanceof SmartGraphVertexNode) {

                SmartGraphVertex n = (SmartGraphVertex) node;
                User u = (User) n.getUnderlyingVertex().element();
                String date = view.dateFilter.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                try {
                    Date dateFilter = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                    Date dateUser = new SimpleDateFormat("yyyy-MM-dd").parse(u.getJoinDate());
                    if (dateUser.after(dateFilter)) {
                        network.removeUser(u);
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

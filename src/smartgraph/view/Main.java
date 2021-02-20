package smartgraph.view;

import com.pa.proj2020.adts.graph.Edge;
import com.pa.proj2020.adts.graph.GraphAdjacencyList;
import com.pa.proj2020.adts.graph.Vertex;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Relationship;
import model.SocialNetwork;
import model.User;
import view.SocialNetworkUI;

public class Main extends Application {
    public static void main(String[] args) {
       launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SocialNetwork sn = new SocialNetwork("Social Network");
        new SocialNetworkUI(sn);

    }
}
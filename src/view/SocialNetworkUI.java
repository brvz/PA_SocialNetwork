package view;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Relationship;
import model.SocialNetwork;
import model.User;
import smartgraph.view.containers.ContentZoomPane;
import smartgraph.view.containers.SmartGraphDemoContainer;
import smartgraph.view.graphview.*;

import java.util.ArrayList;
import java.util.List;

public class SocialNetworkUI extends BorderPane {

    Button addUser;
    Button addIndirectRels;
    SmartGraphPanel<User, Relationship> graphView;
    HBox bottom;
    TextField tf;
    SocialNetwork sn;
    private List<SmartGraphVertex> selected;


    public SocialNetworkUI(SocialNetwork sn) {
        this.sn = sn;
        selected = new ArrayList<>();
        initialize();
    }

    public void initialize() {

        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        graphView = new SmartGraphPanel<>(sn.getSn(), strategy);

        setCenter(new ContentZoomPane(graphView));

        //create bottom pane with controls
        bottom = new HBox(10);

        CheckBox automatic = new CheckBox("Automatic layout");
        addUser = new Button("ADD USER");
        addIndirectRels = new Button("ADD INDIRECT");

        tf = new TextField();

        automatic.selectedProperty().bindBidirectional(graphView.automaticLayoutProperty());

        bottom.getChildren().addAll(automatic, tf, addUser, addIndirectRels);

        setBottom(bottom);

        setTriggers();
        showScene();
    }

    public void setTriggers(){
        addUser.setOnAction(a -> {
            int id = Integer.parseInt(tf.getText().trim());
            if (id > 0 && id < 51) {
                sn.readCSVRelationshipsByUser(id);
                updateGraph();
                graphView.setStyle(null);
                setColors();
                tf.clear();
                updateGraph();
            }
        });

        graphView.setEdgeDoubleClickAction(graphEdge -> {
            updateGraph();
        });
        graphView.setVertexDoubleClickAction(graphVertex -> {
            setSelected(graphVertex);
        });
    }

    public void showScene(){
        Scene scene = new Scene(this, 1024, 768);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("SmartGraph Visualization");
        stage.setMinHeight(500);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.show();
        graphView.init();
    }

    public void setSelected(SmartGraphVertex graphVertex){
        if(selected.size()<2){
            selected.add(graphVertex);
            graphVertex.setStyle("-fx-stroke: pink; -fx-stroke-width: 2;");
        }
        else{
            User u = (User) selected.get(0).getUnderlyingVertex().element();
            if(u.getType()== User.UserType.INCLUDED)
                selected.get(0).setStyle("-fx-stroke: green; -fx-fill: lightgreen;");
            else if(u.getType()== User.UserType.ADDED)
                selected.get(0).setStyle("-fx-stroke: blue; -fx-fill: lightblue;");
            selected.set(0, selected.get(1));
            selected.set(1, graphVertex);
            graphVertex.setStyle("-fx-stroke: pink; -fx-stroke-width: 2;");
        }

        graphView.update();
    }

    public int getSelectedSize(){
        return selected.size();
    }

    public void updateGraph(){
        graphView.update();
    }

    public void clearSelected(){
        selected.clear();
    }

    public void setNodeBlack(Node node){
        node.setStyle("-fx-stroke: black; -fx-fill: grey;");
        graphView.update();
    }

    public List<Node> getGraphNodes(){
        return graphView.getChildren();
    }

    public void setColors(){
        for(Node node: graphView.getChildren()) {
            if(node instanceof SmartGraphVertexNode) {
                SmartGraphVertex n = (SmartGraphVertex) node;
                User u = (User) n.getUnderlyingVertex().element();
                if(u.getType()== User.UserType.INCLUDED)
                    node.setStyle("-fx-stroke: green; -fx-fill: lightgreen;");
                else if(u.getType()== User.UserType.ADDED)
                    node.setStyle("-fx-stroke: blue; -fx-fill: lightblue;");
            }
        }

        for(Node node: graphView.getChildren()) {
            if(node instanceof SmartGraphEdge) {
                SmartGraphEdge n = (SmartGraphEdge) node;
                Relationship r = (Relationship) n.getUnderlyingEdge().element();
                if (r.getType() == Relationship.NameOfRelationship.SIMPLE) {
                    node.setStyle("-fx-stroke: black;");
                }
                else if (r.getType() == Relationship.NameOfRelationship.SHARED_INTEREST) {
                    node.setStyle("-fx-stroke: red;");
                }
            }
        }
    }

}

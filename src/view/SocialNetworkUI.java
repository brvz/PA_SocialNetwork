package view;

import com.pa.proj2020.adts.graph.GraphAdjacencyList;
import command.CommandManager;
import command.CommandRedo;
import command.CommandUser;
import command.CommandUserBatch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Interest;
import model.Relationship;
import model.SocialNetwork;
import model.User;
import observer.Observer;
import smartgraph.view.containers.ContentZoomPane;
import smartgraph.view.graphview.*;

import java.util.ArrayList;
import java.util.List;

public class SocialNetworkUI extends BorderPane implements Observer {

    Button addUser;
    Button undo;
    Button clear;
    Button redo;
    public ObservableList<String> listInterestToFilter;
    public ComboBox interestFilter;
    DatePicker d = new DatePicker();
    public SmartGraphPanel<User, Relationship> graphView;
    HBox bottom;
    TextField tf;
    SocialNetwork sn;
    CommandManager manager;
    private List<SmartGraphVertex> selected;


    public SocialNetworkUI(SocialNetwork sn) {
        this.sn = sn;
        manager = new CommandManager();
        selected = new ArrayList<>();
        initialize();
        sn.addObservers(this);
    }

    public void initialize() {

        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        graphView = new SmartGraphPanel<>(sn.getSn(), strategy);

        setCenter(new ContentZoomPane(graphView));
        graphView.setAutomaticLayout(true);
        //create bottom pane with controls
        bottom = new HBox(10);

       //CheckBox automatic = new CheckBox("Automatic layout");
        addUser = new Button("ADD USER");
        undo = new Button("UNDO");
        redo = new Button("REDO");
        clear = new Button("CLEAR");
        listInterestToFilter = FXCollections.observableList(sn.getNameOfAllInterests());
        interestFilter = new ComboBox(listInterestToFilter);

        tf = new TextField();

        //automatic.selectedProperty().bindBidirectional(graphView.automaticLayoutProperty());

        bottom.getChildren().addAll(tf, addUser, undo, clear, redo, interestFilter,  d);

        setBottom(bottom);

        setTriggers();
        showScene();
        updateGraph();
    }

    public void setTriggers(){
        addUser.setOnAction(a -> {
            String id = tf.getText();
            int temp = 0;
            List<Integer> arr = new ArrayList<>();
            if(id.length() == 1) {
                arr.add(Integer.parseInt(String.valueOf(id.charAt(0))));
            }
            for(int i = 0; i < id.length(); i++){
              for(int j = i+1; j < id.length()  ; j++){
                  if (!String.valueOf(id.charAt(i)).equals(";")) {
                  int e = Integer.parseInt(String.valueOf(id.charAt(i)));
                  if(String.valueOf(id.charAt(j)).equals(";") ){
                      if(e !=temp){
                          arr.add(e);
                          break;
                      }
                  } else{
                          if(e != temp){
                              temp = Integer.parseInt(String.valueOf(id.charAt(j)));
                              arr.add(Integer.parseInt(String.valueOf(id.charAt(i)).concat(String.valueOf(id.charAt(j)))));
                              break;
                          }

                          }
                  if(String.valueOf(id.charAt(i)).equals(e)){
                      arr.add(e);
                  }
                    }

                  }
              }
                if(arr.size() > 1){

                    setColors();
                    manager.executeCommand(new CommandUserBatch(sn, arr));
                    updateGraph();
                    //sn.readCSVRelationshipsByUser(id);
                    graphView.setStyle(null);
                    setColors();
                    tf.clear();
                    updateGraph();


                }else if(arr.size() == 1){
                    if (arr.get(0) > 0 && arr.get(0) < 51) {
                        setColors();
                        manager.executeCommand(new CommandUser(sn, arr.get(0)));
                        updateGraph();
                        //sn.readCSVRelationshipsByUser(id);
                        graphView.setStyle(null);
                        setColors();
                        tf.clear();
                        updateGraph();
                    }
                }
        });

        undo.setOnAction(a -> {
            manager.undo();
            updateGraph();

        });
        redo.setOnAction(a -> {
            manager.executeRedo(new CommandRedo(sn));
            updateGraph();
        });


        interestFilter.setOnAction(a -> {
        for(Node node: graphView.getChildren()) {

            if (node instanceof SmartGraphVertexNode) {

                SmartGraphVertex n = (SmartGraphVertex) node;
                User u = (User) n.getUnderlyingVertex().element();
                if(u.getInterestList().size() > 0) {
                    for (Interest in : u.getInterestList()) {
                        if (!in.getHashtag().equals(interestFilter.getSelectionModel().getSelectedItem())) {
                           //((SmartGraphVertexNode<?>) node).setStyleClass("VertexFiltered");
                            ((SmartGraphVertexNode<?>) node).setStyleClass("VertexXD");
                        }

                    }
                }else{
                    ((SmartGraphVertexNode<?>) node).setStyleClass("VertexXD");
                }
            }
        }
        updateGraph();
});




        graphView.setEdgeDoubleClickAction(graphEdge -> {
            List<Interest> listIn = new ArrayList<>();
            Relationship r = (Relationship) graphEdge.getUnderlyingEdge().element();
            //r.showInterestInComment();
            System.out.println(r.showInterestInCommon());

            Label lbl = new Label(r.showInterestInCommon());
            Pane root = new Pane(lbl);
            root.setMinSize(500,0);
            root.autosize();

            Parent content = root;

            // create scene containing the content
            Scene scene = new Scene(content);

            Stage window = new Stage();
            window.setScene(scene);
            window.setTitle("Interests in Common between " + r.getUser1().getNumber() + " & " + r.getUser2().getNumber());

            // make window visible
            window.show();
            updateGraph();
        });
        graphView.setVertexDoubleClickAction(graphVertex -> {
            List<Interest> listIn = new ArrayList<>();
            User r = (User) graphVertex.getUnderlyingVertex().element();
            //r.showInterestInComment();
            System.out.println(r.showUserToString());

            Label lbl = new Label(r.showUserToString());
            Pane root = new Pane(lbl);
            root.setMinSize(500,0);
            root.autosize();

            Parent content = root;

            // create scene containing the content
            Scene scene = new Scene(content);

            Stage window = new Stage();
            window.setScene(scene);
            window.setTitle("Info user selected: ");

            // make window visible
            window.show();
            updateGraph();
            //select vertex
            setSelected(graphVertex);
        });
        clear.setOnAction(a ->{
            clearSelected();
            updateGraph();
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
        if(selected.size()<1){
            selected.add(graphVertex);
            graphVertex.setStyle("-fx-stroke: black; -fx-fill: grey;");
        }
        if(selected.size()<2 && selected.size()>0){
            clearSelected();
            selected.add(graphVertex);
            graphVertex.setStyle("-fx-stroke: black; -fx-fill: grey;");
        }
        /*else{
            User u = (User) selected.get(0).getUnderlyingVertex().element();
            if(u.getType()== User.UserType.INCLUDED)
                selected.get(0).setStyleClass("VertexIncluded");
            else if(u.getType()== User.UserType.ADDED)
                selected.get(0).setStyleClass("VertexAdded");
            selected.set(0, selected.get(1));
            selected.set(1, graphVertex);
            graphVertex.setStyle("-fx-stroke: black; -fx-fill: grey;");
        }*/

        graphView.update();
    }

    public void updateGraph(){
        graphView.update();
    }

    public void clearSelected(){
        User u = (User) selected.get(0).getUnderlyingVertex().element();
        if(u.getType() == User.UserType.INCLUDED)
            selected.get(0).setStyleClass("VertexIncluded");
        else if(u.getType()== User.UserType.ADDED)
            selected.get(0).setStyleClass("VertexAdded");
        selected.clear();
    }

    /*public void setNodeBlack(Node node){
        node.setStyle("-fx-stroke: black; -fx-fill: grey;");
        graphView.update();
    }*/

    public List<Node> getGraphNodes(){
        return graphView.getChildren();
    }

    public void setColors(){
        for(Node node: graphView.getChildren()) {
            if(node instanceof SmartGraphVertexNode) {
                SmartGraphVertex n = (SmartGraphVertex) node;
                User u = (User) n.getUnderlyingVertex().element();
                if(u.getType()== User.UserType.INCLUDED) {
                    ((SmartGraphVertexNode<?>) node).setStyleClass("VertexIncluded");
                    updateGraph();
                }
                else if(u.getType()== User.UserType.ADDED){
                    ((SmartGraphVertexNode<?>) node).setStyleClass("VertexAdded");
                }
            }
        }

        for(Node node: graphView.getChildren()) {
            if(node instanceof SmartGraphEdge) {

                SmartGraphEdge n = (SmartGraphEdge) node;
                Relationship r = (Relationship) n.getUnderlyingEdge().element();
                if (r.getType() == Relationship.NameOfRelationship.SIMPLE) {
                    ((SmartGraphEdge<?, ?>) node).setStyleClass("edgeSimple");
                    updateGraph();
                }
                else if (r.getType() == Relationship.NameOfRelationship.SHARED_INTEREST) {
                    ((SmartGraphEdge<?, ?>) node).setStyleClass("edgeShared");
                    updateGraph();
                }
            }
        }
    }

    @Override
    public void update(Object obj) {
        System.out.println(manager);
    }

    public SocialNetwork getSocialNetwork(){
        return sn;
    }
}

package view;

import command.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Interest;
import model.Relationship;
import model.SocialNetwork;
import model.User;
import observer.Observer;
import smartgraph.view.containers.ContentZoomPane;
import smartgraph.view.graphview.*;
import view.template.*;

import java.util.*;

public class SocialNetworkUI extends BorderPane implements Observer {

    public Button addUserBtn;
    public Button undoBtn;
    public Button clearBtn;
    public Button redoBtn;
    public Button statistics;
    public Button chartBtn;
    public Button shortestPathBtn;
    public ObservableList<String> listInterestToFilter;
    public ComboBox interestFilter;
    public ComboBox interestBFS;
    public DatePicker dateFilter;
    public SmartGraphPanel<User, Relationship> graphView;
    public HBox addUserHBox;
    public HBox commandHBox;
    public VBox left;
    /*-------------------------------*/
    // TODO: right side of panel
    public VBox right;
    public VBox userInfo;
    public VBox addedUsersStats;
    public VBox userWithMostRelStats;
    /*-------------------------------*/
    public TextField addUserTextField;
    public Label shortestPathLabel;
    public Label interestFilterLabel;
    public Label dateFilterLabel;
    public Separator separator1;
    public Separator separator2;
    public Separator separator3;
    public SocialNetwork sn;
    public CommandManager manager;
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
        left = new VBox(10);
        left.setTranslateY(20);
        left.setPrefWidth(300);
        left.setPrefHeight(this.getHeight());
        separator1 = new Separator();
        separator2 = new Separator();
        separator3 = new Separator();

        //CheckBox automatic = new CheckBox("Automatic layout");

        // add user
        addUserHBox = new HBox(10);
        addUserHBox.setPadding(new Insets(5, 5, 5, 20));
        addUserTextField = new TextField();
        addUserTextField.setPrefWidth(180);
        addUserBtn = new Button("Add User");
        addUserBtn.setPadding(new Insets(5, 5, 5, 5));
        addUserHBox.getChildren().addAll(addUserTextField, addUserBtn);

        // filters
        listInterestToFilter = FXCollections.observableList(sn.getNameOfAllInterests());
        interestFilterLabel = new Label("Filter by interest");
        interestFilterLabel.setPadding(new Insets(5, 5, 5, 20));
        interestFilter = new ComboBox(listInterestToFilter);
        interestFilter.setPrefWidth(180);
        interestFilter.setTranslateX(20);
        dateFilterLabel = new Label("Filter by date");
        dateFilterLabel.setPadding(new Insets(5, 5, 5, 20));
        dateFilter = new DatePicker();
        dateFilter.setPrefWidth(180);
        dateFilter.setTranslateX(20);

        // shortest path
        shortestPathLabel = new Label("Select interest");
        shortestPathLabel.setPadding(new Insets(5, 5, 5, 20));
        interestBFS = new ComboBox(listInterestToFilter);
        interestBFS.setPrefWidth(180);
        interestBFS.setTranslateX(20);
        shortestPathBtn = new Button("Shortest PATH");
        shortestPathBtn.setPadding(new Insets(5, 5, 5, 5));
        shortestPathBtn.setTranslateX(20);

        // other btn
        commandHBox = new HBox(10);
        commandHBox.setPadding(new Insets(5, 5, 5, 20));
        clearBtn = new Button("CLEAR SELECTED");
        clearBtn.setPadding(new Insets(5, 5, 5, 5));
        undoBtn = new Button("UNDO");
        undoBtn.setPadding(new Insets(5, 5, 5, 5));
        redoBtn = new Button("REDO");
        redoBtn.setPadding(new Insets(5, 5, 5, 5));
        commandHBox.getChildren().addAll(clearBtn, undoBtn, redoBtn);

        // set vbox children
        left.getChildren().addAll(addUserHBox,
                separator1,
                interestFilterLabel, interestFilter,dateFilterLabel, dateFilter,
                separator2,
                shortestPathLabel, interestBFS, shortestPathBtn,
                separator3,
                commandHBox);

        // statistics
        statistics = new Button("STATISTICS");
        chartBtn = new Button("CHARTS");

        //automatic.selectedProperty().bindBidirectional(graphView.automaticLayoutProperty());

        //bottom.getChildren().addAll(statistics, chartBtn);

        //setBottom(bottom);
        setLeft(left);

        setTriggers();
        showScene();
        updateGraph();
    }

    public void setTriggers() {
        addUserBtn.setOnAction(a -> {
            String id = addUserTextField.getText();
            int temp = 0;
            List<Integer> arr = new ArrayList<>();
            if (id.length() == 1) {
                arr.add(Integer.parseInt(String.valueOf(id.charAt(0))));
            }
            for (int i = 0; i < id.length(); i++) {
                for (int j = i + 1; j < id.length(); j++) {
                    if (!String.valueOf(id.charAt(i)).equals(";")) {
                        int e = Integer.parseInt(String.valueOf(id.charAt(i)));
                        if (String.valueOf(id.charAt(j)).equals(";")) {
                            if (e != temp) {
                                arr.add(e);
                                break;
                            }
                        } else {
                            if (e != temp) {
                                temp = Integer.parseInt(String.valueOf(id.charAt(j)));
                                arr.add(Integer.parseInt(String.valueOf(id.charAt(i)).concat(String.valueOf(id.charAt(j)))));
                                break;
                            }

                        }
                        if (String.valueOf(id.charAt(i)).equals(e)) {
                            arr.add(e);
                        }
                    }

                }
            }
            if (arr.size() > 1) {

                setColors();
                manager.executeCommand(new CommandUserBatch(sn, arr));
                updateGraph();
                //sn.readCSVRelationshipsByUser(id);
                graphView.setStyle(null);
                setColors();
                addUserTextField.clear();
                updateGraph();


            } else if (arr.size() == 1) {
                if (arr.get(0) > 0 && arr.get(0) < 51) {
                    setColors();
                    manager.executeCommand(new CommandUser(sn, arr.get(0)));
                    updateGraph();
                    //sn.readCSVRelationshipsByUser(id);
                    graphView.setStyle(null);
                    setColors();
                    addUserTextField.clear();
                    updateGraph();
                }
            }
        });

        undoBtn.setOnAction(a -> {
            manager.executeUndo(new CommandUndo(sn));
            sn.logUndo();
            //sn.setRedo(true);
            updateGraph();

        });
        redoBtn.setOnAction(a -> {
            manager.executeRedo(new CommandRedo(sn));
            sn.logRedo();
            updateGraph();
        });


        interestFilter.setOnAction(a -> {
            manager.executeFilterInterest(new CommandFilterInterest(this));
            updateGraph();
            /*for(Node node: graphView.getChildren()) {

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
            updateGraph();*/
        });

        dateFilter.setOnAction(a -> {
            manager.executeFilterDate(new CommandFilterDate(this));
            updateGraph();
            /*for(Node node: graphView.getChildren()) {

                if (node instanceof SmartGraphVertexNode) {

                    SmartGraphVertex n = (SmartGraphVertex) node;
                    User u = (User) n.getUnderlyingVertex().element();
                    String date = d.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    if (!u.getDate().equals(date)) {
                        //((SmartGraphVertexNode<?>) node).setStyleClass("VertexFiltered");
                        ((SmartGraphVertexNode<?>) node).setStyleClass("VertexFilteredHide");
                    }
                }
            }

            for(Node node: graphView.getChildren()) {
                if(node instanceof SmartGraphEdge) {

                    SmartGraphEdge n = (SmartGraphEdge) node;
                    Relationship r = (Relationship) n.getUnderlyingEdge().element();
                    String date = d.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    if (!r.getDate().equals(date)) {
                        //((SmartGraphVertexNode<?>) node).setStyleClass("VertexFiltered");
                        ((SmartGraphEdge<?, ?>) node).setStyleClass("edgeFilteredHide");
                    }
                }
            }
            updateGraph();*/
        });
        statistics.setOnAction(a -> {
            TextTemplate textAddedUsers = new TextAddedUsers(sn);
            TextTemplate textMostRelationships = new TextMostRelationships(sn);
            textAddedUsers.setPage();
            textMostRelationships.setPage();

            updateGraph();
        });
        chartBtn.setOnAction(a -> {
            ChartsTemplate top6 = new Top6Chart(sn);
            ChartsTemplate top10With = new Top10ChartWithSharedInterests(sn);
            ChartsTemplate top10Without = new Top10ChartWithoutSharedInterests(sn);
            top6.setStage("Número de relações", "Utilizadores");
            top10With.setStage("Número de relações", "Utilizadores");
            top10Without.setStage("Número de relações", "Utilizadores");
            updateGraph();
        });
        shortestPathBtn.setOnAction(a -> {
            setColors();
            updateGraph();
            SmartGraphVertex v = getSelected().get(0);
            User u = (User) v.getUnderlyingVertex().element();
            Interest in = null;

            for (Interest i : sn.getInterestList()) {
                if (i.getHashtag().equals(interestBFS.getSelectionModel().getSelectedItem())) {
                    in = i;
                    break;
                }
            }
            List<SmartGraphVertex> bfs = BFS(getSelected().get(0), in);
            List<User> listUsers = new ArrayList<>();
            for (SmartGraphVertex listBFS : bfs) {
                listBFS.setStyleClass("VertexBFS");
                User u1 = (User) listBFS.getUnderlyingVertex().element();
                if (u1.getNumber() != u.getNumber()) {
                    listUsers.add(u1);
                }

            }
            for (User u1 : listUsers) {
                List<User> path = sn.Dijkstra(sn.getSn(), u, u1);
                User current = u;
                for (User user : path) {
                    if (user != current) {
                        Relationship relationshipBetween = sn.getRelationshipBetween(current, user).get(0);
                        //Edge<Relationship, User> relationshipUserEdge = sn.checkRelationship(relationshipBetween);
                        SmartStylableNode stylableEdge = this.graphView.getStylableEdge(relationshipBetween);
                        stylableEdge.setStyleClass("edgeBFS");
                        current = user;
                    }
                }

                /*
                for (Node node : graphView.getChildren()) {
                    if (node instanceof SmartGraphEdge) {
                        List<User> path = sn.Dijkstra(sn.getSn(), u, u1);
                        for (int i = 0; i <= path.size(); i++) {
                            for (int j = i + 1; j < path.size(); j++) {
                                for (Relationship relationship : sn.relationships()) {
                                    if (path.get(j).equals(sn.getSn().opposite(sn.checkUser(path.get(i)), sn.checkRelationship(relationship)).element())) {
                                        if (relationship.equals(node)) {
                                            ((SmartGraphEdge<?, ?>) node).setStyleClass("edgeBFS");
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                */

            }

                    /*for (Interest in : sn.getInterestList()) {
                        if (in.getHashtag().equals(interestBFS.getSelectionModel().getSelectedItem())) {
                            for (SmartGraphVertex s : bfs) {
                                if (u.getInterestList().size() > 0) {
                                    User u1 = (User) s.getUnderlyingVertex().element();
                                    boolean temp = false;
                                    for (Interest in1 : u1.getInterestList()) {
                                        if (in1.getHashtag().equals(interestBFS.getSelectionModel().getSelectedItem())) {
                                            temp = false;
                                            break;
                                        } else {
                                            temp = true;
                                        }
                                    }
                                    if (!temp) {
                                        s.setStyleClass("VertexBFS");
                                        if(u1.getNumber() != u.getNumber()){
                                            listUsers.add(u1);
                                        }
                                        for (Node node : graphView.getChildren()) {
                                            if (node instanceof SmartGraphEdge) {
                                                List<User> path = sn.Dijkstra(sn.getSn(), u, u1);
                                                for (int i = 0; i <= path.size(); i++) {
                                                    for (int j = i + 1; j < path.size(); j++) {
                                                        for (Relationship relationship : sn.relationships()) {
                                                            if(path.get(j).equals(sn.getSn().opposite(sn.checkUser(path.get(i)),sn.checkRelationship(relationship)).element())) {
                                                                if(relationship.equals(node)){
                                                                    ((SmartGraphEdge<?, ?>) node).setStyleClass("edgeBFS");
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        updateGraph();
                                    }
                                }

                            }
                        }
                    }*/
            v.setStyleClass("VertexSelected");
            updateGraph();
        });


        graphView.setEdgeDoubleClickAction(graphEdge ->

        {
            Relationship r = graphEdge.getUnderlyingEdge().element();
            PaneTemplate pT = new PaneTemplate();
            pT.initialize("" + r.showInterestInCommon(), "Interests in Common between " + r.getUser1().getNumber() + " & " + r.getUser2().getNumber());
            updateGraph();
        });
        graphView.setVertexDoubleClickAction(graphVertex ->

        {
            User r = graphVertex.getUnderlyingVertex().element();
            PaneTemplate pT = new PaneTemplate();
            pT.initialize(" " + r.showUserToString(), " Info user selected: ");
            updateGraph();
            //select vertex
            setSelected(graphVertex);
        });
        clearBtn.setOnAction(a ->

        {
            clearSelected();
            updateGraph();
        });
    }

    public void showScene() {
        Scene scene = new Scene(this, 1700, 800);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("SmartGraph Visualization");
        stage.setMinHeight(800);
        stage.setMinWidth(1700);
        stage.setScene(scene);
        stage.show();
        graphView.init();

    }

    public void setSelected(SmartGraphVertex graphVertex) {
        if (selected.size() < 1) {
            selected.add(graphVertex);
            graphVertex.setStyleClass("VertexSelected");
        }
        if (selected.size() < 2 && selected.size() > 0) {
            clearSelected();
            selected.add(graphVertex);
            graphVertex.setStyleClass("VertexSelected");
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

    public List<SmartGraphVertex> getSelected() {
        return selected;
    }

    public void updateGraph() {
        graphView.update();
    }

    public void clearSelected() {
        User u = (User) selected.get(0).getUnderlyingVertex().element();
        if (u.getType() == User.UserType.INCLUDED)
            selected.get(0).setStyleClass("VertexIncluded");
        else if (u.getType() == User.UserType.ADDED)
            selected.get(0).setStyleClass("VertexAdded");
        selected.clear();
    }

    /*public void setNodeBlack(Node node){
        node.setStyle("-fx-stroke: black; -fx-fill: grey;");
        graphView.update();
    }*/

    public List<Node> getGraphNodes() {
        return graphView.getChildren();
    }

    public void setColors() {
        for (Node node : graphView.getChildren()) {
            if (node instanceof SmartGraphVertexNode) {
                SmartGraphVertex n = (SmartGraphVertex) node;
                User u = (User) n.getUnderlyingVertex().element();
                if (u.getType() == User.UserType.INCLUDED) {
                    ((SmartGraphVertexNode<?>) node).setStyleClass("VertexIncluded");
                    updateGraph();
                } else if (u.getType() == User.UserType.ADDED) {
                    ((SmartGraphVertexNode<?>) node).setStyleClass("VertexAdded");
                }
            }
        }

        for (Node node : graphView.getChildren()) {
            if (node instanceof SmartGraphEdge) {

                SmartGraphEdge n = (SmartGraphEdge) node;
                Relationship r = (Relationship) n.getUnderlyingEdge().element();
                if (r.getType() == Relationship.NameOfRelationship.SIMPLE) {
                    ((SmartGraphEdge<?, ?>) node).setStyleClass("edgeSimple");
                    updateGraph();
                } else if (r.getType() == Relationship.NameOfRelationship.SHARED_INTEREST) {
                    ((SmartGraphEdge<?, ?>) node).setStyleClass("edgeShared");
                    updateGraph();
                }
            }
        }
    }


    public List<SmartGraphVertex> BFS(SmartGraphVertex user_root, Interest interest) {
        Queue<SmartGraphVertex> queue = new LinkedList<>();
        List<SmartGraphVertex> visited = new ArrayList<>();

        visited.add(user_root);
        queue.offer(user_root);

        while (!queue.isEmpty()) {
            SmartGraphVertex v = queue.poll();
            /* Processar vertice */
            System.out.println(v);
            for (Node w : graphView.getChildren()) {
                if (w instanceof SmartGraphVertex) {
                    SmartGraphVertex n = (SmartGraphVertex) w;
                    User u = (User) n.getUnderlyingVertex().element();
                    if (sn.areAdjacentUserType((User) v.getUnderlyingVertex().element(), u)) {
                        if (!visited.contains(w)) {
                            Boolean check = false;
                            for (Interest in : u.getInterestList()) {
                                if (in.getIdentify() == interest.getIdentify()) {
                                    check = true;
                                    break;
                                }
                            }
                            if (check) {
                                visited.add((SmartGraphVertex) w);
                                queue.offer((SmartGraphVertex) w);
                            }
                        }
                    }
                }
            }
        }
        return visited;
    }


    @Override
    public void update(Object obj) {
        System.out.println(manager);
    }

    public SocialNetwork getSocialNetwork() {
        return sn;
    }
}

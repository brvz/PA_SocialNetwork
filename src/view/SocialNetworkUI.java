package view;

import command.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Interest;
import model.Relationship;
import model.SocialNetwork;
import model.User;
import observer.Observer;
import smartgraph.view.graphview.*;
import view.template.*;

import java.util.*;

/**
 * SocialNetworkUI is a class that has the methods to transform the SocialNetwork in a User Interface that will help the
 * the client to use it and the goal is to make the entire project more realistic and fucntional.
 */

public class SocialNetworkUI extends BorderPane implements Observer {

    private Button addUserBtn;
    private Button undoBtn;
    private Button clearBtn;
    private Button redoBtn;
    private Button chartBtn;
    private Button shortestPathBtn;
    private ObservableList<String> listInterestToFilter;
    public ComboBox interestFilter;
    private ComboBox interestBFS;
    public DatePicker dateFilter;
    public SmartGraphPanel<User, Relationship> graphView;
    private HBox addUserHBox;
    private HBox commandHBox;
    private VBox left;
    /*-------------------------------*/
    // TODO: right side of panel
    private VBox right;
    private Label userInfoTitleLabel;
    private Label infoLabel;
    private VBox userInfo;
    private VBox textStats;
    private VBox addedUsersStats;
    private Label addedUsersStatsTitleLabel;
    private Label addedUsersStatsLabel;
    private VBox userWithMostRelStats;
    private Label userWithMostRelStatsTitleLabel;
    private Label userWithMostRelStatsLabel;
    /*-------------------------------*/
    private TextField addUserTextField;
    private Label shortestPathLabel;
    private Label interestFilterLabel;
    private Label dateFilterLabel;
    private Separator separator1;
    private Separator separator2;
    private Separator separator3;
    private Separator separator4;
    private Separator separator5;
    private Separator separator6;
    private Separator separator7;
    private Separator separator8;
    private Separator separator9;
    private SocialNetwork sn;
    private CommandManager manager;
    private List<SmartGraphVertex> selectedVertex;
    private List<SmartGraphEdge> selectedEdge;


    public SocialNetworkUI(SocialNetwork sn) {
        this.sn = sn;
        manager = new CommandManager();
        selectedVertex = new ArrayList<>();
        selectedEdge = new ArrayList<>();
        initialize();
        sn.addObservers(this);
    }

    public void initialize() {

        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        graphView = new SmartGraphPanel<>(sn.getSn(), strategy);

        //setCenter(new ContentZoomPane(graphView));
        setCenter(graphView);
        graphView.setAutomaticLayout(true);
        //create bottom pane with controls
        left = new VBox(10);
        left.setTranslateY(20);
        left.setPrefWidth(300);
        left.setPrefHeight(this.getHeight());
        separator1 = new Separator();
        separator2 = new Separator();
        separator3 = new Separator();
        separator4 = new Separator();
        separator5 = new Separator();
        separator6 = new Separator();
        separator7 = new Separator();
        separator8 = new Separator();
        separator9 = new Separator();

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
        interestFilterLabel.setStyle("-fx-font-weight: bold");
        interestFilterLabel.setFont(new Font("Arial", 12));
        interestFilterLabel.setPadding(new Insets(5, 5, 5, 20));
        interestFilter = new ComboBox(listInterestToFilter);
        interestFilter.setPrefWidth(180);
        interestFilter.setTranslateX(20);
        dateFilterLabel = new Label("Filter by date");
        dateFilterLabel.setStyle("-fx-font-weight: bold");
        dateFilterLabel.setFont(new Font("Arial", 12));
        dateFilterLabel.setPadding(new Insets(5, 5, 5, 20));
        dateFilter = new DatePicker();
        dateFilter.setPrefWidth(180);
        dateFilter.setTranslateX(20);

        // shortest path
        shortestPathLabel = new Label("Select interest");
        shortestPathLabel.setStyle("-fx-font-weight: bold");
        shortestPathLabel.setFont(new Font("Arial", 12));
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

        // Right
        right = new VBox(10);
        right.setPrefWidth(280);
        right.setPrefHeight(this.getHeight());

        // User Info
        userInfo = new VBox(10);
        userInfo.setMinSize(200,0);
        userInfo.autosize();
        userInfoTitleLabel = new Label("Info");
        userInfoTitleLabel.setStyle("-fx-font-weight: bold");
        userInfoTitleLabel.setFont(new Font("Arial", 12));
        userInfoTitleLabel.setPadding(new Insets(5, 5, 5, 20));
        infoLabel = new Label("");
        infoLabel.setPadding(new Insets(5, 5, 5, 20));
        userInfo.getChildren().addAll(userInfoTitleLabel, infoLabel, separator4);

        // Stats Btn
        HBox statsHBox = new HBox(10);
        statsHBox.setPadding(new Insets(5, 5, 5, 20));
        //statsBtn = new Button("STATS");
        //statsBtn.setPadding(new Insets(5, 5, 5, 5));
        chartBtn = new Button("CHARTS");
        chartBtn.setPadding(new Insets(5, 5, 5, 5));
        statsHBox.getChildren().addAll(chartBtn);

        // Stats text
        textStats = new VBox(10);
        textStats.setMinSize(200,0);
        textStats.autosize();
        //----------------------------------
        addedUsersStats = new VBox(10);
        addedUsersStats.setMinSize(200,0);
        addedUsersStats.autosize();
        addedUsersStatsTitleLabel = new Label("Added Users");
        addedUsersStatsTitleLabel.setStyle("-fx-font-weight: bold");
        addedUsersStatsTitleLabel.setFont(new Font("Arial", 12));
        addedUsersStatsTitleLabel.setPadding(new Insets(5, 5, 5, 20));
        addedUsersStatsLabel = new Label("");
        ScrollPane sp = new ScrollPane();
        sp.setContent(addedUsersStatsLabel);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        sp.setMaxSize(300, 300);
        addedUsersStatsLabel.setPadding(new Insets(5, 5, 5, 20));
        addedUsersStats.getChildren().addAll(separator5, addedUsersStatsTitleLabel, separator6, sp, separator7);
        //----------------------------------
        userWithMostRelStats = new VBox(10);
        userWithMostRelStats.setMinSize(200,0);
        userWithMostRelStats.autosize();
        userWithMostRelStatsTitleLabel = new Label("User with most relationships");
        userWithMostRelStatsTitleLabel.setStyle("-fx-font-weight: bold");
        userWithMostRelStatsTitleLabel.setFont(new Font("Arial", 12));
        userWithMostRelStatsTitleLabel.setPadding(new Insets(5, 5, 5, 20));
        userWithMostRelStatsLabel = new Label("");
        userWithMostRelStatsLabel.setPadding(new Insets(5, 5, 5, 20));
        userWithMostRelStats.getChildren().addAll(userWithMostRelStatsTitleLabel, separator8, userWithMostRelStatsLabel, separator9);
        //----------------------------------
        textStats.getChildren().addAll(addedUsersStats, userWithMostRelStats);


        // Right adds
        right.getChildren().addAll(userInfo, statsHBox, textStats);
        //automatic.selectedProperty().bindBidirectional(graphView.automaticLayoutProperty());

        //bottom.getChildren().addAll(statistics, chartBtn);

        //setBottom(bottom);
        setLeft(left);
        setRight(right);

        setTriggers();
        showScene();
        updateGraph();
    }

    /**
     * This method defines all the buttons and set on actions that will be implemented in user interface.
     */
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
       /* statsBtn.setOnAction(a -> {
            addedUsersStatsLabel.setText(addedUsersStats());
            userWithMostRelStatsLabel.setText(userWithMostRelationships());

            updateGraph();
        });*/
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
            SmartGraphVertex v = getSelectedVertex().get(0);
            User u = (User) v.getUnderlyingVertex().element();
            Interest in = null;

            for (Interest i : sn.getInterestList()) {
                if (i.getHashtag().equals(interestBFS.getSelectionModel().getSelectedItem())) {
                    in = i;
                    break;
                }
            }
            List<SmartGraphVertex> bfs = BFS(getSelectedVertex().get(0), in);
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
            userInfoTitleLabel.setText("Relationship Info");
            infoLabel.setText("Interests in Common between " + r.getUser1().getNumber() + " & " + r.getUser2().getNumber() + "\n" + r.showInterestInCommon());
            updateGraph();
            setSelectedEdge(graphEdge);
        });
        graphView.setVertexDoubleClickAction(graphVertex ->

        {
            User r = graphVertex.getUnderlyingVertex().element();
            userInfoTitleLabel.setText("User Info");
            infoLabel.setText("" + r.showUserToString());
            updateGraph();
            //select vertex
            setSelectedVertex(graphVertex);
        });
        clearBtn.setOnAction(a -> {
            if(selectedVertex.size() > 0 && getSelectedEdge().size() > 0){
                clearSelectedVertex();
                clearSelectedEdge();
                userInfoTitleLabel.setText("Info");
                infoLabel.setText("");
                updateGraph();
            }else if(selectedVertex.size() > 0 && getSelectedEdge().size() == 0){
                clearSelectedVertex();
                userInfoTitleLabel.setText("Info");
                infoLabel.setText("");
                updateGraph();
            }else if(getSelectedEdge().size() > 0 && selectedVertex.size() == 0){
                clearSelectedEdge();
                userInfoTitleLabel.setText("Info");
                infoLabel.setText("");
                updateGraph();
            }

            setColors();

        });
    }

    /**
     * Has the function of show the scene to the client.
     */
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

    /**
     * Return  a string with added users statistics.
     * @return txt -  String
     */
    public String addedUsersStats() {
        String txt = "";
        for(User u : sn.getUsers()){
            if(u.getType().equals(User.UserType.ADDED)){
                txt += "Utilizador: " + u.getNumber() + "\n -> { ";
                for(Relationship r : sn.incidentRelationships(u)){
                    if(r.getUser2().getNumber() == u.getNumber()){
                        txt += r.getUser1().getNumber() + ";";
                    }
                    if(r.getUser1().getNumber() == u.getNumber()){
                        txt += r.getUser2().getNumber() + ";";
                    }
                }
                txt += " } \n ------------------------------------------------------------------ \n";
            }
        }
        return txt;
    }

    /**
     * /**
     * Return a string of an user with most relationships.
     * @return txt -  String
     */
    public String userWithMostRelationships() {

        int count = 0;
        int idUser = 0;
        for(User u1 : sn.getUsers()){
            if(count < sn.incidentRelationships(u1).size() ){
                idUser = 0;
                idUser = u1.getNumber();
                count = 0;
                count = sn.incidentRelationships(u1).size();
            }
        }
        return "Utilizador " + idUser + " com " + count + " relacionamentos.";
    }


    /**
     * Has the function of a client can click in an user and it makes them stay selected with a different
     * and specific color.
     * @param graphVertex - SmarthGraphVertex.
     */
    public void setSelectedVertex(SmartGraphVertex graphVertex) {
        if (selectedVertex.size() < 1) {
            selectedVertex.add(graphVertex);
            graphVertex.setStyleClass("VertexSelected");
        }
        if (selectedVertex.size() < 2 && selectedVertex.size() > 0) {
            clearSelectedVertex();
            selectedVertex.add(graphVertex);
            graphVertex.setStyleClass("VertexSelected");
        }

        graphView.update();
    }

    /**
     * Has the function of a client can click in a relationship and it makes them stay selected with a different
     * and specific color.
     * @param graphEdge - SmarthGraphEdge.
     */
    public void setSelectedEdge(SmartGraphEdge graphEdge) {
        if (getSelectedEdge().size() < 1) {
            getSelectedEdge().add(graphEdge);
            graphEdge.setStyleClass("edgeSelected");
        }
        if (getSelectedEdge().size() < 2 && getSelectedEdge().size() > 0) {
            clearSelectedEdge();
            getSelectedEdge().add(graphEdge);
            graphEdge.setStyleClass("edgeSelected");
        }

        graphView.update();
    }

    /**
     * Return a selected list of vertex.
     * @return selected - List
     */
    public List<SmartGraphVertex> getSelectedVertex() {
        return selectedVertex;
    }

    /**
     * Return a selected list of edges.
     * @return selected - List
     */
    public List<SmartGraphEdge> getSelectedEdge() {
        return selectedEdge;
    }

    /**
     * Updates the graph.
     */
    public void updateGraph() {
        graphView.update();
        addedUsersStatsLabel.setText(addedUsersStats());
        userWithMostRelStatsLabel.setText(userWithMostRelationships());
    }

    /**
     * Clear the selected vertex.
     */
    public void clearSelectedVertex() {
        User u = (User) getSelectedVertex().get(0).getUnderlyingVertex().element();
        if (u.getType() == User.UserType.INCLUDED)
            getSelectedVertex().get(0).setStyleClass("VertexIncluded");
        else if (u.getType() == User.UserType.ADDED)
            getSelectedVertex().get(0).setStyleClass("VertexAdded");
        getSelectedVertex().clear();
    }

    /**
     * Clear the selected edges.
     */
    public void clearSelectedEdge() {
        Relationship r = (Relationship) getSelectedEdge().get(0).getUnderlyingEdge().element();
        if (r.getType() == Relationship.NameOfRelationship.SHARED_INTEREST)
            getSelectedEdge().get(0).setStyleClass("edgeShared");
        else if (r.getType() == Relationship.NameOfRelationship.SIMPLE)
            getSelectedEdge().get(0).setStyleClass("edgeSimple");
        getSelectedEdge().clear();
    }


    /**
     * Set graph colors in users and relationships.
     */
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

    /**
     * Uses Queue data structure for finding the shortest path.
     * @param user_root - SmartGraphVertex
     * @param interest - Interests
     * @return visited - List
     */
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

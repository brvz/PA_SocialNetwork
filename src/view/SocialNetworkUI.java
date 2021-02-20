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

public class SocialNetworkUI extends BorderPane implements Observer {

    public Button addUserBtn;
    public Button undoBtn;
    public Button clearBtn;
    public Button redoBtn;
    public Button statsBtn;
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
    public Label userInfoTitleLabel;
    public Label infoLabel;
    public VBox userInfo;
    public VBox textStats;
    public VBox addedUsersStats;
    Label addedUsersStatsTitleLabel;
    Label addedUsersStatsLabel;
    public VBox userWithMostRelStats;
    Label userWithMostRelStatsTitleLabel;
    Label userWithMostRelStatsLabel;
    /*-------------------------------*/
    public TextField addUserTextField;
    public Label shortestPathLabel;
    public Label interestFilterLabel;
    public Label dateFilterLabel;
    public Separator separator1;
    public Separator separator2;
    public Separator separator3;
    public Separator separator4;
    public Separator separator5;
    public Separator separator6;
    public Separator separator7;
    public Separator separator8;
    public Separator separator9;
    public SocialNetwork sn;
    public CommandManager manager;
    private List<SmartGraphVertex> selected;
    private List<SmartGraphEdge> selectedEdge;


    public SocialNetworkUI(SocialNetwork sn) {
        this.sn = sn;
        manager = new CommandManager();
        selected = new ArrayList<>();
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
        right.setPrefWidth(250);
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
            setSelected(graphVertex);
        });
        clearBtn.setOnAction(a -> {
            if(selected.size() > 0 && selectedEdge.size() > 0){
                clearSelected();
                clearSelectedEdge();
                userInfoTitleLabel.setText("Info");
                infoLabel.setText("");
                updateGraph();
            }else if(selected.size() > 0 && selectedEdge.size() == 0){
                clearSelected();
                userInfoTitleLabel.setText("Info");
                infoLabel.setText("");
                updateGraph();
            }else if(selectedEdge.size() > 0 && selected.size() == 0){
                clearSelectedEdge();
                userInfoTitleLabel.setText("Info");
                infoLabel.setText("");
                updateGraph();
            }

            setColors();

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

        graphView.update();
    }

    public void setSelectedEdge(SmartGraphEdge graphEdge) {
        if (selectedEdge.size() < 1) {
            selectedEdge.add(graphEdge);
            graphEdge.setStyleClass("edgeSelected");
        }
        if (selectedEdge.size() < 2 && selectedEdge.size() > 0) {
            clearSelectedEdge();
            selectedEdge.add(graphEdge);
            graphEdge.setStyleClass("edgeSelected");
        }

        graphView.update();
    }

    public List<SmartGraphVertex> getSelected() {
        return selected;
    }

    public List<SmartGraphEdge> getSelectedEdge() {
        return selectedEdge;
    }

    public void updateGraph() {
        graphView.update();
        addedUsersStatsLabel.setText(addedUsersStats());
        userWithMostRelStatsLabel.setText(userWithMostRelationships());
    }

    public void clearSelected() {
        User u = (User) selected.get(0).getUnderlyingVertex().element();
        if (u.getType() == User.UserType.INCLUDED)
            selected.get(0).setStyleClass("VertexIncluded");
        else if (u.getType() == User.UserType.ADDED)
            selected.get(0).setStyleClass("VertexAdded");
        selected.clear();
    }

    public void clearSelectedEdge() {
        Relationship r = (Relationship) selectedEdge.get(0).getUnderlyingEdge().element();
        if (r.getType() == Relationship.NameOfRelationship.SHARED_INTEREST)
            selectedEdge.get(0).setStyleClass("edgeShared");
        else if (r.getType() == Relationship.NameOfRelationship.SIMPLE)
            selectedEdge.get(0).setStyleClass("edgeSimple");
        selectedEdge.clear();
    }

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

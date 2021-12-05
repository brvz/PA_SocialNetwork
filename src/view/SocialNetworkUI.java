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
import model.Relationship;
import model.SocialNetwork;
import model.User;
import observer.Observer;
import smartgraph.view.graphview.*;

import java.util.*;

/**
 * Class responsible for the user interaction with a user interface. It is also the View component in the MVC pattern.
 */

public class SocialNetworkUI extends BorderPane implements Observer {

    //Boxes
    private HBox addUserHBox;
    private HBox commandHBox;
    private VBox left;
    private VBox right;
    public ComboBox interestFilter;
    public DatePicker dateFilter;
    private VBox userInfo;

    //Labels
    private TextField addUserTextField;
    private Label interestFilterLabel;
    private Label dateFilterLabel;
    private Label userInfoTitleLabel;
    private Label infoLabel;

    //Buttons
    private Button addUserBtn;
    private Button undoBtn;
    private Button clearBtn;

    //Variables
    private final SocialNetwork model;
    private CommandManager controller;
    private ObservableList<String> listInterestToFilter;
    public SmartGraphPanel<User, Relationship> graphContainer;
    private List<SmartGraphVertex> selectedVertex;
    private List<SmartGraphEdge> selectedEdge;


    public SocialNetworkUI(SocialNetwork model) {
        this.model = model;
        controller = new CommandManager();
        selectedVertex = new ArrayList<>();
        selectedEdge = new ArrayList<>();
        initialize();
        model.addObservers(this);
    }

    public void initialize() {

        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        graphContainer = new SmartGraphPanel<>(model.getSocialNetwork(), strategy);
        setCenter(graphContainer);
        graphContainer.setAutomaticLayout(true);
        left = new VBox(10);
        left.setTranslateY(20);
        left.setPrefWidth(300);
        left.setPrefHeight(this.getHeight());

        //Separators
        Separator separator1 = new Separator();
        Separator separator2 = new Separator();
        Separator separator3 = new Separator();

        addUserHBox = new HBox(10);
        addUserHBox.setPadding(new Insets(5, 5, 5, 20));
        addUserTextField = new TextField();
        addUserTextField.setPrefWidth(180);
        addUserBtn = new Button("Add User");
        addUserBtn.setPadding(new Insets(5, 5, 5, 5));
        addUserHBox.getChildren().addAll(addUserTextField, addUserBtn);

        listInterestToFilter = FXCollections.observableList(model.getNameOfAllInterests());
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

        commandHBox = new HBox(10);
        commandHBox.setPadding(new Insets(5, 5, 5, 20));
        clearBtn = new Button("Clear Selected");
        clearBtn.setPadding(new Insets(5, 5, 5, 5));
        undoBtn = new Button("Undo");
        undoBtn.setPadding(new Insets(5, 5, 5, 5));
        commandHBox.getChildren().addAll(clearBtn, undoBtn);

        left.getChildren().addAll(addUserHBox,
                separator1,
                interestFilterLabel, interestFilter,dateFilterLabel, dateFilter,
                separator2,
                commandHBox);

        right = new VBox(10);
        right.setPrefWidth(280);
        right.setPrefHeight(this.getHeight());

        userInfo = new VBox(10);
        userInfo.setMinSize(200,0);
        userInfo.autosize();
        userInfoTitleLabel = new Label("Info");
        userInfoTitleLabel.setStyle("-fx-font-weight: bold");
        userInfoTitleLabel.setFont(new Font("Arial", 12));
        userInfoTitleLabel.setPadding(new Insets(5, 5, 5, 20));
        infoLabel = new Label("");
        infoLabel.setPadding(new Insets(5, 5, 5, 20));
        userInfo.getChildren().addAll(userInfoTitleLabel, infoLabel);

        right.getChildren().addAll(userInfo, separator3);

        setLeft(left);
        setRight(right);

        setTriggers();
        showScene();
        updateGraph();
    }

    public void showScene() {
        Scene scene = new Scene(this, 1700, 800);
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("SmartGraph Visualization");
        stage.setMinHeight(800);
        stage.setMinWidth(1700);
        stage.setScene(scene);
        stage.show();
        graphContainer.init();
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
                controller.executeCommand(new CommandUserBatch(model, arr));
                updateGraph();
                graphContainer.setStyle(null);
                setColors();
                addUserTextField.clear();
                updateGraph();


            } else if (arr.size() == 1) {
                if (arr.get(0) > 0 && arr.get(0) < 51) {
                    setColors();
                    controller.executeCommand(new CommandUser(model, arr.get(0)));
                    updateGraph();
                    graphContainer.setStyle(null);
                    setColors();
                    addUserTextField.clear();
                    updateGraph();
                }
            }
        });

        undoBtn.setOnAction(a -> {
            controller.executeUndo(new CommandUndo(model));
            model.logUndo();
            updateGraph();

        });


        interestFilter.setOnAction(a -> {
            controller.executeFilterInterest(new CommandFilterInterest(this));
            updateGraph();
        });

        dateFilter.setOnAction(a -> {
            controller.executeFilterDate(new CommandFilterDate(this));
            updateGraph();
        });

        graphContainer.setEdgeDoubleClickAction(graphEdge -> {
            Relationship r = graphEdge.getUnderlyingEdge().element();
            userInfoTitleLabel.setText("Relationship Info");
            infoLabel.setText("Users " + r.getUser1().getId() + " & " + r.getUser2().getId() + " interests in common\n" + r.showInterestInCommon());
            updateGraph();
            setSelectedEdge(graphEdge);
        });

        graphContainer.setVertexDoubleClickAction(graphVertex -> {
            User r = graphVertex.getUnderlyingVertex().element();
            userInfoTitleLabel.setText("User Info");
            infoLabel.setText("" + r.showUserToString());
            updateGraph();
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

        graphContainer.update();
    }

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

        graphContainer.update();
    }

    public List<SmartGraphVertex> getSelectedVertex() {
        return selectedVertex;
    }

    public List<SmartGraphEdge> getSelectedEdge() {
        return selectedEdge;
    }

    public void updateGraph() {
        graphContainer.update();
    }

    public void clearSelectedVertex() {
        User u = (User) getSelectedVertex().get(0).getUnderlyingVertex().element();
        if (u.getType() == User.UserType.INCLUIDO)
            getSelectedVertex().get(0).setStyleClass("VertexIncluded");
        else if (u.getType() == User.UserType.ADICIONADO)
            getSelectedVertex().get(0).setStyleClass("VertexAdded");
        getSelectedVertex().clear();
    }

    public void clearSelectedEdge() {
        Relationship r = (Relationship) getSelectedEdge().get(0).getUnderlyingEdge().element();
        if (r.getType() == Relationship.RelationshipType.PARTILHA)
            getSelectedEdge().get(0).setStyleClass("edgeShared");
        else if (r.getType() == Relationship.RelationshipType.SIMPLES)
            getSelectedEdge().get(0).setStyleClass("edgeSimple");
        getSelectedEdge().clear();
    }

    public void setColors() {
        for (Node node : graphContainer.getChildren()) {
            if (node instanceof SmartGraphVertexNode) {
                SmartGraphVertex n = (SmartGraphVertex) node;
                User u = (User) n.getUnderlyingVertex().element();
                if (u.getType() == User.UserType.INCLUIDO) {
                    ((SmartGraphVertexNode<?>) node).setStyleClass("VertexIncluded");
                    updateGraph();
                } else if (u.getType() == User.UserType.ADICIONADO) {
                    ((SmartGraphVertexNode<?>) node).setStyleClass("VertexAdded");
                }
            }
        }

        for (Node node : graphContainer.getChildren()) {
            if (node instanceof SmartGraphEdge) {

                SmartGraphEdge n = (SmartGraphEdge) node;
                Relationship r = (Relationship) n.getUnderlyingEdge().element();
                if (r.getType() == Relationship.RelationshipType.SIMPLES) {
                    ((SmartGraphEdge<?, ?>) node).setStyleClass("edgeSimple");
                    updateGraph();
                } else if (r.getType() == Relationship.RelationshipType.PARTILHA) {
                    ((SmartGraphEdge<?, ?>) node).setStyleClass("edgeShared");
                    updateGraph();
                }
            }
        }
    }

    @Override
    public void update(Object obj) {
        System.out.println(controller);
    }

    public SocialNetwork getSocialNetwork() {
        return model;
    }
}

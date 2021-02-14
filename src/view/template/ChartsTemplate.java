package view.template;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class ChartsTemplate {
    public Label templateLabel;

    public void initialize(String text, String title){
        templateLabel = new Label("");
        templateLabel.setText(text);
        Pane root = new Pane(templateLabel);
        root.setMinSize(500,0);
        root.autosize();

        Parent content = root;

        // create scene containing the content
        Scene scene = new Scene(content);

        Stage window = new Stage();
        window.setScene(scene);
        window.setTitle(title);

        // make window visible
        window.show();
    }

    public void show(){

    }
}

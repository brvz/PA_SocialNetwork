package view.template;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import view.PaneTemplate;

public abstract class TextTemplate  {

    public Label templateLabel;

    public void setPage(){
        PaneTemplate pT = new PaneTemplate();
        pT.initialize(showText(),showTitle());
        /*TemplateLabel = new Label("");
        templateLabel.setText(showText());
        Pane root = new Pane(templateLabel);
        root.setMinSize(500,0);
        root.autosize();

        Parent content = root;

        // create scene containing the content
        Scene scene = new Scene(content);

        Stage window = new Stage();
        window.setScene(scene);
        window.setTitle(showTitle());

        // make window visible
        window.show();*/
    }

    public abstract String showTitle();
    public abstract String showText();
}

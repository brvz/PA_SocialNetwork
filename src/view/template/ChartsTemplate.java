package view.template;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public abstract class ChartsTemplate {

    public void setStage(String yName, String xName){
        Stage stage1 = new Stage();

        VBox box = new VBox();
        box.setPadding(new Insets(30));
        box.setAlignment(Pos.CENTER);

        final NumberAxis yAxis = new NumberAxis();
        final CategoryAxis xAxis = new CategoryAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis, yAxis);
        bc.setTitle(getTitle());
        xAxis.setLabel(xName);
        yAxis.setLabel(yName);

        XYChart.Series series1 = new XYChart.Series();
        series1.setName(getTitle());

        addData(series1);
        bc.getData().addAll(series1);
        box.getChildren().addAll(series1.getChart());
        bc.setLegendSide(Side.RIGHT);
        StackPane pane = new StackPane(bc);

        Scene scene1 = new Scene(pane, 1024, 800);
        stage1.setTitle(getTitle());
        stage1.setScene(scene1);
        stage1.show();

    }

    public abstract String getTitle();

    public abstract void addData(XYChart.Series series1);

}

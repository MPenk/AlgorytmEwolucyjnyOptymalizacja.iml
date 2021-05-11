package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main extends Application {

    static int count = 0;
    static int sizee = 100;
    static Text text = new Text( 30, 30, "HELLO WORLD");

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("Main.fxml"));

        StackPane mainPanel = loader.load();
        primaryStage.setTitle("Algorytm Ewolucyjny");
        Scene scene = new Scene(mainPanel, 300,300);
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(300);
        primaryStage.show();
        MainController controller = loader.getController();
        //mainPanel.getChildren().add(text);


        Informations informationsForQuadratic = new Informations(2,5,-2,2,2);
        Informations informationsForRastring = new Informations(10,3,-5.21,5.21,10,8);

        ObservableList<Informations> informationsObservableList = FXCollections.observableArrayList(informationsForQuadratic,informationsForRastring);
        controller.setFunction(informationsObservableList);
        controller.setThreads();

    }

    static public void setChartData()
    {

    }

    static public void incrementCount(int k) {
        count++;
        text.setText(Integer.toString(k));
    }



    public static void main(String[] args) {
        launch(args);
    }


}

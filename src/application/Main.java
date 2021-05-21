package application;

import application.enums.EFunctions;
import application.functions.*;
import application.views.MainController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("views/Main.fxml"));
        StackPane mainPanel = loader.load();
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/application/img/ico.png")));

        primaryStage.setTitle("Algorytmy Genetyczne");
        Scene scene = new Scene(mainPanel, 300,300);
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(400);
        primaryStage.setResizable(false);
        primaryStage.getScene().getStylesheets().add(this.getClass().getResource("views/style.css").toExternalForm());

        primaryStage.show();
        MainController controller = loader.getController();

        controller.setFunction(createFunctions());
        controller.setThreads();
        System.out.println(System.getProperty("java.version"));

    }

    private ObservableList<Function> createFunctions(){

        //Funkcja kwadratowa
        double[] min = new double[2];
        double[] max = new double[2];
        Arrays.fill(min,-2);
        Arrays.fill(max,2);
        Function FuncQuadratic = new Quadratic(2,5,min,max,2);

        //Funkcja rastringa
        min = new double[10];
        max = new double[10];
        Arrays.fill(min,-5.21);
        Arrays.fill(max,5.21);
        Function FuncRastring = new Rastring(10,3,min,max,8, 10);

        //Funkcja z ograniczeniami
        min = new double[13];
        max = new double[13];
        Arrays.fill(min,0);
        Arrays.fill(max,1);
        max[9] = 7;
        max[10] = 7;
        max[11] = 7;
        Function FuncQContinuousTaskWithConstraints = new ContinuousTaskWithConstraints(13,0,min,max,4);


        return FXCollections.observableArrayList(FuncQuadratic,FuncRastring,FuncQContinuousTaskWithConstraints);
    }

    public static void main(String[] args) {
        launch(args);
    }


}

package application.views;

import application.functions.Function;
import application.geneticAlgorithm.GAProperties;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private Button btnStart;

    @FXML
    private ChoiceBox<Function> chbxFunction;

    @FXML
    private TextField tboxRepetitions;

    @FXML
    private Spinner<Integer> spinnerThreads;

    @FXML
    private TextField tboxFrom;

    @FXML
    private TextField tboxTo;

    @FXML
    private TextField tboxStep;

    @FXML
    private TextField tboxGenerations;

    @FXML
    void btnStart_OnClicked(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("Calculations.fxml"));
        StackPane calculationPanel = loader.load();
        Stage stage = new Stage();
        stage.setMinHeight(400);
        stage.setMinWidth(600);
        stage.setTitle("Obliczenia");
        stage.setScene(new Scene(calculationPanel));
        stage.getScene().getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());
        CalculationsController controller = loader.getController();
        stage.setOnHidden(e->controller.shutdown());
        GAProperties gaProperties = new GAProperties(this.getFunction(),this.getThreads(),this.getRepetitions(),this.getGenerations(),this.getFrom(),this.getTo(),this.getStep());
        controller.initData(gaProperties);
        stage.show();
        controller.main();
    }

    public void setFunction(ObservableList<Function> list){
        for (Function functions:list) {
            this.chbxFunction.getItems().add(functions);
        }
        chbxFunction.setValue(chbxFunction.getItems().get(2));
    }
    public void setThreads(){
        int threads = Runtime.getRuntime().availableProcessors();
        SpinnerValueFactory spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,threads,threads/2,1);
        this.spinnerThreads.setValueFactory(spinnerValueFactory);
        spinnerValueFactory.setValue((threads/2)-1);
    }

    int getThreads(){
        return spinnerThreads.getValue();
    }
    Function getFunction() {
        return chbxFunction.getValue();
    }

    int getRepetitions(){
        return Integer.parseInt(tboxRepetitions.getText());
    }
    int getGenerations(){
        return Integer.parseInt(tboxGenerations.getText());
    }
    int getFrom(){
        return Integer.parseInt(tboxFrom.getText());
    }
    int getTo(){
        return Integer.parseInt(tboxTo.getText());
    }
    int getStep(){
        return Integer.parseInt(tboxStep.getText());
    }


}

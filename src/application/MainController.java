package application;

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
    private ChoiceBox<Informations> chbxFunction;

    @FXML
    private TextField tboxRepetitions;

    @FXML
    private Spinner<Integer> spinnerThreads;

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
        controller.initData(this.getFunction(),this.getRepetitions(),this.getThreads());
        stage.show();
        controller.main();
    }

    @FXML
    void initialize(){

    }

    void setFunction(ObservableList<Informations> lista){
        for (Informations information:lista) {
            this.chbxFunction.getItems().add(information);
        }
        chbxFunction.setValue(chbxFunction.getItems().get(0));
    }
    void setThreads(){
        int threads = Runtime.getRuntime().availableProcessors();
        SpinnerValueFactory spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,threads,threads/2,1);
        this.spinnerThreads.setValueFactory(spinnerValueFactory);
    }

    int getThreads(){
        return spinnerThreads.getValue();
    }
    Informations getFunction() {
        return chbxFunction.getValue();
    }

    int getRepetitions(){
        return Integer.parseInt(tboxRepetitions.getText());
    }

}

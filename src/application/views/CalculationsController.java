package application.views;

import application.filesOperations.FileOperations;
import application.geneticAlgorithm.GAProperties;
import application.geneticAlgorithm.GATask;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class CalculationsController {

    @FXML
    private StackPane actualPane;

    @FXML
    private Text txtProgress;

    @FXML
    private VBox VBoxProgress;

    @FXML
    private TabPane TabPane;

    final NumberAxis xAxisActual = new NumberAxis();
    final NumberAxis yAxisActual = new NumberAxis();
    final LineChart<Number,Number> lineChartActual = new LineChart<Number,Number>(xAxisActual,yAxisActual);
    XYChart.Series seriesActual = new XYChart.Series();

    GAProperties gaProperties;
    Thread mainThread;

    @FXML
    void initialize(){
        lineChartActual.getData().add(seriesActual);
        lineChartActual.setAnimated(false);
        actualPane.getChildren().add(lineChartActual);
        yAxisActual.setForceZeroInRange(false);
        yAxisActual.setAutoRanging(true);
        yAxisActual.setLowerBound(0);

    }

    void initData(GAProperties gaProperties){
        this.gaProperties = gaProperties;
        createProgressBars();

    }
    void createProgressBars(){
        for (int i = 0; i <  this.gaProperties.getnThreads(); i++) {
            ProgressBar progressBar = new ProgressBar();
            progressBar.setStyle("-fx-accent:#5ebe8e;");
            HBox hBox = new HBox();
            hBox.getChildren().add(new Text("Wątek " + i));
            hBox.getChildren().add(progressBar);
            VBoxProgress.getChildren().add(hBox);
        }
    }

    public void updateProgress(int thread, double progress){
       HBox hBox = (HBox) VBoxProgress.getChildren().get(thread);
       ProgressBar progressBar = (ProgressBar) hBox.getChildren().get(1);
       progressBar.setProgress(progress);
    }

    void main(){
        mainThread = new Thread(() -> {
            try {
                System.out.println("URUCHOMIONE");
                startGA();
            } catch (InterruptedException e) {
                System.out.println("COS NIE DZIALA");
                e.printStackTrace();
            }
        });
        mainThread.setDaemon(true);
        mainThread.start();
        TabPane.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()){
                    case RIGHT:
                        TabPane.getSelectionModel().selectNext();
                        break;
                    case LEFT:
                        TabPane.getSelectionModel().selectLast();
                        break;
                }
            }
        });
    }

    public void addDataToActualChart(int i,int graduationOnTheChart, ArrayList<Double[]> tab, String name)
    {
        synchronized (tab) {
            if (tab.size() == 0)
                return;
            try {
                for (int j = 0; j < tab.size(); j++) {

                    seriesActual.getData().add(new XYChart.Data(tab.get(j)[0], tab.get(j)[1]));
                    i = (tab.get(j)[0]).intValue();

                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            tab.clear();
        }
        seriesActual.setName(name);
        this.xAxisActual.setAutoRanging(false);
        this.xAxisActual.setUpperBound(i);
        this.xAxisActual.setTickUnit(i/graduationOnTheChart);
        this.xAxisActual.setLowerBound(0);
    }

    public void addDataToActualChart(int i,int graduationOnTheChart, double[] tab, String name)
    {
        for (double value: tab
             ) {
            seriesActual.getData().add(new XYChart.Data(i, value));
            i++;
        }

        seriesActual.setName(name);
        this.xAxisActual.setAutoRanging(false);
        this.xAxisActual.setUpperBound(i);
        this.xAxisActual.setTickUnit(i/graduationOnTheChart);
        this.xAxisActual.setLowerBound(0);
    }

    public void clearDataToActualChart() {
        seriesActual.getData().clear();
    }

    void addNewTab(double[] data, int number) {
        Tab newTab = new Tab();
        this.TabPane.getTabs().add(newTab);
        newTab.setText("Populacja o wielkości " + number);
        StackPane stackPane = new StackPane();
        newTab.setContent(stackPane);
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
        XYChart.Series series = new XYChart.Series();
        stackPane.getChildren().add(lineChart);
        int i;
        for (i = 0; i < gaProperties.getGenerations(); i++) {
            series.getData().add(new XYChart.Data(i, data[i]));

        }
        series.setName("Wykres dla GA o wielkości populacji " + number);
        xAxis.setAutoRanging(false);
        xAxis.setUpperBound(i);
        xAxis.setTickUnit(i/100);
        xAxis.setLowerBound(0);
        yAxis.setForceZeroInRange(false);
        yAxis.setAutoRanging(true);
        lineChart.getData().add(series);
        data = null;

    }
    void startGA() throws InterruptedException {
        long startTime = System.nanoTime();
        long lastLapTime = startTime;
        gaProperties.reloadThreadPool();
        //Platform.runLater(() ->  createProgressBars());
        //Pętla odpowiedzialna za obliczanie algorytmów genetycznego dla różnych wielkości populacji
        for (int i = (gaProperties.getFrom()/ gaProperties.getStep()); i <= (gaProperties.getTo() / gaProperties.getStep()); i++) {
            gaProperties.reloadThreadPool();
            //Zapisywanie wyników algorytmu do tablicy
            gaProperties.setPopulationSize(i* gaProperties.getStep());
            double tab[] = GATask.GAStart(gaProperties,this);

            String fileName = "Funckja_"+gaProperties.getFunction().toString()+"_o_wielkosc_populacji_"+gaProperties.getPopulationSize();
            //Tworzenie pliku
            FileOperations.createFile(fileName);
            //Zapisywanie wyników do pliku
            FileOperations.saveToFile(fileName,tab,gaProperties);

            int finalI = gaProperties.getPopulationSize();
            Platform.runLater(() ->  this.addNewTab(tab, finalI));
            //Informacja w konsoli
            Long lapTime = (System.nanoTime() - lastLapTime);
            long duration = lapTime/1000000000;
            System.out.println("==========================================");
            System.out.println("Zrobiono dla populaci o wielkości "+ gaProperties.getPopulationSize() + " w " + duration + " sekund");
            System.out.println("==========================================");
            lastLapTime = System.nanoTime();
        }
        Platform.runLater(() ->  clearDataToActualChart());


        long endTime = System.nanoTime();
        long duration = (endTime - startTime)/1000000000;
        double minutes = ((double)duration)/60;
        minutes = round(minutes,2);
        System.out.println("==========================================");
        System.out.println("KONIEC W: " + minutes + " minuty");
        System.out.println("==========================================");
        double finalMinutes = minutes;
        Platform.runLater(() ->  this.txtProgress.setText("KONIEC W: " + finalMinutes + " minuty"));
        Platform.runLater(() ->  this.txtProgress.setStyle("-fx-fill: green;"));

    }
    protected static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public void shutdown()
    {
        if(!gaProperties.threadPool.isShutdown())
            gaProperties.threadPool.shutdown();
        if(mainThread != null)
            mainThread.stop();
        System.out.println("ZAMYSKANIE");

        //System.exit(0);
    }
}

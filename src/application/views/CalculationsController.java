package application.views;

import application.filesOperations.FileOperations;
import application.geneticAlgorithm.GAProperties;
import application.geneticAlgorithm.GATask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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
        for (int i = 0; i <  this.gaProperties.getnThreads(); i++) {
            ProgressBar progressBar = new ProgressBar();
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
        Thread thread = new Thread(() -> {
            try {
                System.out.println("URUCHOMIONE");
                startGA();
            } catch (InterruptedException e) {
                System.out.println("COS NIE DZIALA");
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void addDataToActualChart(double[] tab, String name)
    {
        clearDataToActualChart();
        int i = 0;
        for (double value: tab
        ) {
            seriesActual.getData().add(new XYChart.Data(i, value));
            i++;
        }
        seriesActual.setName(name);
        this.xAxisActual.setAutoRanging(false);
        this.xAxisActual.setUpperBound(i);
        this.xAxisActual.setTickUnit(i/100);
        this.xAxisActual.setLowerBound(0);
    }

    void clearDataToActualChart() {
        if (!seriesActual.getData().isEmpty()) {
            seriesActual.getData().clear();
        }
    }

    void addNewTab(double[][] data, int number) {
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
            series.getData().add(new XYChart.Data(i, data[gaProperties.getRepetitions()][i]));

        }
        series.setName("Wykres dla ga");
        xAxis.setAutoRanging(false);
        xAxis.setUpperBound(i);
        xAxis.setTickUnit(i/100);
        xAxis.setLowerBound(0);
        yAxis.setForceZeroInRange(false);
        yAxis.setAutoRanging(true);
        lineChart.getData().add(series);

    }
    void startGA() throws InterruptedException {
        long startTime = System.nanoTime();
        long lastLapTime = startTime;
        //Pętla odpowiedzialna za obliczanie algorytmów genetycznego dla różnych wielkości populacji
        for (int i = (gaProperties.getFrom()/ gaProperties.getStep()); i <= (gaProperties.getTo() / gaProperties.getStep()); i++) {
            gaProperties.reloadThreadPool();

            //Zapisywanie wyników algorytmu do tablicy
            gaProperties.setPopulationSize(i* gaProperties.getStep());
            double tab[][] = GATask.GAStart(gaProperties,this);

            String fileName = "Funckja_"+gaProperties.getFunction().getFunction().toString()+"_o_wielkosc_populacji_"+String.valueOf(i);
            //Tworzenie pliku
            FileOperations.createFile(fileName);
            //Zapisywanie wyników do pliku
            FileOperations.saveToFile(fileName,tab,gaProperties);

            int finalI = i* gaProperties.getStep();
            Platform.runLater(() ->  this.addNewTab(tab, finalI));

            //Informacja w konsoli
            Long lapTime = (System.nanoTime() - lastLapTime);
            long duration = lapTime/1000000000;
            System.out.println("==========================================");
            System.out.println("Zrobiono dla populaci o wielkości "+ i*gaProperties.getStep() + " w " + duration + " sekund");
            System.out.println("==========================================");
            lastLapTime = System.nanoTime();
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime)/1000000000;
        double minutes = ((double)duration)/60;
        System.out.println("==========================================");
        System.out.println("KONIEC W: " + minutes + " minuty");
        System.out.println("==========================================");
        Platform.runLater(() ->  this.txtProgress.setText("KONIEC W: " + minutes + " minuty"));
        Platform.runLater(() ->  this.txtProgress.setStyle("-fx-fill: green;"));

    }

    public void shutdown()
    {
        if(!gaProperties.threadPool.isShutdown())
            gaProperties.threadPool.shutdown();
        System.exit(0);
    }
}

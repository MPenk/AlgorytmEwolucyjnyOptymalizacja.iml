package application.views;

import application.filesOperations.FileOperations;
import application.geneticAlgorithm.GAProperties;
import application.geneticAlgorithm.GATask;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
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

    @FXML
    private Label fps;

    final NumberAxis xAxisActual = new NumberAxis();
    final NumberAxis yAxisActual = new NumberAxis();
    final LineChart<Number,Number> lineChartActual = new LineChart<Number,Number>(xAxisActual,yAxisActual);
    XYChart.Series seriesActual = new XYChart.Series();

    GAProperties gaProperties;
    Thread mainThread;

    AnimationTimer frameRateMeter;
    private final long[] frameTimes = new long[10];
    private int frameTimeIndex = 0 ;
    private boolean arrayFilled = false ;
    @FXML
    void initialize(){
        lineChartActual.getData().add(seriesActual);
        lineChartActual.setAnimated(false);
        actualPane.getChildren().add(lineChartActual);
        yAxisActual.setForceZeroInRange(false);
        yAxisActual.setAutoRanging(true);
        yAxisActual.setLowerBound(0);

        frameRateMeter = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long oldFrameTime = frameTimes[frameTimeIndex] ;
                frameTimes[frameTimeIndex] = now ;
                frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length ;
                if (frameTimeIndex == 0) {
                    arrayFilled = true ;
                }
                if (arrayFilled) {
                    long elapsedNanos = now - oldFrameTime ;
                    long elapsedNanosPerFrame = elapsedNanos / frameTimes.length ;
                    double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame ;
                    fps.setStyle("-fx-text-fill: green;");
                    if(frameRate<45) fps.setStyle("-fx-text-fill: goldenrod;");
                    if(frameRate<15) fps.setStyle("-fx-text-fill: red;");
                    fps.setText(String.format("FPS: %.1f", frameRate));
                }
            }
        };
        frameRateMeter.start();
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

    /**
     * Uruchomienie algorytmu GA na osobnym wątku
     */
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
        TabPane.getScene().setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()){
                case RIGHT:
                    TabPane.getSelectionModel().selectNext();
                    break;
                case LEFT:
                    TabPane.getSelectionModel().selectLast();
                    break;
            }
        });
    }

    public void addDataToActualChart(int i,int graduationOnTheChart, ArrayList<Double[]> tab, String name) {
        if (tab.size() == 0)
            return;
        synchronized (tab) {
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

    /**
     * Wyczyszczenie wykresu z aktualnymi danymi
     */
    public void clearDataToActualChart() {
        seriesActual.getData().clear();
    }

    /**
     * Dodawanie nowego wykresu
     * @param data Dane do wyświetlenia na wykresie
     * @param number Informacja które to powtórzenie
     */
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
            series.getData().add(new XYChart.Data(i+1, data[i]));

        }
        series.setName("Wykres dla GA o wielkości populacji " + number);
        xAxis.setAutoRanging(false);
        xAxis.setUpperBound(i);
        xAxis.setTickUnit(i/100);
        xAxis.setLowerBound(1);
        yAxis.setForceZeroInRange(false);
        yAxis.setAutoRanging(true);
        lineChart.getData().add(series);
    }

    /**
     * Uruchomienie GA
     */
    void startGA() throws InterruptedException {
        long startTime = System.nanoTime();
        long lastLapTime = startTime;
        gaProperties.reloadThreadPool();

        //Pętla odpowiedzialna za obliczanie algorytmów genetycznego dla różnych wielkości populacji
        for (int i = gaProperties.getFrom(); i <= gaProperties.getTo() ; i+=gaProperties.getStep()) {
            gaProperties.reloadThreadPool();
            //Zapisywanie wyników algorytmu do tablicy
            gaProperties.setPopulationSize(i);
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

    /**
     * Zaokrąglenie liczby do określonej ilości miejsc po przecinku
     * @param value Wartość do zaokrąglenia
     * @param places Ilość miejsc po przecinku
     * @return Zaokrąglona liczba
     */
    protected static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Zatrzymanie wszystkich aktywnych wątków
     */
    public void shutdown() {
        if(!gaProperties.threadPool.isShutdown())
            gaProperties.threadPool.shutdown();
        if(mainThread != null)
            mainThread.stop();
        frameRateMeter.stop();
        System.out.println("ZAMYSKANIE");
    }
}

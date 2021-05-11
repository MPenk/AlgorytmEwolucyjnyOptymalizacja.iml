package application;

import javafx.application.Platform;
import javafx.scene.chart.XYChart;

import java.util.concurrent.Callable;

public class PopulationTask implements Callable<double[]> {

    GAProperties gaProperties;
    int j;
    CalculationsController controller;

    public PopulationTask( GAProperties gaProperties,int j,CalculationsController controller) {
        this.j = j;
        this.controller = controller;
        this.gaProperties  = gaProperties;

    }

    @Override
    public double[] call() {

        System.out.println("Uruchomiono " + j + " powtórzenie dla ga " + gaProperties.getPopulationSize());

        //Tworzenie pierwszej populacji
        Population p1 = new Population();

        //Tworzenie w populacji N losowych chromosomów
        p1.createNChromosomes(gaProperties.getPopulationSize(), gaProperties.getInformations());

        //Pętla do zapisywania danych
        double tmpTab[] = new double[gaProperties.getGenerations()];

        tmpTab[0] = p1.getTheBest();
        //System.out.println(p1.getTheBest() + " i = START ");
        //Przejście przez sizee powtórzeń
        for (int i = 1; i < gaProperties.getGenerations(); i++) {

            //wywołanie algorytmu genetycznego
            p1 = p1.ga(gaProperties.getPc(), gaProperties.getPm());

            if (i % 1 == 0) {
                double best = p1.getTheBest();
                //zapisywanie najlepszego osobnika
                tmpTab[i] = best;
            }
            int finalI = i;
            if(i%(gaProperties.getGenerations()/100) == 0)
                Platform.runLater(() ->  controller.updateProgress(j% gaProperties.getnThreads(),(Double.valueOf(finalI) / gaProperties.getGenerations())));
            //System.out.println(i + " Przeszło dla ga " + gaProperties.getPopulationSize());

        }
        System.out.println("Wykonano " + j + " powtórzenie dla ga " + gaProperties.getPopulationSize());
        XYChart.Series series = new XYChart.Series();
        series.setName(j + " powtórzenie dla ga " + gaProperties.getPopulationSize());
        if(j% gaProperties.getnThreads()==0)
            Platform.runLater(() ->  controller.addDataToActualChart(tmpTab,j + " powtórzenie dla ga " + gaProperties.getPopulationSize()));

        return tmpTab;
    }

}

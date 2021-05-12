package application.geneticAlgorithm;

import application.geneticAlgorithm.GAProperties;
import application.other.Population;
import application.views.CalculationsController;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class GATask implements Callable<double[]> {

    GAProperties gaProperties;
    int j;
    CalculationsController controller;

    public GATask(GAProperties gaProperties, int j, CalculationsController controller) {
        this.j = j;
        this.controller = controller;
        this.gaProperties  = gaProperties;

    }

    @Override
    public double[] call() {

        System.out.println("Uruchomiono " + j + " powtórzenie dla ga " + gaProperties.getPopulationSize());

        //Tworzenie pierwszej populacji
        Population population = new Population();

        //Tworzenie w populacji N losowych chromosomów
        population.createNChromosomes(gaProperties.getPopulationSize(), gaProperties.getFunction());

        //Pętla do zapisywania danych
        double tmpTab[] = new double[gaProperties.getGenerations()];

        tmpTab[0] = population.getWantedValue();
        //System.out.println(p1.getTheBest() + " i = START ");
        //Przejście przez sizee powtórzeń
        for (int i = 1; i < gaProperties.getGenerations(); i++) {

            //wywołanie algorytmu genetycznego
            population = population.ga(gaProperties.getPc(), gaProperties.getPm());

            if (i % 1 == 0) {
                //zapisywanie szukanego osobnika
                tmpTab[i] = population.getWantedValue();
            }
            int finalI = i;
            if(i%(gaProperties.getGenerations()/100) == 0)
                Platform.runLater(() ->  controller.updateProgress(j% gaProperties.getnThreads(),(Double.valueOf(finalI) / gaProperties.getGenerations())));
            //System.out.println(i + " Przeszło dla ga " + gaProperties.getPopulationSize());

        }
        System.out.println("Wykonano " + j + " powtórzenie dla ga " + gaProperties.getPopulationSize());
        XYChart.Series series = new XYChart.Series();
        series.setName(j + " powtórzenie dla ga " + gaProperties.getPopulationSize());
        if(j% gaProperties.getnThreads()==gaProperties.getnThreads()-1)
            Platform.runLater(() ->  controller.addDataToActualChart(tmpTab,(j+1) + " powtórzenie dla populacji o wielkości " + gaProperties.getPopulationSize()));

        return tmpTab;
    }

    static public double [][] GAStart(GAProperties gaProperties, CalculationsController controller) throws InterruptedException {

        List<GATask> tasks = new ArrayList<>();
        double tab[][] = new double[gaProperties.getRepetitions()+1][gaProperties.getGenerations()+1];
        int k =0;

        //Tworzenie N powtórzeń dla przedstawienia wyników w ujęciu statystycznym
        for (int j = 0; j < gaProperties.getRepetitions(); j++) {

            //Dodawanie nowego zadania (Tworzenie populacji, sizee krotne krzyżowanie jej i mutowanie)
            tasks.add(new GATask(gaProperties, j, controller));

            System.out.println("Aktywowano " + j + " powtórzenie dla ga "+ gaProperties.getPopulationSize());
        }

        //Wykonanie zadań wielowątkowo
        List<Future<double[]>> futures = gaProperties.threadPool.invokeAll(tasks);

        //Pobieranie wyników obliczeń
        for (Future<double[]> future : futures) {
            if (!future.isCancelled()) {
                try {
                    tab[k] = future.get();
                    k++;
                } catch (ExecutionException e) {

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        gaProperties.threadPool.shutdown();

        Double wanted = null;

        //Obliczanie średniej z wszyskich przejść algorytmu i wybieranie szukanego osobnika
        for (int i = 0; i < gaProperties.getGenerations(); i++) {
            double sum = 0;

            for (int j = 0; j < gaProperties.getRepetitions(); j++) {
                sum+=tab[j][i];
            }

            if(wanted==null || gaProperties.getFunction().getWanted(wanted,(sum/gaProperties.getRepetitions()))){
                wanted = sum/gaProperties.getRepetitions();
            }
            tab[gaProperties.getRepetitions()][i] = wanted;
            // System.out.println( tab[gaProperties.getRepetitions()][i] + " i = " + i);
        }

        //zwrócenie wyniku
        return tab;
    }

}

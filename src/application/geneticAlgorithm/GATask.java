package application.geneticAlgorithm;

import application.other.Population;
import application.views.CalculationsController;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class GATask implements Callable<double[]> {

    GAProperties gaProperties;
    int repetitionNumber;
    CalculationsController controller;

    public GATask(GAProperties gaProperties, int repetitionNumber, CalculationsController controller) {
        this.repetitionNumber = repetitionNumber;
        this.controller = controller;
        this.gaProperties  = gaProperties;
    }

    @Override
    public double[] call() {

        boolean isGoodThread =false;
        int threadNumber = Integer.parseInt(String.valueOf(Thread.currentThread().getName().charAt(Thread.currentThread().getName().length()-1)))-1;
        if( threadNumber== 0)
            isGoodThread=true;
        int iterationToSplit = (gaProperties.getGenerations()/ gaProperties.getGraduationOnTheChart());

        //Tworzenie pierwszej populacji
        Population population = new Population();


        //Tabela do przechowywania danych
        double allWantedValues[] = new double[gaProperties.getGenerations()];
        ArrayList<Double[]> arrayListToLiveChart = new ArrayList<>();

        if(isGoodThread)
            Platform.runLater(() ->  controller.clearDataToActualChart());

        //Tworzenie w populacji N losowych chromosomów
        population.createNChromosomes(gaProperties.getPopulationSize(), gaProperties.getFunction());

        //Dodanie pierwszej populacji do wyników
        allWantedValues[0] = population.getWantedValue();
        if(isGoodThread)
            synchronized (arrayListToLiveChart) {arrayListToLiveChart.add(new Double[]{Double.valueOf(0),allWantedValues[0]});}

        //Przejście przez gaProperties.getGenerations powtórzeń
        for (int i = 1; i < gaProperties.getGenerations(); i++) {

            //wywołanie algorytmu genetycznego
            population = population.ga(gaProperties.getPc(), gaProperties.getPm(),gaProperties);

            //zapisywanie szukanego osobnika
            allWantedValues[i] = population.getWantedValue();
            if(isGoodThread)
                synchronized (arrayListToLiveChart) {
                arrayListToLiveChart.add(new Double[]{Double.valueOf(i),allWantedValues[i]});
                }


            if(i%iterationToSplit == 0) {
                int finalI = i;
                //aktualizacja postepu
                Platform.runLater(() -> controller.updateProgress(threadNumber, (Double.valueOf(finalI) / gaProperties.getGenerations())));
                if(isGoodThread)
                    //Aktualizacja wykresu
                    Platform.runLater(() -> controller.addDataToActualChart(finalI,gaProperties.getGraduationOnTheChart(), arrayListToLiveChart, (repetitionNumber + 1) + " powtórzenie dla populacji o wielkości " + gaProperties.getPopulationSize()));
            }
        }
        return allWantedValues;
    }

    static public double [] GAStart(GAProperties gaProperties, CalculationsController controller) throws InterruptedException {

        //Utworzenie nowej listy zadań
        List<GATask> tasks = new ArrayList<>();

        double tab[][] = new double[gaProperties.getRepetitions()+1][gaProperties.getGenerations()+1];
        int k =0;

        //Tworzenie N powtórzeń dla przedstawienia wyników w ujęciu statystycznym
        for (int j = 0; j < gaProperties.getRepetitions(); j++) {
            //Dodawanie nowego zadania (Tworzenie populacji, krzyżowanie jej i mutowanie)
            tasks.add(new GATask(gaProperties, j, controller));
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
        //Zamknięcie wątków
        gaProperties.threadPool.shutdown();

        Double wanted = null;
        double newTab[] = new double[gaProperties.getGenerations()];

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
            newTab[i]=wanted;
        }

        //zwrócenie wyniku
        return newTab;
    }

}

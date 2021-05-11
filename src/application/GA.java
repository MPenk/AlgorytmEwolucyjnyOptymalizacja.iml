package application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class GA {

    static public double [][] GAStart(GAProperties gaProperties, CalculationsController controller) throws InterruptedException {

        List<PopulationTask> tasks = new ArrayList<>();
        double tab[][] = new double[gaProperties.getRepetitions()+1][gaProperties.getGenerations()+1];
        int k =0;

        //Tworzenie N powtórzeń dla przedstawienia wyników w ujęciu statystycznym
        for (int j = 0; j < gaProperties.getRepetitions(); j++) {

            //Dodawanie nowego zadania (Tworzenie populacji, sizee krotne krzyżowanie jej i mutowanie)
            tasks.add(new PopulationTask(gaProperties, j, controller));

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

        double theBest = -1000;

        //Obliczanie średniej z wszyskich 50ciu przejść algorytmu
        for (int i = 0; i < gaProperties.getGenerations(); i++) {
            double sum = 0;
            for (int j = 0; j < gaProperties.getRepetitions(); j++) {
                sum+=tab[j][i];
            }
            if(theBest<sum/gaProperties.getRepetitions()){
                theBest = sum/gaProperties.getRepetitions();
            }
            tab[gaProperties.getRepetitions()][i] = theBest;
            // System.out.println( tab[gaProperties.getRepetitions()][i] + " i = " + i);
        }

        //zwrócenie wyniku
        return tab;
    }



}

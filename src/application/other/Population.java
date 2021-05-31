package application.other;

import application.functions.Function;
import application.geneticAlgorithm.GAProperties;
import application.views.CalculationsController;

import java.util.*;

public class Population {
    /**
     * Przechowywanie populacji
     */
    public ArrayList<Chromosome> population = new ArrayList<>();

    public Population() {
    }

    /**
     * Dodawanie chromosomu do populacji
     * @param chromosome chromosom który ma zostać dodany do populacji
     */
    public void add(Chromosome chromosome) {
        this.population.add(chromosome);
    }

    /**
     * Tworzenie okreslonej ilości losowych chromosomów i dodanie ich do populacji
     * @param N ilośc chromosomów
     * @param function informacje o chromosomie
     */
    public void createNChromosomes(int N, Function function) {
        for(int i = 0; i < N; ++i)
            this.add(new Chromosome(function));

    }
    public double getWantedValue(){
        double wanted = population.get(0).decodeChromosome();
        for (int i = 1; i < population.size(); i++) {
            double value = population.get(i).decodeChromosome();
            if(population.get(i).function.getWanted(wanted,value)){
                wanted = value;
            }
        }
        return wanted;
    }

    /**
     * Zwrócenie wartości najlepszego chromosomu w populacji
     * @return wartość funkcji nalepszego chromosomui w populacji
     */
    public double getTheBest(){
        double max = population.get(0).decodeChromosome();
        for (int i = 1; i < population.size(); i++) {
            if(population.get(i).decodeChromosome()>max) {
                max = population.get(i).decodeChromosome();
            }
        }
        return max;
    }
    /**
     * Zwrócenie wartości najgorszego chromosomu w populacji
     * @return wartość funkcji najgorszego chromosomui w populacji
     */
    public double getTheWorst(){
        double min = population.get(0).decodeChromosome();
        for (int i = 1; i < population.size(); i++) {
            if(population.get(i).decodeChromosome()<min) {
                min = population.get(i).decodeChromosome();
            }
        }
        return min;
    }

    private void removeBad(int oldSize){
        //Sortowanie dla najelpszych osobników
        population.sort(population.get(0).function.chromosomeComparator);

        //Wyciągnięcie najlepszych osobników
        for (int i = oldSize; i < population.size(); i++) {
            population.remove(i--);
        }
    }


    /**
     * Uruchomienie algorytmu genetycznego dla populacji
     * @param pc Prawdopdobieństo krzyżowania
     * @param pm Prawdopodobieństwo mutacji
     * @return Nowa populacja
     */
    public Population ga(double pc, double pm, GAProperties gaProperties, CalculationsController controller) throws InterruptedException {
        //Krzyżowanie populacji
        Population p = multipointCrossingPopulation(pc, gaProperties.getNumberOfCuts());

        //Mutowanie Populacji
        p.mutatePopulation(pm);

        //Usuwanie złych osobników
        p.removeBad(population.size());

        //Sprawdzanie ograniczeń
        p.checkingLimitations();

        //Zwracanie populacji
        return p;
    }
    public void checkingLimitations() {
        for (Chromosome chromosome: population)
               chromosome.checkingLimitations();
    }

    /**
     * Mutowanie całej populacji.
     * @param pm Prawdopodobieństo mutacji pojedyńczego osobnika
     */
    public void mutatePopulation(double pm){
        Random r = new Random();

        //Przejście po całej populacji
        for (int i = 0; i < this.population.size(); i++) {
            //Jeśli wylosowana liczba jest większa niż prawdopodobieństow
            if(r.nextDouble()>pm)
                //Pomiń chromosom
                continue;
            //Mutacja chromosomu
            population.get(i).mutate();
        }
    }

    /**
     * Krzyżowanie wszyskich osobników w populacji.
     * @param pc Prawdopodobieństwo skrzyżowania pojedyńczego osobnika
     * @return Populację nowych (skrzyżowanych) i statych osobników
     */
    public Population multipointCrossingPopulation(double pc,int numberOfCuts) {
        Random r = new Random();
        int wasCrossedInt = 0;
        //tablica przechowywująca informacje czy Chromosom był już skrzyżowany i wypełnienie jej
        boolean wasCrossed[] = new boolean[this.population.size()];
        Arrays.fill(wasCrossed,false);

        //Tworzenie nowej populacji
        Population newPopulation = new Population();

        //Przejście po każdym osobniku w populacji
        for (int i = 0; i < this.population.size(); i++) {

            //Jeśli Chromosom był już krzyżowany albo wylosowana liczba jest mniejsza niż prawdopodobieństwo skrzyżowania
            if(wasCrossed[i] || r.nextDouble()>pc)
                //Przejdź do następnego sobnika
                continue;
            if(wasCrossedInt>=(population.size()-1)) continue;
            //Zmienna do szukania drugiego osobnika do skrzyżowania
            int j;

            //Szukanie osobnika zdatnego do skrzyżowania
            while(true){
                j = r.nextInt(population.size());
                if (j==i || wasCrossed[j]==true) continue;
                break;
            }
            //Dodanie do populacji dzieci dwóch skrzyżowanych osobników
            newPopulation.add( Chromosome.createChild(population.get(i), population.get(j),numberOfCuts,true));
            newPopulation.add( Chromosome.createChild(population.get(i), population.get(j),numberOfCuts,false));

            //Ustawienie osobników aby nie byli już dostepni do następnego krzyżowania
            wasCrossed[i] = true;
            wasCrossed[j] = true;
            wasCrossedInt +=2;

        }

        //Wypełnienie nowej populacji osobnikami które nie zostały skrzyżowane
        //for (int i = 0; i < this.population.size(); i++) {
        //    if (wasCrossed[i] == false) {
        //        newPopulation.add(population.get(i));
        //    }
        //}

        //Dodanie rodziców do nowej populacji
        for (Chromosome chromosome:population)
            newPopulation.add(chromosome);

        //zwrócenie nowej populacji
        return newPopulation;
    }
}

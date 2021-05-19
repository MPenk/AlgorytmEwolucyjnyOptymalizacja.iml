package application.other;

import application.functions.Function;
import application.views.CalculationsController;
import javafx.application.Platform;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
        for(int i = 0; i < N; ++i) {
            this.add(new Chromosome(function));
        }
    }

    public Chromosome getWanted(){
        Chromosome wanted = population.get(0);
        for (int i = 1; i < population.size(); i++) {
            Chromosome chromosome = population.get(i);
            if(chromosome.function.getWanted(wanted.decodeChromosome(),chromosome.decodeChromosome())){
                wanted = chromosome;
            }
        }
        return wanted;
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

    public void showOnlyFunction() {
        System.out.println("=======================");
        this.population.forEach((n) -> System.out.println(n.decodeChromosome()));
        System.out.println();
    }

    public void showOnlyBits() {
        System.out.println("=======================");
        this.population.forEach((n) -> System.out.println(n.showChromosome()));
        System.out.println();
    }

    public void showAll() {
        System.out.println("=======================");
        System.out.println(population.size());
        System.out.println();
        this.population.forEach((n) -> {
            PrintStream var10000 = System.out;
            String var10001 = n.showChromosome();
            var10000.println(var10001 + " " + n.decodeChromosome());
        });
        System.out.println();
    }

    public void showAverage() {
        System.out.println("=======================");
        System.out.println("Średnia wartość to: " + Chromosome.averageFun(this.population));
        System.out.println();
    }

    public void showLowerThanAverage() {
        System.out.println("=======================");
        System.out.println("Mniej niż średnia wartość: " + Chromosome.lowerThanAverage(this.population));
        System.out.println();
    }

    public void showHigherThanAverage() {
        System.out.println("=======================");
        System.out.println("Więcej lub tyle samo co średnia wartość: " + Chromosome.higherThanAverage(this.population));
        System.out.println();
    }

    private  void addToPoplation(Population population){
        for (Chromosome chromosom: this.population
             ) {
            population.population.add(chromosom);
        }
    }

    double tabALL[] = new double[population.size()];
    private void removeBad(int oldSize){

        this.getTab();
        population.sort(new Comparator<Chromosome>() {
            @Override
            public int compare(Chromosome first, Chromosome second)
            {
                return Double.compare(first.decodeChromosome(),second.decodeChromosome());
            }
        });
        this.getTab();
        for (int i = oldSize; i < population.size(); i++) {
            population.remove(i--);
        }
        this.getTab();

    }
    public double[] getTab(){

        double tab[] = new double[population.size()];

        for (int i = 0; i <population.size(); i++) {
            tab[i] = population.get(i).decodeChromosome();
        }
        tabALL =tab;
        return tab;
    }

    /**
     * Uruchomienie algorytmu genetycznego dla populacji
     * @param pc Prawdopdobieństo krzyżowania
     * @param pm Prawdopodobieństwo mutacji
     * @return Nowa populacja
     */
    public Population ga(double pc, double pm, CalculationsController controller) throws InterruptedException {
        //Krzyżowanie populacji

        Population p = multipointCrossingPopulation(pc);

        addToPoplation(p);

        //Mutowanie Populacji
        p.mutatePopulation(pm);
       //p.getTab();
        p.removeBad(population.size());
        //Sprawdzanie ograniczeń
        p.checkingLimitations();
        //p.getTab();

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
            //Jeśli wylosowana liczba jest mniejsza niż prawdopodobieństow
            if(r.nextDouble()>pm)
                //Pomiń chromosom
                continue;
            //W aktualne miejsce chromosomu, wstaw nowy zmutowany chromosom
            population.set(i,population.get(i).mutate());
        }
    }

    /**
     * Krzyżowanie wszyskich osobników w populacji.
     * @param pc Prawdopodobieństwo skrzyżowania pojedyńczego osobnika
     * @return Populację nowych (skrzyżowanych) i statych osobników
     */
    public Population multipointCrossingPopulation(double pc) {
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
            newPopulation.add( Chromosome.createChild(population.get(i), population.get(j),true));
            newPopulation.add( Chromosome.createChild(population.get(i), population.get(j),false));

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

        //zwrócenie nowej populacji
        return newPopulation;
    }

    /**
     * Metoda ruletkowa
     * @return Nowa populacja
     */
    public Population rouletteMethod() {
        Population newPopulation = new Population();

        try {
            for(int i = 0; i < this.population.size(); ++i) {
                newPopulation.add(Chromosome.rouletteMethod(this.population));
            }
        } catch (Exception var3) {
            System.out.println("Nie udało się stworzył nowej populacji: " + var3.getMessage());
        }

        return newPopulation;
    }
}

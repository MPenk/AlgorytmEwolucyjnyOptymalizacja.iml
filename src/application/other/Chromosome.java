package application.other;

import application.exceptions.IncorrectLimitationException;
import application.functions.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class Chromosome {

    /**
     * Geny chromosomu
     */
    Gen[] gens;

    /**
     * Długość pojedyńczego genu
     */
    int gensLenght;

    Double value = null;

    /**
     * Informacje dla chromosomu
     */
    public Function function;

    /**
     * Konsturktor chromosomu
     * @param function Informacje o chromosomie
     */
    public Chromosome(Function function) {
        this.function = function;
        this.gens = new Gen[function.getGenesNumber()];

        for(int i = 0; i < function.getGenesNumber(); ++i) {
            this.gens[i] = new Gen(function.getD(), function.getMin(i), function.getMax(i));
            this.gens[i].generateGen();
        }
        this.gensLenght = this.gens[0].genLength;
        this.checkingLimitations();
    }

    public void checkingLimitations() {
        try {
            this.function.checkingLimitations(this);
        }catch (IncorrectLimitationException exception){
            mutate();
            checkingLimitations();
        }
    }

    public Gen getGen(int i) {
        this.value = null;
        return gens[i];
    }

    /**
     * Ustawienie wybranego bitu w chromosomie na ustaloną wartość
     * @param n Miejsce w chromosomie do zmieny bitu
     * @param value Wartość dla bitu
     */
    private void setOneBit(int n, int value) {
        int whichGen = n/this.gensLenght;
        int whichBit = n%this.gensLenght;
        gens[whichGen].setBit(whichBit,value);

    }

    /**
     * Pobranie bitu z danego miejsca
     * @param n Miejsce do pobrania wartości
     * @return Wartość bitu
     */
    private int getOneBit(int n) {
        int whichGen = n/this.gensLenght;
        int whichBit = n%this.gensLenght;

        return gens[whichGen].getBit(whichBit);
    }

    /**
     * Mutacja Chromosomu
     * @return nowy - zmutowany Chromosom
     */
    public void mutate() {
        //Prawdopodobieństwo zmutowania bitu
        double pm = 0.1;

        //Tworzenie nowego chromosomu
        Random r = new Random();

        //Mutowanie chromosomu
        for(int i=0; i<this.function.getGenesNumber()*this.gensLenght; i++) {
            if(r.nextDouble()<pm) {
                changeBit(i);
            }
        }
    }

    /**
     * Zmiana danego bitu w chromosomie na przeciwny
     * @param bit Miejsce w którym znajduje się bit do zmiany
     */
    void changeBit(int bit) {
        if(getOneBit(bit) == 0)  setOneBit(bit,1);
        else setOneBit(bit,0);
        value = null;
    }

    /**
     * Skrzyżowanie dwóch chromosomów
     * @param dad Rodzic 1
     * @param mom Rodzic 2
     * @param itsFirstChild Informacja które to dziecko, czy  pierwsze czy drugie
     * @return Dziecko podanych rodziców
     */
    public static Chromosome createChild(Chromosome dad, Chromosome mom,int numberOfCuts, boolean itsFirstChild){
        //Tworzenie dziecka z wartościami jak u taty
        Chromosome child = new Chromosome(dad.function);

        //Czy aktualne bity są pobierane od taty czy od mamy
        boolean getFromDad;
        if(itsFirstChild)
            getFromDad = true;
        else
            getFromDad = false;
        //Obliczanie długości chromomosomu
        int chromosomeLength = dad.function.getGenesNumber()* dad.gensLenght;
        //Tworzenie tablicy z informacjami gdzie ciąć;
        int[] wherCut = new int[numberOfCuts];

        //Przypisywanie losowych wartości i sortowanie miejsc gdzie uciąć
        Random r = new Random();
        for (int i = 0; i < wherCut.length ; i++) {
            wherCut[i] = r.nextInt(chromosomeLength - 1) + 1;
        }
        Arrays.sort(wherCut);

        //Tworzenie zmiennej odpowiedzialnej za sprawdzanie informacji w którym miejscu cięcia jesteśmy
        int j = 0;

        for (int i = 0; i < chromosomeLength; i++) {
            if(j>=wherCut.length || i<wherCut[j] )
            {
                int value;
                //Pobieranie wartości od rodziców
                if(getFromDad)
                    value = dad.getOneBit(i);
                else
                    value = mom.getOneBit(i);

                //Przypisanie wartości dziecku
                child.setOneBit(i,value);
                continue;
            }
            // Jeśli trzeba zacząc przypisywać wartości od innego rodzica, zmiana następnego cięcia,
            // wybranie drugiego rodzica oraz zatrzymanie pętni na tym samym bicie.
            j++;
            getFromDad = !getFromDad;
            i--;
        }

        // Zwrócenie dziecka
        return child;
    }

    public String showChromosome() {
        String tmp = "";

        for(int i = 0; i < this.function.getGenesNumber(); ++i) {
            tmp = tmp + " " + this.gens[i].convertGenToString();
        }

        return tmp;
    }

    /**
     * Obliczenie wartości funkcji dla danego chromosomu
     * @return Wartosć funkcji
     */
    public double decodeChromosome() {
        if(this.value == null)
             this.value = function.decodeFunction(this);
        return this.value;
    }
}

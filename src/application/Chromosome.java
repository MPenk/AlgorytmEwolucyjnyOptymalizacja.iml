package application;

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

    /**
     * Informacje dla chromosomu
     */
    public Informations informations;

    /**
     * Konsturktor chromosomu
     * @param informations Informacje o chromosomie
     */
    public Chromosome(Informations informations) {
        this.informations = informations;
        this.gens = new Gen[informations.getGenesNumber()];

        for(int i = 0; i < informations.getGenesNumber(); ++i) {
            this.gens[i] = new Gen(informations.getD(), informations.getAi(), informations.getBi());
            this.gens[i].generateGen();
        }
        this.gensLenght = this.gens[0].genLength;
    }

    /**
     * Ustawienie wybranego bitu w chromosomie na ustaloną wartość
     * @param n Miejsce w chromosomie do zmieny bitu
     * @param value Wartość dla bitu
     */
    void setOneBit(int n, int value) {
        int whichGen = n/this.gensLenght;
        int whichBit = n%this.gensLenght;

        gens[whichGen].setBit(whichBit,value);
    }

    /**
     * Pobranie bitu z danego miejsca
     * @param n Miejsce do pobrania wartości
     * @return Wartość bitu
     */
    int getOneBit(int n) {
        int whichGen = n/this.gensLenght;
        int whichBit = n%this.gensLenght;

        return gens[whichGen].getBit(whichBit);
    }

    /**
     * Mutacja Chromosomu
     * @return nowy - zmutowany Chromosom
     */
    public Chromosome mutate() {
        //Prawdopodobieństwo zmutowania bitu
        double pm = 0.1;

        //Tworzenie nowego chromosomu
        Chromosome newChromosome = new Chromosome(this.informations);
        Random r = new Random();
        int tmp =0;

        //Mutowanie chromosomu
        for(int i=0; i<this.informations.getGenesNumber()*this.gensLenght; i++) {
            if(r.nextDouble()<pm) {
                changeBit(newChromosome,i);
                tmp++;
            }else {
                newChromosome.setOneBit(i,this.getOneBit(i));
            }
        }

        return newChromosome;
    }

    /**
     * Zmiana danego bitu w chromosomie na przeciwny
     * @param chromosome Chromosom do zmiany bitu
     * @param bit Miejsce w którym znajduje się bit do zmiany
     */
    static void changeBit(Chromosome chromosome,int bit) {
        if(chromosome.getOneBit(bit) == 0)  chromosome.setOneBit(bit,1);
        else chromosome.setOneBit(bit,0);
    }

    /**
     * Skrzyżowanie dwóch chromosomów
     * @param dad Rodzic 1
     * @param mom Rodzic 2
     * @param itsFirstChild Informacja które to dziecko, czy  pierwsze czy drugie
     * @return Dziecko podanych rodziców
     */
    public static Chromosome createChild(Chromosome dad, Chromosome mom, boolean itsFirstChild){
        //Tworzenie dziecka z wartościami jak u taty
        Chromosome child = new Chromosome(dad.informations);

        //Czy aktualne bity są pobierane od taty czy od mamy
        boolean getFromDad;
        if(itsFirstChild)
            getFromDad = true;
        else
            getFromDad = false;
        //Obliczanie długości chromomosomu
        int chromosomeLength = dad.informations.getGenesNumber()* dad.gensLenght;
        //Tworzenie tablicy z informacjami gdzie ciąć;
        int[] wherCut = new int[dad.informations.getNumberOfCuts()];

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

    String showChromosome() {
        String tmp = "";

        for(int i = 0; i < this.informations.getGenesNumber(); ++i) {
            tmp = tmp + this.gens[i].convertGenToString();
        }

        return tmp;
    }

    /**
     * Stara metoda wykorzystywana do metody ruletkowej (możliwe błędy z uwagi na brak aktualizacji)
     * @param chromosomesList Lista chromosomów
     * @return Chromosom
     * @throws Exception
     */
    public static Chromosome rouletteMethod(ArrayList<Chromosome> chromosomesList) throws Exception {
        Random r = new Random();
        double randomValue = 0.0D + 100.0D * r.nextDouble();
        double temp = 0.0D;
        double min = minValueInChromosomes(chromosomesList) + 1.0D;
        double sum = sumChromosomesValues(chromosomesList) + min * (double)chromosomesList.size();

        double sizeInThePopulation;
        for(Iterator<Chromosome> var10 = chromosomesList.iterator(); var10.hasNext(); temp += sizeInThePopulation) {
            Chromosome chromosom = (Chromosome)var10.next();
            sizeInThePopulation = (chromosom.decodeChromosome() + min) / sum * 100.0D;
            if (randomValue >= temp && randomValue <= temp + sizeInThePopulation) {
                System.out.println();
                return chromosom;
            }
        }
        throw new Exception("Nie zawiera się w przedziale");
    }

    private static double sumChromosomesValues(ArrayList<Chromosome> chromosomesList) {
        double sum = 0.0D;

        Chromosome chromosom;
        for(Iterator var3 = chromosomesList.iterator(); var3.hasNext(); sum += chromosom.decodeChromosome()) {
            chromosom = (Chromosome)var3.next();
        }

        return sum;
    }

    private static double minValueInChromosomes(ArrayList<Chromosome> chromosomesList) {
        double min = -10000.0D;
        Iterator var3 = chromosomesList.iterator();

        while(var3.hasNext()) {
            Chromosome chromosom = (Chromosome)var3.next();
            if (min < chromosom.decodeChromosome()) {
                min = chromosom.decodeChromosome();
            }
        }

        return min;
    }

    static double averageFun(ArrayList<Chromosome> chromosomesList) {
        double avg = 0.0D;

        Chromosome chromosom;
        for(Iterator var3 = chromosomesList.iterator(); var3.hasNext(); avg += chromosom.decodeChromosome()) {
            chromosom = (Chromosome)var3.next();
        }

        return avg / (double)chromosomesList.size();
    }

    static int lowerThanAverage(ArrayList<Chromosome> chromosomesList) {
        double avg = averageFun(chromosomesList);
        int n = 0;
        Iterator var4 = chromosomesList.iterator();

        while(var4.hasNext()) {
            Chromosome chromosom = (Chromosome)var4.next();
            if (avg > chromosom.decodeChromosome()) {
                ++n;
            }
        }

        return n;
    }

    static int higherThanAverage(ArrayList<Chromosome> chromosomesList) {
        double avg = averageFun(chromosomesList);
        int n = 0;
        Iterator var4 = chromosomesList.iterator();

        while(var4.hasNext()) {
            Chromosome chromosom = (Chromosome)var4.next();
            if (avg <= chromosom.decodeChromosome()) {
                ++n;
            }
        }

        return n;
    }

    /**
     * Obliczenie wartości funkcji dla danego chromosomu
     * @return Wartosć funkcji
     */
    public double decodeChromosome() {
        switch(this.informations.getFunction()) {
            case Rastring :
                return this.funcRastring();
            case Quadratic:
                return this.funcQuadratic();
            default:
                return  -100000;
        }
    }

    /**
     * Obliczenie wartości funkcji Restringa dla danego chromosomu
     * @return Wartosć funkcji
     */
    double funcRastring() {
        double sum = (double)(this.informations.getA() * this.informations.getGenesNumber());

        for(int i = 0; i < this.informations.getGenesNumber(); ++i) {
            double gen = this.gens[i].decodeGen();
            sum += gen * gen - (double)this.informations.getA() * Math.cos((2*Math.PI) * gen);
        }

        return sum;
    }

    /**
     * Obliczenie wartości funkcji kwadratowej dla danego chromosomu
     * @return Wartosć funkcji
     */
    double funcQuadratic() {
        double x1 = this.gens[0].decodeGen();
        double x2 = this.gens[1].decodeGen();
        return -Math.pow(x1, 2.0D) - Math.pow(x2, 2.0D) + 2.0D;
    }
}

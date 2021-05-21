package application.functions;

import application.enums.EFunctions;
import application.other.Chromosome;
import application.other.Population;

import java.util.Comparator;

public abstract class Function {


    EFunctions function; //Używana funkcja

    int genesNumber; //Ilość genów w chromosomie
    int d; //Wartość d, potrzebna do wyliczenia dokładności
    double min[]; //Wartość min
    double max[]; //Wartosć max
    int recommendedNumberOfCuts; //Ilosć cięć



    public Function(int genesNumber, int d, double min[], double max[], int recommendedNumberOfCuts, EFunctions eFunctions){
        this.function = eFunctions;
        this.genesNumber = genesNumber;
        this.d = d;
        this.min = min;
        this.max = max;
        this.recommendedNumberOfCuts = recommendedNumberOfCuts;
    }

    // Zwracanie czy nowa wartość jest poszukiwaną wartością
    abstract public boolean getWanted(double oldValue, double newValue);
    abstract public double getWanted(Population population);

    abstract public void checkingLimitations(Chromosome chromosome);

    abstract public double decodeFunction(Chromosome chromosome);

    public double getMin(int i) {
        return min[i];
    }

    public double getMax(int i) {
        return max[i];
    }

    public EFunctions getFunction() {
        return function;
    }

    public int getD() {
        return d;
    }

    public int getGenesNumber() {
        return genesNumber;
    }

    public int getRecommendedNumberOfCuts() {
        return recommendedNumberOfCuts;
    }

    public Comparator<Chromosome> chromosomeComparator = (first, second) -> Double.compare(second.decodeChromosome(),first.decodeChromosome());


    @Override
    public String toString() {
        return String.format(this.getFunction().toString());
    }

}

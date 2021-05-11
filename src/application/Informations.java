package application;

public class Informations {

    EFunctions function; //Używana funkcja

    int genesNumber; //Ilość genów w chromosomie
    int d; //Wartość d, potrzebna do wyliczenia dokładności
    double min[]; //Wartość min
    double max[]; //Wartosć max
    int numberOfCuts; //Ilosć cięć

    int A; //Wartosć A dla funkcji Rastringa

    /**
     * Konstruktor tworzący dane dla funkcji kwadratowej
     * @param genesNumber Ilość genów w chromosomie
     * @param d Potrzenma wartość do wyliczenia dokładności
     * @param min Wartość min
     * @param max Wartość max
     */
    Informations(int genesNumber, int d, double min[], double max[], int numberOfCuts, EFunctions eFunctions){
        this.function = eFunctions;
        this.genesNumber = genesNumber;
        this.d = d;
        this.min = min;
        this.max = max;
        this.numberOfCuts = numberOfCuts;
    }

    /**
     * Konstruktor tworzący dane dla funkcji Rastring
     * @param genesNumber Ilość genów w chromosomie
     * @param d Potrzenma wartość do wyliczenia dokładności
     * @param min Wartość min
     * @param max Wartość max
     * @param A Wartość A
     */
    Informations(int genesNumber, int d, double[] min, double[] max, int A, int numberOfCuts){
        this.function = EFunctions.Rastring;
        this.genesNumber = genesNumber;
        this.d = d;
        this.min = min;
        this.max = max;
        this.A = A;
        this.numberOfCuts = numberOfCuts;
    }

    public double getMin(int i) {
        return min[i];
    }

    public double getMax(int i) {
        return max[i];
    }

    public EFunctions getFunction() {
        return function;
    }

    public int getA() {
        return A;
    }

    public int getD() {
        return d;
    }

    public int getGenesNumber() {
        return genesNumber;
    }

    public int getNumberOfCuts() {
        return numberOfCuts;
    }

    @Override
    public String toString() {
        return String.format(this.getFunction().toString());
    }
}

package application;

public class Informations {

    EFunctions function; //Używana funkcja

    int genesNumber; //Ilość genów w chromosomie
    int d; //Wartość d, potrzebna do wyliczenia dokładności
    double ai; //Wartość a
    double bi; //Wartosć b
    int numberOfCuts; //Ilosć cięć

    int A; //Wartosć A dla funkcji Rastringa

    /**
     * Konstruktor tworzący dane dla funkcji kwadratowej
     * @param genesNumber Ilość genów w chromosomie
     * @param d Potrzenma wartość do wyliczenia dokładności
     * @param ai Wartość ai
     * @param bi Wartość bi
     */
    Informations(int genesNumber, int d, double ai, double bi, int numberOfCuts){
        this.function = EFunctions.Quadratic;
        this.genesNumber = genesNumber;
        this.d = d;
        this.ai = ai;
        this.bi = bi;
        this.numberOfCuts = numberOfCuts;
    }

    /**
     * Konstruktor tworzący dane dla funkcji Rastring
     * @param genesNumber Ilość genów w chromosomie
     * @param d Potrzenma wartość do wyliczenia dokładności
     * @param ai Wartość ai
     * @param bi Wartość bi
     * @param A Wartość A
     */
    Informations(int genesNumber, int d, double ai, double bi, int A, int numberOfCuts){
        this.function = EFunctions.Rastring;
        this.genesNumber = genesNumber;
        this.d = d;
        this.ai = ai;
        this.bi = bi;
        this.A = A;
        this.numberOfCuts = numberOfCuts;
    }

    public double getAi() {
        return ai;
    }

    public double getBi() {
        return bi;
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

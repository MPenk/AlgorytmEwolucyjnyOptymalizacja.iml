package application.functions;

import application.enums.EFunctions;
import application.other.Chromosome;
import application.other.Population;

public class Rastring extends Function {
    int A = 0;
    public Rastring(int genesNumber, int d, double[] min, double[] max, int numberOfCuts, int A) {
        super(genesNumber, d, min, max, numberOfCuts, EFunctions.Rastring);
        this.A = A;
    }

    @Override
    public boolean getWanted(double oldValue, double newValue) {
        if(oldValue<newValue)
            return true;
        return false;
    }
    @Override
    public double getWanted(Population population) {
        return population.getTheBest();
    }

    @Override
    public void checkingLimitations(Chromosome chromosome) {

    }

    @Override
    public double decodeFunction(Chromosome chromosome) {
        double sum = (double)(this.getA() * this.getGenesNumber());

        for(int i = 0; i < this.getGenesNumber(); ++i) {
            double gen = chromosome.getGen(i).decodeGen();
            sum += gen * gen - (double)this.getA() * Math.cos((2*Math.PI) * gen);
        }

        return sum;
    }

    public int getA() {
        return A;
    }
}

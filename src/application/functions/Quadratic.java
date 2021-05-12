package application.functions;

import application.enums.EFunctions;
import application.other.Chromosome;
import application.other.Population;

public class Quadratic extends Function {

    public Quadratic(int genesNumber, int d, double[] min, double[] max, int numberOfCuts) {
        super(genesNumber, d, min, max, numberOfCuts,EFunctions.Quadratic);
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
        double x1 = chromosome.getGen(0).decodeGen();
        double x2 = chromosome.getGen(1).decodeGen();
        return -Math.pow(x1, 2.0D) - Math.pow(x2, 2.0D) + 2.0D;
    }
}

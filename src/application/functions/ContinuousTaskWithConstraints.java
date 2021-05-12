package application.functions;

import application.enums.EFunctions;
import application.other.Chromosome;
import application.other.Population;

public class ContinuousTaskWithConstraints extends Function {

    public ContinuousTaskWithConstraints(int genesNumber, int d, double[] min, double[] max, int numberOfCuts) {
        super(genesNumber, d, min, max, numberOfCuts, EFunctions.ContinuousTaskWithConstraints);
    }

    @Override
    public boolean getWanted(double oldValue, double newValue) {
        if(oldValue>newValue)
            return true;
        return false;
    }

    @Override
    public double getWanted(Population population) {
        return population.getTheWorst();
    }

    @Override
    public void checkingLimitations(Chromosome chromosome) {

        //TODO Dodanie operacji zmieniajcych geny jeśli nie spełniają ograniczeń
        boolean preserveLimitations = true;
        do{
            preserveLimitations = true;

            //Ograniczenie 1
            double limitation = 0;
            limitation += (2*chromosome.getGen(0).decodeGen());
            limitation += (2*chromosome.getGen(1).decodeGen());
            limitation += chromosome.getGen(9).decodeGen();
            limitation += chromosome.getGen(10).decodeGen();
            if(limitation>10)
            {
                ///////////////////////////
                //ZMIANA JEŚLI NIESPEŁNIONE
                ///////////////////////////
                //FIXME Operacje na genach, ponieważ nie spełniają ograniczenia

                preserveLimitations = false;
                continue;
            }

            //Ograniczenie 2
            limitation = 0;
            limitation += (2*chromosome.getGen(1).decodeGen());
            limitation += (2*chromosome.getGen(2).decodeGen());
            limitation += chromosome.getGen(10).decodeGen();
            limitation += chromosome.getGen(11).decodeGen();
            if(limitation>10)
            {
                ///////////////////////////
                //ZMIANA JEŚLI NIESPEŁNIONE
                ///////////////////////////
                //FIXME Operacje na genach, ponieważ nie spełniają ograniczenia

                preserveLimitations = false;
                continue;
            }

            //Ograniczenie 3
            limitation = 0;
            limitation -= (8*chromosome.getGen(1).decodeGen());
            limitation += (chromosome.getGen(10).decodeGen());
            if(limitation>0)
            {
                ///////////////////////////
                //ZMIANA JEŚLI NIESPEŁNIONE
                ///////////////////////////
                //FIXME Operacje na genach, ponieważ nie spełniają ograniczenia

                preserveLimitations = false;
                continue;
            }

            //Ograniczenie 4
            limitation = 0;
            limitation -= (2*chromosome.getGen(3).decodeGen());
            limitation -= (chromosome.getGen(4).decodeGen());
            limitation += (chromosome.getGen(9).decodeGen());
            if(limitation>0)
            {
                ///////////////////////////
                //ZMIANA JEŚLI NIESPEŁNIONE
                ///////////////////////////
                //FIXME Operacje na genach, ponieważ nie spełniają ograniczenia

                preserveLimitations = false;
                continue;
            }

            //Ograniczenie 5
            limitation = 0;
            limitation -= (2*chromosome.getGen(3).decodeGen());
            limitation -= (chromosome.getGen(8).decodeGen());
            limitation += (chromosome.getGen(11).decodeGen());
            if(limitation>0)
            {
                ///////////////////////////
                //ZMIANA JEŚLI NIESPEŁNIONE
                ///////////////////////////
                //FIXME Operacje na genach, ponieważ nie spełniają ograniczenia

                preserveLimitations = false;
                continue;
            }


            //Ograniczenie 6
            limitation = 0;
            limitation += (2*chromosome.getGen(0).decodeGen());
            limitation += (2*chromosome.getGen(2).decodeGen());
            limitation += chromosome.getGen(9).decodeGen();
            limitation += chromosome.getGen(10).decodeGen();
            if(limitation>10)
            {
                ///////////////////////////
                //ZMIANA JEŚLI NIESPEŁNIONE
                ///////////////////////////
                //FIXME Operacje na genach, ponieważ nie spełniają ograniczenia

                preserveLimitations = false;
                continue;
            }

            //Ograniczenie 7
            limitation = 0;
            limitation -= (8*chromosome.getGen(0).decodeGen());
            limitation += (chromosome.getGen(9).decodeGen());
            if(limitation>0) {
                ///////////////////////////
                //ZMIANA JEŚLI NIESPEŁNIONE
                ///////////////////////////
                //FIXME Operacje na genach, ponieważ nie spełniają ograniczenia

                preserveLimitations = false;
                continue;
            }
            //Ograniczenie 8
            limitation = 0;
            limitation -= (8*chromosome.getGen(2).decodeGen());
            limitation += (chromosome.getGen(11).decodeGen());
            if(limitation>0)
            {
                ///////////////////////////
                //ZMIANA JEŚLI NIESPEŁNIONE
                ///////////////////////////
                //FIXME Operacje na genach, ponieważ nie spełniają ograniczenia

                preserveLimitations = false;
                continue;
            }

            //Ograniczenie 9
            limitation = 0;
            limitation -= (2*chromosome.getGen(5).decodeGen());
            limitation -= (chromosome.getGen(6).decodeGen());
            limitation += (chromosome.getGen(10).decodeGen());
            if(limitation>0)
            {
                ///////////////////////////
                //ZMIANA JEŚLI NIESPEŁNIONE
                ///////////////////////////
                //FIXME Operacje na genach, ponieważ nie spełniają ograniczenia

                preserveLimitations = false;
                continue;
            }



        }while (!preserveLimitations);

    }

    @Override
    public double decodeFunction(Chromosome chromosome) {
        double x1 = chromosome.getGen(0).decodeGen();
        double x2 = chromosome.getGen(1).decodeGen();
        double x3 = chromosome.getGen(2).decodeGen();
        double x4 = chromosome.getGen(3).decodeGen();
        double sum1 = 5*(x1+x2+x3+x4);
        double subtract1 = 0;
        for (int i = 0; i < 4; i++) {
            double gen = chromosome.getGen(i).decodeGen();
            subtract1+= (gen * gen);
        }
        subtract1*=5;
        double subtract2 = 0;
        for (int i = 4; i < 13; i++) {
            subtract2+= chromosome.getGen(i).decodeGen();
        }
        subtract2*=5;

        return sum1-subtract1*subtract2;
    }
}

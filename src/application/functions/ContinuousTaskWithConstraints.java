package application.functions;

import application.enums.EFunctions;
import application.exceptions.IncorrectLimitationException;
import application.other.Chromosome;
import application.other.Gen;
import application.other.Population;

import java.util.Random;

public class ContinuousTaskWithConstraints extends Function {

    public ContinuousTaskWithConstraints(int genesNumber, int d, double[] min, double[] max, int numberOfCuts) {
        super(genesNumber, d, min, max, numberOfCuts, EFunctions.ContinuousTaskWithConstraints);
        super.chromosomeComparator = (first, second) -> Double.compare(first.decodeChromosome(),second.decodeChromosome());
    }

    @Override
    public boolean getWanted(double oldValue, double newValue) {
        return oldValue > newValue;
    }

    @Override
    public double getWanted(Population population) {
        return population.getTheWorst();
    }

    //Ograniczenie dla 2 podanych genów
    protected boolean checkLimitation(Gen gen1, Gen gen2) {
        double limitation = 0;
        limitation -= (8 * gen1.decodeGen());
        limitation += (gen2.decodeGen());
        return limitation <= 0;
    }
    //Ograniczenie dla 3 podanychGenów
    protected boolean checkLimitation(Gen gen1, Gen gen2, Gen gen3) {
        double limitation = 0;
        limitation -= (2*gen1.decodeGen());
        limitation -= (gen2.decodeGen());
        limitation += (gen3.decodeGen());
        return limitation <= 0;
    }
    //Ograniczenie dla 4 podanychGenów
    protected boolean checkLimitation(Gen gen1, Gen gen2, Gen gen3, Gen gen4) {
        double limitation = 0;
        limitation += (2*gen1.decodeGen());
        limitation += (2*gen2.decodeGen());
        limitation += gen3.decodeGen();
        limitation += gen4.decodeGen();
        return limitation <= 10;
    }

    @Override
    public void checkingLimitations(Chromosome chromosome) throws IncorrectLimitationException {

        Random r = new Random();
        double rand;

        //Ograniczenie 3 //-8x2+x11<=0
        while (!checkLimitation(chromosome.getGen(1),chromosome.getGen(10)))
        {
            // losowanie 1:8 czy zmienić x11 na 0 czy x2 na 1
            rand = r.nextDouble();
            if(rand<0.125){
                chromosome.getGen(10).setGen(0);
                continue;
            }
            chromosome.getGen(1).setGen(1);
        }

        //Ograniczenie 7 //-8x1+x10<=0
        while (!checkLimitation(chromosome.getGen(0),chromosome.getGen(9)))
        {
            //losowanie 1:8 czy zmienić x10 na 0 czy x1 na 1
            rand = r.nextDouble();
            if(rand<0.125){
                chromosome.getGen(9).setGen(0);
                continue;
            }
            chromosome.getGen(0).setGen(1);

        }

        //Ograniczenie 8 //-8x3+x12<=0
        while (!checkLimitation(chromosome.getGen(2),chromosome.getGen(11)))
        {
            //losowanie 1:8 czy zmienić x12 na 0 czy x3 na 1
            rand = r.nextDouble();
            if(rand<0.125){
                chromosome.getGen(11).setGen(0);
                continue;
            }
            chromosome.getGen(2).setGen(1);
        }

        //Ograniczenie 4 //-2x4-x5+x10<=0
        while (!checkLimitation(chromosome.getGen(3),chromosome.getGen(4),chromosome.getGen(9)))
        {
            //jesli x10>3 to wylosuj nowe od (0;3) i sprawdź czy ok jeśli nie to losuj co zmienić
            if(chromosome.getGen(9).decodeGen()>3){
                chromosome.getGen(9).generateGenInRange(0,3);
                continue;
            }
            rand = r.nextDouble();
            if(rand<0.333){
                chromosome.getGen(3).generateGenInRange(0,1);
                continue;
            }
            if(rand<0.666){
                chromosome.getGen(4).generateGenInRange(0,1);
                continue;
            }
            chromosome.getGen(9).generateGenInRange(0,3);
        }

        //Ograniczenie 5 //-2x8-x9+x12<=0
        while (!checkLimitation(chromosome.getGen(7),chromosome.getGen(8),chromosome.getGen(11)))
        {
            //jesli x12>3 to wylosuj nowe od (0;3) i sprawdź czy ok jeśli nie to losuj co zmienić
            if(chromosome.getGen(11).decodeGen()>3){
                chromosome.getGen(11).generateGenInRange(0,3);
                continue;
            }
            rand = r.nextDouble();
            if(rand<0.333){
                chromosome.getGen(7).generateGenInRange(0,1);
                continue;
            }
            if(rand<0.666){
                chromosome.getGen(8).generateGenInRange(0,1);
                continue;
            }
            chromosome.getGen(11).generateGenInRange(0,3);
        }

        //Ograniczenie 9 //-2x6-x7+x11<=0
        while (!checkLimitation(chromosome.getGen(5),chromosome.getGen(6),chromosome.getGen(10)))
        {
            //jesli x11>3 to wylosuj nowe od (0;3) i sprawdź czy ok jeśli nie to losuj co zmienić
            if(chromosome.getGen(10).decodeGen()>3){
                chromosome.getGen(10).generateGenInRange(0,3);
                continue;
            }
            rand = r.nextDouble();
            if(rand<0.333){
                chromosome.getGen(5).generateGenInRange(0,1);
                continue;
            }
            if(rand<0.666){
                chromosome.getGen(6).generateGenInRange(0,1);
                continue;
            }
            chromosome.getGen(10).generateGenInRange(0,3);
        }


        //Ograniczenie 1 //2x1+2x2+x10+x11<=10
        if(!checkLimitation(chromosome.getGen(0),chromosome.getGen(1),chromosome.getGen(9),chromosome.getGen(10)))
        {
            //zawsze powinno byc spelnione jesli wszystkie wyzej sa ok
            System.out.println("cos jest nie tak");
            throw new IncorrectLimitationException("Błąd na  ograniczeniu nr 1",chromosome);
        }

        //Ograniczenie 2  //2x2+2x3+x11+x12<=10
        if(!checkLimitation(chromosome.getGen(1),chromosome.getGen(2),chromosome.getGen(10),chromosome.getGen(11)))
        {
            //zawsze powinno byc spelnione jesli wszystkie wyzej sa ok
            System.out.println("cos jest nie tak");
            throw new IncorrectLimitationException("Błąd na  ograniczeniu nr 2",chromosome);
        }

        //Ograniczenie 6 //2x1+2x3+x10+x12<=10
        if(!checkLimitation(chromosome.getGen(0),chromosome.getGen(2),chromosome.getGen(9),chromosome.getGen(10)))
        {
            //zawsze powinno byc spelnione jesli wszystkie wyzej sa ok
            System.out.println("cos jest nie tak");
            throw new IncorrectLimitationException("Błąd na  ograniczeniu nr 6",chromosome);
        }
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

        return sum1-subtract1-subtract2;
    }
}

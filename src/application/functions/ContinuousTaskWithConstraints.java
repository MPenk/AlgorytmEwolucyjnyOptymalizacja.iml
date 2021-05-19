package application.functions;

import application.enums.EFunctions;
import application.other.Chromosome;
import application.other.Population;

import java.util.Random;

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
            double limitation = 0;
            Random r = new Random();
            double rand = 0;

            boolean czyzmiana=false;

        //Ograniczenie 3 //-8x2+x11<=0
        do{
            limitation = 0;
            limitation -= (8 * chromosome.getGen(1).decodeGen());
            limitation += (chromosome.getGen(10).decodeGen());
            if(limitation<=0)
                break;
            /////////////////////////// losowanie 1:8 czy zmienić x11 na 0 czy x2 na 1
            //ZMIANA JEŚLI NIESPEŁNIONE
            ///////////////////////////
            //FIXME Operacje na genach, ponieważ nie spełniają ograniczenia

            rand = r.nextDouble();
            if(rand<0.125){
                chromosome.getGen(10).setGen(0);
                continue;
            }
            chromosome.getGen(1).setGen(1);
            continue;
        }while(limitation>0);
        //Ograniczenie 7 //-8x1+x10<=0
        do{
            limitation = 0;
            limitation -= (8*chromosome.getGen(0).decodeGen());
            limitation += (chromosome.getGen(9).decodeGen());
            if(limitation<=0)
                break;
                ///////////////////////////losowanie 1:8 czy zmienić x10 na 0 czy x1 na 1
                //ZMIANA JEŚLI NIESPEŁNIONE
                ///////////////////////////
                //FIXME Operacje na genach, ponieważ nie spełniają ograniczenia

            rand = r.nextDouble();
            if(rand<0.125){
                chromosome.getGen(9).setGen(0);
                continue;
            }
            chromosome.getGen(0).setGen(1);

        }while(limitation>0);


        //Ograniczenie 8 //-8x3+x12<=0
        do{
            limitation = 0;
            limitation -= (8*chromosome.getGen(2).decodeGen());
            limitation += (chromosome.getGen(11).decodeGen());
            if(limitation<=0)
                break;
                ///////////////////////////losowanie 1:8 czy zmienić x12 na 0 czy x3 na 1
                //ZMIANA JEŚLI NIESPEŁNIONE
                ///////////////////////////
                //FIXME Operacje na genach, ponieważ nie spełniają ograniczenia

            rand = r.nextDouble();
            if(rand<0.125){
                chromosome.getGen(11).setGen(0);
                continue;
            }
            chromosome.getGen(2).setGen(1);
        }while(limitation>0);

            //Ograniczenie 4 //-2x4-x5+x10<=0
            limitation = 0;
            limitation -= (2*chromosome.getGen(3).decodeGen());
            limitation -= (chromosome.getGen(4).decodeGen());
            limitation += (chromosome.getGen(9).decodeGen());
            if(limitation>0)
            {
                ///////////////////////////jesli x10>3 to wylosuj nowe od (0;3) i sprawdź czy ok jeśli nie to losuj co zmienić
                //ZMIANA JEŚLI NIESPEŁNIONE
                ///////////////////////////
                //FIXME Operacje na genach, ponieważ nie spełniają ograniczenia

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

            //Ograniczenie 5 //-2x4-x9+x12<=0
            limitation = 0;
            limitation -= (2*chromosome.getGen(3).decodeGen());
            limitation -= (chromosome.getGen(8).decodeGen());
            limitation += (chromosome.getGen(11).decodeGen());
            if(limitation>0)
            {
                ///////////////////////////jesli x12>3 to wylosuj nowe od (0;3) i sprawdź czy ok jeśli nie to losuj co zmienić
                //ZMIANA JEŚLI NIESPEŁNIONE
                /////////////////////////// PS.Wydaje mi się ze tu zamiast x4 powinno być x8
                //FIXME Operacje na genach, ponieważ nie spełniają ograniczenia

                if(chromosome.getGen(11).decodeGen()>3){
                    chromosome.getGen(11).generateGenInRange(0,3);
                    continue;
                }
                rand = r.nextDouble();
                if(rand<0.333){
                    chromosome.getGen(3).generateGenInRange(0,1);
                    czyzmiana=true;
                    continue;
                }
                if(rand<0.666){
                    chromosome.getGen(8).generateGenInRange(0,1);
                    continue;
                }
                chromosome.getGen(11).generateGenInRange(0,3);

            }while(limitation > 0);

            //Ograniczenie 9 //-2x6-x7+x11<=0
        do{
            limitation = 0;
            limitation -= (2*chromosome.getGen(5).decodeGen());
            limitation -= (chromosome.getGen(6).decodeGen());
            limitation += (chromosome.getGen(10).decodeGen());
            if(limitation>0)
            {
                ///////////////////////////jesli x11>3 to wylosuj nowe od (0;3) i sprawdź czy ok jeśli nie to losuj co zmienić
                //ZMIANA JEŚLI NIESPEŁNIONE
                ///////////////////////////
                //FIXME Operacje na genach, ponieważ nie spełniają ograniczenia

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

        }while(limitation > 0);


            //Ograniczenie 1 //2x1+2x2+x10+x11<=10

            limitation += (2*chromosome.getGen(0).decodeGen());
            limitation += (2*chromosome.getGen(1).decodeGen());
            limitation += chromosome.getGen(9).decodeGen();
            limitation += chromosome.getGen(10).decodeGen();
            if(limitation>10)
            {
                ///////////////////////////zawsze powinno byc spelnione jesli wszystkie wyzej sa ok
                //ZMIANA JEŚLI NIESPEŁNIONE
                ///////////////////////////
                //FIXME Operacje na genach, ponieważ nie spełniają ograniczenia
                System.out.println("cos jest nie tak");

            }

            //Ograniczenie 2  //2x2+2x3+x11+x12<=10
            limitation = 0;
            limitation += (2*chromosome.getGen(1).decodeGen());
            limitation += (2*chromosome.getGen(2).decodeGen());
            limitation += chromosome.getGen(10).decodeGen();
            limitation += chromosome.getGen(11).decodeGen();
            if(limitation>10)
            {
                ///////////////////////////zawsze powinno byc spelnione jesli wszystkie wyzej sa ok
                //ZMIANA JEŚLI NIESPEŁNIONE
                ///////////////////////////
                //FIXME Operacje na genach, ponieważ nie spełniają ograniczenia
                System.out.println("cos jest nie tak" );


            }

            //Ograniczenie 6 //2x1+2x3+x10+x12<=10
            limitation = 0;
            limitation += (2*chromosome.getGen(0).decodeGen());
            limitation += (2*chromosome.getGen(2).decodeGen());
            limitation += chromosome.getGen(9).decodeGen();
            limitation += chromosome.getGen(10).decodeGen();
            if(limitation>10)
            {
                ///////////////////////////zawsze powinno byc spelnione jesli wszystkie wyzej sa ok
                //ZMIANA JEŚLI NIESPEŁNIONE
                ///////////////////////////
                //FIXME Operacje na genach, ponieważ nie spełniają ograniczenia
                System.out.println("cos jest nie tak");


            }
            if(czyzmiana){
                //System.out.println(czyzmiana);
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

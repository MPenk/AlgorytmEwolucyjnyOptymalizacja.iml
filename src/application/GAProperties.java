package application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GAProperties {

    private double pm = 0.2;
    private double pc = 0.6;
    private int generations = 1000;
    private Informations informations;
    private int nThreads = 1;
    private int repetitions = 50;
    private int populationSize = 20;
    ExecutorService threadPool;

    public GAProperties(Informations informations, int nThreads, int repetitions, int generations) {
        this.informations = informations;
        this.nThreads = nThreads;
        this.repetitions = repetitions;
        this.generations = generations;
    }

    public double getPc() {
        return pc;
    }

    public double getPm() {
        return pm;
    }

    public Informations getInformations() {
        return informations;
    }

    public int getGenerations() {
        return generations;
    }

    public int getnThreads() {
        return nThreads;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }
    public void reloadThreadPool()
    {
        this.threadPool = Executors.newFixedThreadPool(this.nThreads);
    }
}

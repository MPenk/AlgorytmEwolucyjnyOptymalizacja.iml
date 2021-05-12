package application.geneticAlgorithm;

import application.functions.Function;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GAProperties {

    private double pm = 0.2;
    private double pc = 0.6;
    private int generations = 1000;
    private Function functions;
    private int nThreads = 1;
    private int repetitions = 50;
    private int populationSize = 20;
    private int from = 20;
    private int to = 200;
    private int step = 20;
    public ExecutorService threadPool;

    public GAProperties(Function functions, int nThreads, int repetitions, int generations, int from, int to, int step) {
        this.functions = functions;
        this.nThreads = nThreads;
        this.repetitions = repetitions;
        this.generations = generations;
        this.from = from;
        this.to = to;
        this.step = step;
    }

    public double getPc() {
        return pc;
    }

    public double getPm() {
        return pm;
    }

    public Function getFunction() {
        return functions;
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

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getStep() {
        return step;
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

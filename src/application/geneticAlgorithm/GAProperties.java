package application.geneticAlgorithm;

import application.functions.Function;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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
    private int graduationOnTheChart;
    private int numberOfCuts; //Ilosć cięć
    public ExecutorService threadPool;

    public GAProperties(Function functions, int nThreads, int repetitions, int generations, int from, int to, int step,int numberOfCuts) {
        this.functions = functions;
        this.nThreads = nThreads;
        this.repetitions = repetitions;
        this.generations = generations;
        this.from = from;
        this.to = to;
        this.step = step;
        this.numberOfCuts = numberOfCuts;
        this.graduationOnTheChart = getGenerations();
        if(graduationOnTheChart>100)
            graduationOnTheChart = 100;
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

    public int getNumberOfCuts() {
        return numberOfCuts;
    }

    public int getGraduationOnTheChart() {
        return graduationOnTheChart;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }
    public void reloadThreadPool()
    {
        this.threadPool = Executors.newFixedThreadPool(nThreads);
    }
}

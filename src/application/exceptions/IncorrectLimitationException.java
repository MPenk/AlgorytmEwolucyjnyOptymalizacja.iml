package application.exceptions;

import application.other.Chromosome;

public class IncorrectLimitationException extends Exception{
    private final Chromosome chromosome;
    public  IncorrectLimitationException(String errorMessage, Chromosome chromosome){
        super(errorMessage);
        this.chromosome = chromosome;
    }

    public Chromosome getChromosome() {
        return chromosome;
    }
}

package hr.fer.zemris.ga;

public class Chromosome {
    public static final double MIN_PARAM_VALUE = -4;
    public static final double MAX_PARAM_VALUE = 4;
    public static final int PARAMETERS_NUMBER = 5;
    private double[] parameters;
    private double penalty;

    public Chromosome(double... parameters) {
        this.parameters = parameters;
    }

    public double[] getParameters() {
        return parameters;
    }

    public double getPenalty() {
        return penalty;
    }

    public double getParameter(int index) {
        return parameters[index];
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    public static Chromosome newRandomChromosome() {
        double[] params = new double[PARAMETERS_NUMBER];
        for(int i = 0; i < PARAMETERS_NUMBER; i++) {
            params[i] = RandomGenerator.generateDouble(MIN_PARAM_VALUE,MAX_PARAM_VALUE);
        }

        return new Chromosome(params);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < parameters.length; i++) {
            stringBuilder.append(String.format("param%d = %.15f; ",i,parameters[i]));
        }
        return stringBuilder.toString();
    }
}

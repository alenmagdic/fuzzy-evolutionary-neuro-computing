public class Chromosome {
    private double[] parameters;
    private double fitness;

    public Chromosome(double... parameters) {
        this.parameters = parameters;
    }

    public double[] getParameters() {
        return parameters;
    }

    public double getParameter(int index) {
        return parameters[index];
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public static Chromosome newRandomChromosome(int numberOfParameters, double standardDeviation) {
        double[] params = new double[numberOfParameters];
        for(int i = 0; i < numberOfParameters; i++) {
            params[i] = RandomGenerator.generateGaussianDouble(standardDeviation);
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

    public int getNumberOfParameters() {
        return parameters.length;
    }
}

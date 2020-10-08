package hr.fer.zemris.ga;

import java.util.Random;

public class RandomGenerator {
    private static Random random = new Random();

    public static double generateDouble(double min, double max) {
        double value = random.nextDouble();
        return min+(max-min)*value;
    }

    public static int generateInt(int min, int max) {
        int value = Math.abs(random.nextInt());
        return min + value%(max-min+1);
    }
}

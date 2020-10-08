package hr.fer.zemris.nenr.dz6;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

public class DataPrinter {
    private static BufferedWriter errorPerIteration;

    static {
        try {
            errorPerIteration = Files.newBufferedWriter(Paths.get("./errorPerIteration.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printNetworkOutputForSampleInputs(AnfisNetwork net) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("./networkOutput.txt"));
        for(int x = -4; x <= 4; x++) {
            for (int y = -4; y <= 4; y++) {
                double out = net.getOutputFor(x, y);
                writer.write(String.format(Locale.ENGLISH,"%d %d %f\n",x,y, out));
            }
        }
        writer.close();
    }

    public static void printMeanSquareErrorsForSampleInputs(AnfisNetwork net) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("./meanSquareErrors.txt"));
        for(int x = -4; x <= 4; x++) {
            for (int y = -4; y <= 4; y++) {
                double out = net.getOutputFor(x, y);
                double error = out - NeuroFuzzySystemApp.calculateTargetFunctionValue(x,y);
                writer.write(String.format(Locale.ENGLISH,"%d %d %f\n",x,y, error));
            }
        }
        writer.close();
    }

    public static void printMembershipFunctions(AnfisNetwork net) throws IOException {
        AnfisNetwork.Parameters params = net.getParameters();
        for(int i = 0 ; i < net.getRulesNumber(); i++) {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("./function_A"+(i+1)+".txt"));
            for(int x = - 4; x <= 4; x++) {
                double membershipValue = net.getSigmoidFunctionValue(x,params.a[i],params.b[i]);
                writer.write(String.format(Locale.ENGLISH,"%d %f\n",x,membershipValue));
            }
            writer.close();
        }

        for(int i = 0 ; i < net.getRulesNumber(); i++) {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("./function_B"+(i+1)+".txt"));
            for(int x = - 4; x <= 4; x++) {
                double membershipValue = net.getSigmoidFunctionValue(x,params.c[i],params.d[i]);
                writer.write(String.format(Locale.ENGLISH,"%d %f\n",x,membershipValue));
            }
            writer.close();
        }
    }

    public static void printIterationError(int iteration, double iterationError) {
        try {
            errorPerIteration.write(String.format(Locale.ENGLISH,"%d %f\n",iteration,iterationError));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        try {
            errorPerIteration.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

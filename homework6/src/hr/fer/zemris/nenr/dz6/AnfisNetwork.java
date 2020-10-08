package hr.fer.zemris.nenr.dz6;
import java.util.Random;

public class AnfisNetwork {

    static class Parameters {
        double[] a;
        double[] b;
        double[] c;
        double[] d;
        double[] p;
        double[] q;
        double[] r;

        public Parameters(int rulesNumber) {
            a = new double[rulesNumber];
            b = new double[rulesNumber];
            c = new double[rulesNumber];
            d = new double[rulesNumber];
            p = new double[rulesNumber];
            q = new double[rulesNumber];
            r = new double[rulesNumber];
        }
    }

    private static class ParametersCorrection {
        private double[] a;
        private double[] b;
        private double[] c;
        private double[] d;
        private double[] p;
        private double[] q;
        private double[] r;

        public ParametersCorrection(int rulesNumber) {
            a = new double[rulesNumber];
            b = new double[rulesNumber];
            c = new double[rulesNumber];
            d = new double[rulesNumber];
            p = new double[rulesNumber];
            q = new double[rulesNumber];
            r = new double[rulesNumber];
        }
    }

    public static class OutputCalculationParams {
        double x;
        double y;
        double[] A;
        double[] B;
        double[] w;
        double wSum;
        double[] wShares;
        double[] f;
        double[] wShareTimesF;
        double output;

        public OutputCalculationParams(int rulesNumber) {
            A = new double[rulesNumber];
            B = new double[rulesNumber];
            w = new double[rulesNumber];
            wShares = new double[rulesNumber];
            f = new double[rulesNumber];
            wShareTimesF = new double[rulesNumber];
        }
    }

    private Parameters params;
    private ParametersCorrection corrections;
    private TrainingSample[] trainingSamples;
    private int rulesNumber;

    public AnfisNetwork(TrainingSample[] trainingSamples, int rulesNumber) {
        this.trainingSamples = trainingSamples;
        this.rulesNumber = rulesNumber;
    }

    public double getOutputFor(double x, double y) {
        return calculateOutput(x,y).output;
    }

    public OutputCalculationParams calculateOutput(double x, double y) {
        OutputCalculationParams output = new OutputCalculationParams(rulesNumber);
        output.x = x;
        output.y = y;

        //layer 1
        for(int i = 0 ; i < rulesNumber; i++) {
            output.A[i] = getSigmoidFunctionValue(x,params.a[i],params.b[i]);
            output.B[i] = getSigmoidFunctionValue(y,params.c[i],params.d[i]);
        }

        //layer 2
        for(int i = 0; i < rulesNumber; i++) {
            output.w[i] = output.A[i] * output.B[i];
            output.wSum += output.w[i];
        }

        //layer 3
        for(int i = 0; i < rulesNumber; i++) {
            output.wShares[i] = output.w[i] / output.wSum;
        }

        //layer 4
        for(int i = 0; i < rulesNumber; i++) {
            output.f[i] = calculateFi(i,x,y);
            output.wShareTimesF[i] = output.wShares[i] * output.f[i];
        }

        //layer 5
        for(int i = 0; i < rulesNumber;i++) {
            output.output += output.wShareTimesF[i];
        }

        return output;
    }

    public double getSigmoidFunctionValue(double x, double a, double b) {
        return 1/(1+Math.exp(b*(x-a)));
    }

    public int getRulesNumber() {
        return rulesNumber;
    }

    public Parameters getParameters() {
        return params;
    }

    private double calculateFi(int i, double x, double y) {
        return params.p[i]*x+params.q[i]*y+params.r[i];
    }

    private Parameters getInitialParameters() {
        Parameters params = new Parameters(rulesNumber);
        fillArrayWithRandomValues(params.a);
        fillArrayWithRandomValues(params.b);
        fillArrayWithRandomValues(params.c);
        fillArrayWithRandomValues(params.d);
        fillArrayWithRandomValues(params.p);
        fillArrayWithRandomValues(params.q);
        fillArrayWithRandomValues(params.r);
        return params;
    }

    private void fillArrayWithRandomValues(double[] array) {
        Random rnd = new Random();
        for(int i = 0; i < array.length; i++) {
            array[i] = -1+2*rnd.nextDouble();
        }
    }

    enum TrainingAlgorithm {
        BATCH_LEARNING, ONLINE_LEARNING
    }

    public void train(int iterations, double learningRate, TrainingAlgorithm algorithm) {
        this.params = getInitialParameters();
        double mse = 0;
        for(int iteration = 1; iteration <= iterations ; iteration++) {
            corrections = new ParametersCorrection(rulesNumber);
            for (int s = 0; s < trainingSamples.length; s++) {
                TrainingSample sample = trainingSamples[s];
                OutputCalculationParams output = calculateOutput(sample.getX(),sample.getY());

                if(algorithm == TrainingAlgorithm.ONLINE_LEARNING) { //kod online učenja se resetiraju korekcije za svaki sample
                    corrections = new ParametersCorrection(rulesNumber);
                }

                for(int i = 0; i < rulesNumber; i++) {
                    corrections.a[i] += getMsePartialDerivativeWithRespectTo_ai(i,sample.getZ(),output);
                    corrections.b[i] += getMsePartialDerivativeWithRespectTo_bi(i,sample.getZ(),output);
                    corrections.c[i] += getMsePartialDerivativeWithRespectTo_ci(i,sample.getZ(),output);
                    corrections.d[i] += getMsePartialDerivativeWithRespectTo_di(i,sample.getZ(),output);
                    corrections.p[i] += getMsePartialDerivativeWithRespectToPi(i,sample.getZ(),output);
                    corrections.q[i] += getMsePartialDerivativeWithRespectToQi(i,sample.getZ(),output);
                    corrections.r[i] += getMsePartialDerivativeWithRespectToRi(i,sample.getZ(),output);
                }


                if(algorithm == TrainingAlgorithm.ONLINE_LEARNING) {
                    updateParameters(learningRate);
                }
            }

            if(algorithm == TrainingAlgorithm.BATCH_LEARNING) {
                updateParameters(learningRate);
            }

            mse = calculateMse();
            DataPrinter.printIterationError(iteration,mse);
            if(iteration%1000 == 0) {
                System.out.printf("Iteracija %d, mse = %f\n ",iteration,mse);
            }
        }
    }

    private void updateParameters(double learningRate) {
        for(int i = 0; i < rulesNumber; i++) {
            params.a[i] -= learningRate*corrections.a[i];
            params.b[i] -= learningRate*corrections.b[i];
            params.c[i] -= learningRate*corrections.c[i];
            params.d[i] -= learningRate*corrections.d[i];
            params.p[i] -= learningRate*corrections.p[i];
            params.q[i] -= learningRate*corrections.q[i];
            params.r[i] -= learningRate*corrections.r[i];
        }
    }

    private double calculateMse() {
        double sumSquare = 0;
        for(int s = 0; s < trainingSamples.length ; s++) {
            OutputCalculationParams out = calculateOutput(trainingSamples[s].getX(),trainingSamples[s].getY());
            sumSquare += Math.pow(trainingSamples[s].getZ() - out.output,2);
        }
        return sumSquare/trainingSamples.length;
    }

    private double getMsePartialDerivativeWithRespectTo_ai(int i, double correctOutput, OutputCalculationParams output) {
        return getMsePartialDerivativeWithRespectToOutputValue(correctOutput,output) *
                getOutputValuePartialDerivativeWithRespectToWi(i,output) *
                getWiPartialDerivativeWithRespectToAi(i,output) *
                getAiPartialDerivativeWithRespectTo_ai(i,output);
    }

    private double getMsePartialDerivativeWithRespectTo_bi(int i, double correctOutput, OutputCalculationParams output) {
        return getMsePartialDerivativeWithRespectToOutputValue(correctOutput,output) *
                getOutputValuePartialDerivativeWithRespectToWi(i,output) *
                getWiPartialDerivativeWithRespectToAi(i,output) *
                getAiPartialDerivativeWithRespectTo_bi(i,output);
    }

    private double getMsePartialDerivativeWithRespectTo_ci(int i, double correctOutput, OutputCalculationParams output) {
        return getMsePartialDerivativeWithRespectToOutputValue(correctOutput,output) *
                getOutputValuePartialDerivativeWithRespectToWi(i,output) *
                getWiPartialDerivativeWithRespectToBi(i,output) *
                getBiPartialDerivativeWithRespectTo_ci(i,output);
    }

    private double getMsePartialDerivativeWithRespectTo_di(int i, double correctOutput, OutputCalculationParams output) {
        return getMsePartialDerivativeWithRespectToOutputValue(correctOutput,output) *
                getOutputValuePartialDerivativeWithRespectToWi(i,output) *
                getWiPartialDerivativeWithRespectToBi(i,output) *
                getBiPartialDerivativeWithRespectTo_di(i,output);
    }

    private double getMsePartialDerivativeWithRespectToPi(int i,double correctValue, OutputCalculationParams outputCalculationParams) {
        return getMsePartialDerivativeWithRespectToOutputValue(correctValue,outputCalculationParams) *
                getOutputValuePartialDerivativeWithRespectToPi(i,outputCalculationParams);
    }

    private double getMsePartialDerivativeWithRespectToQi(int i,double correctValue, OutputCalculationParams outputCalculationParams) {
        return getMsePartialDerivativeWithRespectToOutputValue(correctValue,outputCalculationParams) *
                getOutputValuePartialDerivativeWithRespectToQi(i,outputCalculationParams);
    }

    private double getMsePartialDerivativeWithRespectToRi(int i,double correctValue, OutputCalculationParams outputCalculationParams) {
        return getMsePartialDerivativeWithRespectToOutputValue(correctValue,outputCalculationParams) *
                getOutputValuePartialDerivativeWithRespectToRi(i,outputCalculationParams);
    }

    private double getMsePartialDerivativeWithRespectToOutputValue(double correctValue, OutputCalculationParams outputCalculationParams) {
        return outputCalculationParams.output - correctValue;
    }

    private double getOutputValuePartialDerivativeWithRespectToPi(int i, OutputCalculationParams outputCalculationParams) {
        return outputCalculationParams.x * outputCalculationParams.wShares[i];
    }

    private double getOutputValuePartialDerivativeWithRespectToQi(int i, OutputCalculationParams outputCalculationParams) {
        return outputCalculationParams.y * outputCalculationParams.wShares[i];
    }

    private double getOutputValuePartialDerivativeWithRespectToRi(int i, OutputCalculationParams outputCalculationParams) {
        return outputCalculationParams.wShares[i];
    }

    private double getOutputValuePartialDerivativeWithRespectToWi(int i, OutputCalculationParams out) {
        double numeratorSum = 0; //suma koja je brojnik u konacnom razlomku
        for(int j = 0; j < rulesNumber ; j++) {
            numeratorSum += out.w[j]*(out.f[i]-out.f[j]);
        }

        return numeratorSum/(out.wSum*out.wSum);
    }

    private double getWiPartialDerivativeWithRespectToAi(int i, OutputCalculationParams outputCalculationParams) {
        return outputCalculationParams.B[i];
    }

    private double getWiPartialDerivativeWithRespectToBi(int i, OutputCalculationParams outputCalculationParams) {
        return outputCalculationParams.A[i];
    }

    private double getAiPartialDerivativeWithRespectTo_ai(int i, OutputCalculationParams out) {
        return out.A[i]*(1-out.A[i])*params.b[i];
    }

    private double getBiPartialDerivativeWithRespectTo_ci(int i, OutputCalculationParams out) {
        return out.B[i]*(1-out.B[i])*params.d[i];
    }

    private double getAiPartialDerivativeWithRespectTo_bi(int i, OutputCalculationParams out) {
        return out.A[i]*(1-out.A[i])*(params.a[i] - out.x);
    }

    private double getBiPartialDerivativeWithRespectTo_di(int i, OutputCalculationParams out) {
        return out.B[i]*(1-out.B[i])*(params.c[i] - out.y);
    }

}

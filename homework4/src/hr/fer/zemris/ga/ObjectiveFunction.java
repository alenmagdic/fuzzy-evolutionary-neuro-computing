package hr.fer.zemris.ga;

public class ObjectiveFunction {

    public double calculateValueFor(double x, double y, double... params) {
        double sinValue = Math.sin(params[0]+params[1]*x);
        double cosValue = Math.cos(x*(params[3]+y));
        double fraction = 1/
                (1+Math.exp(Math.pow(x-params[4],2)));

        return  sinValue + params[2]*cosValue*fraction;
    }
}

package hr.fer.zemris.nenr.dz5.util;

import java.util.List;

public class ListToPrimitivesArray {

    public static double[] convertToArrayOfDoubles(List<Double> list) {
        double[] array = new double[list.size()];
        for(int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }
}

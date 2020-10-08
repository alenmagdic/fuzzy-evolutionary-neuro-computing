package hr.fer.zemris.nenr.dz5.neuralnetwork.training;

import java.util.Iterator;
import java.util.List;

public class TrainingSampleGroup implements Iterable<TrainingSample> {
    private List<TrainingSample> groupSamples;

    public TrainingSampleGroup(List<TrainingSample> groupSamples) {
        this.groupSamples = groupSamples;
    }

    public int getGroupSize() {
        return groupSamples.size();
    }

    @Override
    public Iterator<TrainingSample> iterator() {
        return groupSamples.iterator();
    }
}

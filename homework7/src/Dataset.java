import java.util.Iterator;
import java.util.List;

public class Dataset implements Iterable<DatasetSample> {
    private List<DatasetSample> samples;

    public Dataset(List<DatasetSample> samples) {
        this.samples = samples;
    }

    public DatasetSample getSample(int index) {
        return samples.get(index);
    }

    public int getNumberOfSamples() {
        return samples.size();
    }

    @Override
    public Iterator<DatasetSample> iterator() {
        return samples.iterator();
    }
}

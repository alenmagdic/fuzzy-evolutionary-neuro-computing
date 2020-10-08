import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

public class DatasetVisualizingTool {

    public static void main(String[] args) throws IOException {
        Dataset dataset = new Dataset(Main.loadDataset("./dataset.txt"));
        String outputFolderPath = "./dataset/";

        BufferedWriter class1Writer = Files.newBufferedWriter(Paths.get(outputFolderPath+"class1.txt"));
        BufferedWriter class2Writer = Files.newBufferedWriter(Paths.get(outputFolderPath+"class2.txt"));
        BufferedWriter class3Writer = Files.newBufferedWriter(Paths.get(outputFolderPath+"class3.txt"));

        for(DatasetSample sample : dataset) {
            if(sample.getOutputValues()[0] == 1) {
                writeSample(class1Writer,sample);
            } else if(sample.getOutputValues()[1] == 1) {
                writeSample(class2Writer,sample);
            } else if(sample.getOutputValues()[2] == 1) {
                writeSample(class3Writer,sample);
            }
        }

        class1Writer.close();
        class2Writer.close();
        class3Writer.close();
    }

    private static void writeSample(BufferedWriter class3Writer, DatasetSample sample) throws IOException {
        class3Writer.write(String.format(Locale.ENGLISH,"%f %f\n",sample.getInputValues()[0],sample.getInputValues()[1]));
    }


}

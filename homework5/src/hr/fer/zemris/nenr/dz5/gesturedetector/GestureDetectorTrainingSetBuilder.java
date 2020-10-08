package hr.fer.zemris.nenr.dz5.gesturedetector;

import hr.fer.zemris.nenr.dz5.util.Point;
import hr.fer.zemris.nenr.dz5.util.PointDouble;
import hr.fer.zemris.nenr.dz5.util.Range;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GestureDetectorTrainingSetBuilder {
    private List<GestureSample> gestureSamples = new ArrayList<>();
    private GesturePointsConverter pointsConverter = new GesturePointsConverter();

    public void addSample(int classNumber, List<Point> points) {
        gestureSamples.add(new GestureSample(classNumber,points));
    }

    public void exportSamples(Range representativeDotsNumberRange, Path exportFilePath) throws IOException {
        int rangeStart = representativeDotsNumberRange.getRangeStart();
        int rangeEnd = representativeDotsNumberRange.getRangeEnd();
        int rangeStep = representativeDotsNumberRange.getRangeStep();
        int digitsNumber = Integer.toString(rangeEnd).length();
        for(int dotsNumber = rangeStart; dotsNumber < rangeEnd; dotsNumber+=rangeStep) {
            processSamples(dotsNumber);
            exportToFile(Paths.get(String.format("%s_%0"+digitsNumber+"d",exportFilePath,dotsNumber)));
        }

    }

    private void exportToFile(Path exportFilePath) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(exportFilePath);
        int numberOfClasses = getNumberOfClasses();
        for(GestureSample sample : gestureSamples) {
            List<Double> representativePointsValues = pointsConverter.getRepresentativePointsValues(sample.representativePoints);
            for(double value : representativePointsValues) {
                writer.write(String.format(Locale.ENGLISH,"%f ",value));

            }
            writer.write(getCodeForClassNumber(sample.classNumber,numberOfClasses));
            writer.write("\n");
        }
        writer.close();
    }


    private int getNumberOfClasses() { //pretpostavke: brojevi klasa cine neprekidan niz pocevsi od 1
        return gestureSamples.stream().map(s -> s.classNumber).max(Comparator.comparingInt(n->n)).get();
    }

    private String getCodeForClassNumber(int classNumber, int numberOfClasses) {
        StringBuilder codeBuilder = new StringBuilder();
        for(int i = 1; i <= numberOfClasses; i++) {
            if(i==classNumber) {
                codeBuilder.append('1');
            } else {
                codeBuilder.append('0');
            }
        }

        return codeBuilder.toString();
    }

    private void processSamples(int representativeDotsNumber) {
        for(GestureSample sample : gestureSamples) {
            sample.representativePoints = pointsConverter.convertSamplePointsToRepresentativePoints(sample.points,representativeDotsNumber);
        }
    }

    private static class GestureSample {
        int classNumber;
        List<Point> points;
        List<PointDouble> representativePoints;

        GestureSample(int classNumber, List<Point> points) {
            this.classNumber = classNumber;
            this.points = points;
        }
    }
}

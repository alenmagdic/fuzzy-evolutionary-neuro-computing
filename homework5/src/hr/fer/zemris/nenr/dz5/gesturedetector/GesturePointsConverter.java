package hr.fer.zemris.nenr.dz5.gesturedetector;

import hr.fer.zemris.nenr.dz5.util.Point;
import hr.fer.zemris.nenr.dz5.util.PointDouble;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GesturePointsConverter {



    public List<PointDouble> convertSamplePointsToRepresentativePoints(List<Point> samplePoints, int representativeDotsNumber) {
        PointDouble averageOfPoints = calculateAverageValueOfPoints(samplePoints);
        List<PointDouble> points = samplePoints.stream()
                .map(p -> new PointDouble(p.getX()-averageOfPoints.getX(),p.getY()-averageOfPoints.getY()))
                .collect(Collectors.toList());

        double maxAbsoluteX = Math.abs(points.stream().map(PointDouble::getX).max(Comparator.comparingDouble(Math::abs)).get());
        double maxAbsoluteY = Math.abs(points.stream().map(PointDouble::getY).max(Comparator.comparingDouble(Math::abs)).get());
        double maxAbsoluteXY = Math.max(maxAbsoluteX,maxAbsoluteY);

        points.forEach(p -> p.setX(p.getX()/maxAbsoluteXY));
        points.forEach(p -> p.setY(p.getY()/maxAbsoluteXY));

        double gestureLength = calculateGestureLength(points);

        List<PointDouble> representativePoints = new ArrayList<>();
        int k = 0;
        double lengthToPoint = 0;
        boolean testPreviousPointAgain = false;
        for(int i = 0; i < points.size() - 1; i++) {
            if(testPreviousPointAgain) {
                i--;
                testPreviousPointAgain = false;
            } else if(i>0) {
                lengthToPoint += getDistanceBetweenPoints(points.get(i), points.get(i - 1));
            }
            double lengthToNextPoint = lengthToPoint + getDistanceBetweenPoints(points.get(i),points.get(i+1));
            double lengthToTargetPoint = k*gestureLength/(representativeDotsNumber-1); //duljina na kojoj se nalazi trazena tocka za trenutni parametar k (k je indeks u listi reprezentativnih tocaka)
            if(lengthToTargetPoint >= lengthToPoint && lengthToTargetPoint < lengthToNextPoint) { //provjera nalazi li se trazena tocka taman po udaljenosti izmedu ove i sljedece tocke
                if(lengthToTargetPoint - lengthToPoint < lengthToNextPoint - lengthToTargetPoint) { //ako je bliza ova tocka, uzima se ona
                    representativePoints.add(points.get(i));
                } else {
                    representativePoints.add(points.get(i+1));
                }
                k++;
                testPreviousPointAgain = true; //u sljedecoj iteraciji treba opet provjeriti za istu ovu tocku za slucaj da se izmedu nje i sljedece nalazi jos jedna reprezentativna tocka
                if(k==representativeDotsNumber-1) {
                    break;
                }
            }
        }
        while(representativePoints.size() < representativeDotsNumber) { //u pravilu bi se ova petlja trebala samo jednom izvrtit, ali u slucaju da ima vise od jedne reprezentativne tocke za koju vrijedi da joj je najbliza bas zadnja tocka ce se ova petlja izvrtit vise puta
            representativePoints.add(points.get(points.size() - 1));
        }

        return representativePoints;
    }

    private double calculateGestureLength(List<PointDouble> points) {
        double length = 0;
        for(int i = 1; i < points.size(); i++) {
            length += getDistanceBetweenPoints(points.get(i),points.get(i-1));
        }
        return length;
    }

    private double getDistanceBetweenPoints(PointDouble p1, PointDouble p2) {
        return Math.sqrt(Math.pow(p1.getX()-p2.getX(),2)+Math.pow(p1.getY()-p2.getY(),2));
    }


    private PointDouble calculateAverageValueOfPoints(List<Point> points) {
        double avgX = points.stream().mapToInt(Point::getX).average().getAsDouble();
        double avgY = points.stream().mapToInt(Point::getY).average().getAsDouble();
        return new PointDouble(avgX,avgY);
    }

    public List<Double> getRepresentativePointsValues(List<PointDouble> representativePoints) {
        List<Double> representativePointsValues = new ArrayList<>();
        for(PointDouble p : representativePoints) {
            representativePointsValues.add(p.getX());
            representativePointsValues.add(p.getY());
        }
        return representativePointsValues;
    }

    public List<PointDouble> getRepresentativePointsFromValues(double[] values) {
        List<PointDouble> points = new ArrayList<>();
        for(int i = 0; i < values.length; i+=2) {
            points.add(new PointDouble(values[i],values[i+1]));
        }
        return points;
    }

    //pretvara reprezentativne tocke u oblik koji omogucuje prikaz na canvasu
    public List<Point> convertRepresentativePointsToDisplayablePoints(List<PointDouble> points, double resizeFactor) {
        //translatira se tako da lijevi gornji kut bude na poziciji (0,0) pa se mora sve translatirati za najnegativniji x i za najnegativniji y
        double translationDistanceX = - points.stream().map(PointDouble::getX).min(Double::compareTo).get()*resizeFactor;
        double translationDistanceY = - points.stream().map(PointDouble::getY).min(Double::compareTo).get()*resizeFactor;

        points.forEach(p -> p.setX(p.getX()*resizeFactor+translationDistanceX));
        points.forEach(p -> p.setY(p.getY()*resizeFactor+translationDistanceY));

        return points.stream().map(p -> new Point((int)p.getX(),(int)p.getY())).collect(Collectors.toList());
    }


}

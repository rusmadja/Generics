package Week05Generics.Ex5Clustering;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TwoDPoint implements Clusterable<TwoDPoint>{
    double x;
    double y;
    public TwoDPoint(String str){
        String[] points = str.split(",");
        this.x = Double.parseDouble(points[0]);
        this.y = Double.parseDouble(points[1]);
    }
    public TwoDPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public double distance(TwoDPoint other) {
        return Math.sqrt(
                        Math.pow(other.x - this.x ,2) +
                        Math.pow(other.y - this.y,2)
                        );
    }

    public static Set<TwoDPoint> readClusterableSet(String path) throws IOException{
        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            return lines.map(TwoDPoint::new)
                            .collect(Collectors.toSet());

        }
    }

    @Override
    public String toString() {
        return x + "," + y;
    }

    @Override
    public boolean equals(Object other) {
        TwoDPoint otherP = (TwoDPoint) other;
        return (otherP.x == x && otherP.y == y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

package Week05Generics.Ex5Clustering;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AgglomerativeClustering<T extends Clusterable<T>> implements Clustering<T> {
    double threshold;

    public AgglomerativeClustering(double threshold) {
        this.threshold = threshold;
    }

    public Set<Set<T>> clusterSet(Set<T> elements) {
        Set<Set<T>> clusters = elements.parallelStream()
                .map(element -> new HashSet<>(Arrays.asList(element)))
                .collect(Collectors.toSet());

        while (clusters.size() != 1) {
            //find the pair of two more similar Clusterable with they distance.
            Pair<Set<T>, Pair<Set<T>, Double>> closest_pair =
                    clusters.parallelStream()
                            .map(c1 -> new Pair<Set<T>, Pair<Set<T>, Double>>(
                                            c1,
                                            clusters.stream()
                                                    .filter(c2 -> c2 != c1)
                                                    .map(c2 -> new Pair<Set<T>, Double>(c2, Distance(c1, c2)))
                                                    .min(Comparator.comparing(pair -> pair.y))
                                                    .get()
                                    )
                            )
                            .min(Comparator.comparing(pair -> pair.y.y))
                            .get();


            if (closest_pair.y.y > threshold)// if the smallest distance greater than threshold,  you have your clusters
                return clusters;

            //set value that we have found
            Set<T> cluster1 = closest_pair.x, cluster2 = closest_pair.y.x;
            // remove the two closest clusters
            clusters.remove(cluster2);
            // must be removed then added, because union will change the hash
            clusters.remove(cluster1);
            // union them
            Union(cluster1, cluster2);
            // insert the union back into the set
            clusters.add(cluster1);
        }

        return clusters;

    }

    private void Union(Set<T> c1, Set<T> c2) {
        c1.addAll(c2);
    }

    private Double Distance(Set<T> c1, Set<T> c2) {
        return c1.parallelStream()
                .flatMap(e1 -> c2.stream().map(e2 -> e1.distance(e2)))
                .mapToDouble(d1 -> d1)
                .min()
                .getAsDouble();
    }
}



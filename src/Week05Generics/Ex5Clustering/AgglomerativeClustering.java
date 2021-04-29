package Week05Generics.Ex5Clustering;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class AgglomerativeClustering <T extends Clusterable<T>> implements Clustering<T>{
    double threshold;
    public AgglomerativeClustering(double threshold) {
        this.threshold = threshold;
    }
    public Set<Set<T>> clusterSet(Set<T> elements) {
        Set<Set<T>> clusters = elements.parallelStream()
                .map(element -> new HashSet<>(Arrays.asList(element)))
                .collect(Collectors.toSet());
        while (clusters.size()!=1) {
            // create matrix
            Pair<Set<T>, Pair<Set<T>, Double>> closest_pair = clusters.parallelStream()
                    .map(c1 -> new Pair<Set<T>, Pair<Set<T>, Double>>
                            (
                                    c1,
                                    clusters.parallelStream() // convert set to stream
                                    .filter(c2 -> c1 != c2)
                                    .map(c2 -> new Pair<Set<T>, Double>
                                            (
                                                c2,
                                                c1.parallelStream().map(
                                                    element -> c2.parallelStream()
                                                            .map(other_element -> other_element.distance(element))
                                                            .min(Comparator.naturalOrder())
                                                            .get()
                                                )
                                                .min(Comparator.naturalOrder())
                                                .get())
                                    )
                                    .min(Comparator.comparing(pair -> pair.y))
                                    .get()
                            )
                    )
                    .min(Comparator.comparing(pair -> pair.y.y))
                    .get();
            if(closest_pair.y.y > threshold)
                return clusters;
            Set<T> cluster1 = closest_pair.x;
            Set<T> cluster2 = closest_pair.y.x;
            // remove the two closest clusters
            clusters.remove(cluster2);
            // must be removed then added, because union will change the hash
            clusters.remove(cluster1);
            // union them
            cluster1.addAll(cluster2);
            // insert the union back into the set
            clusters.add(cluster1);
        }

        return clusters;
    }

    private Set<T> union(Set<T> c1, Set<T> c2) {
       return null;
    }
}
/*
Cluster(items, threshold)
clusters = A set of singletons, where each item starts in its own cluster
	while (clusters is not of size 1)
		find the two most similar clusters, c1,c2 from clusters
		if (distance(c1, c2) > threshold) return clusters;
		replace c1,c2 in clusters with the union of c1,c2
	return clusters

 */

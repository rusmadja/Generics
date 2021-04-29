package Week05Generics.Ex5Clustering;

import java.util.Set;

public interface Clustering<T> {
    public Set<Set<T>> clusterSet(Set<T> elements);
}

package Week05Generics.Ex5Clustering;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static java.lang.Math.sqrt;

public class BitArray implements Clusterable<BitArray>{
    private ArrayList<Boolean> bits;

    /**
     * Ctor by String
     * @param str
     */
    public BitArray(String str){
        this.bits =(ArrayList<Boolean>)
                 Arrays.stream(str.split(","))
                            .map(Boolean::parseBoolean)
                            .collect(Collectors.toList());
    }

    /**
     * ctor by Boolean Arrays
     * @param bits
     */
    public BitArray(Boolean[] bits) {
        this.bits = new ArrayList<Boolean>();
        Collections.addAll(this.bits , bits);
    }

    private int getSize() {
        return this.bits.size();
    }

    @Override
    public double distance(BitArray other) {
        double hamming;
        if(this.bits.size() <= other.bits.size() )
            hamming = IntStream.range(0,this.bits.size())
                    .filter(i->this.bits.get(i) != other.bits.get(i))
                    .count()
                    +
                    other.bits.size() - this.bits.size();
        else
            hamming = IntStream.range(0,other.bits.size())
                    .filter(i->other.bits.get(i) != this.bits.get(i))
                    .count()
                    +
                    this.bits.size() - other.bits.size();
        return hamming;
    }

    public static Set<BitArray> readClusterableSet(String path) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            List<BitArray> array =new ArrayList<>(
                                lines.map(BitArray::new)
                                     .collect(Collectors.toList())
                                );
            Optional<BitArray> longestBit = array.parallelStream().max(Comparator.comparing(BitArray::getSize));
            return longestBit.isPresent() ?
                    array.parallelStream().filter(i -> i.getSize() == longestBit.get().getSize()).collect(Collectors.toSet())
                    : new HashSet<>();
        }
    }

    @Override
    public String toString() {
        return bits.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BitArray bitArray = (BitArray) o;
        return bits.equals(bitArray.bits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bits);
    }
}
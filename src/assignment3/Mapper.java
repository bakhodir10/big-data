package assignment3;

import java.util.List;

public class Mapper<K, V> {
    private K key;
    private List<Pair<K, V>> pairs;

    public Mapper(K key, List<Pair<K, V>> pairs) {
        this.key = key;
        this.pairs = pairs;
    }

    public List<Pair<K, V>> getPairs() {
        return pairs;
    }

    public K getKey() {
        return key;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Pair<K, V> pair : this.pairs) b.append(pair.toString()).append("\n");
        return b.toString();
    }
}

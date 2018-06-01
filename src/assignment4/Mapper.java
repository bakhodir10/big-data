package assignment4;

import java.util.Map;

public class Mapper<K, V> {
    private K key;
    private Map<K, Pair<K, V>> pairs;

    public Mapper(K key, Map<K, Pair<K, V>> pairs) {
        this.key = key;
        this.pairs = pairs;
    }

    public Map<K, Pair<K, V>> getPairs() {
        return pairs;
    }

    public K getKey() {
        return key;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Pair<K, V> pair : this.pairs.values()) {
            b.append(pair.toString()).append("\n");
        }
        return b.toString();
    }
}

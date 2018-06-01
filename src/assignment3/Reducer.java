package assignment3;

import java.util.List;

public class Reducer<K, V> {
    private List<Pair<K, V>> pairs;

    public Reducer(List<Pair<K, V>> pairs) {
        this.pairs = pairs;
    }

    public List<Pair<K, V>> getPairs() {
        return pairs;
    }

    public void addPair(Pair<K, V> pair) {
        this.pairs.add(pair);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Pair<K, V> pair : this.pairs) b.append(pair.toString()).append("\n");
        return b.toString();
    }
}

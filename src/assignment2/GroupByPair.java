package assignment2;

import java.util.List;
import java.util.Objects;

public class GroupByPair<K extends Comparable<K>, V> {
    private K key;
    private List<V> values;

    public GroupByPair(K key, List<V> values) {
        this.key = key;
        this.values = values;
    }

    public K getKey() {
        return key;
    }

    public List<V> getValues() {
        return values;
    }

    public void setKey(K key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupByPair<?, ?> that = (GroupByPair<?, ?>) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public String toString() {
        return "< " + this.key + ", " + this.values + " >";
    }
}

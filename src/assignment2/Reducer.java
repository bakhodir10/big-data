package assignment2;

public class Reducer<K, V> {
    private K key;
    private V value;

    public Reducer(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "< " + key + ", " + value + " >";
    }
}

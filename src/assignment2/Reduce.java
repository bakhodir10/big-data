package assignment2;

public class Reduce<K, V> {
    private K key;
    private V value;

    public Reduce(K key, V value) {
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

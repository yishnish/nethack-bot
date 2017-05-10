package collections;

import java.util.HashMap;

public class DefaultValueHashMap<K, V> extends HashMap<K, V> {

    private final V defaultValue;

    public DefaultValueHashMap(V defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public V get(Object o) {
        V v = super.get(o);
        return v == null? defaultValue : v;
    }
}

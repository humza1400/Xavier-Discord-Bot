package me.comu.exeter.objects;

import java.util.Objects;

// merge compositekey into this

public class ObjectKey {

    private final Object key;
    private final Object value;

    public ObjectKey(Object key, Object value) {
        this.key = key;
        this.value = value;
    }

    public Object getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public static ObjectKey of(Object key, Object value) {
        return new ObjectKey(key, value);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectKey that = (ObjectKey) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return "ObjectKey{" + "key='" + key + '\'' + ", value='" + value + '\'' + '}';
    }
}


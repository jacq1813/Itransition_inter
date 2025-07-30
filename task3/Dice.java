package task3;

import java.util.*;

public class Dice {
    private final int[] values;

    public Dice(int[] values) {
        this.values = Arrays.copyOf(values, values.length);
    }

    public int getValue(int index) {
        if (index < 0 || index >= values.length) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return values[index];
    }

    public int getSize() {
        return values.length;
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Dice dice = (Dice) o;
        return Arrays.equals(values, dice.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }
}
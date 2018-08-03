package ch.kup.nono.solver;

public class Hint {
    private final int length;

    public Hint(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return Integer.toString(length);
    }
}

package ch.kup.nono.solver;

import ch.kup.nono.solver.Nonogram.State;

import java.util.Arrays;

public class Nonostate {
    private State[][] state;
    private int rows;
    private int cols;

    public Nonostate(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        state = new State[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                state[row][col] = State.UNTOUCHED;
            }
        }
    }

    public static Nonostate copyOf(Nonostate other) {
        Nonostate newState = new Nonostate(other.rows, other.cols);
        for (int row = 0; row < other.rows; row++) {
            for (int col = 0; col < other.cols; col++) {
                newState.state[row][col] = other.state[row][col];
            }
        }
        return newState;
    }

    public State get(int row, int col) {
        return state[row][col];
    }

    public void set(int row, int col, State newState) {
        if (get(row, col) != State.UNTOUCHED && newState != get(row, col))
            throw new IllegalStateException(
                    String.format("Setting different state (%s) for a field that was already touched: [%d,%d] was %s",
                            newState, row, col, get(row, col).digit()));
        state[row][col] = newState;
    }

    public int getUntouchedCount() {
        int sum = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (state[i][j] == State.UNTOUCHED)
                    sum++;
            }
        }
        return sum;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Nonostate)) return false;
        Nonostate o = (Nonostate) obj;
        return rows == o.rows && cols == o.cols && Arrays.deepEquals(state, o.state);
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }
}

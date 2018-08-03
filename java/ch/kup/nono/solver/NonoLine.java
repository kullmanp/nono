package ch.kup.nono.solver;

import ch.kup.nono.CellState;
import ch.kup.nono.Hint;

import java.util.List;

public interface NonoLine {
    int size();
    CellState getState(int i);
    void setState(int i, CellState state);
    List<Hint> getHints();

    void performSolveStep();

    /**
     * @return true if done and correct
     */
    boolean isOk();
}

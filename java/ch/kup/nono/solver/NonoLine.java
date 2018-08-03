package ch.kup.nono.solver;

import java.util.List;

public interface NonoLine {
    int size();
    Nonogram.State getState(int i);
    void setState(int i, Nonogram.State state);
    List<Hint> getHints();

    void performSolveStep();

    /**
     * @return true if done and correct
     */
    boolean isOk();
}

package ch.kup.nono.solver;

import java.util.*;
import java.util.stream.Collectors;

import static ch.kup.nono.solver.Nonogram.State.*;

class Nonogram {
    private List<List<Hint>> rowHints = new ArrayList<>();
    private List<List<Hint>> colHints = new ArrayList<>();
    private Nonostate state;

    private Set<Nonostate> solutionsContainer;

    int getRows() {
        return rowHints.size();
    }

    int getCols() {
        return colHints.size();
    }

    public Nonostate getNonostate() {
        return state;
    }

    public enum State {
        UNTOUCHED('.'), EMPTY('-'), FILLED('X');

        private char digit;

        State(char digit) {
            this.digit = digit;
        }

        public char digit() {
            return digit;
        }

    }

    public List<List<Hint>> getRowHints() {
        return rowHints;
    }

    public List<List<Hint>> getColHints() {
        return colHints;
    }

    State getState(int row, int col) {
        if (state == null) return State.UNTOUCHED;
        return state.get(row, col);
    }

    void solve() {
        if (state == null) {
            state = new Nonostate(getRows(), getCols());
        }
        doDeterministicSolveUntilNoFurtherChange(state);
        System.out.println("Untouched pieces: " + state.getUntouchedCount());
    }

    void solve2(Runnable callback) {
        probeSolveFilled(state, callback);
        doDeterministicSolveUntilNoFurtherChange(state);
        System.out.println("Untouched pieces: " + state.getUntouchedCount());
        probeSolveEmpty(state, callback);
        doDeterministicSolveUntilNoFurtherChange(state);
        System.out.println("Untouched pieces: " + state.getUntouchedCount());
    }

    private void probeSolveFilled(Nonostate probeState, Runnable callback) {
        probeSolve(probeState, FILLED, callback);
    }

    private void probeSolveEmpty(Nonostate probeState, Runnable callback) {
        probeSolve(probeState, EMPTY, callback);
    }

    private void probeSolve(Nonostate probeState, State newState, Runnable callback) {
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                if (probeState.get(row, col) == UNTOUCHED) {
                    Nonostate testState = Nonostate.copyOf(probeState);
                    testState.set(row, col, newState);
                    try {
                        doDeterministicSolveUntilNoFurtherChange(testState);
                    } catch (Exception e) {
                        probeState.set(row, col, newState == FILLED ? EMPTY : FILLED);
                        doDeterministicSolveUntilNoFurtherChange(probeState);
                        callback.run();
                    }
                }
            }
        }
    }

    void toggleState() {
        ArrayList<Nonostate> list = new ArrayList<>(solutionsContainer);
        int i = list.indexOf(state);
        i++;
        i %= list.size();
        state = list.get(i);
    }

    private Set<Nonostate> solveRecurisvely(Nonostate someState, int depth) {
        Set<Nonostate> solutions = new HashSet<>();
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                if (someState.get(row, col) == State.UNTOUCHED) {
                    solutions.addAll(trySettingFieldXY(someState, row, col, depth));
                }
            }
        }
        return solutions;
    }

    /**
     * @return the solutions after setting the field (or null)
     */
    private Set<Nonostate> trySettingFieldXY(Nonostate someState, int row, int col, int depth) {
        if (depth < 5) System.out.printf("Trying %d:%d%n", row, col);
        if (depth % 10 == 0) System.out.println("Depth = " + depth);
        Nonostate myState = Nonostate.copyOf(someState);
        myState.set(row, col, State.FILLED);
        try {
            doDeterministicSolveUntilNoFurtherChange(myState);
            if (depth == 100) System.out.println(myState.getUntouchedCount());
            if (isDoneAndOk(myState)) {
                System.out.println("Got one!");
                return Collections.singleton(myState);
            }
            if (myState.getUntouchedCount() == 0) {
                return Collections.emptySet();
            }
        } catch (Exception e) {
            return Collections.emptySet();
        }

        // there was no problem up to now. Try deeper
        return solveRecurisvely(myState, depth + 1);
    }

    private void doDeterministicSolveUntilNoFurtherChange(Nonostate someState) {
        int diff;
        do {
            int untouchedCount = someState.getUntouchedCount();
            deterministicSolve(someState);
            diff = untouchedCount - someState.getUntouchedCount();
        } while (diff != 0);
    }

    private void deterministicSolve(Nonostate someState) {
        getAllLines(someState).forEach(NonoLine::performSolveStep);
    }

    private boolean isDoneAndOk(Nonostate someState) {
        List<NonoLine> allLines = getAllLines(someState);
        for (NonoLine line : allLines) {
            if (!line.isOk()) return false;
        }
        return true;
    }


    private NonoLine getRow(int row, Nonostate state) {
        return new NonoLineSolver() {
            @Override
            public int size() {
                return getCols();
            }

            @Override
            public State getState(int i) {
                return state.get(row, i);
            }

            @Override
            public void setState(int i, State newState) {
                state.set(row, i, newState);
            }

            @Override
            public List<Hint> getHints() {
                return rowHints.get(row);
            }

            @Override
            public String toString() {
                return "Row " + row + ": " + super.toString();
            }
        };
    }

    private NonoLine getCol(int col, Nonostate state) {
        return new NonoLineSolver() {
            @Override
            public int size() {
                return getRows();
            }

            @Override
            public State getState(int i) {
                return state.get(i, col);
            }

            @Override
            public void setState(int i, State newState) {
                state.set(i, col, newState);
            }

            @Override
            public List<Hint> getHints() {
                return colHints.get(col);
            }

            @Override
            public String toString() {
                return "Col " + col + ": " + super.toString();
            }
        };
    }

    private List<NonoLine> getAllLines(Nonostate state) {
        List<NonoLine> res = new ArrayList<>();
        for (int i = 0; i < getRows(); i++) {
            res.add(getRow(i, state));
        }
        for (int i = 0; i < getCols(); i++) {
            res.add(getCol(i, state));
        }
        return res;
    }

    static class NonogramBuilder {
        private Nonogram nonogram = new Nonogram();

        NonogramBuilder addRow(Integer... hints) {
            nonogram.rowHints.add(Arrays.stream(hints).map(Hint::new).collect(Collectors.toList()));
            return this;
        }

        NonogramBuilder addCol(Integer... hints) {
            nonogram.colHints.add(Arrays.stream(hints).map(Hint::new).collect(Collectors.toList()));
            return this;
        }

        Nonogram build() {
            checkRowsAndColumnsHaveSameTotal();
            return nonogram;
        }

        private void checkRowsAndColumnsHaveSameTotal() {
            if (calcRowSum() != calcColSum())
                throw new IllegalStateException(
                        String.format("Rowsum (%d) not equal to colsum (%d)", calcRowSum(), calcColSum()));
        }

        private int calcRowSum() {
            int rowsum = 0;
            for (int i = 0; i < nonogram.getRows(); i++) {
                NonoLine row = nonogram.getRow(i, nonogram.state);
                rowsum += row.getHints().stream().mapToInt(Hint::getLength).sum();
            }
            return rowsum;
        }

        private int calcColSum() {
            int colsum = 0;
            for (int i = 0; i < nonogram.getCols(); i++) {
                NonoLine col = nonogram.getCol(i, nonogram.state);
                colsum += col.getHints().stream().mapToInt(Hint::getLength).sum();
            }
            return colsum;
        }
    }


}

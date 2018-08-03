package ch.kup.nono.solver;

import ch.kup.nono.CellState;
import ch.kup.nono.Hint;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static ch.kup.nono.CellState.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class NonoLineSolverTest {
    private List<Hint> hints = new ArrayList<>();
    private List<CellState> state = new ArrayList<>();
    NonoLineSolver cut;


    @Before
    public void setUp() throws Exception {
        cut = new NonoLineSolver() {
            @Override
            public int size() {
                return state.size();
            }

            @Override
            public CellState getState(int i) {
                return state.get(i);
            }

            @Override
            public void setState(int i, CellState newState) {
                state.set(i, newState);
            }

            @Override
            public List<Hint> getHints() {
                return hints;
            }
        };
    }

    private void init(int size) {
        IntStream.range(0, size).forEach(i -> state.add(CellState.UNTOUCHED));
    }

    @Test
    public void testEmptyHints() {
        init(1);
        cut.performSolveStep();
        assertThat(cut.getState(0), is(EMPTY));
    }

    @Test
    public void testNoSlack_full_1() {
        init(1);
        addHint(1);
        cut.performSolveStep();
        assertThat(cut.getState(0), is(FILLED));
    }

    @Test
    public void testNoSlack_full_3() {
        init(3);
        addHint(3);
        cut.performSolveStep();
        assertThat(cut.getState(0), is(FILLED));
        assertThat(cut.getState(1), is(FILLED));
        assertThat(cut.getState(2), is(FILLED));
    }

    @Test
    public void testNoSlack_with_empty() {
        init(3);
        addHint(1);
        addHint(1);
        cut.performSolveStep();
        assertThat(cut.getState(0), is(FILLED));
        assertThat(cut.getState(1), is(EMPTY));
        assertThat(cut.getState(2), is(FILLED));
    }

    @Test
    public void testSlack() {
        init(3);
        addHint(2);
        cut.performSolveStep();
        assertThat(cut.getState(0), is(CellState.UNTOUCHED));
        assertThat(cut.getState(1), is(FILLED));
        assertThat(cut.getState(2), is(CellState.UNTOUCHED));
    }

    @Test
    public void testSlack_with_hint() {
        init(3);
        addHint(2);
        state.set(0, FILLED);
        cut.performSolveStep();
        assertThat(cut.getState(0), is(FILLED));
        assertThat(cut.getState(1), is(FILLED));
        assertThat(cut.getState(2), is(EMPTY));
    }

    @Test
    public void testFixedByEmptyCell() {
        init(6);
        addHint(2);
        state.set(2, FILLED);
        state.set(3, EMPTY);
        cut.performSolveStep();
        assertThat(cut.getState(0), is(EMPTY));
        assertThat(cut.getState(1), is(FILLED));
        assertThat(cut.getState(2), is(FILLED));
        assertThat(cut.getState(3), is(EMPTY));
        assertThat(cut.getState(4), is(EMPTY));
    }


    @Test
    public void testFixedByBeginning() {
        init(4);
        addHint(2);
        state.set(0, FILLED);
        cut.performSolveStep();
        assertThat(cut.getState(0), is(FILLED));
        assertThat(cut.getState(1), is(FILLED));
        assertThat(cut.getState(2), is(EMPTY));
        assertThat(cut.getState(3), is(EMPTY));
    }


    @Test
    public void testSubline() {
        init(10);
        addHint(1);
        addHint(2);
        addHint(3);
        state.set(0, FILLED);
        state.set(1, EMPTY);
        state.set(2, FILLED);
        cut.performSolveStep();
        System.out.println(cut);
        assertThat(cut.getState(0), is(FILLED));
        assertThat(cut.getState(1), is(EMPTY));
        assertThat(cut.getState(2), is(FILLED));
        assertThat(cut.getState(3), is(FILLED));
        assertThat(cut.getState(4), is(EMPTY));
    }

    @Test
    public void testSublineLeft() {
        init(6);
        addHint(1);
        addHint(1);
        addHint(1);
        state.set(1, EMPTY);
        state.set(2, FILLED);
        state.set(3, EMPTY);
        cut.performSolveStep();
        System.out.println(cut);
        assertThat(cut.getState(0), is(FILLED));
    }

    @Test
    public void testMarkUnreachableAsEmpty() {
        init(4);
        addHint(2);
        state.set(2, FILLED);
        cut.performSolveStep();
        assertThat(cut.getState(0), is(EMPTY));
        assertThat(cut.getState(1), is(UNTOUCHED));
        assertThat(cut.getState(2), is(FILLED));
        assertThat(cut.getState(3), is(UNTOUCHED));
    }

    @Test
    public void testMarkUnreachableAsEmpty_FromRightSide() {
        init(4);
        addHint(2);
        state.set(1, FILLED);
        cut.performSolveStep();
        assertThat(cut.getState(0), is(UNTOUCHED));
        assertThat(cut.getState(1), is(FILLED));
        assertThat(cut.getState(2), is(UNTOUCHED));
        assertThat(cut.getState(3), is(EMPTY));
    }

    @Test
    public void testAnchorWithUnclearHint() {
        init(7);
        addHint(1);
        addHint(1);
        addHint(1);
        state.set(0, FILLED);
        state.set(1, EMPTY);
        state.set(2, FILLED);
        state.set(5, EMPTY);
        cut.performSolveStep();
        assertThat(cut.getState(3), is(EMPTY));
    }

    @Test
    public void testLonelyAnchor() {
        init(40);
        addHint(3);
        addHint(7);
        state.set(34, FILLED);
        cut.performSolveStep();
        assertThat(cut.getState(33), is(FILLED));
    }

    @Test
    public void testFilledNearABoundary() {
        init(7);
        addHint(1);
        addHint(1);
        state.set(0, EMPTY);
        state.set(1, EMPTY);
        state.set(3, FILLED);
        cut.performSolveStep();
        assertThat(cut.getState(2), is(EMPTY));
        assertThat(cut.getState(4), is(EMPTY));
    }

    private void addHint(int h) {
        hints.add(new Hint(h));
    }

}
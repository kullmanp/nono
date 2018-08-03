package ch.kup.nono;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class NonogramTest {

    @Test
    public void testSimpleFullRow() {
        Nonogram nonogram = new Nonogram.NonogramBuilder().addRow(3)
                .addCol(1).addCol(1).addCol(1).build();
        nonogram.solveWithLineSolver();
        assertThat(nonogram.getState(0, 0), is(CellState.FILLED));
        assertThat(nonogram.getState(0, 1), is(CellState.FILLED));
        assertThat(nonogram.getState(0, 2), is(CellState.FILLED));
    }

    @Test
    public void testSimpleFullCol() {
        Nonogram nonogram = new Nonogram.NonogramBuilder().addCol(3)
                .addRow(1).addRow(1).addRow(1).build();
        nonogram.solveWithLineSolver();
        assertThat(nonogram.getState(0, 0), is(CellState.FILLED));
        assertThat(nonogram.getState(1, 0), is(CellState.FILLED));
        assertThat(nonogram.getState(2, 0), is(CellState.FILLED));
    }

    @Test
    public void testEmptySimplestEmptyCell() {
        Nonogram nonogram = new Nonogram.NonogramBuilder()
                .addCol(1)
                .addCol(2)

                .addRow(2)
                .addRow(1)
                .build();

        nonogram.solveWithLineSolver();

        assertThat(nonogram.getState(0, 0), is(CellState.FILLED));
        assertThat(nonogram.getState(0, 1), is(CellState.FILLED));
        assertThat(nonogram.getState(1, 0), is(CellState.EMPTY));
        assertThat(nonogram.getState(1, 1), is(CellState.FILLED));
    }
}
package ch.kup.nono.ui;

import ch.kup.nono.CellState;
import ch.kup.nono.Nonogram;

import javax.swing.*;
import java.awt.*;

class Nonoframe extends JPanel {
    private final Nonogram nonogram;

    private static final int MARGIN = 40;
    private static final int CELL_SIZE = 20;

    Nonoframe(Nonogram nonogram) {
        this.nonogram = nonogram;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawState(g.create());
        drawGrid((Graphics2D) g.create());
    }

    private void drawState(Graphics g) {
        for (int row = 0; row < nonogram.getRows(); row++) {
            for (int col = 0; col < nonogram.getCols(); col++) {
                drawStateOfCell(g, row, col, nonogram.getState(row, col));
            }
        }
    }

    private void drawStateOfCell(Graphics g, int row, int col, CellState state) {
        int x = MARGIN + col * CELL_SIZE + 1;
        int y = MARGIN + row * CELL_SIZE + 1;
        g.setColor(getColor(state));
        g.fillRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
    }

    private Color getColor(CellState state) {
        switch (state) {
            case FILLED:
                return Color.BLACK;
            case EMPTY:
                return Color.WHITE;
            case UNTOUCHED:
                return Color.GRAY;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void drawGrid(Graphics2D g) {
        for (int row = 0; row < nonogram.getRows(); row++) {
            drawTopOfRow(g, row);
        }
        drawBottomOfLastRow(g);
        for (int col = 0; col < nonogram.getCols(); col++) {
            drawLeftOfColumn(g, col);
        }
        drawRightOfLastCol(g);
    }

    private void drawLeftOfColumn(Graphics2D g, int col) {
        int x = MARGIN + col * CELL_SIZE;
        g.setStroke(col % 5 == 0 ? getThickStroke() : getNormalStroke());
        g.drawLine(x, MARGIN, x, MARGIN + nonogram.getRows() * CELL_SIZE);
    }

    private void drawTopOfRow(Graphics2D g, int row) {
        int y = MARGIN + row * CELL_SIZE;
        g.setStroke(row % 5 == 0 ? getThickStroke() : getNormalStroke());
        g.drawLine(MARGIN, y, MARGIN + nonogram.getCols() * CELL_SIZE, y);
    }

    private void drawRightOfLastCol(Graphics2D g) {
        drawLeftOfColumn(g, nonogram.getCols());
    }

    private void drawBottomOfLastRow(Graphics2D g) {
        drawTopOfRow(g, nonogram.getRows());
    }

    private BasicStroke getNormalStroke() {
        return new BasicStroke(1);
    }

    private BasicStroke getThickStroke() {
        return new BasicStroke(2);
    }

    void solveWithLineSolver() {
        nonogram.solveWithLineSolver();
        repaint();
    }

    void solveWithProbe() {
        nonogram.solveWithProbe();
        repaint();
    }

}

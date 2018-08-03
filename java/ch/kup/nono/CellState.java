package ch.kup.nono;

public enum CellState {
    UNTOUCHED('.'), EMPTY('-'), FILLED('X');

    private char digit;

    CellState(char digit) {
        this.digit = digit;
    }

    public char digit() {
        return digit;
    }

}

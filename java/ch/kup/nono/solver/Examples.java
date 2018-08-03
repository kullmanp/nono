package ch.kup.nono.solver;

import ch.kup.nono.solver.Nonogram.NonogramBuilder;

public class Examples {

    public static Nonogram createExample10() {
        NonogramBuilder builder = new NonogramBuilder();
        builder
                .addCol(1, 11)
                .addCol(1, 5, 2, 2)
                .addCol(9, 3)
                .addCol(5, 1, 5)
                .addCol(4, 1, 5)

                .addCol(4, 2, 1, 1)
                .addCol(2, 1, 1, 2, 1)
                .addCol(2, 1, 1, 1)
                .addCol(4, 1, 1, 1, 2)
                .addCol(3, 2, 2, 4)

                .addCol(4, 1, 3)
                .addCol(2, 8, 1)
                .addCol(1, 4, 3)
                .addCol(1, 1, 3, 3)
                .addCol(3, 1, 1)

                .addRow(4, 3)
                .addRow(11, 1)
                .addRow(1, 4, 6)
                .addRow(6, 1, 1)
                .addRow(4, 1, 1)

                .addRow(3, 3, 5, 1)
                .addRow(3, 1, 1, 4)
                .addRow(4, 4)
                .addRow(1, 1, 5, 3)
                .addRow(1, 2, 1, 1, 1)

                .addRow(2, 2, 2, 3)
                .addRow(2, 3, 6)
                .addRow(1, 3, 5, 2)
                .addRow(5, 2, 1, 2)
                .addRow(3, 1, 1)
        ;

        return builder.build();
    }


    public static Nonogram createExample13() {
        NonogramBuilder builder = new NonogramBuilder();
        builder
                .addCol(1, 1)
                .addCol(1, 2)
                .addCol(1, 2, 3)
                .addCol(10)
                .addCol(1, 4, 4)

                .addCol(15)
                .addCol(1, 4, 4)
                .addCol(10)
                .addCol(2, 3)
                .addCol(3)

                .addCol(1, 1, 1, 3)
                .addCol(3, 1, 1, 3)
                .addCol(2, 2, 1, 2)
                .addCol(5, 1)
                .addCol(3)

                .addRow(1)
                .addRow(3, 3)
                .addRow(1, 5)
                .addRow(1, 1, 1, 1, 2)
                .addRow(1, 3, 1, 3)

                .addRow(5, 3)
                .addRow(7)
                .addRow(7, 1, 1)
                .addRow(1, 1, 1, 1)
                .addRow(1, 1, 1)

                .addRow(1, 1, 1)
                .addRow(5)
                .addRow(10)
                .addRow(12)
                .addRow(14)
        ;

        return builder.build();
    }


    public static Nonogram createExample52() {
        NonogramBuilder builder = new NonogramBuilder();
        builder
                .addCol(26)
                .addCol(15, 13)
                .addCol(22, 9)
                .addCol(12, 9, 5)
                .addCol(6, 1, 4, 3, 3)

                .addCol(4, 1, 1, 4, 2, 6)
                .addCol(5, 1, 2, 5, 3, 2)
                .addCol(2, 2, 1, 1, 7)
                .addCol(3, 1, 1, 2, 6)
                .addCol(1, 3, 1, 3, 5)

                .addCol(2, 3, 1, 1, 2, 3)
                .addCol(3, 2, 1, 1, 3, 7)
                .addCol(4, 1, 3, 2, 4)
                .addCol(5, 1, 3, 4)
                .addCol(5, 1, 2, 3, 1, 2)

                .addCol(6, 1, 4, 2, 1, 2)
                .addCol(1, 6, 1, 3, 2, 1, 4)
                .addCol(1, 5, 2, 2, 5, 6)
                .addCol(2, 6, 2, 1, 4, 1, 4)
                .addCol(2, 5, 1, 5, 2, 2)

                .addCol(2, 4, 1, 5, 1, 1)
                .addCol(1, 2, 3, 1, 5, 1)
                .addCol(1, 3, 4, 1, 5, 1)
                .addCol(1, 1, 3, 3, 1, 6, 1, 8)
                .addCol(2, 1, 2, 4, 4, 11, 1)

                .addCol(2, 1, 7, 6, 4, 1, 1)
                .addCol(1, 2, 8, 2, 6, 1, 1)
                .addCol(2, 2, 9, 1, 5, 1, 1)
                .addCol(3, 2, 6, 1, 1, 4, 1, 1)
                .addCol(7, 4, 1, 1, 1, 1, 1)

                .addCol(3, 1, 1, 3, 4, 1, 2)
                .addCol(8, 3, 2, 2, 1, 1, 2)
                .addCol(8, 4, 3, 2)
                .addCol(8, 7, 7, 5)
                .addCol(24)

                .addCol(22)
                .addCol(8, 1, 10)
                .addCol(7, 1, 9)
                .addCol(6, 1, 9)
                .addCol(5, 1, 7)

                .addRow(12, 3, 1, 2, 5)
                .addRow(7, 6, 2, 1, 2, 5)
                .addRow(6, 2, 6, 2, 1, 2, 5)
                .addRow(6, 1, 6, 3, 1, 2, 1, 3)
                .addRow(4, 2, 2, 6, 3, 1, 3, 3)

                .addRow(5, 1, 1, 6, 3, 2, 4)
                .addRow(3, 1, 1, 2, 6, 4, 1, 4)
                .addRow(3, 1, 7, 4, 5)
                .addRow(3, 12, 6)
                .addRow(7, 13, 5)

                .addRow(4, 3, 3, 8, 1, 5)
                .addRow(4, 9, 4)
                .addRow(3, 4, 8)
                .addRow(3, 2, 7)
                .addRow(3, 1, 4, 8)

                .addRow(3, 3, 7)
                .addRow(3, 2, 3, 3)
                .addRow(1, 2, 2, 4, 4, 2)
                .addRow(1, 2, 3, 2, 1, 3, 1, 1)
                .addRow(1, 2, 4, 2, 3, 1)

                .addRow(1, 3, 2, 1, 1, 3, 1, 4, 1)
                .addRow(1, 3, 11, 1, 4, 6)
                .addRow(2, 3, 1, 3, 1, 5, 6)
                .addRow(2, 3, 8, 1, 6, 7)
                .addRow(2, 1, 2, 6, 1, 5, 7)

                .addRow(2, 1, 3, 3, 1, 5, 8)
                .addRow(3, 1, 3, 2, 5, 8)
                .addRow(3, 1, 4, 1, 6, 9)
                .addRow(3, 1, 5, 8, 1, 6)
                .addRow(4, 1, 15, 1, 1, 2)

                .addRow(4, 16, 2, 1)
                .addRow(5, 4, 1, 2, 2, 1, 1, 1)
                .addRow(7, 1, 1, 1, 9)
                .addRow(6, 1, 1, 2, 1, 1)
                .addRow(3, 1, 1, 5, 10)

                .addRow(1, 1, 1, 1, 1)
                .addRow(1, 1, 3, 1, 1)
                .addRow(1, 1, 5, 1, 2, 1)
                .addRow(1, 1, 6, 1, 2, 1)
                .addRow(1, 1, 1, 3, 1, 1, 1)

        ;

        return builder.build();
    }

    public static String printAsTealNonogram(Nonogram nonogram) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"ver\":[");
        printRows(nonogram, sb);
        sb.append("]");
        sb.append(", \"hor\":[");
        printColumns(nonogram, sb);
        sb.append("]}");

        return sb.toString();
    }

    private static void printRows(Nonogram nonogram, StringBuilder sb) {
        nonogram.getRowHints().stream()
                .forEach(l -> {
                    sb.append(l.toString());
                    sb.append(",");
                });
        sb.deleteCharAt(sb.length() - 1);
    }

    private static void printColumns(Nonogram nonogram, StringBuilder sb) {
        nonogram.getColHints().stream()
                .forEach(l -> {
                    sb.append(l.toString());
                    sb.append(",");
                });
        sb.deleteCharAt(sb.length() - 1);
    }

    public static void main(String[] args) {
        System.out.println(printAsTealNonogram(createExample52()));
    }
}

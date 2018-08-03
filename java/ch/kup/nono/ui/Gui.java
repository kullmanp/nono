package ch.kup.nono.ui;

import ch.kup.nono.Examples;

import javax.swing.*;
import java.awt.*;

public class Gui {
    public Gui() {
        JFrame frame = new JFrame("Nono");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Nonoframe nonoframe = new Nonoframe(Examples.createExample52());
        frame.add(nonoframe, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton button = new JButton("Line solver");
        button.addActionListener(e -> {
            nonoframe.solveWithLineSolver();
        });
        buttonPanel.add(button);

        JButton button2 = new JButton("Probe solver");
        button2.addActionListener(e -> {
            nonoframe.solveWithProbe();
        });
        buttonPanel.add(button2);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setSize(400, 300);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}

package ch.kup.nono.solver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui {
    public Gui() {
        JFrame frame = new JFrame("Nono");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Nonoframe nonoframe = new Nonoframe(Examples.createExample52());
        frame.add(nonoframe, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton button = new JButton("Go");
        button.addActionListener(e -> {
            nonoframe.go();
        });
        buttonPanel.add(button);

        JButton button2 = new JButton("Go2");
        button2.addActionListener(e -> {
            nonoframe.go2();
        });
        buttonPanel.add(button2);

        JButton buttonToggle = new JButton("Toggle");
        buttonToggle.addActionListener(e -> {
            nonoframe.toggle();
        });
        buttonPanel.add(buttonToggle);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setSize(400, 300);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}

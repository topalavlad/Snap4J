package com.snap4j.demo;

import com.snap4j.Snap4JListener;

import javax.swing.*;
import java.awt.*;

/**
 * @author vlad.topala
 */
public class Snap4JDemo {

    public Snap4JDemo() {
        JFrame f = new JFrame();
        JFrame f1 = new JFrame();
        f.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        f.setUndecorated(true);
        f.setBounds(new Rectangle(100, 100, 300, 300));
        f.add(new JButton("On"));
        f.pack();
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f1.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        f1.setBounds(new Rectangle(100, 100, 300, 300));
        f1.setUndecorated(true);
        f1.add(new JButton("Test"));
        f1.pack();
        f1.setVisible(true);
        f1.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        new Snap4JListener();
    }

    public static void main(String[] args) {
        new Snap4JDemo();
    }
}

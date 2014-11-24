package com.snap4j.demo;

import com.snap4j.OverlayGenerator;
import com.snap4j.ScreenEdgeSnapper;
import com.snap4j.State;
import com.snap4j.StateGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * @author vlad.topala
 */
public class Snap4JDemo {

    private final JButton button;
    ScreenEdgeSnapper snapper = new ScreenEdgeSnapper();
    StateGenerator stateGenerator = new StateGenerator();
    private OverlayGenerator overlayGenerator = new OverlayGenerator();
    private boolean on = true;
    private boolean dragged = false;

    public Snap4JDemo() {
        JFrame f = new JFrame();
        JFrame f1 = new JFrame();
        f.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        f.setUndecorated(true);
        button = new JButton("On");
        f.add(button);
        f.pack();
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f1.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        f1.setUndecorated(true);
        f1.add(new JButton("Test"));
        f1.pack();
        f1.setVisible(true);
        f1.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.setText(on ? "Off" : "On");
                on = !on;
            }
        });

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (on && event.getSource() instanceof JFrame) {
                    JFrame source = (JFrame) event.getSource();
                    Point location = MouseInfo.getPointerInfo().getLocation();
                    State nextState;
                    switch (event.getID()) {
                        case MouseEvent.MOUSE_DRAGGED:
                            nextState = stateGenerator.getNextState(source, location, true);
                            if (shouldReSnap(nextState, source)) {
                                overlayGenerator.generate(nextState);
                                dragged = true;
                            } else {
                                overlayGenerator.clear();
                            }
                            break;
                        case MouseEvent.MOUSE_RELEASED:
                            if (!dragged) {
                                break;
                            }
                            overlayGenerator.clear();
                            nextState = stateGenerator.getNextState(source, location);
                            if (shouldReSnap(nextState, source)) {
                                snapper.setState(source, nextState);
                                dragged = false;
                            }
                            break;
                    }
                }
            }
        }, AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
    }

    public static void main(String[] args) {
        new Snap4JDemo();
    }

    private boolean shouldReSnap(State nextState, JFrame source) {
        return nextState.getFrameState() != source.getExtendedState() || (nextState.getFrameState() == Frame.MAXIMIZED_BOTH && !nextState.getBounds().equals(source.getBounds()));
    }
}

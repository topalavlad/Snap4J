package com.snap4j;

import javax.swing.*;
import java.awt.*;

/**
 * @author vlad.topala
 */
public class OverlayGenerator {

    private static final Rectangle DUMMY_BOUNDS = new Rectangle(0, 0, 0, 0);
    private Rectangle currentBounds;
    private JWindow jWindow;

    public OverlayGenerator() {
        jWindow = new JWindow();
        currentBounds = DUMMY_BOUNDS;
        jWindow.getRootPane().setBorder(BorderFactory.createLineBorder(Color.ORANGE, 3, true));
        jWindow.setOpacity(0.4f);
    }

    /**
     * I'll assume that there will never be two overlays required at the same time
     *
     * @param state - size and location of overlay
     */
    public void generate(State state) {
        Rectangle bounds = state.getBounds();
        if (!bounds.equals(currentBounds)) {
            jWindow.setVisible(true);
            currentBounds = bounds;
            jWindow.setBounds(bounds);
        }
    }

    public void clear() {
        currentBounds = DUMMY_BOUNDS;
        jWindow.dispose();
    }
}

package com.snap4j;

import javax.swing.*;
import java.awt.*;

/**
 * @author vlad.topala
 *         This class sets the size and state of a frame
 */
public class ScreenEdgeSnapper {
    public void setState(JFrame source, State nextState) {
        Rectangle bounds = nextState.getBounds();
        if (bounds.height != 0 && bounds.width != 0) {
            source.setBounds(bounds);
        }
        source.setExtendedState(nextState.getFrameState());
    }
}

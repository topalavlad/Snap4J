package com.snap4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;

/**
 * @author vlad.topala
 *         This is the main class of this library. Once this is instantiated it will listen for
 *         all drag events on JFrames.
 */
public class Snap4JListener {
    ScreenEdgeSnapper snapper = new ScreenEdgeSnapper();
    StateGenerator stateGenerator = new StateGenerator();
    private OverlayGenerator overlayGenerator = new OverlayGenerator();
    private boolean dragged = false;

    public Snap4JListener() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (event.getSource() instanceof JFrame) {
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

    private boolean shouldReSnap(State nextState, JFrame source) {
        return nextState.getFrameState() != source.getExtendedState() || (nextState.getFrameState() == Frame.MAXIMIZED_BOTH && !nextState.getBounds().equals(source.getBounds()));
    }
}

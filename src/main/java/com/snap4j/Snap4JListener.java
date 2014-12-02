package com.snap4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.util.EnumSet;

/**
 * @author vlad.topala
 *         This is the main class of this library. Once this is instantiated it will listen for
 *         all drag events on JFrames.
 */
public class Snap4JListener {

    private static final Point ORIGIN = new Point(0, 0);
    private final ScreenEdgeSnapper snapper = new ScreenEdgeSnapper();
    private final StateGenerator stateGenerator;
    private final OverlayGenerator overlayGenerator = new OverlayGenerator();
    private boolean dragged = false;

    public Snap4JListener() {
        this(EnumSet.allOf(NextWindowState.class));
    }

    public Snap4JListener(EnumSet<NextWindowState> states) {
        stateGenerator = new StateGenerator(states);
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (event.getSource() instanceof JFrame) {
                    JFrame source = (JFrame) event.getSource();
                    Point location = MouseInfo.getPointerInfo().getLocation();
                    State nextState;
                    switch (event.getID()) {
                        case MouseEvent.MOUSE_PRESSED:
                            pressedPoint = ((MouseEvent) event).getPoint();
                            break;
                        case MouseEvent.MOUSE_DRAGGED:
                            nextState = stateGenerator.getNextState(source, location, true, pressedPoint);
                            if (shouldReSnap(nextState, source)) {
                                overlayGenerator.generate(nextState);
                                dragged = true;
                            } else {
                                overlayGenerator.clear();
                            }
                            break;
                        case MouseEvent.MOUSE_RELEASED:
                            if (dragged) {
                                overlayGenerator.clear();
                                nextState = stateGenerator.getNextState(source, location, false, pressedPoint);
                                if (shouldReSnap(nextState, source)) {
                                    snapper.setState(source, nextState);
                                    dragged = false;
                                }
                            }
                            pressedPoint = ORIGIN;
                            break;
                    }
                }
            }

            private Point pressedPoint = ORIGIN;
        }, AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
    }

    /**
     * This method is useful for when a frame is opened directly maximized but the application
     * using this library previously saved some normal state bounds
     *
     * @param frame  - the frame for which we'll set the bounds
     * @param bounds - the bounds
     */
    @SuppressWarnings("unused")
    public void setLastBounds(JFrame frame, Rectangle bounds) {
        stateGenerator.setLastBounds(frame, bounds);
    }

    private boolean shouldReSnap(State nextState, JFrame source) {
        return nextState.getFrameState() != source.getExtendedState() || (nextState.getFrameState() == Frame.MAXIMIZED_BOTH && !nextState.getBounds().equals(source.getBounds()));
    }
}

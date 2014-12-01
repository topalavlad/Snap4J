package com.snap4j;

import javax.swing.*;
import java.awt.*;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vlad.topala
 *         The state generator checks each possible next state until
 *         it finds a valid one
 */
public class StateGenerator {

    private static final int SNAP_DISTANCE = 5;
    private final EnumSet<NextWindowState> states;
    private Map<JFrame, Rectangle> lastBounds = new HashMap<>();

    public StateGenerator(EnumSet<NextWindowState> states) {
        this.states = states;
    }

    public State getNextState(JFrame source, Point mousePosition, boolean readOnly, Point pressedPoint) {
        State nextState = State.DUMMY_STATE;
        int mouseX = mousePosition.x;
        int mouseY = mousePosition.y;

        if (!readOnly && source.getExtendedState() != Frame.MAXIMIZED_BOTH && source.getExtendedState() != Frame.MAXIMIZED_VERT) {
            saveBounds(source);
        }
        if (!lastBounds.containsKey(source)) {
            lastBounds.put(source, State.DUMMY_STATE.getBounds());
        }
        for (NextWindowState windowState : states) {
            nextState = windowState.getNextState(mouseX, mouseY, SNAP_DISTANCE);
            if (!nextState.equals(State.DUMMY_STATE)) {
                break;
            }
        }
        if (nextState.getFrameState() == Frame.NORMAL) {
            Rectangle lastBoundsForSource = lastBounds.get(source);
            nextState = new State(Frame.NORMAL, new Rectangle(mouseX - getXRelativeToMouse(source.getWidth(),
                    lastBoundsForSource.getWidth(), pressedPoint), mouseY - pressedPoint.y,
                    lastBoundsForSource.width, lastBoundsForSource.height));
        }
        return nextState;
    }

    private int getXRelativeToMouse(double currentWidth, double normalWidth, Point pressedPoint) {
        return (int) ((normalWidth / currentWidth) * pressedPoint.getX());
    }

    private void saveBounds(JFrame source) {
        lastBounds.put(source, source.getBounds());
    }

    public void setLastBounds(JFrame frame, Rectangle bounds) {
        lastBounds.put(frame, bounds);
    }
}

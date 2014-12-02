package com.snap4j;

import javax.swing.*;
import java.awt.*;
import java.util.EnumSet;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author vlad.topala
 *         The state generator checks each possible next state until
 *         it finds a valid one
 */
public class StateGenerator {

    private static final int SNAP_DISTANCE = 5;
    private final EnumSet<NextWindowState> states;
    private Map<JFrame, Rectangle> lastBounds = new WeakHashMap<>();

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
            lastBounds.put(source, source.getExtendedState() == Frame.NORMAL ?
                    State.DUMMY_STATE.getBounds()
                    : new Rectangle(0, 0, source.getWidth() / 2, source.getHeight() / 2));
        }
        for (NextWindowState windowState : states) {
            nextState = windowState.getNextState(mouseX, mouseY, SNAP_DISTANCE);
            if (!nextState.equals(State.DUMMY_STATE)) {
                break;
            }
        }
        if (nextState.getFrameState() == Frame.NORMAL) {
            Rectangle lastBoundsForSource = lastBounds.get(source);
            nextState = new State(Frame.NORMAL, new Rectangle(
                    mouseX - getCoordRelativeToMouse(source.getWidth(), lastBoundsForSource.getWidth(), pressedPoint.getX()),
                    mouseY - getCoordRelativeToMouse(source.getHeight(), lastBoundsForSource.getHeight(), pressedPoint.getY()),
                    lastBoundsForSource.width, lastBoundsForSource.height));
        }
        return nextState;
    }

    private int getCoordRelativeToMouse(double currentLength, double normalLength, double pressedCoordinate) {
        return (int) ((normalLength / currentLength) * pressedCoordinate);
    }

    private void saveBounds(JFrame source) {
        lastBounds.put(source, source.getBounds());
    }

    public void setLastBounds(JFrame frame, Rectangle bounds) {
        lastBounds.put(frame, bounds);
    }
}

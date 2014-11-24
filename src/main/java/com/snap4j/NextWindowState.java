package com.snap4j;

import com.snap4j.utils.SwingUtil;

import java.awt.*;

/**
 * @author vlad.topala
 *         Currently supported window states
 *         In the future these might be transformed into an XML like description to make
 *         it easier to add new states
 *         On Windows JFrames MAXIMIZED_VERT is not supported so an implementation will be necessary
 *         in order to revert a frame to the initial size after snapping to screen sides
 */
public enum NextWindowState {
    MAXIMIZED() {
        @Override
        public State getNextState(int mouseX, int mouseY, int snapDistance) {
            Rectangle screenBoundsForPoint = SwingUtil.getScreenBoundsForPoint(new Point(mouseX, mouseY));
            return (screenBoundsForPoint.y <= mouseY && mouseY <= screenBoundsForPoint.y + snapDistance
                    && LEFT_MAXIMIZED_VERT.getNextState(mouseX, mouseY, snapDistance).equals(State.DUMMY_STATE)
                    && RIGHT_MAXIMIZED_VERT.getNextState(mouseX, mouseY, snapDistance).equals(State.DUMMY_STATE))
                    ? new State(Frame.MAXIMIZED_BOTH, new Rectangle(screenBoundsForPoint))
                    : State.DUMMY_STATE;
        }
    },
    LEFT_MAXIMIZED_VERT() {
        @Override
        public State getNextState(int mouseX, int mouseY, int snapDistance) {
            Rectangle screenBoundsForPoint = SwingUtil.getScreenBoundsForPoint(new Point(mouseX, mouseY));
            return screenBoundsForPoint.x <= mouseX && mouseX <= screenBoundsForPoint.x + snapDistance
                    ? new State(Frame.MAXIMIZED_VERT, new Rectangle(screenBoundsForPoint.x, screenBoundsForPoint.y,
                    screenBoundsForPoint.width / 2, screenBoundsForPoint.height))
                    : State.DUMMY_STATE;
        }

    },
    RIGHT_MAXIMIZED_VERT() {
        @Override
        public State getNextState(int mouseX, int mouseY, int snapDistance) {
            Rectangle screenBoundsForPoint = SwingUtil.getScreenBoundsForPoint(new Point(mouseX, mouseY));
            int rightX = screenBoundsForPoint.x + screenBoundsForPoint.width;
            return (rightX - snapDistance <= mouseX && mouseX <= rightX)
                    ? new State(Frame.MAXIMIZED_VERT, new Rectangle(screenBoundsForPoint.x + screenBoundsForPoint.width / 2, 0,
                    screenBoundsForPoint.width / 2, screenBoundsForPoint.height))
                    : State.DUMMY_STATE;
        }
    },
    NORMAL() {
        @Override
        public State getNextState(int mouseX, int mouseY, int snapDistance) {
            return new State(Frame.NORMAL, new Rectangle(0, 0, 0, 0));
        }
    };

    public State getNextState(int mouseX, int mouseY, int snapDistance) {
        return new State(0, new Rectangle(0, 0, 0, 0));
    }
}

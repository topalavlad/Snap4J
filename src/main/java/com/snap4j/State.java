package com.snap4j;

import java.awt.*;

/**
 * @author vlad.topala
 */
public class State {
    public static final State DUMMY_STATE = new State(-1, new Rectangle(0, 0, 0, 0));
    private final int frameState;
    private final Rectangle bounds;

    public State(int frameState, Rectangle bounds) {
        this.frameState = frameState;
        this.bounds = bounds;
    }

    public int getFrameState() {
        return frameState;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}

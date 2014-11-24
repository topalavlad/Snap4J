package com.snap4j.utils;

import java.awt.*;

/**
 * @author vlad.topala
 */
public class SwingUtil {

    /**
     * Used for getting the actual bound of the monitor that contains Point p
     *
     * @param p - point to check monitor for
     * @return - current monitor bounds
     */
    public static Rectangle getScreenBoundsForPoint(Point p) {
        Rectangle bounds;
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();

        GraphicsConfiguration graphicsConfiguration = null;
        for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            if (gd.getDefaultConfiguration().getBounds().contains(p)) {
                graphicsConfiguration = gd.getDefaultConfiguration();
                break;
            }
        }
        if (graphicsConfiguration != null) {
            bounds = graphicsConfiguration.getBounds();
            Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(graphicsConfiguration);
            bounds.x += screenInsets.left;
            bounds.y += screenInsets.top;
            bounds.height -= screenInsets.bottom;
            bounds.width -= screenInsets.right;
        } else {
            bounds = env.getMaximumWindowBounds();
        }
        return (bounds);
    }

}

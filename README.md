Snap4J
======

Small library that enables frames without native decorations to snap to and from the screen edges.

The easiest way to use this library is adding the following line of code
    snap4JListener = new Snap4JListener();
or if you want to limit the possible states:
    snap4JListener = new Snap4JListener(EnumSet.of(NextWindowState.NORMAL, NextWindowState.MAXIMIZED));

By instantiating a Snap4JListener() all drag events on JFrame instances will be handled.

Known limitations: - no restore from maximized vertically because Windows doesn't natively support
  vertically maximized Java frames.
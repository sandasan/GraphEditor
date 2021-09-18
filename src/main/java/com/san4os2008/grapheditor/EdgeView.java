package com.san4os2008.grapheditor;

import javafx.scene.shape.Line;

public class EdgeView extends Line {
    int lineIndexInGroup;

    public EdgeView(double lineStartX, double lineStartY, double lineEndX, double lineEndY) {
        super(lineStartX,
                lineStartY,
                lineEndX,
                lineEndY);
    }
}

package com.san4os2008.grapheditor;

import javafx.scene.shape.Circle;

public class VertexView extends Circle {
    int circleIndexInGroup;
    private boolean isActive;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

package com.san4os2008.grapheditor;

public class Edge {
    private double startX;
    private double endX;
    private double startY;
    private double endY;
    private int index;

    public double getStartX() {
        return startX;
    }

    public double getEndX() {
        return endX;
    }

    public double getStartY() {
        return startY;
    }

    public double getEndY() {
        return endY;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

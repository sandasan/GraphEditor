package com.san4os2008.grapheditor;

import java.io.Serializable;
import java.util.ArrayList;

public class Vertex implements Serializable {
    private double x;
    private double y;
    private int index;
    private boolean isActive;
    private ArrayList<Vertex> neighboringVertices;
    private ArrayList<Integer> neighboringVerticesIndexes;

    public Vertex() {
        super();
        neighboringVertices = new ArrayList<>();
        neighboringVerticesIndexes = new ArrayList<>();
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void addNeighboringVertex(Vertex vertex) {
        neighboringVertices.add(vertex);
    }

    public void addNeighboringVertexIndex(Integer vertexIndex) {
        neighboringVerticesIndexes.add(vertexIndex);
    }

    public void deleteNeighboringVertex(Vertex vertex) {
        if (!neighboringVertices.isEmpty()) {
            neighboringVertices.remove(vertex);
        }
    }

    public void deleteNeighboringVertexIndex(Integer vertexIndex) {
        if (!neighboringVerticesIndexes.isEmpty()) {
            neighboringVerticesIndexes.remove(vertexIndex);
        }
    }

    public ArrayList<Vertex> getNeighboringVertices() {
        return neighboringVertices;
    }

    public ArrayList<Integer> getNeighboringVerticesIndexes() {
        return neighboringVerticesIndexes;
    }

    /*void onClick() {
        if (!isActive) {
            isActive = true;
        }
    }*/
}


package com.san4os2008.grapheditor;

import java.util.ArrayList;

class VerticesManager {
//    Vertex activeNode;
    private ArrayList<Vertex> vertices;
    private int lastVertexIndex;
//    Display display;

    public VerticesManager() {
        super();
        vertices = new ArrayList<>();
        lastVertexIndex = 0;
//        display = new Display();
    }

    /*public void onClickOnNode(Vertex node) {
        if (activeNode == null) {
            activeNode = node;
        } else if (activeNode != node) {
            addVertex(activeNode);
        }
    }*/

    public void addVertex(Vertex vertex) {
        vertex.setIndex(lastVertexIndex);
        lastVertexIndex++;
        vertices.add(vertex);
    }

    private Vertex getVertexByIndex(Integer index) {
        Vertex vertex = new Vertex();
        for (Vertex currentVertex :
                vertices) {
            if (currentVertex.getIndex() == index) {
                vertex = currentVertex;
                break;
            }
        }
        return vertex;
    }

    public Vertex getVertexByCoordinates(double vertexX, double vertexY) {
        Vertex vertex = new Vertex();
        for (Vertex currentVertex :
                vertices) {
            if (currentVertex.getX() == vertexX && currentVertex.getY() == vertexY) {
                vertex = currentVertex;
                break;
            }
        }
        return vertex;
    }

    public void deleteVertex(double vertexX, double vertexY) {
        for (Vertex vertex : vertices) {
            if (vertex.getX() == vertexX && vertex.getY() == vertexY) {
                // Проходимся по вершинам-соседям и удаляем из списка соседей каждой вершины данную вершину
                /*for (Integer currentVertexIndex :
                        vertex.getNeighboringVerticesIndexes()) {
                    getVertexByIndex(currentVertexIndex).deleteNeighboringVertexIndex(vertex.getIndex());
                }*/
                for (Vertex currentVertex :
                        vertex.getNeighboringVertices()) {
                    currentVertex.deleteNeighboringVertex(vertex);
                }
                vertices.remove(vertex);
                break;
            }
        }
    }

    public int getVerticesCount() {
        return vertices.size();
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(ArrayList<Vertex> vertices) {
        this.vertices = vertices;
    }
}

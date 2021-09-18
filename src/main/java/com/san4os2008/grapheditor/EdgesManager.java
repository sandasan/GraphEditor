package com.san4os2008.grapheditor;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

class EdgesManager {
//    Vertex activeNode;
    ArrayList<Edge> edges;
    int lastEdgeIndex;
//    Display display;

    public EdgesManager() {
        super();
        edges = new ArrayList<>();
        lastEdgeIndex = 0;
//        display = new Display();
    }

    /*public void onClickOnNode(Vertex node) {
        if (activeNode == null) {
            activeNode = node;
        } else if (activeNode != node) {
            addVertex(activeNode);
        }
    }*/

    public void addEdge(Edge edge) {
        edge.setIndex(lastEdgeIndex);
        lastEdgeIndex++;
        edges.add(edge);
    }

    public Edge getEdgeByCoordinates(double startX, double startY, double endX, double endY) {
        Edge result = new Edge();
        if (!isPresent(startX,
                startY,
                endX,
                endY)) return null;
        for (Edge edge :
                edges) {
            if (edge.getStartX() == startX &&
                    edge.getStartY() == startY &&
                    edge.getEndX() == endX &&
                    edge.getEndY() == endY) {
                result = edge;
                break;
            }
        }
        return result;
    }

    public boolean isPresent(double startX, double startY, double endX, double endY) {
        boolean result = false;
        for (Edge edge :
                edges) {
            if (edge.getStartX() == startX &&
                edge.getStartY() == startY &&
                edge.getEndX() == endX &&
                edge.getEndY() == endY) {
                result = true;
                break;
            }
        }
        return result;
    }

    public void deleteEdgeByIndex(int index) {
        if (edges.size() > 0) {
            edges.remove(index);
        }
    }

    public void deleteEdgesOfVertex(double vertexX, double vertexY) {
        ArrayList<Edge> edgesForRemove = new ArrayList<>();
        for (Edge edge :
            edges) {
            if ((edge.getStartX() == vertexX &&
                edge.getStartY() == vertexY) ||
                (edge.getEndX() == vertexX &&
                edge.getEndY() == vertexY)){
                edgesForRemove.add(edge);
            }
        }
        for (Edge edge :
                edgesForRemove) {
            edges.remove(edge);
        }
    }

    public void deleteEdge(Edge edge) {
        for (Edge currentEdge : edges) {
            if (currentEdge.getStartX() == edge.getStartX() &&
                currentEdge.getStartY() == edge.getStartY() &&
                currentEdge.getEndX() == edge.getEndX() &&
                currentEdge.getEndY() == edge.getEndY()
                ) {
                // Проходимся по вершинам-соседям и удаляем из списка соседей каждой вершины данную вершину
                /*for (Integer currentVertexIndex :
                        vertex.getNeighboringVerticesIndexes()) {
                    getVertexByIndex(currentVertexIndex).deleteNeighboringVertexIndex(vertex.getIndex());
                }*/
                edges.remove(edge);
                break;
            }
        }
    }

    public int getEdgesCount() {
        return edges.size();
    }
}

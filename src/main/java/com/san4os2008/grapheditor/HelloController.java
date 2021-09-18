package com.san4os2008.grapheditor;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;

public class HelloController {
    private String welcomeLabelText;
    private String debugLabelText;
    private VerticesManager verticesManager;
    private EdgesManager edgesManager;
    private ArrayList<VertexView> circles;
    private ArrayList<EdgeView> lines;
    private Group group;
    private Stage stage;
    private Vertex nearestStartVertex, nearestEndVertex;
    private boolean isStartClick;
    private double lineStartX, lineStartY, lineEndX, lineEndY;
    private Gson gson;
    @FXML
    private Label debugText;
    @FXML
    private Label welcomeText;
    @FXML
    private Button buttonAddVertex;
    @FXML
    private Button buttonAddEdge;
    @FXML
    private Button buttonDeleteVertex;
    @FXML
    private Button buttonDeleteEdge;
    @FXML
    private Button buttonSaveGraph;
    @FXML
    private Button buttonLoadGraph;
    @FXML
    private Pane pane;

    public HelloController() {
        super();
        welcomeLabelText = "";
        verticesManager = new VerticesManager();
        edgesManager = new EdgesManager();
        group = new Group();
        circles = new ArrayList<>();
        lines = new ArrayList<>();
        isStartClick = true;
        nearestStartVertex = new Vertex();
        nearestEndVertex = new Vertex();
        gson = new Gson();
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            pane.setMaxWidth((Double) newValue);
            pane.setMinWidth((Double) newValue);
            pane.setPrefWidth((Double) newValue);
        });

        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            pane.setMaxHeight((Double) newValue);
            pane.setMinHeight((Double) newValue);
            pane.setPrefHeight((Double) newValue);
        });
    }

    @FXML
    protected void onButtonAddVertexClick() {
        welcomeLabelText = "Add Vertex";
        welcomeText.setText(welcomeLabelText);
    }
    @FXML
    public void onButtonAddEdgeClick() {
        welcomeLabelText = "Add Edge";
        welcomeText.setText(welcomeLabelText);
    }
    @FXML
    protected void onButtonDeleteVertexClick() {
        welcomeLabelText = "Delete Vertex";
        welcomeText.setText(welcomeLabelText);
    }
    @FXML
    protected void onButtonDeleteEdgeClick() {
        welcomeLabelText = "Delete Edge";
        welcomeText.setText(welcomeLabelText);
    }
    @FXML
    protected void onMouseMoved(MouseEvent event) {
        /*debugLabelText =
                "(x: " + event.getX() + ", y: " + event.getY() + ") -- " +
                        "(sceneX: " + event.getSceneX() + ", sceneY: " + event.getSceneY() + ") -- " +
                        "(screenX: " + event.getScreenX() + ", screenY: " + event.getScreenY() + ")";
        debugText.setText(debugLabelText);*/
    }
    @FXML
    protected void onPanelMouseClick(MouseEvent event) {
        switch (welcomeLabelText) {
            case "Add Vertex":
                VertexView circle = new VertexView();
                circle.setCenterX(event.getX());
                circle.setCenterY(event.getY());
                circle.setRadius(10); // TODO: Вынести в параметры
                circle.setFill(Color.RED); // TODO: Вынести в параметры
                circles.add(circle);
                group.getChildren().addAll(circle);
                circle.circleIndexInGroup = group.getChildren().size() - 1;
                pane.getChildren().setAll(group);
                Vertex vertex = new Vertex();
                vertex.setX(event.getX());
                vertex.setY(event.getY());
                verticesManager.addVertex(vertex);
                if (verticesManager.getVerticesCount() > 0) {
                    buttonDeleteVertex.setDisable(false);
                }
                if (verticesManager.getVerticesCount() > 1) {
                    buttonAddEdge.setDisable(false);
                }
                break;
            case "Add Edge":
                if (isStartClick) {
                    // TODO: подсветить начальную выбранную вершину
                    nearestStartVertex = getNearestVertex(event.getX(), event.getY(), verticesManager.getVertices());
                    lineStartX = nearestStartVertex.getX();
                    lineStartY = nearestStartVertex.getY();
                    debugLabelText = "x: " + lineStartX + ", y: " + lineStartY;
                    debugText.setText(debugLabelText);
                    isStartClick = false;
                } else {
                    // TODO: отключить подсветку выбранной вершины
                    nearestEndVertex = getNearestVertex(event.getX(), event.getY(), verticesManager.getVertices());
                    lineEndX = nearestEndVertex.getX();
                    lineEndY = nearestEndVertex.getY();
                    nearestStartVertex.addNeighboringVertex(nearestEndVertex);
                    nearestEndVertex.addNeighboringVertex(nearestStartVertex);
                    nearestStartVertex.addNeighboringVertexIndex(nearestEndVertex.getIndex());
                    nearestEndVertex.addNeighboringVertexIndex(nearestStartVertex.getIndex());
                    debugLabelText = "x: " + lineEndX + ", y: " + lineEndY;
                    debugText.setText(debugLabelText);
                    EdgeView line = new EdgeView(lineStartX, lineStartY, lineEndX, lineEndY);
                    line.strokeWidthProperty().set(5); // TODO: Вынести в параметры
                    line.setStroke(Color.RED); // TODO: Вынести в параметры
                    lines.add(line);
                    group.getChildren().addAll(line);
                    line.lineIndexInGroup = group.getChildren().size() - 1;
                    line.toBack();
                    pane.getChildren().setAll(group);
                    isStartClick = true;
                    Edge edge = new Edge();
                    edge.setStartX(lineStartX);
                    edge.setStartY(lineStartY);
                    edge.setEndX(lineEndX);
                    edge.setEndY(lineEndY);
                    edgesManager.addEdge(edge);
                    if (edgesManager.getEdgesCount() > 0) {
                        buttonDeleteEdge.setDisable(false);
                    }
                }
                break;
            case "Delete Vertex":
                for (int i = 0; i < circles.size(); i++) {
                    if (circles.get(i).isHover()) {
//                        debugText.setText(String.valueOf(i));
                        edgesManager.deleteEdgesOfVertex(circles.get(i).getCenterX(), circles.get(i).getCenterY());
//                        Number n = 0;  Class<? extends Number> c = n.getClass();
                        // Выбираем из группы кругов и линий линии
                        ArrayList<Line> lines = new ArrayList<>();
                        for (Node node :
                                group.getChildren()) {
                            try {
                                EdgeView line = (EdgeView) node;
                                if (node.getClass().isInstance(line)) {
                                    if ((line.getStartX() == circles.get(i).getCenterX() &&
                                            line.getStartY() == circles.get(i).getCenterY()) ||
                                            (line.getEndX() == circles.get(i).getCenterX() &&
                                                    line.getEndY() == circles.get(i).getCenterY())) {
                                        lines.add(line);
                                    }
                                }
                            } catch (ClassCastException e) {
                                continue;
                            }
                        }
                        // Удаляем нужные линии
                        for (Line line :
                                lines) {
                            if ((line.getStartX() == circles.get(i).getCenterX() &&
                                    line.getStartY() == circles.get(i).getCenterY()) ||
                                    (line.getEndX() == circles.get(i).getCenterX() &&
                                            line.getEndY() == circles.get(i).getCenterY())) {
                                group.getChildren().remove(line);
                            }
                        }
                        verticesManager.deleteVertex(circles.get(i).getCenterX(), circles.get(i).getCenterY());
//                        group.getChildren().remove(circles.get(i).circleIndexInGroup);
                        group.getChildren().remove(circles.get(i));
                        circles.remove(i);
                        if (verticesManager.getVerticesCount() < 1) {
                            buttonDeleteVertex.setDisable(true);
                            welcomeText.setText("Choose mode");
                        }
                        if (verticesManager.getVerticesCount() < 2) {
                            buttonAddEdge.setDisable(true);
                        }
                        if (edgesManager.getEdgesCount() == 0) {
                            buttonDeleteEdge.setDisable(true);
                        }
                        break;
                    }
                }
                break;
            case "Delete Edge":
                for (int i = 0; i < lines.size(); i++) {
                    EdgeView line = lines.get(i);
                    if (line.isHover()) {
                        debugText.setText("Oho!");
                        edgesManager.deleteEdge(edgesManager.getEdgeByCoordinates(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()));
                        group.getChildren().remove(line);
                        lines.remove(i);
                        if (edgesManager.getEdgesCount() == 0) {
                            buttonDeleteEdge.setDisable(true);
                        }
                        break;
                    }
                }
                break;
            default:
                break;
        }
    }

    @FXML
    protected void onButtonSaveGraphClick() {
        debugLabelText = "onButtonSaveGraphClick";
        debugText.setText(debugLabelText);
        try {
            ArrayList<Vertex> vertices = verticesManager.getVertices();
            // TODO: Предоставить пользователю выбирать место сохранения и имя файла
            FileOutputStream file = new FileOutputStream("GraphEditor.saved");
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(vertices);
            out.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onButtonLoadGraphClick() {
        debugLabelText = "onButtonLoadGraphClick";
        debugText.setText(debugLabelText);
        try {
            // TODO: Предоставить пользователю выбирать место и имя файла для открытия
            FileInputStream file = new FileInputStream("GraphEditor.saved");
            ObjectInputStream in = new ObjectInputStream(file);
            ArrayList<Vertex> vertices = (ArrayList<Vertex>) in.readObject();
            in.close();
            file.close();
            verticesManager.setVertices(vertices);
            for (Vertex vertex :
                    vertices) {
                // Восстанавливаем вершины
                VertexView circle = new VertexView();
                circle.setCenterX(vertex.getX());
                circle.setCenterY(vertex.getY());
                circle.setRadius(10); // TODO: Вынести в параметры
                circle.setFill(Color.RED); // TODO: Вынести в параметры
                circles.add(circle);
                group.getChildren().addAll(circle);
                circle.circleIndexInGroup = group.getChildren().size() - 1;
                pane.getChildren().setAll(group);
                // Восстанавливаем рёбра
                for (Vertex currentVertex :
                        vertex.getNeighboringVertices()) {
                    lineStartX = vertex.getX();
                    lineStartY = vertex.getY();
                    lineEndX = currentVertex.getX();
                    lineEndY = currentVertex.getY();
                    if (!edgesManager.isPresent(lineStartX, lineStartY, lineEndX, lineEndY)) {
                        Edge edge = new Edge();
                        edge.setStartX(lineStartX);
                        edge.setStartY(lineStartY);
                        edge.setEndX(lineEndX);
                        edge.setEndY(lineEndY);
                        edgesManager.addEdge(edge);
                        Line line = new Line(lineStartX, lineStartY, lineEndX, lineEndY);
                        line.strokeWidthProperty().set(5); // TODO: Вынести в параметры
                        line.setStroke(Color.RED); // TODO: Вынести в параметры
                        group.getChildren().addAll(line);
                        line.toBack();
                        pane.getChildren().setAll(group);
                    }
                }
            }
            if (verticesManager.getVerticesCount() > 0) {
                buttonDeleteVertex.setDisable(false);
            }
            if (verticesManager.getVerticesCount() > 1) {
                buttonAddEdge.setDisable(false);
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private Vertex getNearestVertex(double x, double y, ArrayList<Vertex> vertices) {
        Vertex vertex = new Vertex();
        Point2D p = new Point2D(x, y);
        double distance = Double.MAX_VALUE;
        for (Vertex currentVertex :
                vertices) {
            double currentDistance = p.distance(currentVertex.getX(), currentVertex.getY());
            if (currentDistance < distance) {
                distance = currentDistance;
                vertex = currentVertex;
            }
        }
        return vertex;
    }
}

/*
* TODO: При удалении вершины удалять её из списка соседей каждой вершины, к которая является её соседом и удалять все рёбра, которые от неё идут.
* TODO: При удалении ребра удалять из списка соседей в соединённых им вершинах бывшие соседние по этому ребру вершины.
* TODO: Если такой нет, то создать группу (group) для расположения в ней кнопок и панели для рисования. Привязать размеры этой группы к размерам окна. Расположить кнопки в привязке к верху окна, чтобы они сохраняли положение и при изменении размеров окна.
* TODO: Привязать размеры панели для рисования графов к размеру внешнего окна, с учётом кнопок сверху.
* TODO: Явно ограничить возможность рисования вершин и рёбер графа панелью для рисования.
* TODO: Создать анимированное отображение ребра при выборе второй его вершины.
* TODO: Не допускать пересечения рёбер (и вершин - для удобства восприятия графа) при добавлении нового ребра.
* TODO: Реализовать перетаскивание вершин графа, при этом чтобы вместе с вершиной перетаскивались и её ребра. Перетаскивать система должна разрешать только до сближения с другими элементами графа (может, реализовать их "толкание" при сдвигании перетаскиваемых элементов дальше).
* TODO: Реализовать перетаскивание рёбер графа с их вершинами, с учётом правил размещения этих элементов относительно других из предыдущего пункта.
* TODO: Добавить возможность изменять параметры внешнего вида элементов графа; цвет фона панели для рисования.
* */

/*
* Алгоритм:
* Кликаем по панели: создаётся вершина.
* Создать функцию отрисовки или удаления окружностей соответственно добавляемым или удаляемым вершинам
* При добавлении вершины рисовать окружность, при удалении - удалять.
* */
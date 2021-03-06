package com.san4os2008.grapheditor;

import javafx.animation.PauseTransition;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import com.google.gson.Gson;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HelloController {
    private String welcomeLabelText;
    private String debugLabelText;
    private VerticesManager verticesManager;
//    private EdgesManager edgesManager;
    private ArrayList<VertexView> circles;
    private ArrayList<EdgeView> lines;
//    private List<EdgeView> lines;
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
//        edgesManager = new EdgesManager();
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
                circle.setActive(true);
                circle.setRadius(10); // TODO: ?????????????? ?? ??????????????????
                circle.setFill(Color.DARKGREEN); // TODO: ?????????????? ?? ??????????????????
                for (VertexView currentCircle :
                        circles) {
                    currentCircle.setActive(false);
                    // ???????????????????????? ????????????????: ?????????????????? ?????????????????????? (????????????????) ?? ???????? ????????, ?? ?????? ?????????????????? - ?? ????????????
                    if (currentCircle.isActive()) {
                        currentCircle.setFill(Color.DARKGREEN); // TODO: ?????????????? ?? ??????????????????
                    } else {
                        PauseTransition pauseTransition = new PauseTransition(Duration.millis(1));
                        pauseTransition.setOnFinished(event1 -> currentCircle.setFill(Color.RED)); // TODO: ?????????????? ?? ??????????????????
                        pauseTransition.play();
                    }
                }
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
                    // TODO: ???????????????????? ?????????????????? ?????????????????? ??????????????
                    nearestStartVertex = getNearestVertex(event.getX(), event.getY(), verticesManager.getVertices());
                    lineStartX = nearestStartVertex.getX();
                    lineStartY = nearestStartVertex.getY();
                    debugLabelText = "x: " + lineStartX + ", y: " + lineStartY;
                    debugText.setText(debugLabelText);
                    isStartClick = false;
                    // ???????????????? ???? ???????????? ???????????? ?? ?????????? ????????, ?? ?????????????????????? ???????????????? ?????????????? ???????????? ????????????????????????, ?? ???????????? ?????? ???????????????? ?? ???????????????????? ?? ??????????. ????????
                    ArrayList<Circle> circles = new ArrayList<>();
                    for (Node node :
                            group.getChildren()) {
                        try {
                            VertexView currentCircle = (VertexView) node;
                            circles.add(currentCircle);
                            if (node.getClass().isInstance(currentCircle)) {
                                if (currentCircle.getCenterX() == lineStartX &&
                                    currentCircle.getCenterY() == lineStartY) {
                                    currentCircle.setActive(true);
                                    currentCircle.setFill(Color.DARKGREEN); // TODO: ?????????????? ?? ??????????????????
                                } else {
                                    currentCircle.setActive(false);
                                    PauseTransition pauseTransition = new PauseTransition(Duration.millis(1));
                                    pauseTransition.setOnFinished(event1 -> currentCircle.setFill(Color.RED)); // TODO: ?????????????? ?? ??????????????????
                                    pauseTransition.play();
                                }
                            }
                        } catch (ClassCastException e) {
                            continue;
                        }
                    }
                } else {
                    // TODO: ?????????????????? ?????????????????? ?????????????????? ??????????????
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
                    line.strokeWidthProperty().set(5); // TODO: ?????????????? ?? ??????????????????
                    line.setStroke(Color.RED); // TODO: ?????????????? ?? ??????????????????
                    lines.add(line);
                    group.getChildren().addAll(line);
                    line.lineIndexInGroup = group.getChildren().size() - 1;
                    line.toBack();
                    pane.getChildren().setAll(group);
                    isStartClick = true;
                    setButtonDeleteEdgeAvailability();
                }
                break;
            case "Delete Vertex":
                for (int i = 0; i < circles.size(); i++) {
                    if (circles.get(i).isHover()) {
                        // ???????????????? ???? ???????????? ???????????? ?? ?????????? ??????????
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
                        // ?????????????? ???????????? ??????????
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
                        group.getChildren().remove(circles.get(i));
                        circles.remove(i);
                        if (verticesManager.getVerticesCount() < 1) {
                            buttonDeleteVertex.setDisable(true);
                            welcomeText.setText("Choose mode");
                        }
                        if (verticesManager.getVerticesCount() < 2) {
                            buttonAddEdge.setDisable(true);
                        }
                        setButtonDeleteEdgeAvailability();
                        break;
                    }
                }
                break;
            case "Delete Edge":
                for (int i = 0; i < lines.size(); i++) {
                    EdgeView line = lines.get(i);
                    if (line.isHover()) {
                        debugText.setText("Oho! " + i);
                        Vertex vertex1 = verticesManager.getVertexByCoordinates(line.getStartX(), line.getStartY());
                        Vertex vertex2 = verticesManager.getVertexByCoordinates(line.getEndX(), line.getEndY());
                        vertex1.deleteNeighboringVertex(vertex2);
                        vertex2.deleteNeighboringVertex(vertex1);
                        group.getChildren().remove(line);
                        lines.remove(i);
                        setButtonDeleteEdgeAvailability();
                        break;
                    }
                }
                break;
            default:
                break;
        }
    }

    protected void setButtonDeleteEdgeAvailability() {
        // ?????????????????????????????? ???????????? ??????????????, ?? ?????????????? ???????? ????????????, ?? ?????????????? ???? ????????????????????
        long verticesWithNaighboursCount = verticesManager.getVertices().stream().filter(vertex -> !vertex.getNeighboringVertices().isEmpty()).count();
        buttonDeleteEdge.setDisable(verticesWithNaighboursCount == 0);
    }

    @FXML
    protected void onButtonSaveGraphClick() {
        debugLabelText = "onButtonSaveGraphClick";
        debugText.setText(debugLabelText);
        try {
            ArrayList<Vertex> vertices = verticesManager.getVertices();
            // TODO: ???????????????????????? ???????????????????????? ???????????????? ?????????? ???????????????????? ?? ?????? ??????????
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
        // TODO: ???????????????? ???????????????????????????? ?? ???????????? ?????????????????? ?????? ???????????????????? ?? ?????????????? ???????????? ?????????? ???????????????????? ?????????? ???? ??????????
        debugLabelText = "onButtonLoadGraphClick";
        debugText.setText(debugLabelText);
        // ?????????????? ?????????????? ?????????????? ???? ????????????
        verticesManager.getVertices().clear();
        // ?????????????? ?????????????????????? ???????????????? ?? ????????????
        group.getChildren().clear();
        pane.getChildren().setAll(group);
        try {
            // TODO: ???????????????????????? ???????????????????????? ???????????????? ?????????? ?? ?????? ?????????? ?????? ????????????????
            FileInputStream file = new FileInputStream("GraphEditor.saved");
            ObjectInputStream in = new ObjectInputStream(file);
            ArrayList<Vertex> vertices = (ArrayList<Vertex>) in.readObject();
            in.close();
            file.close();
            verticesManager.setVertices(vertices);
            for (Vertex vertex :
                    vertices) {
                // ?????????????????????????????? ??????????????
                VertexView circle = new VertexView();
                circle.setCenterX(vertex.getX());
                circle.setCenterY(vertex.getY());
                circle.setRadius(10); // TODO: ?????????????? ?? ??????????????????
                circle.setFill(Color.RED); // TODO: ?????????????? ?? ??????????????????
                circles.add(circle);
                group.getChildren().addAll(circle);
                circle.circleIndexInGroup = group.getChildren().size() - 1;
                pane.getChildren().setAll(group);
                // ?????????????????????????????? ??????????
                for (Vertex currentVertex :
                        vertex.getNeighboringVertices()) {
                    lineStartX = vertex.getX();
                    lineStartY = vertex.getY();
                    lineEndX = currentVertex.getX();
                    lineEndY = currentVertex.getY();
                    EdgeView line = new EdgeView(lineStartX, lineStartY, lineEndX, lineEndY);
                    if (lines.stream().filter(line1 -> line1.getStartX() == lineEndX &&
                            line1.getStartY() == lineEndY &&
                            line1.getEndX() == lineStartX &&
                            line1.getEndY() == lineStartY
                    ).count() == 0) {
                        lines.add(line);
                        line.strokeWidthProperty().set(5); // TODO: ?????????????? ?? ??????????????????
                        line.setStroke(Color.RED); // TODO: ?????????????? ?? ??????????????????
                        group.getChildren().addAll(line);
                        line.toBack();
                    }
                    // ?????????????????????????????? ???????????? ??????????????, ?? ?????????????? ???????? ????????????, ?? ?????????????? ???? ????????????????????
                    long verticesWithNaighboursCount = verticesManager.getVertices().stream().filter(vertex1 -> !vertex1.getNeighboringVertices().isEmpty()).count();
                    if (verticesWithNaighboursCount > 0) {
                        buttonDeleteEdge.setDisable(false);
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
* TODO: ?????? ???????????????? ?????????????? ?????????????? ???? ???? ???????????? ?????????????? ???????????? ??????????????, ?? ?????????????? ???????????????? ???? ?????????????? ?? ?????????????? ?????? ??????????, ?????????????? ???? ?????? ????????.
* TODO: ?????? ???????????????? ?????????? ?????????????? ???? ???????????? ?????????????? ?? ?????????????????????? ???? ???????????????? ???????????? ???????????????? ???? ?????????? ?????????? ??????????????.
* TODO: ???????? ?????????? ??????, ???? ?????????????? ???????????? (group) ?????? ???????????????????????? ?? ?????? ???????????? ?? ???????????? ?????? ??????????????????. ?????????????????? ?????????????? ???????? ???????????? ?? ???????????????? ????????. ?????????????????????? ???????????? ?? ???????????????? ?? ?????????? ????????, ?????????? ?????? ?????????????????? ?????????????????? ?? ?????? ?????????????????? ???????????????? ????????.
* TODO: ?????????????????? ?????????????? ???????????? ?????? ?????????????????? ???????????? ?? ?????????????? ???????????????? ????????, ?? ???????????? ???????????? ????????????.
* TODO: ???????? ???????????????????? ?????????????????????? ?????????????????? ???????????? ?? ?????????? ?????????? ?????????????? ?????? ??????????????????.
* TODO: ?????????????? ?????????????????????????? ?????????????????????? ?????????? ?????? ???????????? ???????????? ?????? ??????????????.
* TODO: ???? ?????????????????? ?????????????????????? ?????????? (?? ???????????? - ?????? ???????????????? ???????????????????? ??????????) ?????? ???????????????????? ???????????? ??????????.
* TODO: ?????????????????????? ???????????????????????????? ???????????? ??????????, ?????? ???????? ?????????? ???????????? ?? ???????????????? ?????????????????????????????? ?? ???? ??????????. ?????????????????????????? ?????????????? ???????????? ?????????????????? ???????????? ???? ?????????????????? ?? ?????????????? ???????????????????? ?????????? (??????????, ?????????????????????? ???? "????????????????" ?????? ?????????????????? ?????????????????????????????? ?????????????????? ????????????).
* TODO: ?????????????????????? ???????????????????????????? ?????????? ?????????? ?? ???? ??????????????????, ?? ???????????? ???????????? ???????????????????? ???????? ?????????????????? ???????????????????????? ???????????? ???? ?????????????????????? ????????????.
* TODO: ???????????????? ?????????????????????? ???????????????? ?????????????????? ???????????????? ???????? ?????????????????? ??????????; ???????? ???????? ???????????? ?????? ??????????????????.
* TODO: ?????????????????????? ???????????????? ?????????????? ???????????????? ?? ?????????????? ???? ???? ??????.
* TODO: ?? ???????????? ???????????????? ?????????????? ?????? ?????????????? ?????????????? ?????????????????????? (?? ???? ?????????????????????? ???????????????? ???????????? ???????????????????? ????????????).
* */

/*
* ????????????????:
* ?????????????? ???? ????????????: ?????????????????? ??????????????.
* ?????????????? ?????????????? ?????????????????? ?????? ???????????????? ?????????????????????? ???????????????????????????? ?????????????????????? ?????? ?????????????????? ????????????????
* ?????? ???????????????????? ?????????????? ???????????????? ????????????????????, ?????? ???????????????? - ??????????????.
* */
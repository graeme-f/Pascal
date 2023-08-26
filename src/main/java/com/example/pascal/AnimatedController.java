package com.example.pascal;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AnimatedController {
    static AnimatedController controller;
    @FXML private VBox vBoxLeftPanel;
    @FXML private Label lblDimensions;
    @FXML private Canvas cnvDrawingArea;
    private AnimatedCanvas timer = null;

    @FXML private void onClickPascal(){
        if (timer != null){timer.stop();}
        timer = new AnimatedPascalTriangle(this, cnvDrawingArea);
        timer.start();
    }

    private void displayDimensions(){
        lblDimensions.setText((int)cnvDrawingArea.getWidth() + " x " + (int)cnvDrawingArea.getHeight());
    }
    public void setWidth(int newWidth){
        cnvDrawingArea.setWidth(newWidth-vBoxLeftPanel.getWidth());
        displayDimensions();
    }
    public void setHeight(int newHeight){
        cnvDrawingArea.setHeight(newHeight);
        displayDimensions();
    }

    public void initialize() {

        controller = this;
    }

    public void runTriangle(){
        if (timer != null){timer.stop();}
        timer = new AnimatedPascalTriangle(this, cnvDrawingArea);
        timer.start();

    }

    protected void showLeftPanel(){
        vBoxLeftPanel.setMinWidth(200);
        vBoxLeftPanel.setPrefWidth(200);
        vBoxLeftPanel.setMaxWidth(200);
        vBoxLeftPanel.setVisible(true);
    }
    protected void hideLeftPanel(){
        vBoxLeftPanel.setMinWidth(0);
        vBoxLeftPanel.setPrefWidth(0);
        vBoxLeftPanel.setMaxWidth(0);
        vBoxLeftPanel.setVisible(false);
    }
}
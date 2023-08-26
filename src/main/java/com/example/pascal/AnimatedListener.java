package com.example.pascal;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public abstract class AnimatedListener extends AnimatedDrawing{
    private final AnimatedController controller;
    private EventHandler<KeyEvent> keyEventHandler;
    private EventHandler<MouseEvent> mouseEventHandler;

    AnimatedListener(AnimatedController controller,
                     Canvas cnvDrawingArea,
                     int width,
                     int height,
                     String title,
                     boolean keyboard,
                     boolean mouse){
        super(cnvDrawingArea);
        this.controller = controller;
        Stage stage = (Stage)(canvas.getScene().getWindow());
        stage.setTitle(title);
        controller.hideLeftPanel();
        stage.setWidth(width);
        stage.setHeight(height);
        frameLimit = 10;
        isLooping(true);
        resize(false);
        cnvDrawingArea.setWidth(width);
        cnvDrawingArea.setHeight(height-30);
        keyEventHandler = null;
        if (keyboard) {
            keyEventHandler = new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    switch (event.getCode()) {
                        case W, UP -> actionUp();
                        case S, DOWN -> actionDown();
                        case A, LEFT -> actionLeft();
                        case D, RIGHT -> actionRight();
                        case F, SPACE -> actionSpace();
                        case ENTER -> actionEnter();
                        default -> keyboardAction(event.getCode());
                    }
                }
            };
            cnvDrawingArea.addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
        }

        mouseEventHandler = null;
        if (mouse) {
            mouseEventHandler = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
                        mouseMoved((int) event.getX(), (int) event.getY());
                    } else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            mouseLeftPressed((int) event.getX(), (int) event.getY());
                        } else if (event.getButton() == MouseButton.SECONDARY) {
                            mouseRightPressed((int) event.getX(), (int) event.getY());
                        }
                    } else {
                        mouseAction(event);
                    }
                }
            };
            cnvDrawingArea.addEventHandler(MouseEvent.MOUSE_MOVED,mouseEventHandler);
            cnvDrawingArea.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEventHandler);
        }
        cnvDrawingArea.requestFocus();
    }

    protected void showControls(){
        Stage stage = (Stage)(canvas.getScene().getWindow());
        stage.setTitle("Canvas Example");
        stage.setWidth((int)canvas.getWidth()+200);
        if (keyEventHandler != null) {
            canvas.removeEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
        }
        if (mouseEventHandler != null) {
            canvas.removeEventHandler(MouseEvent.MOUSE_MOVED, mouseEventHandler);
            canvas.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandler);
        }
        controller.showLeftPanel();
    }
    protected void actionUp(){}
    protected void actionDown(){}
    protected void actionLeft(){}
    protected void actionRight(){}
    protected void actionSpace(){}
    protected void actionEnter(){}
    protected void keyboardAction(KeyCode key){}

    protected void mouseMoved(int x, int y) {}
    protected void mouseLeftPressed(int x, int y) {}
    protected void mouseRightPressed(int x, int y) {}
    protected void mouseAction(MouseEvent mouseEvent) {}

} // end of class AnimatedListener

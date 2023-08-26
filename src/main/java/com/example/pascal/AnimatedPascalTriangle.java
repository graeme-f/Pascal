package com.example.pascal;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.math.BigInteger;

import static javafx.scene.input.KeyCode.*;

public class AnimatedPascalTriangle extends AnimatedListener{
    int fontSize = 14;
    int rowHeight;
    int level = 16;
    int factor = 0;
    boolean dumpSummary = false;
    boolean showTotals = false;
    boolean showNumber = true;
    boolean showHelp = false;
    boolean dirty = true;
    char action = 'R';
    AnimatedPascalTriangle(AnimatedController controller, Canvas cnvDrawingArea) {
        super(controller, cnvDrawingArea, 1000, 830, "Pascal's Triangle", true, false);
        Stage stage = (Stage)(canvas.getScene().getWindow());
        resize(false);
        frameLimit = 100;
        isLooping(true);
    }

    @Override
    protected void paint() {
        if (dirty) {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setFill(Color.rgb(64,64,64));
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            dirty = false;
            drawTriangle();
        }
    }

    protected void keyboardAction(KeyCode key){
        System.out.println(key);
        if (key.isDigitKey()) {
            int digit = Integer.parseInt(key.getChar());
            factor = factor * 10 + digit;
            System.out.println(factor);
            dirty = true;
        } else if (key == R) {
            action = 'R';
        } else if (key == T) {
            showTotals = !showTotals;
            dirty = true;
        } else if (key == D){
            dumpSummary = ! dumpSummary;
            dirty = true;
        } else if (key == N){
            showNumber = ! showNumber;
            dirty = true;
        } else if (key == BACK_SPACE){
            factor = 0;
        } else if (key == PLUS || key == EQUALS){
            if (action == 'R'){
                level ++;
            } else if (action == 'F'){
                fontSize++;
            }
            dirty = true;
        } else if (key == H) {
            showHelp = !showHelp;
            dirty = true;
        } else if (key == MINUS){
            if (action == 'R'){
                level --;
            } else if (action == 'F'){
                fontSize --;
            }
            dirty = true;
        }else if (key == ESCAPE){
            showControls();
        } else {
            System.out.println("unhandled key: " + key);
        }
        System.out.println("Action: " + action);
    }

    protected void actionSpace(){
        action = 'F';
    }
    protected void actionUp(){
        level /= 2;
        dirty = true;
    }
    protected void actionDown(){
        level *= 2;
        dirty = true;
    }

    protected void drawTriangle(){
        double columnWidth = canvas.getWidth() / (level+3);
        double halfColumnWidth = columnWidth /2;
        double firstColumn = canvas.getWidth() / 2 - columnWidth;
        rowHeight = (int)(fontSize*1.1);
        int rowPosition = rowHeight+5;

        int factorTotal = 0;
        int numberTotal = 0;
        // Initialise the first row to be all zeros with a 1 at the start
        BigInteger[] row = new BigInteger[level+2];
        for (int i = 0; i < level+2; i++){
            row[i] = BigInteger.ZERO;
        }
        row[0]=BigInteger.ONE;

        // Now display on the screen
        gc.setFont(new Font(fontSize));
        BigInteger bigFactor = new BigInteger(""+factor);
        for (int i = 0; i <= level; i++){
            double column = firstColumn;
            for (int t = 0; t < row.length; t++) {
                if (row[t].equals(BigInteger.ZERO)) break; // don't display any zeros
                if (factor == 0) {
                    gc.setFill(Color.LIGHTGREEN);
                    gc.setStroke(Color.LIGHTGREEN);
                } else {
                    if (row[t].remainder(bigFactor).equals(BigInteger.ZERO)) {
//                        gc.setFill(Color.ORANGE);
//                        gc.setStroke(Color.ORANGE);
                        gc.setFill(Color.YELLOW);
                        gc.setStroke(Color.RED);
                        factorTotal++;
                    } else {
                        if (showNumber) {
                            gc.setFill(Color.LIGHTBLUE);
                            gc.setStroke(Color.LIGHTBLUE);
                        } else {
                            gc.setFill(Color.rgb(64,80,255));
                            gc.setStroke(Color.rgb(64,80,255));
                        }
                    }
                }
                numberTotal++;
                if (showNumber) {
                    gc.setFill(Color.BLACK);
                    gc.fillText(String.valueOf(row[t]), column, rowPosition);
                } else {
                    gc.setLineWidth(5);
                    gc.strokeText("▼", column, rowPosition);
                    gc.fillText("▼", column, rowPosition);
                    gc.setLineWidth(1);
                }
                column += columnWidth;
            }
            System.out.println(factorTotal + "\t" + numberTotal);
            firstColumn -= halfColumnWidth;
            rowPosition += rowHeight;
            // Calculate the next row
            BigInteger[] tempRow = new BigInteger[level+2];
            tempRow[0] = BigInteger.ONE;
            for (int t = 1; t < row.length; t++) {
                tempRow[t] = row[t-1].add(row[t]);
            }
            row = tempRow;
        }
        if (showTotals){
            gc.setFont(new Font (16));
            gc.setFill(Color.RED);
            int startText =(int)(canvas.getWidth() *0.75);
            gc.fillText("Total rows displayed: "+level, startText, 20);
            gc.fillText("Factor searched for: "+factor, startText, 40);
            gc.fillText("Total factors seen: "+factorTotal, startText, 60);
            gc.fillText("Total numbers seen: "+numberTotal, startText, 80);
            gc.fillText("Ratio of factors to numbers: "+(factorTotal * 100 / numberTotal) + "%", startText, 100);
        }
        if (showHelp){
            double helpTextX = canvas.getWidth()*.5 -250;
            double helpTextY = canvas.getHeight()*.5-150;
            gc.setFill(Color.GRAY);
            int shadowOffset = 3;
            gc.fillRect(helpTextX+shadowOffset, helpTextY+shadowOffset, 500, 300);
            gc.setFill(Color.WHITE);
            gc.fillRect(helpTextX, helpTextY, 500, 300);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(helpTextX, helpTextY, 500, 300);

            helpTextX += 20;
            double helpTextSecondColumnOffset = 75;
            gc.setFont(new Font (16));
            gc.setFill(Color.BLACK);
            helpTextY += 30;
            gc.fillText("Quick Help", helpTextX, helpTextY);

            helpTextY += 30;
            gc.setFont(new Font (12));
            gc.fillText("H", helpTextX, helpTextY);
            gc.fillText("Toggle this help screen", helpTextX+helpTextSecondColumnOffset, helpTextY);

            helpTextY += 20;
            gc.fillText("T", helpTextX, helpTextY);
            gc.fillText("Toggle the display of totals", helpTextX+helpTextSecondColumnOffset, helpTextY);

            helpTextY += 20;
            gc.fillText("R", helpTextX, helpTextY);
            gc.fillText("Set action to rows", helpTextX+helpTextSecondColumnOffset, helpTextY);

            helpTextY += 20;
            gc.fillText("F", helpTextX, helpTextY);
            gc.fillText("Set action to font size", helpTextX+helpTextSecondColumnOffset, helpTextY);

            helpTextY += 20;
            gc.fillText("+", helpTextX, helpTextY);
            gc.fillText("increment the number of rows or the font size based on the selected action", helpTextX+helpTextSecondColumnOffset, helpTextY);

            helpTextY += 20;
            gc.fillText("-", helpTextX, helpTextY);
            gc.fillText("decrement the number of rows or the font size based on the selected action", helpTextX+helpTextSecondColumnOffset, helpTextY);

            helpTextY += 20;
            gc.fillText("N", helpTextX, helpTextY);
            gc.fillText("Toggle between displaying a number or a triangle", helpTextX+helpTextSecondColumnOffset, helpTextY);

            helpTextY += 20;
            gc.fillText("Digit", helpTextX, helpTextY);
            gc.fillText("Concatenate this digit to the factor being searched for", helpTextX+helpTextSecondColumnOffset, helpTextY);

            helpTextY += 20;
            gc.fillText("Backspace", helpTextX, helpTextY);
            gc.fillText("Set the factor to zero", helpTextX+helpTextSecondColumnOffset, helpTextY);

            helpTextY += 20;
            gc.fillText("D", helpTextX, helpTextY);
            gc.fillText("print to the console a running total of factors seen at each row", helpTextX+helpTextSecondColumnOffset, helpTextY);

            helpTextY += 20;
            gc.fillText("Up", helpTextX, helpTextY);
            gc.fillText("Halve the number of rows displayed", helpTextX+helpTextSecondColumnOffset, helpTextY);

            helpTextY += 20;
            gc.fillText("Down", helpTextX, helpTextY);
            gc.fillText("Double the number of rows displayed", helpTextX+helpTextSecondColumnOffset, helpTextY);
        }
    } // end of method drawTriangle()
}

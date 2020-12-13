package gui;

import businesslogic.Cell;
import businesslogic.GlobalData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class View {
    Canvas canvas;
    int numberOfGrainsAtX;
    int numberOfGrainsAtY;
    double cellXDimension;
    double cellYDimension;
    int numberOfInitialGrains;
    int[][] cellsRGB;


    public View(Canvas canvas, GlobalData globalData) {
        this.canvas = canvas;
        this.numberOfGrainsAtX = globalData.getNumberOfGrainsAtX();
        this.numberOfGrainsAtY = globalData.getNumberOfGrainsAtY();
        this.numberOfInitialGrains = globalData.getNumberOfInitialGrains();

        cellXDimension = canvas.getHeight() / numberOfGrainsAtX;
        cellYDimension = canvas.getWidth() / numberOfGrainsAtY;

        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        setRGBForEachCellId();
    }

    public void generateView(Cell[][] board) {

        //canvas.requestFocus();

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        for (int i = 0; i < this.numberOfGrainsAtX; i++) {
            for (int j = 0; j < this.numberOfGrainsAtY; j++) {

                graphicsContext.beginPath();

                if (board[i][j].isEmpty()) {
                    graphicsContext.setFill(Color.rgb(255, 255, 255));
                } else if (board[i][j].isInclusion()) {
                    graphicsContext.setFill(Color.rgb(0, 0, 0));
                } else {
                    int r = cellsRGB[board[i][j].getId()][0];
                    int g = cellsRGB[board[i][j].getId()][1];
                    int b = cellsRGB[board[i][j].getId()][2];
                    graphicsContext.setFill(Color.rgb(r, g, b));
                }

                graphicsContext.rect(j * cellYDimension, i * cellXDimension, cellYDimension, cellXDimension);
                graphicsContext.fill();
            }
            //canvas.requestFocus();
        }

    }

    private void setRGBForEachCellId() {
        cellsRGB = new int[this.numberOfInitialGrains][];
        for (int i = 0; i < numberOfInitialGrains; i++) {
            Random random = new Random();
            cellsRGB[i] = new int[]{1 + random.nextInt(254), 1 + random.nextInt(254), 1 + random.nextInt(254)};
        }
    }
}

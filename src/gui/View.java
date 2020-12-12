package gui;

import businesslogic.Cell;
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


    public View(Canvas canvas, int numberOfGrainsAtX, int numberOfGrainsAtY, int numberOfInitialGrains) {
        this.canvas = canvas;
        this.numberOfGrainsAtX = numberOfGrainsAtX;
        this.numberOfGrainsAtY = numberOfGrainsAtY;
        this.numberOfInitialGrains = numberOfInitialGrains;

        cellXDimension = canvas.getWidth() / numberOfGrainsAtX;
        cellYDimension = canvas.getHeight() / numberOfGrainsAtY;

        System.out.println("cellXDimension: " + cellXDimension);
        System.out.println("cellYDimension: " + cellYDimension);

        setRGBForEachCellId();
    }

    public void generateView(Cell[][] board) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        for (int i = 0; i < this.numberOfGrainsAtX; i++) {
            for (int j = 0; j < this.numberOfGrainsAtY; j++) {

                graphicsContext.beginPath();

                if (board[i][j].getId() == -1) {
                    graphicsContext.setFill(Color.rgb(255, 255, 255));
                } else {
                    int r = cellsRGB[board[i][j].getId()][0];
                    int g = cellsRGB[board[i][j].getId()][1];
                    int b = cellsRGB[board[i][j].getId()][2];
                    graphicsContext.setFill(Color.rgb(r, g, b));
                }

                graphicsContext.rect(j * cellXDimension, i * cellYDimension, cellXDimension, cellYDimension);
                graphicsContext.fill();
            }
        }

    }

    private void setRGBForEachCellId() {
        cellsRGB = new int[this.numberOfInitialGrains][];
        for (int i = 0; i < numberOfInitialGrains; i++) {
            Random random = new Random();
            cellsRGB[i] = new int[]{random.nextInt(255), random.nextInt(255), random.nextInt(255)};
        }
    }
}

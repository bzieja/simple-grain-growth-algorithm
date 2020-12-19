package gui;

import app.grid.Cell;
import app.AppConfiguration;
import app.grid.GrainMap;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class CanvasPrinter {
    private static CanvasPrinter instance;
    Canvas canvas;
    GrainMap grainMap;
    int numberOfGrainsAtX;
    int numberOfGrainsAtY;
    double cellXDimension;
    double cellYDimension;
    int numberOfInitialGrains;
    int[][] cellsRGB;   //[cellId][r, g, b]

    private CanvasPrinter(Canvas canvas, AppConfiguration appConfiguration, GrainMap grainMap) {
        this.canvas = canvas;
        this.numberOfGrainsAtX = appConfiguration.getNumberOfGrainsAtX();
        this.numberOfGrainsAtY = appConfiguration.getNumberOfGrainsAtY();
        this.numberOfInitialGrains = appConfiguration.getNumberOfInitialGrains();
        this.grainMap = grainMap;

        cellXDimension = canvas.getHeight() / numberOfGrainsAtX;
        cellYDimension = canvas.getWidth() / numberOfGrainsAtY;

        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        setRGBForEachCellId();
    }

    public static CanvasPrinter getInstance(Canvas canvas, AppConfiguration appConfiguration, GrainMap grainMap) {
        if (CanvasPrinter.instance == null) {
            CanvasPrinter.instance = new CanvasPrinter(canvas, appConfiguration, grainMap);
            return CanvasPrinter.instance;
        } else {
            return CanvasPrinter.instance;
        }
    }

    public void generateView() {
        Cell[][] board = this.grainMap.getCurrentStep();

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
        }

    }

    private void setRGBForEachCellId() {
        cellsRGB = new int[this.numberOfInitialGrains][];
        Random random = new Random(this.numberOfInitialGrains);
        for (int i = 0; i < numberOfInitialGrains; i++) {
            cellsRGB[i] = new int[]{1 + random.nextInt(254), 1 + random.nextInt(254), 1 + random.nextInt(254)};
        }
    }

    public int[][] getCellsRGB() {
        return cellsRGB;
    }

    public void setCellsRGB(int[][] cellsRGB) {
        this.cellsRGB = cellsRGB;
    }

    public void clear() {
        CanvasPrinter.instance = null;
    }
}

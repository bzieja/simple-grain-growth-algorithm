package gui;

import app.grid.Cell;
import app.AppConfiguration;
import app.grid.GrainMap;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;
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
    int[][] cellsRGB;
    Map<Integer, Integer[]> mapRGB; //IDcell, color
    Random random;

    private CanvasPrinter(Canvas canvas, AppConfiguration appConfiguration, GrainMap grainMap) {
        this.canvas = canvas;
        this.numberOfGrainsAtX = appConfiguration.getNumberOfGrainsAtX();
        this.numberOfGrainsAtY = appConfiguration.getNumberOfGrainsAtY();
        this.numberOfInitialGrains = appConfiguration.getNumberOfInitialGrains();
        this.grainMap = grainMap;
        this.random = new Random(this.numberOfInitialGrains);

        this.mapRGB = new HashMap<>();

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

    public static CanvasPrinter getInstance(){
        return CanvasPrinter.instance;
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
                } else if (board[i][j].isImmutablePhase()) {
                    graphicsContext.setFill(Color.rgb(255, 0, 255));
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
        //Random random = new Random(this.numberOfInitialGrains);
        for (int i = 0; i < numberOfInitialGrains; i++) {
            cellsRGB[i] = new int[]{1 + random.nextInt(254), 1 + random.nextInt(254), 1 + random.nextInt(254)};
        }
    }

    public int getCellIdByCoordinates(double x, double y) {

        for (int i = 0; i < this.numberOfGrainsAtX; i++) {
            for (int j = 0; j < this.numberOfGrainsAtY; j++) {
                if (x >= i * cellXDimension && x < (i + 1) * cellXDimension &&
                    y >= j * cellYDimension && y < (j + 1) * cellYDimension) {

                    return grainMap.currentStep[j][i].getId();
                }
            }
        }

        return 0;
    }

    public Cell getCellByCoordinates(double x, double y) {
        for (int i = 0; i < this.numberOfGrainsAtX; i++) {
            for (int j = 0; j < this.numberOfGrainsAtY; j++) {
                if (x >= i * cellXDimension && x < (i + 1) * cellXDimension &&
                        y >= j * cellYDimension && y < (j + 1) * cellYDimension) {

                    return grainMap.currentStep[j][i];
                }
            }
        }
        return null;
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

    public void increaseNumberOfInitialGrains(int howMuchToIncrease) {

        int[][] biggerCellsRGB = new int[this.cellsRGB.length + howMuchToIncrease][3];

        for (int i = 0; i < this.cellsRGB.length; i++) {
            for (int j = 0; j < this.cellsRGB[0].length; j++) {
                biggerCellsRGB[i][j] = cellsRGB[i][j];
            }
        }

        for (int i = this.cellsRGB.length; i < this.cellsRGB.length + howMuchToIncrease; i++) {
            biggerCellsRGB[i] = new int[]{1 + random.nextInt(254), 1 + random.nextInt(254), 1 + random.nextInt(254)};
        }

        this.cellsRGB = biggerCellsRGB;
    }
}

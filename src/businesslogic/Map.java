package businesslogic;

import java.util.Random;

public class Map {
    static int IdCounter = 1;

    int numberOfInitialGrains;
    int dimensionX; //numberOfCellsAtX
    int dimensionY; //numberOfCellsAtY

    static Cell[][] currentStep;
    static Cell[][] previousStep;

    public Map(int numberOfInitialGrains, int dimensionX, int dimensionY) {
        this.numberOfInitialGrains = numberOfInitialGrains;
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;

        if (Map.currentStep != null) {
            Map.currentStep = new Cell[dimensionX][dimensionY];

            for (int i = 0; i < dimensionX; i++) {
                for (int j = 0; j < dimensionY; j++) {
                    Map.currentStep[i][j] = new Cell(i, j);
                }
            }
        }

    }


    //method for start


}

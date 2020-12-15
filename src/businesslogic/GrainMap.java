package businesslogic;

import businesslogic.inclusion.Inclusion;

import java.util.Random;

public class GrainMap {
    public static int IdCounter = 0;
    //int emptyCellsCounter;

    int numberOfInitialGrains;
    int numberOfCellsAtX;
    int numberOfCellsAtY;
    String neighbourType;

    public Cell[][] currentStep;

    public GrainMap(GlobalData globalData) {
        this.numberOfInitialGrains = globalData.getNumberOfInitialGrains();
        this.numberOfCellsAtX = globalData.getNumberOfGrainsAtX();
        this.numberOfCellsAtY = globalData.getNumberOfGrainsAtY();
        this.neighbourType = globalData.getNeighbourType();

        this.currentStep = new Cell[numberOfCellsAtX][numberOfCellsAtY];

        for (int i = 0; i < numberOfCellsAtX; i++) {
            for (int j = 0; j < numberOfCellsAtY; j++) {
                this.currentStep[i][j] = new Cell(i, j);
                //this.emptyCellsCounter++;
            }
        }


        //initialize grains
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < numberOfInitialGrains; i++) {
            int randX;
            int randY;

            while (true) {
                randX = random.nextInt(numberOfCellsAtX);
                randY = random.nextInt(numberOfCellsAtY);
                if (this.currentStep[randX][randY].isEmpty()) {
                    break;
                }
            }

            this.currentStep[randX][randY].setId(IdCounter);
            //this.emptyCellsCounter--;
            IdCounter++;
        }

        //add Inclusions at the start
        if (globalData.isStartWithInclusions()) {
            Inclusion inclusion = new Inclusion(globalData, this.currentStep, this.hasEmptyCells());
            inclusion.add();
        }

    }

    public void nextStep() {
        Cell[][] nextStep = new Cell[numberOfCellsAtX][numberOfCellsAtY];

        for (int i = 0; i < numberOfCellsAtX; i++) {
            for (int j = 0; j < numberOfCellsAtY; j++) {
                nextStep[i][j] = new Cell(currentStep[i][j].getX(), currentStep[i][j].getY());
                nextStep[i][j].setId(currentStep[i][j].getId());

                if (this.currentStep[i][j].isEmpty()) { //state of cell can be changed only if it's empty

                    Neighborhood neighborhood = new Neighborhood(this.neighbourType, this.currentStep, i, j, numberOfInitialGrains);
                    nextStep[i][j].setId(neighborhood.getNextState());
                    //emptyCellsCounter--;
                    //neighbour logic for each cell

                }
            }
        }

        this.currentStep = nextStep;
    }

    public boolean hasEmptyCells() {
       /*
        if (this.emptyCellsCounter > 0) {
            return true;
        } else {
            return false;
        }*/

        for (int i = 0; i < numberOfCellsAtX; i++) {
            for (int j = 0; j < numberOfCellsAtY; j++) {
                if (this.currentStep[i][j].isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Cell[][] getCurrentStep() {
        return this.currentStep;
    }

    public void printCurrentStepForDebug() {
        for (int i = 0; i < numberOfCellsAtX; i++) {
            for (int j = 0; j < numberOfCellsAtY; j++) {
                System.out.print(this.currentStep[i][j].id + " ");
            }
            System.out.print("\n");
        }

    }
}

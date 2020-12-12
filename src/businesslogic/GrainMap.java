package businesslogic;

import java.util.Random;

public class GrainMap {
    public static int IdCounter = 0;

    int numberOfInitialGrains;
    int numberOfCellsAtX; //numberOfCellsAtX
    int numberOfCellsAtY; //numberOfCellsAtY
    String neighbourType;

    public static Cell[][] currentStep;
    public static Cell[][] previousStep;

    public GrainMap(int numberOfInitialGrains, int numberOfCellsAtX, int numberOfCellsAtY, String neighbourType) {
        this.numberOfInitialGrains = numberOfInitialGrains;
        this.numberOfCellsAtX = numberOfCellsAtX;
        this.numberOfCellsAtY = numberOfCellsAtY;
        this.neighbourType = neighbourType;
        
        GrainMap.currentStep = new Cell[numberOfCellsAtX][numberOfCellsAtY];

        for (int i = 0; i < numberOfCellsAtX; i++) {
            for (int j = 0; j < numberOfCellsAtY; j++) {
                GrainMap.currentStep[i][j] = new Cell(i, j);
            }
        }

        //initialize grains
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < numberOfInitialGrains; i++) {
            int randX = random.nextInt(numberOfCellsAtX);
            int randY = random.nextInt(numberOfCellsAtY);
            currentStep[randX][randY].setId(IdCounter);
            IdCounter++;
        }



    }


    public void nextStep(){
        GrainMap.previousStep = GrainMap.currentStep;

        for (int i = 0; i < numberOfCellsAtX; i++) {
            for (int j = 0; j < numberOfCellsAtY; j++) {

                if (GrainMap.previousStep[i][j].isEmpty()) {

                    //neighbour logic for each cell
                    if (this.neighbourType.equals("absorbing")) {

                        System.out.println("Absorbing boundary conditions - to do");


                    } else if (this.neighbourType.equals("periodic")) {

                        Neighborhood neighborhood = new Neighborhood();
                        neighborhood.exploreTheNeighbourhoodWithPeriodicBoundaries(GrainMap.previousStep, i, j, numberOfInitialGrains);
                        GrainMap.currentStep[i][j].setId(neighborhood.getNextState());

                    } else {
                        System.out.println("Unknown type of boundary conditions");
                    }

                }
            }
        }
    }

    public boolean hasEmptyCells() {
        for (int i = 0; i < numberOfCellsAtX; i++) {
            for (int j = 0; j < numberOfCellsAtY; j++) {
                if (currentStep[i][j].getId() == -1) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Cell[][] getCurrentStep() {
        return currentStep;
    }

    public static Cell[][] getPreviousStep() {
        return previousStep;
    }
}

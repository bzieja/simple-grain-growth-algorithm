package app;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Neighborhood {
    Cell[][] boardToExplore;
    int i;
    int j;
    int numberOfInitialGrains;
    String typeOfNeighbourhood;
    int[] neighboursCounter;
    boolean isChangingState;

    public Neighborhood(String typeOfNeighbourhood, Cell[][] boardToExplore, int i, int j, int numberOfInitialGrains) {
        this.boardToExplore = boardToExplore;
        this.i = i;
        this.j = j;
        this.numberOfInitialGrains = numberOfInitialGrains;
        this.typeOfNeighbourhood = typeOfNeighbourhood;

        this.isChangingState = false;
        this.neighboursCounter = new int[numberOfInitialGrains];

        switch (this.typeOfNeighbourhood) {
            case "periodic":
                exploreTheNeighbourhoodWithPeriodicBoundaries();
                break;
            case "absorbing":
                exploreTheNeighbourhoodWithAbsorbingBoundaries();
                break;
            default:
                System.err.println("Unknown type of neighbourhood!");
        }
    }

    public void exploreTheNeighbourhoodWithPeriodicBoundaries() {
        int xDimension = boardToExplore.length;
        int yDimension = boardToExplore[0].length;

        //up
        if (!boardToExplore[(i - 1 + xDimension) % xDimension][j].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(i - 1 + xDimension) % xDimension][j].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //up-right corner
        if (!boardToExplore[(i - 1 + xDimension) % xDimension][(j + 1) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(i - 1 + xDimension) % xDimension][(j + 1) % yDimension].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //right
        if (!boardToExplore[i][(j + 1) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[i][(j + 1) % yDimension].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //down-right corner
        if (!boardToExplore[(i + 1) % xDimension][(j + 1) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(i + 1) % xDimension][(j + 1) % yDimension].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //down
        if (!boardToExplore[(i + 1) % xDimension][j].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(i + 1) % xDimension][j].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //down-left corner
        if (!boardToExplore[(i + 1) % xDimension][(j - 1 + yDimension) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(i + 1) % xDimension][(j - 1 + yDimension) % yDimension].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //left corner
        if (!boardToExplore[i][(j - 1 + yDimension) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[i][(j - 1 + yDimension) % yDimension].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //up-left corner
        if (!boardToExplore[(i - 1 + xDimension) % xDimension][(j - 1 + yDimension) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(i - 1 + xDimension) % xDimension][(j - 1 + yDimension) % yDimension].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

    }

    public void exploreTheNeighbourhoodWithAbsorbingBoundaries() {
        int xDimension = boardToExplore.length;
        int yDimension = boardToExplore[0].length;

        //up

        if (i - 1 > 0 && !boardToExplore[i - 1][j].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[i - 1][j].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //up-right corner
        if (i - 1 > 0 && j + 1 < yDimension && !boardToExplore[i - 1][j + 1].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[i - 1][j + 1].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //right
        if (j + 1 < yDimension && !boardToExplore[i][j + 1].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[i][j + 1].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //down-right corner
        if (i + 1 < xDimension && j + 1 < yDimension && !boardToExplore[i + 1][j + 1].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[i + 1][j + 1].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //down
        if (i + 1 < xDimension && !boardToExplore[i + 1][j].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[i + 1][j].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //down-left corner
        if (i + 1 < xDimension && j - 1 > 0 && !boardToExplore[i + 1][j - 1].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[i + 1][j - 1].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //left corner
        if (j - 1 > 0 && !boardToExplore[i][j - 1 ].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[i][j - 1 ].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //up-left corner
        if (i - 1 > 0 && j - 1 > 0 && !boardToExplore[i - 1][j - 1].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[i - 1][j - 1].getId()]++;
            isChangingState = true;
            //neighbours++;
        }
    }

    public int getNextState() {

        if (isChangingState) {

            List<Integer> theIndexesOfMostFrequentGrains = new ArrayList<>();
            int currentMaxOfNeighboursType = Integer.MIN_VALUE;

            for (int i = 0; i < neighboursCounter.length; i++) {

                if (neighboursCounter[i] > currentMaxOfNeighboursType) {
                    theIndexesOfMostFrequentGrains.clear();
                    currentMaxOfNeighboursType = neighboursCounter[i];
                    theIndexesOfMostFrequentGrains.add(i);
                } else if (neighboursCounter[i] == currentMaxOfNeighboursType) {
                    theIndexesOfMostFrequentGrains.add(i);
                }

            }

            //System.out.println("Cell i, j: " + i + " " + j + "\tmy neighbour: " + theIndexesOfMostFrequentGrains + "max value: " + currentMaxOfNeighboursType);

            Random random = new Random(System.currentTimeMillis());
            return theIndexesOfMostFrequentGrains.get(random.nextInt(theIndexesOfMostFrequentGrains.size()));

        } else {
            return -1;
        }


    }


}

package app.exploration;

import app.grid.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleRule extends ExplorationRule {



    public SimpleRule(Cell[][] boardToExplore, int x, int y, int numberOfInitialGrains) {
        super(boardToExplore, x, y, numberOfInitialGrains);
    }

    @Override
    public void exploreWithPeriodicBoundaries() {
        //up
        if (!boardToExplore[(x - 1 + xDimension) % xDimension][y].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(x - 1 + xDimension) % xDimension][y].getId()]++;
            //isChangingState = true;
            //neighbours++;
        }

        //up-right corner
        if (!boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension].getId()]++;
            //isChangingState = true;
            //neighbours++;
        }

        //right
        if (!boardToExplore[x][(y + 1) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x][(y + 1) % yDimension].getId()]++;
            //isChangingState = true;
            //neighbours++;
        }

        //down-right corner
        if (!boardToExplore[(x + 1) % xDimension][(y + 1) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(x + 1) % xDimension][(y + 1) % yDimension].getId()]++;
            //isChangingState = true;
            //neighbours++;
        }

        //down
        if (!boardToExplore[(x + 1) % xDimension][y].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(x + 1) % xDimension][y].getId()]++;
            //isChangingState = true;
            //neighbours++;
        }

        //down-left corner
        if (!boardToExplore[(x + 1) % xDimension][(y - 1 + yDimension) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(x + 1) % xDimension][(y - 1 + yDimension) % yDimension].getId()]++;
            //isChangingState = true;
            //neighbours++;
        }

        //left corner
        if (!boardToExplore[x][(y - 1 + yDimension) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x][(y - 1 + yDimension) % yDimension].getId()]++;
            //isChangingState = true;
            //neighbours++;
        }

        //up-left corner
        if (!boardToExplore[(x - 1 + xDimension) % xDimension][(y - 1 + yDimension) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(x - 1 + xDimension) % xDimension][(y - 1 + yDimension) % yDimension].getId()]++;
            //isChangingState = true;
            //neighbours++;
        }
    }

    @Override
    public void exploreWithAbsorbingBoundaries() {

        //up
        if (x - 1 > 0 && !boardToExplore[x - 1][y].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x - 1][y].getId()]++;
            //isChangingState = true;
            //neighbours++;
        }

        //up-right corner
        if (x - 1 > 0 && y + 1 < yDimension && !boardToExplore[x - 1][y + 1].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x - 1][y + 1].getId()]++;
            //isChangingState = true;
            //neighbours++;
        }

        //right
        if (y + 1 < yDimension && !boardToExplore[x][y + 1].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x][y + 1].getId()]++;
            //isChangingState = true;
            //neighbours++;
        }

        //down-right corner
        if (x + 1 < xDimension && y + 1 < yDimension && !boardToExplore[x + 1][y + 1].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x + 1][y + 1].getId()]++;
            //isChangingState = true;
            //neighbours++;
        }

        //down
        if (x + 1 < xDimension && !boardToExplore[x + 1][y].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x + 1][y].getId()]++;
            //isChangingState = true;
            //neighbours++;
        }

        //down-left corner
        if (x + 1 < xDimension && y - 1 > 0 && !boardToExplore[x + 1][y - 1].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x + 1][y - 1].getId()]++;
            //isChangingState = true;
            //neighbours++;
        }

        //left corner
        if (y - 1 > 0 && !boardToExplore[x][y - 1 ].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x][y - 1 ].getId()]++;
            //isChangingState = true;
            //neighbours++;
        }

        //up-left corner
        if (x - 1 > 0 && y - 1 > 0 && !boardToExplore[x - 1][y - 1].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x - 1][y - 1].getId()]++;
            //isChangingState = true;
            //neighbours++;
        }
    }

    @Override
    public boolean isSatisfied() {
        boolean isChangingState = false;

        for (int i = 0; i < neighboursCounter.length; i++) {
            if (neighboursCounter[i] > 0) {
                isChangingState = true;
                break;
            }
        }

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
            nextIdOfCurrentCell = theIndexesOfMostFrequentGrains.get(random.nextInt(theIndexesOfMostFrequentGrains.size()));
            return true;
        } else {
            return false;
        }




    }
}

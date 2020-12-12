package businesslogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Neighborhood {

    int[] neighboursCounter;
    boolean isChangingState = false;

    public void exploreTheNeighbourhoodWithPeriodicBoundaries(Cell[][] boardToExplore, int i, int j, int numberOfGrains) { //where to explore, which cell...
        //System.out.print("exploreTheNeighbourhoodWithPeriodicBoundaries");
        neighboursCounter = new int[numberOfGrains]; //number of grains given type (number)
        int xDimension = boardToExplore.length;
        int yDimension = boardToExplore[0].length;

        //up
        if(!boardToExplore[(i - 1 + xDimension) % xDimension][j].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(i - 1 + xDimension) % xDimension][j].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //up-right corner
        if(!boardToExplore[(i - 1 + xDimension) % xDimension][(j + 1) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(i - 1 + xDimension) % xDimension][(j + 1) % yDimension].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //right
        if(!boardToExplore[i][(j + 1) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[i][(j + 1) % yDimension].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //down-right corner
        if(!boardToExplore[(i + 1) % xDimension][(j + 1) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(i + 1) % xDimension][(j + 1) % yDimension].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //down
        if(!boardToExplore[(i + 1) % xDimension][j].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(i + 1) % xDimension][j].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //down-left corner
        if(!boardToExplore[(i + 1) % xDimension][(j - 1 + yDimension) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(i + 1) % xDimension][(j - 1 + yDimension) % yDimension].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //left corner
        if(!boardToExplore[i][(j - 1 + yDimension) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[i][(j - 1 + yDimension) % yDimension].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

        //up-left corner
        if(!boardToExplore[(i - 1 + xDimension) % xDimension][(j - 1 + yDimension) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(i - 1 + xDimension) % xDimension][(j - 1 + yDimension) % yDimension].getId()]++;
            isChangingState = true;
            //neighbours++;
        }

    }

    public int getNextState() {

        if (isChangingState) {
            List<Integer> theIndexesOfMostFrequentGrains = new ArrayList<>();
            theIndexesOfMostFrequentGrains.add(Integer.MIN_VALUE);

            for (int i = 0; i < neighboursCounter.length; i++) {

                if (neighboursCounter[i] > theIndexesOfMostFrequentGrains.get(0)) {
                    theIndexesOfMostFrequentGrains.clear();
                    theIndexesOfMostFrequentGrains.add(i);
                } else if (neighboursCounter[i] == theIndexesOfMostFrequentGrains.get(0)) {
                    theIndexesOfMostFrequentGrains.add(i);
                }
            }

            if (theIndexesOfMostFrequentGrains.size() == 0) {
                return -1;
            } else if (theIndexesOfMostFrequentGrains.size() == 1) {
                return theIndexesOfMostFrequentGrains.get(0);
            } else {
                Random random = new Random(System.currentTimeMillis());
                return theIndexesOfMostFrequentGrains.get(random.nextInt(theIndexesOfMostFrequentGrains.size()));
            }
        } else {
            return -1;
        }


    }


}

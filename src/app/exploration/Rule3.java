package app.exploration;

import app.grid.Cell;

public class Rule3 extends ExplorationRule {

    public Rule3(Cell[][] boardToExplore, int x, int y, int numberOfInitialGrains) {
        super(boardToExplore, x, y, numberOfInitialGrains);
    }


    @Override
    public void exploreWithPeriodicBoundaries() {

        //up-right corner
        if (!boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension].getId()]++;
        }

        //down-right corner
        if (!boardToExplore[(x + 1) % xDimension][(y + 1) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(x + 1) % xDimension][(y + 1) % yDimension].getId()]++;
        }

        //down-left corner
        if (!boardToExplore[(x + 1) % xDimension][(y - 1 + yDimension) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(x + 1) % xDimension][(y - 1 + yDimension) % yDimension].getId()]++;
        }

        //up-left corner
        if (!boardToExplore[(x - 1 + xDimension) % xDimension][(y - 1 + yDimension) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(x - 1 + xDimension) % xDimension][(y - 1 + yDimension) % yDimension].getId()]++;
        }

    }

    @Override
    public void exploreWithAbsorbingBoundaries() {

        //up-right corner
        if (x - 1 > 0 && y + 1 < yDimension && !boardToExplore[x - 1][y + 1].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x - 1][y + 1].getId()]++;
        }

        //down-right corner
        if (x + 1 < xDimension && y + 1 < yDimension && !boardToExplore[x + 1][y + 1].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x + 1][y + 1].getId()]++;
        }

        //down-left corner
        if (x + 1 < xDimension && y - 1 > 0 && !boardToExplore[x + 1][y - 1].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x + 1][y - 1].getId()]++;
        }

        //up-left corner
        if (x - 1 > 0 && y - 1 > 0 && !boardToExplore[x - 1][y - 1].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x - 1][y - 1].getId()]++;
        }
    }

    @Override
    public boolean isSatisfied() {

        for (int i = 0; i < neighboursCounter.length; i++) {
            if (neighboursCounter[i] >= 3) {
                nextIdOfCurrentCell = i;
                return true;
            }
        }

        return false;
    }
}

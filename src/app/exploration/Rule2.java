package app.exploration;

import app.grid.Cell;

public class Rule2 extends ExplorationRule {

    public Rule2(Cell[][] boardToExplore, int x, int y, int numberOfInitialGrains) {
        super(boardToExplore, x, y, numberOfInitialGrains);
    }

    @Override
    public void exploreWithPeriodicBoundaries() {
        //up
        if (!boardToExplore[(x - 1 + xDimension) % xDimension][y].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(x - 1 + xDimension) % xDimension][y].getId()]++;
            //isSatisfied = true;
            //neighbours++;
        }

        //right
        if (!boardToExplore[x][(y + 1) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x][(y + 1) % yDimension].getId()]++;
            //isSatisfied = true;
            //neighbours++;
        }

        //down
        if (!boardToExplore[(x + 1) % xDimension][y].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(x + 1) % xDimension][y].getId()]++;
            //isSatisfied = true;
            //neighbours++;
        }

        //left
        if (!boardToExplore[x][(y - 1 + yDimension) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x][(y - 1 + yDimension) % yDimension].getId()]++;
            //isSatisfied = true;
            //neighbours++;
        }

    }

    @Override
    public void exploreWithAbsorbingBoundaries() {
        //up
        if (x - 1 > 0 && !boardToExplore[x - 1][y].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x - 1][y].getId()]++;
        }

        //right
        if (y + 1 < yDimension && !boardToExplore[x][y + 1].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x][y + 1].getId()]++;
        }

        //down
        if (x + 1 < xDimension && !boardToExplore[x + 1][y].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x + 1][y].getId()]++;
        }

        //left
        if (y - 1 > 0 && !boardToExplore[x][y - 1 ].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x][y - 1 ].getId()]++;
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

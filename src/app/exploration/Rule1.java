package app.exploration;

import app.grid.Cell;

public class Rule1 extends ExplorationRule{

    public Rule1(Cell[][] boardToExplore, int x, int y, int numberOfInitialGrains) {
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

        //up-right corner
        if (!boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension].getId()]++;
            //isSatisfied = true;
            //neighbours++;
        }

        //right
        if (!boardToExplore[x][(y + 1) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x][(y + 1) % yDimension].getId()]++;
            //isSatisfied = true;
            //neighbours++;
        }

        //down-right corner
        if (!boardToExplore[(x + 1) % xDimension][(y + 1) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(x + 1) % xDimension][(y + 1) % yDimension].getId()]++;
            //isSatisfied = true;
            //neighbours++;
        }

        //down
        if (!boardToExplore[(x + 1) % xDimension][y].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(x + 1) % xDimension][y].getId()]++;
            //isSatisfied = true;
            //neighbours++;
        }

        //down-left corner
        if (!boardToExplore[(x + 1) % xDimension][(y - 1 + yDimension) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(x + 1) % xDimension][(y - 1 + yDimension) % yDimension].getId()]++;
            //isSatisfied = true;
            //neighbours++;
        }

        //left corner
        if (!boardToExplore[x][(y - 1 + yDimension) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x][(y - 1 + yDimension) % yDimension].getId()]++;
            //isSatisfied = true;
            //neighbours++;
        }

        //up-left corner
        if (!boardToExplore[(x - 1 + xDimension) % xDimension][(y - 1 + yDimension) % yDimension].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[(x - 1 + xDimension) % xDimension][(y - 1 + yDimension) % yDimension].getId()]++;
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

        //up-right corner
        if (x - 1 > 0 && y + 1 < yDimension && !boardToExplore[x - 1][y + 1].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x - 1][y + 1].getId()]++;
        }

        //right
        if (y + 1 < yDimension && !boardToExplore[x][y + 1].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x][y + 1].getId()]++;
        }

        //down-right corner
        if (x + 1 < xDimension && y + 1 < yDimension && !boardToExplore[x + 1][y + 1].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x + 1][y + 1].getId()]++;
        }

        //down
        if (x + 1 < xDimension && !boardToExplore[x + 1][y].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x + 1][y].getId()]++;
        }

        //down-left corner
        if (x + 1 < xDimension && y - 1 > 0 && !boardToExplore[x + 1][y - 1].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x + 1][y - 1].getId()]++;
        }

        //left corner
        if (y - 1 > 0 && !boardToExplore[x][y - 1 ].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x][y - 1 ].getId()]++;
        }

        //up-left corner
        if (x - 1 > 0 && y - 1 > 0 && !boardToExplore[x - 1][y - 1].isEmptyOrInclusion()) {
            neighboursCounter[boardToExplore[x - 1][y - 1].getId()]++;
        }
    }

    @Override
    public boolean isSatisfied() {

        for (int i = 0; i < neighboursCounter.length; i++) {
            if (neighboursCounter[i] >= 5) {
                nextIdOfCurrentCell = i;
                return true;
            }
        }

        return false;
    }

}

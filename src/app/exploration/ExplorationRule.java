package app.exploration;

import app.grid.Cell;

public abstract class ExplorationRule {

    boolean isSatisfied;
    Cell[][] boardToExplore;
    int x;
    int y;
    int xDimension;
    int yDimension;
    int[] neighboursCounter;
    int nextIdOfCurrentCell;

    public ExplorationRule(Cell[][] boardToExplore, int x, int y, int numberOfInitialGrains) {
        this.boardToExplore = boardToExplore;
        this.x = x;
        this.y = y;
        this.neighboursCounter = new int[numberOfInitialGrains];

        this.xDimension = boardToExplore.length;
        this.yDimension = boardToExplore[0].length;
    }

    public abstract void exploreWithPeriodicBoundaries();
    public abstract void exploreWithAbsorbingBoundaries();
    public abstract boolean isSatisfied();

    public int getNextCellState() {
        return nextIdOfCurrentCell;
    }
}

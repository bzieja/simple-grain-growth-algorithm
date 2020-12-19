package app.exploration;

import app.grid.Cell;

public class Neighborhood {
    Cell[][] boardToExplore;
    int x;
    int y;
    int numberOfInitialGrains;
    String typeOfBoundaries;
    int nextIdOfCurrentCell;


    public Neighborhood(String typeOfBoundaries, Cell[][] boardToExplore, int x, int y, int numberOfInitialGrains) {
        this.boardToExplore = boardToExplore;
        this.x = x;
        this.y = y;
        this.numberOfInitialGrains = numberOfInitialGrains;
        this.typeOfBoundaries = typeOfBoundaries;
        this.nextIdOfCurrentCell = -1;

        ExplorationRule[] explorationRules = new ExplorationRule[4];
        explorationRules[0] = new Rule1(boardToExplore, x, y, numberOfInitialGrains);
        explorationRules[1] = new Rule2(boardToExplore, x, y, numberOfInitialGrains);
        explorationRules[2] = new Rule3(boardToExplore, x, y, numberOfInitialGrains);
        explorationRules[3] = new Rule4(boardToExplore, x, y, numberOfInitialGrains);
        //System.out.println("In Neighbourhood for x y:" + x + " " + y);

        switch (this.typeOfBoundaries) {
            case "periodic":
                for (int i = 0; i < explorationRules.length; i++) {
                    explorationRules[i].exploreWithPeriodicBoundaries();
                    //System.out.println("periodic after exploration");
                    if (explorationRules[i].isSatisfied()) {
                        nextIdOfCurrentCell = explorationRules[i].getNextCellState();
                        //System.out.println("periodic returned id: " + nextIdOfCurrentCell);
                        break;
                    }
                }
                break;
            case "absorbing":
                for (int i = 0; i < explorationRules.length; i++) {
                    explorationRules[i].exploreWithAbsorbingBoundaries();
                    if (explorationRules[i].isSatisfied()) {
                        nextIdOfCurrentCell = explorationRules[i].getNextCellState();
                        break;
                    }
                }
                break;
            default:
                System.err.println("Unknown type of neighbourhood!");
        }
    }


    public int getNextState() {
        return nextIdOfCurrentCell;
    }


}

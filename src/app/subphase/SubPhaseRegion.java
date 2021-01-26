package app.subphase;

import app.grid.Cell;
import app.grid.GrainMap;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubPhaseRegion {
    List<Cell> cells;
    int idOfCellsInRegion;

    public SubPhaseRegion(int idOfCellsInRegion, GrainMap grainMap) {
        this.idOfCellsInRegion = idOfCellsInRegion;
        this.cells = new ArrayList<>();
        initRegion(grainMap);
    }

    public SubPhaseRegion(int idOfCellsInRegion, List<Cell> subPhaseCells) {
        this.idOfCellsInRegion = idOfCellsInRegion;
        this.cells = subPhaseCells;
        //initRegion(GrainMap.getInstance());
    }

    private void initRegion(GrainMap grainMap) {

        for (int i = 0; i < grainMap.numberOfCellsAtX; i++) {
            for (int j = 0; j < grainMap.numberOfCellsAtY; j++) {

                //poczatkowe utworzenie regiow -> kolor = region
                if (grainMap.getCurrentStep()[i][j].getId() == idOfCellsInRegion) {
                    this.cells.add(grainMap.currentStep[i][j]);
                }

            }
        }

    }

    public void nextStep(GrainMap grainMap) {
        //Cell[][] nextStep = new Cell[grainMap.numberOfCellsAtX][grainMap.numberOfCellsAtY];
        Map<Cell, Integer> mapCellsToChange = new HashMap<>();
        Cell[][] boardToExplore = grainMap.getCurrentStep();
        int xDimension = boardToExplore.length;
        int yDimension = boardToExplore[0].length;

        for (Cell currentCell : cells) {

            if (!currentCell.isChangedSecondTime()) {   //jesli nie byla jeszcze zmieniana to zbadaj sasiadow
                Map<Integer, Integer> map = new HashMap<>();  //id, occurences

                int i = currentCell.getX();
                int j = currentCell.getY();

                //up
                if (this.cells.contains(boardToExplore[(i - 1 + xDimension) % xDimension][j])
                && this.idOfCellsInRegion != boardToExplore[(i - 1 + xDimension) % xDimension][j].getId()) {
                    //subPhaseCell.addNeighbour(boardToExplore[(i - 1 + xDimension) % xDimension][j]);
                    map.putIfAbsent(boardToExplore[(i - 1 + xDimension) % xDimension][j].getId(), 0);
                    int count = map.get(boardToExplore[(i - 1 + xDimension) % xDimension][j].getId());
                    map.put(boardToExplore[(i - 1 + xDimension) % xDimension][j].getId(), count + 1);
                    //isSatisfied = true;
                    //neighbours++;
                }

                //up-right corner
                if (this.cells.contains(boardToExplore[(i - 1 + xDimension) % xDimension][(j + 1) % yDimension])
                        && this.idOfCellsInRegion != boardToExplore[(i - 1 + xDimension) % xDimension][(j + 1) % yDimension].getId()) {
                    map.putIfAbsent(boardToExplore[(i - 1 + xDimension) % xDimension][(j + 1) % yDimension].getId(), 0);
                    int count = map.get(boardToExplore[(i - 1 + xDimension) % xDimension][(j + 1) % yDimension].getId());
                    map.put(boardToExplore[(i - 1 + xDimension) % xDimension][(j + 1) % yDimension].getId(), count + 1);
                    //isSatisfied = true;
                    //neighbours++;
                }

                //right
                if (this.cells.contains(boardToExplore[i][(j + 1) % yDimension])
                        && this.idOfCellsInRegion != boardToExplore[i][(j + 1) % yDimension].getId()) {
                    map.putIfAbsent(boardToExplore[i][(j + 1) % yDimension].getId(), 0);
                    int count = map.get(boardToExplore[i][(j + 1) % yDimension].getId());
                    map.put(boardToExplore[i][(j + 1) % yDimension].getId(), count + 1);
                }

                //down-right corner
                if (this.cells.contains(boardToExplore[(i + 1) % xDimension][(j + 1) % yDimension])
                        && this.idOfCellsInRegion != boardToExplore[(i + 1) % xDimension][(j + 1) % yDimension].getId()) {
                    map.putIfAbsent(boardToExplore[(i + 1) % xDimension][(j + 1) % yDimension].getId(), 0);
                    int count = map.get(boardToExplore[(i + 1) % xDimension][(j + 1) % yDimension].getId());
                    map.put(boardToExplore[(i + 1) % xDimension][(j + 1) % yDimension].getId(), count + 1);
                }

                //down
                if (this.cells.contains(boardToExplore[(i + 1) % xDimension][j])
                        && this.idOfCellsInRegion != boardToExplore[(i + 1) % xDimension][j].getId()) {
                    map.putIfAbsent(boardToExplore[(i + 1) % xDimension][j].getId(), 0);
                    int count = map.get(boardToExplore[(i + 1) % xDimension][j].getId());
                    map.put(boardToExplore[(i + 1) % xDimension][j].getId(), count + 1);
                }

                //down-left corner
                if (this.cells.contains(boardToExplore[(i + 1) % xDimension][(j - 1 + yDimension) % yDimension])
                        && this.idOfCellsInRegion != boardToExplore[(i + 1) % xDimension][(j - 1 + yDimension) % yDimension].getId()) {
                    map.putIfAbsent(boardToExplore[(i + 1) % xDimension][(j - 1 + yDimension) % yDimension].getId(), 0);
                    int count = map.get(boardToExplore[(i + 1) % xDimension][(j - 1 + yDimension) % yDimension].getId());
                    map.put(boardToExplore[(i + 1) % xDimension][(j - 1 + yDimension) % yDimension].getId(), count + 1);
                }

                //left corner
                if (this.cells.contains(boardToExplore[i][(j - 1 + yDimension) % yDimension])
                        && this.idOfCellsInRegion != boardToExplore[i][(j - 1 + yDimension) % yDimension].getId()) {
                    map.putIfAbsent(boardToExplore[i][(j - 1 + yDimension) % yDimension].getId(), 0);
                    int count = map.get(boardToExplore[i][(j - 1 + yDimension) % yDimension].getId());
                    map.put(boardToExplore[i][(j - 1 + yDimension) % yDimension].getId(), count + 1);
                }

                //up-left corner
                if (this.cells.contains(boardToExplore[(i - 1 + xDimension) % xDimension][(j - 1 + yDimension) % yDimension])
                        && this.idOfCellsInRegion != boardToExplore[(i - 1 + xDimension) % xDimension][(j - 1 + yDimension) % yDimension].getId()) {
                    map.putIfAbsent(boardToExplore[(i - 1 + xDimension) % xDimension][(j - 1 + yDimension) % yDimension].getId(), 0);
                    int count = map.get(boardToExplore[(i - 1 + xDimension) % xDimension][(j - 1 + yDimension) % yDimension].getId());
                    map.put(boardToExplore[(i - 1 + xDimension) % xDimension][(j - 1 + yDimension) % yDimension].getId(), count + 1);
                }
                if (map.size() > 0) { //bedzie zmiana
                    int idOfMaxNeighbours = map.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
                    mapCellsToChange.put(currentCell, idOfMaxNeighbours);
                }

                }

            }

        for (Map.Entry<Cell, Integer> cellIntegerEntry : mapCellsToChange.entrySet()) {
            cellIntegerEntry.getKey().setId(cellIntegerEntry.getValue());
            cellIntegerEntry.getKey().setChangedSecondTime(true);
        }

    }

    public List<SubPhaseRegion> divideOnSubregions() {

        Map<Integer, List<Cell>> regions = cells.stream().collect(Collectors.groupingBy(Cell::getId));
        //idCells, listOfCells


        List<SubPhaseRegion> result = new ArrayList<>();
        for (Map.Entry<Integer, List<Cell>> integerListEntry : regions.entrySet()) {
            result.add(new SubPhaseRegion(integerListEntry.getKey(), integerListEntry.getValue()));
        }
        return result;

    }

    public List<Cell> getRegionCells() {
        return cells;
    }
}

package app.boundaries;

import app.grid.Cell;
import app.grid.GrainMap;
import app.subphase.SubPhase;
import app.subphase.SubPhaseRegion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Boundaries {

    private static Boundaries instance;
    Map<Cell, Integer> cellMap; //list of <Cell, OldId>


    public Boundaries() {
        cellMap = new HashMap<>();
    }

    public void drawBoundaries(int thickness) {
        if (thickness == 2) {
            findBoundaryCells();
            markBoundaryCells();
        }
    }

    private void findBoundaryCells() {
        Cell[][] boardToExplore = GrainMap.getInstance().currentStep;
        SubPhase subPhase = SubPhase.getInstance();
        subPhase.divideIntoRegions();
        int xDimension = boardToExplore.length;
        int yDimension = boardToExplore[0].length;

        for (SubPhaseRegion subPhaseRegion : subPhase.getSubPhaseRegions()) {

            for (Cell regionCell : subPhaseRegion.getRegionCells()) {

                int x = regionCell.getX();
                int y = regionCell.getY();

                //up
                if (!subPhaseRegion.containsCell(boardToExplore[(x - 1 + xDimension) % xDimension][y])) {
                    cellMap.putIfAbsent(regionCell, regionCell.getId());
                    continue;
                }

                //up-right corner
                if (!subPhaseRegion.containsCell(boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension])) {
                    cellMap.putIfAbsent(regionCell, regionCell.getId());
                    continue;
                }

                //right
                if (!subPhaseRegion.containsCell(boardToExplore[x][(y + 1) % yDimension])) {
                    cellMap.putIfAbsent(regionCell, regionCell.getId());
                    continue;
                }

                //down-right corner
                if (!subPhaseRegion.containsCell(boardToExplore[(x + 1) % xDimension][(y + 1) % yDimension])) {
                    cellMap.putIfAbsent(regionCell, regionCell.getId());
                    continue;
                }

                //down
                if (!subPhaseRegion.containsCell(boardToExplore[(x + 1) % xDimension][y])) {
                    cellMap.putIfAbsent(regionCell, regionCell.getId());
                    continue;
                }
                //down-left corner
                if (!subPhaseRegion.containsCell(boardToExplore[(x + 1) % xDimension][(y - 1 + yDimension) % yDimension])) {
                    cellMap.putIfAbsent(regionCell, regionCell.getId());
                    continue;
                }

                //left corner
                if (!subPhaseRegion.containsCell(boardToExplore[x][(y - 1 + yDimension) % yDimension])) {
                    cellMap.putIfAbsent(regionCell, regionCell.getId());
                    continue;
                }

                //up-left corner
                if (!subPhaseRegion.containsCell(boardToExplore[(x - 1 + xDimension) % xDimension][(y - 1 + yDimension) % yDimension])) {
                    cellMap.putIfAbsent(regionCell, regionCell.getId());
                    continue;
                }
            }
        }

        addAdditionalBoundariesOnImmutablePhase();
    }

    private void addAdditionalBoundariesOnImmutablePhase() {
        GrainMap grainMap = GrainMap.getInstance();
        Cell[][] boardToExplore = grainMap.currentStep;

        int xDimension = boardToExplore.length;
        int yDimension = boardToExplore[0].length;

        for (int i = 0; i < grainMap.numberOfCellsAtX; i++) {
            for (int j = 0; j < grainMap.numberOfCellsAtY; j++) {

                if (grainMap.getCurrentStep()[i][j].isImmutablePhase()) {
                    int x = i;
                    int y = j;

                    //immutable condition will be enough
                    //up
                    if (!boardToExplore[(x - 1 + xDimension) % xDimension][y].isImmutableOrEmptyOrInclusion()) {
                        cellMap.putIfAbsent(grainMap.getCurrentStep()[i][j], grainMap.getCurrentStep()[i][j].getId());
                        continue;
                    }

                    //up-right corner
                    if (!boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension].isImmutableOrEmptyOrInclusion()) {
                        cellMap.putIfAbsent(grainMap.getCurrentStep()[i][j], grainMap.getCurrentStep()[i][j].getId());
                        continue;
                    }

                    //right
                    if (!boardToExplore[x][(y + 1) % yDimension].isImmutableOrEmptyOrInclusion()) {
                        cellMap.putIfAbsent(grainMap.getCurrentStep()[i][j], grainMap.getCurrentStep()[i][j].getId());
                        continue;
                    }

                    //down-right corner
                    if (!boardToExplore[(x + 1) % xDimension][(y + 1) % yDimension].isImmutableOrEmptyOrInclusion()) {
                        cellMap.putIfAbsent(grainMap.getCurrentStep()[i][j], grainMap.getCurrentStep()[i][j].getId());
                        continue;
                    }

                    //down
                    if (!boardToExplore[(x + 1) % xDimension][y].isImmutableOrEmptyOrInclusion()) {
                        cellMap.putIfAbsent(grainMap.getCurrentStep()[i][j], grainMap.getCurrentStep()[i][j].getId());
                        continue;
                    }

                    //down-left corner
                    if (!boardToExplore[(x + 1) % xDimension][(y - 1 + yDimension) % yDimension].isImmutableOrEmptyOrInclusion()) {
                        cellMap.putIfAbsent(grainMap.getCurrentStep()[i][j], grainMap.getCurrentStep()[i][j].getId());
                        continue;
                    }

                    //left corner
                    if (!boardToExplore[x][(y - 1 + yDimension) % yDimension].isImmutableOrEmptyOrInclusion()) {
                        cellMap.putIfAbsent(grainMap.getCurrentStep()[i][j], grainMap.getCurrentStep()[i][j].getId());
                        continue;
                    }

                    //up-left corner
                    if (!boardToExplore[(x - 1 + xDimension) % xDimension][(y - 1 + yDimension) % yDimension].isImmutableOrEmptyOrInclusion()) {
                        cellMap.putIfAbsent(grainMap.getCurrentStep()[i][j], grainMap.getCurrentStep()[i][j].getId());
                        continue;
                    }
                }

            }
        }


    }

    private void markBoundaryCells() {
        for (Map.Entry<Cell, Integer> cellIntegerEntry : cellMap.entrySet()) {
            cellIntegerEntry.getKey().setId(-2);
        }
    }

    public void clearAllGrains() {
        Arrays.stream(GrainMap.getInstance().currentStep).flatMap(Stream::of).filter(x -> !cellMap.containsKey(x)).forEach(x -> x.setId(-1));
    }

    public static Boundaries getInstance() {
        if (instance == null) {
            instance = new Boundaries();
        }
        return instance;
    }

    public static boolean hasInstance() {
        if (instance == null) {
            return false;
        } else {
            return true;
        }
    }
    
    public static void clear() {
        instance = null;
    }

}

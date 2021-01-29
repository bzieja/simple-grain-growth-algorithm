package app.boundaries;

import app.AppConfiguration;
import app.grid.Cell;
import app.grid.GrainMap;
import app.subphase.SubPhase;
import app.subphase.SubPhaseRegion;

import java.util.*;
import java.util.stream.Stream;

public class Boundaries {

    private Cell[][] currentStepSnapshot;

    private List<SubPhaseRegion> subPhaseRegionsSnapshot;
    private int startSnap;
    private int endSnap;

    private static Boundaries instance;
    Map<Cell, Map.Entry<Integer, SubPhaseRegion>> cellMap; //list of <Cell, OldId>
    Map<Cell, Map.Entry<Integer, SubPhaseRegion>> cellMapFor2px;
    Map<Cell, Map.Entry<Integer, SubPhaseRegion>> cellMapFor4px;
    Map<Cell, Map.Entry<Integer, SubPhaseRegion>> cellMapFor6px;

    public Boundaries() {
        cellMap = new HashMap<>();
        cellMapFor2px = new HashMap<>();
        cellMapFor4px = new HashMap<>();
        cellMapFor6px = new HashMap<>();

        currentStepSnapshot = null;
        subPhaseRegionsSnapshot = null;
    }

    public synchronized void drawBoundaries(int thickness) {
        thickness = thickness % 2 == 0 ? thickness : thickness + 1;

        if (this.cellMapFor2px == null || this.cellMapFor2px.size() == 0) {
            calculate2pxBoundary();
        }
        if (this.cellMapFor4px == null || this.cellMapFor4px.size() == 0) {
            calculate4pxBoundary();
        }
        if (this.cellMapFor6px == null || this.cellMapFor6px.size() == 0) {
            calculate6pxBoundary();
        }

        if (this.cellMap != null) {
            hideBoundaries();
        }

        SubPhase.getInstance().divideIntoRegions();
        if (thickness == 2) {
            this.cellMap = cellMapFor2px;
        } else if (thickness == 4) {
            this.cellMap = cellMapFor4px;
        } else if (thickness == 6) {
            this.cellMap = cellMapFor6px;
        }



        markBoundaryCells();

//
//        if (currentStepSnapshot == null && subPhaseRegionsSnapshot == null) {
//            currentStepSnapshot = GrainMap.getInstance().copyCurrentStep();
//
//            subPhaseRegionsSnapshot = SubPhase.getInstance().copySubRegions();
//            startSnap = SubPhase.getInstance().getNumberOfGrainsAtTheStart();
//            endSnap = SubPhase.getInstance().getNumberOfGrainsAtTheEnd();
//        } else {
//            hideBoundaries();
//            //SubPhase.getInstance().setSubPhaseRegions(subPhaseRegionsSnapshot);
//            //GrainMap.getInstance().setCurrentStep(currentStepSnapshot);
//            //GrainMap.replaceInstance(currentStepSnapshot);
//            //SubPhase.replaceAndGetInstance(subPhaseRegionsSnapshot, startSnap, endSnap);
//            GrainMap.getInstance().setCurrentStep(currentStepSnapshot);
//            SubPhase.getInstance().setConfigurables(subPhaseRegionsSnapshot, startSnap, endSnap);
//        }
//
//        //jesli null to snapshot;
//        //if (cellMap.size() > 0) {
//            //hideBoundaries();
//        //
//        //SubPhase.getInstance().divideIntoRegions();
//
//
//        for (int i = 0; i < thickness; i++) {
//            SubPhase subPhase = SubPhase.getInstance();
//            subPhase.divideIntoRegions();
//            findBoundaryCells();
//            markBoundaryCells();
//            System.out.println("Iteracja " + i + " znaleziono komorek granicznych: " + cellMap.size());
//        }


    }

    public synchronized void drawBoundaries(int thickness, SubPhaseRegion subPhaseRegion) {
        thickness = thickness % 2 == 0 ? thickness : thickness + 1;


        if (this.cellMapFor2px == null || this.cellMapFor2px.size() == 0) {
            calculate2pxBoundary(subPhaseRegion);
        }
        if (this.cellMapFor4px == null || this.cellMapFor4px.size() == 0) {
            calculate4pxBoundary();
        }
        if (this.cellMapFor6px == null || this.cellMapFor6px.size() == 0) {
            calculate6pxBoundary();
        }

        if (this.cellMap != null) {
            hideBoundaries();
        }

        SubPhase.getInstance().divideIntoRegions();
        if (thickness == 2) {
            this.cellMap = cellMapFor2px;
        } else if (thickness == 4) {
            this.cellMap = cellMapFor4px;
        } else if (thickness == 6) {
            this.cellMap = cellMapFor6px;
        }
        markBoundaryCells();
    }

    private synchronized void calculate2pxBoundary(SubPhaseRegion subPhaseRegion) {
        SubPhase subPhase = SubPhase.getInstance();
        subPhase.divideIntoRegions();
        findBoundaryCells(subPhaseRegion);
        //markBoundaryCells();
    }

    private synchronized void calculate2pxBoundary() {
            SubPhase subPhase = SubPhase.getInstance();
            subPhase.divideIntoRegions();
            findBoundaryCells();
            //markBoundaryCells();
    }

    private synchronized void calculate4pxBoundary() {
        Cell[][] boardToExplore = GrainMap.getInstance().copyCurrentStep();

        int xDimension = boardToExplore.length;
        int yDimension = boardToExplore[0].length;

        List<Cell> previousCells = new ArrayList<>();

        for (Map.Entry<Cell, Map.Entry<Integer, SubPhaseRegion>> cellEntryEntry : this.cellMapFor2px.entrySet()) {
            this.cellMapFor4px.put(cellEntryEntry.getKey(), new AbstractMap.SimpleEntry<>(cellEntryEntry.getValue().getKey(), cellEntryEntry.getValue().getValue()));
            previousCells.add(cellEntryEntry.getKey());
        }
        //for (int i = 0; i < GrainMap.getInstance().numberOfCellsAtX; i++) {
        //  for (int j = 0; j < GrainMap.getInstance().numberOfCellsAtY; j++) {

        for (Cell cellFromList : previousCells) {
            int x = cellFromList.getX();
            int y = cellFromList.getY();

            //SubPhaseRegion s = SubPhase.getInstance().getSubPhaseRegionByCell(boardToExplore[i][j]);
            //immutable condition will be enough
            //up

            if (AppConfiguration.getInstance().getNeighbourType().equals("periodic")) {
                Cell neighbour1 = boardToExplore[(x - 1 + xDimension) % xDimension][y];
                if (!previousCells.contains(neighbour1)) {
                    this.cellMapFor4px.putIfAbsent(neighbour1, new AbstractMap.SimpleEntry<>(neighbour1.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour1)));
                }

                //up-right corner
                //if (!boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension].isImmutableOrEmptyOrInclusion()) {
                Cell neighbour2 = boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension];
                if (!previousCells.contains(boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension])) {
                    this.cellMapFor4px.putIfAbsent(neighbour2, new AbstractMap.SimpleEntry<>(neighbour2.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour2)));
                }

                //right
                Cell neighbour3 = boardToExplore[x][(y + 1) % yDimension];
                if (!previousCells.contains(boardToExplore[x][(y + 1) % yDimension])) {
                    this.cellMapFor4px.putIfAbsent(neighbour3, new AbstractMap.SimpleEntry<>(neighbour3.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour3)));
                }

                //down-right corner
                Cell neighbour4 = boardToExplore[(x + 1) % xDimension][(y + 1) % yDimension];
                if (!previousCells.contains(boardToExplore[(x + 1) % xDimension][(y + 1) % yDimension])) {
                    this.cellMapFor4px.putIfAbsent(neighbour4, new AbstractMap.SimpleEntry<>(neighbour4.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour4)));
                }

                //down
                Cell neighbour5 = boardToExplore[(x + 1) % xDimension][y];
                if (!previousCells.contains(boardToExplore[(x + 1) % xDimension][y])) {
                    this.cellMapFor4px.putIfAbsent(neighbour5, new AbstractMap.SimpleEntry<>(neighbour5.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour5)));
                }

                //down-left corner
                Cell neighbour6 = boardToExplore[(x + 1) % xDimension][(y - 1 + yDimension) % yDimension];
                if (!previousCells.contains(boardToExplore[(x + 1) % xDimension][(y - 1 + yDimension) % yDimension])) {
                    this.cellMapFor4px.putIfAbsent(neighbour6, new AbstractMap.SimpleEntry<>(neighbour6.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour6)));
                }

                //left corner
                Cell neighbour7 = boardToExplore[x][(y - 1 + yDimension) % yDimension];
                if (!previousCells.contains(boardToExplore[x][(y - 1 + yDimension) % yDimension])) {
                    this.cellMapFor4px.putIfAbsent(neighbour7, new AbstractMap.SimpleEntry<>(neighbour7.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour7)));
                }

                //up-left corner
                Cell neighbour8 = boardToExplore[(x - 1 + xDimension) % xDimension][(y - 1 + yDimension) % yDimension];
                if (!previousCells.contains(boardToExplore[(x - 1 + xDimension) % xDimension][(y - 1 + yDimension) % yDimension])) {
                    this.cellMapFor4px.putIfAbsent(neighbour8, new AbstractMap.SimpleEntry<>(neighbour8.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour8)));
                }
            } else if(AppConfiguration.getInstance().getNeighbourType().equals("absorbing")) {

                if (x - 1 > 0) {
                    Cell neighbour1 = boardToExplore[(x - 1 + xDimension) % xDimension][y];
                    if (!previousCells.contains(neighbour1)) {
                        this.cellMapFor4px.putIfAbsent(neighbour1, new AbstractMap.SimpleEntry<>(neighbour1.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour1)));
                    }
                }

                //up-right corner
                if (x - 1 > 0 && y + 1 < yDimension) {
                    Cell neighbour2 = boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension];
                    if (!previousCells.contains(boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension])) {
                        this.cellMapFor4px.putIfAbsent(neighbour2, new AbstractMap.SimpleEntry<>(neighbour2.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour2)));
                    }
                }

                //right
                if (y + 1 < yDimension) {
                    Cell neighbour3 = boardToExplore[x][(y + 1) % yDimension];
                    if (!previousCells.contains(boardToExplore[x][(y + 1) % yDimension])) {
                        this.cellMapFor4px.putIfAbsent(neighbour3, new AbstractMap.SimpleEntry<>(neighbour3.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour3)));
                    }
                }


                //down-right corner
                if (x + 1 < xDimension && y + 1 < yDimension) {
                    Cell neighbour4 = boardToExplore[(x + 1) % xDimension][(y + 1) % yDimension];
                    if (!previousCells.contains(boardToExplore[(x + 1) % xDimension][(y + 1) % yDimension])) {
                        this.cellMapFor4px.putIfAbsent(neighbour4, new AbstractMap.SimpleEntry<>(neighbour4.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour4)));
                    }
                }

                //down
                if (x + 1 < xDimension) {
                    Cell neighbour5 = boardToExplore[(x + 1) % xDimension][y];
                    if (!previousCells.contains(boardToExplore[(x + 1) % xDimension][y])) {
                        this.cellMapFor4px.putIfAbsent(neighbour5, new AbstractMap.SimpleEntry<>(neighbour5.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour5)));
                    }
                }


                //down-left corner
                if(x + 1 < xDimension && y - 1 > 0) {
                    Cell neighbour6 = boardToExplore[(x + 1) % xDimension][(y - 1 + yDimension) % yDimension];
                    if (!previousCells.contains(boardToExplore[(x + 1) % xDimension][(y - 1 + yDimension) % yDimension])) {
                        this.cellMapFor4px.putIfAbsent(neighbour6, new AbstractMap.SimpleEntry<>(neighbour6.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour6)));
                    }
                }

                //left corner
                if (y - 1 > 0){
                    Cell neighbour7 = boardToExplore[x][(y - 1 + yDimension) % yDimension];
                    if (!previousCells.contains(boardToExplore[x][(y - 1 + yDimension) % yDimension])) {
                        this.cellMapFor4px.putIfAbsent(neighbour7, new AbstractMap.SimpleEntry<>(neighbour7.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour7)));
                    }
                }

                //up-left corner
                if (x - 1 > 0 && y - 1 > 0) {
                    Cell neighbour8 = boardToExplore[(x - 1 + xDimension) % xDimension][(y - 1 + yDimension) % yDimension];
                    if (!previousCells.contains(boardToExplore[(x - 1 + xDimension) % xDimension][(y - 1 + yDimension) % yDimension])) {
                        this.cellMapFor4px.putIfAbsent(neighbour8, new AbstractMap.SimpleEntry<>(neighbour8.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour8)));
                    }
                }
            }

        }
    }

    private synchronized void calculate6pxBoundary() {
        Cell[][] boardToExplore = GrainMap.getInstance().copyCurrentStep();

        int xDimension = boardToExplore.length;
        int yDimension = boardToExplore[0].length;

        List<Cell> previousCells = new ArrayList<>();

        for (Map.Entry<Cell, Map.Entry<Integer, SubPhaseRegion>> cellEntryEntry : this.cellMapFor4px.entrySet()) {
            this.cellMapFor6px.put(cellEntryEntry.getKey(), new AbstractMap.SimpleEntry<>(cellEntryEntry.getValue().getKey(), cellEntryEntry.getValue().getValue()));
            previousCells.add(cellEntryEntry.getKey());
        }
        //for (int i = 0; i < GrainMap.getInstance().numberOfCellsAtX; i++) {
        //  for (int j = 0; j < GrainMap.getInstance().numberOfCellsAtY; j++) {

        for (Cell cellFromList : previousCells) {
            int x = cellFromList.getX();
            int y = cellFromList.getY();

            //SubPhaseRegion s = SubPhase.getInstance().getSubPhaseRegionByCell(boardToExplore[i][j]);
            //immutable condition will be enough
            //up

            if (AppConfiguration.getInstance().getNeighbourType().equals("periodic")) {
                Cell neighbour1 = boardToExplore[(x - 1 + xDimension) % xDimension][y];
                if (!previousCells.contains(neighbour1)) {
                    this.cellMapFor6px.putIfAbsent(neighbour1, new AbstractMap.SimpleEntry<>(neighbour1.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour1)));
                }

                //up-right corner
                //if (!boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension].isImmutableOrEmptyOrInclusion()) {
                Cell neighbour2 = boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension];
                if (!previousCells.contains(boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension])) {
                    this.cellMapFor6px.putIfAbsent(neighbour2, new AbstractMap.SimpleEntry<>(neighbour2.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour2)));
                }

                //right
                Cell neighbour3 = boardToExplore[x][(y + 1) % yDimension];
                if (!previousCells.contains(boardToExplore[x][(y + 1) % yDimension])) {
                    this.cellMapFor6px.putIfAbsent(neighbour3, new AbstractMap.SimpleEntry<>(neighbour3.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour3)));
                }

                //down-right corner
                Cell neighbour4 = boardToExplore[(x + 1) % xDimension][(y + 1) % yDimension];
                if (!previousCells.contains(boardToExplore[(x + 1) % xDimension][(y + 1) % yDimension])) {
                    this.cellMapFor6px.putIfAbsent(neighbour4, new AbstractMap.SimpleEntry<>(neighbour4.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour4)));
                }

                //down
                Cell neighbour5 = boardToExplore[(x + 1) % xDimension][y];
                if (!previousCells.contains(boardToExplore[(x + 1) % xDimension][y])) {
                    this.cellMapFor6px.putIfAbsent(neighbour5, new AbstractMap.SimpleEntry<>(neighbour5.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour5)));
                }

                //down-left corner
                Cell neighbour6 = boardToExplore[(x + 1) % xDimension][(y - 1 + yDimension) % yDimension];
                if (!previousCells.contains(boardToExplore[(x + 1) % xDimension][(y - 1 + yDimension) % yDimension])) {
                    this.cellMapFor6px.putIfAbsent(neighbour6, new AbstractMap.SimpleEntry<>(neighbour6.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour6)));
                }

                //left corner
                Cell neighbour7 = boardToExplore[x][(y - 1 + yDimension) % yDimension];
                if (!previousCells.contains(boardToExplore[x][(y - 1 + yDimension) % yDimension])) {
                    this.cellMapFor6px.putIfAbsent(neighbour7, new AbstractMap.SimpleEntry<>(neighbour7.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour7)));
                }

                //up-left corner
                Cell neighbour8 = boardToExplore[(x - 1 + xDimension) % xDimension][(y - 1 + yDimension) % yDimension];
                if (!previousCells.contains(boardToExplore[(x - 1 + xDimension) % xDimension][(y - 1 + yDimension) % yDimension])) {
                    this.cellMapFor6px.putIfAbsent(neighbour8, new AbstractMap.SimpleEntry<>(neighbour8.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour8)));
                }
            } else if (AppConfiguration.getInstance().getNeighbourType().equals("absorbing")){
                //up
                if (x - 1 > 0){
                    Cell neighbour1 = boardToExplore[(x - 1 + xDimension) % xDimension][y];
                    if (!previousCells.contains(neighbour1)) {
                        this.cellMapFor6px.putIfAbsent(neighbour1, new AbstractMap.SimpleEntry<>(neighbour1.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour1)));
                    }
                }

                //up-right corner
                if (x - 1 > 0 && y + 1 < yDimension){
                    Cell neighbour2 = boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension];
                    if (!previousCells.contains(boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension])) {
                        this.cellMapFor6px.putIfAbsent(neighbour2, new AbstractMap.SimpleEntry<>(neighbour2.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour2)));
                    }
                }

                //right
                if (y + 1 < yDimension){
                    Cell neighbour3 = boardToExplore[x][(y + 1) % yDimension];
                    if (!previousCells.contains(boardToExplore[x][(y + 1) % yDimension])) {
                        this.cellMapFor6px.putIfAbsent(neighbour3, new AbstractMap.SimpleEntry<>(neighbour3.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour3)));
                    }
                }

                //down-right corner
                if (x + 1 < xDimension && y + 1 < yDimension){
                    Cell neighbour4 = boardToExplore[(x + 1) % xDimension][(y + 1) % yDimension];
                    if (!previousCells.contains(boardToExplore[(x + 1) % xDimension][(y + 1) % yDimension])) {
                        this.cellMapFor6px.putIfAbsent(neighbour4, new AbstractMap.SimpleEntry<>(neighbour4.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour4)));
                    }
                }

                //down
                if (x + 1 < xDimension){
                    Cell neighbour5 = boardToExplore[(x + 1) % xDimension][y];
                    if (!previousCells.contains(boardToExplore[(x + 1) % xDimension][y])) {
                        this.cellMapFor6px.putIfAbsent(neighbour5, new AbstractMap.SimpleEntry<>(neighbour5.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour5)));
                    }
                }

                //down-left corner
                if (x + 1 < xDimension && y - 1 > 0){
                    Cell neighbour6 = boardToExplore[(x + 1) % xDimension][(y - 1 + yDimension) % yDimension];
                    if (!previousCells.contains(boardToExplore[(x + 1) % xDimension][(y - 1 + yDimension) % yDimension])) {
                        this.cellMapFor6px.putIfAbsent(neighbour6, new AbstractMap.SimpleEntry<>(neighbour6.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour6)));
                    }
                }

                //left corner
                if (y - 1 > 0){
                    Cell neighbour7 = boardToExplore[x][(y - 1 + yDimension) % yDimension];
                    if (!previousCells.contains(boardToExplore[x][(y - 1 + yDimension) % yDimension])) {
                        this.cellMapFor6px.putIfAbsent(neighbour7, new AbstractMap.SimpleEntry<>(neighbour7.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour7)));
                    }
                }

                //up-left corner
                if (x - 1 > 0 && y - 1 > 0) {
                    Cell neighbour8 = boardToExplore[(x - 1 + xDimension) % xDimension][(y - 1 + yDimension) % yDimension];
                    if (!previousCells.contains(boardToExplore[(x - 1 + xDimension) % xDimension][(y - 1 + yDimension) % yDimension])) {
                        this.cellMapFor6px.putIfAbsent(neighbour8, new AbstractMap.SimpleEntry<>(neighbour8.getId(), SubPhase.getInstance().getSubPhaseRegionByCell(neighbour8)));
                    }
                }
            }

        }
    }

    private void findBoundaryCells(SubPhaseRegion subPhaseRegion) {
        Cell[][] boardToExplore = GrainMap.getInstance().copyCurrentStep();
        SubPhase subPhase = SubPhase.getInstance();
        //subPhase.divideIntoRegions();

        int xDimension = boardToExplore.length;
        int yDimension = boardToExplore[0].length;


        for (Cell regionCell : subPhaseRegion.getRegionCells()) {

            int x = regionCell.getX();
            int y = regionCell.getY();

            if (AppConfiguration.getInstance().getNeighbourType().equals("periodic")) {
                //up
                if (!subPhaseRegion.containsCell(boardToExplore[(x - 1 + xDimension) % xDimension][y])) {
                    this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                    continue;
                }

                //up-right corner
                if (!subPhaseRegion.containsCell(boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension])) {
                    this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                    continue;
                }

                //right
                if (!subPhaseRegion.containsCell(boardToExplore[x][(y + 1) % yDimension])) {
                    this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                    continue;
                }

                //down-right corner
                if (!subPhaseRegion.containsCell(boardToExplore[(x + 1) % xDimension][(y + 1) % yDimension])) {
                    this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                    continue;
                }

                //down
                if (!subPhaseRegion.containsCell(boardToExplore[(x + 1) % xDimension][y])) {
                    this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                    continue;
                }
                //down-left corner
                if (!subPhaseRegion.containsCell(boardToExplore[(x + 1) % xDimension][(y - 1 + yDimension) % yDimension])) {
                    this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                    continue;
                }

                //left corner
                if (!subPhaseRegion.containsCell(boardToExplore[x][(y - 1 + yDimension) % yDimension])) {
                    this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                    continue;
                }

                //up-left corner
                if (!subPhaseRegion.containsCell(boardToExplore[(x - 1 + xDimension) % xDimension][(y - 1 + yDimension) % yDimension])) {
                    this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                    continue;
                }
            } else if (AppConfiguration.getInstance().getNeighbourType().equals("absorbing")){
                //up
                if (x - 1 > 0 && !subPhaseRegion.containsCell(boardToExplore[x - 1][y])) {
                    this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                    continue;
                }

                //up-right corner
                if (x - 1 > 0 && y + 1 < yDimension && !subPhaseRegion.containsCell(boardToExplore[x - 1][y + 1])) {
                    this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                    continue;
                }

                //right
                if (y + 1 < yDimension && !subPhaseRegion.containsCell(boardToExplore[x][y + 1])) {
                    this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                    continue;
                }

                //down-right corner
                if (x + 1 < xDimension && y + 1 < yDimension && !subPhaseRegion.containsCell(boardToExplore[x + 1][y + 1])) {
                    this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                    continue;
                }

                //down
                if (x + 1 < xDimension && !subPhaseRegion.containsCell(boardToExplore[x + 1][y])) {
                    this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                    continue;
                }

                //down-left corner
                if (x + 1 < xDimension && y - 1 > 0 && !subPhaseRegion.containsCell(boardToExplore[x + 1][y - 1])) {
                    this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                    continue;
                }

                //left corner
                if (y - 1 > 0 && !subPhaseRegion.containsCell(boardToExplore[x][y - 1 ])) {
                    this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                    continue;
                }

                //up-left corner
                if (x - 1 > 0 && y - 1 > 0 && !subPhaseRegion.containsCell(boardToExplore[x - 1][y - 1])) {
                    this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                    continue;
                }
            }

        }


        //addAdditionalBoundariesOfImmutablePhaseToBoundaries();
    }

    private void findBoundaryCells() {
        Cell[][] boardToExplore = GrainMap.getInstance().copyCurrentStep();
        SubPhase subPhase = SubPhase.getInstance();
        //subPhase.divideIntoRegions();

        int xDimension = boardToExplore.length;
        int yDimension = boardToExplore[0].length;

        for (SubPhaseRegion subPhaseRegion : subPhase.getSubPhaseRegions()) {

            for (Cell regionCell : subPhaseRegion.getRegionCells()) {

                int x = regionCell.getX();
                int y = regionCell.getY();

                if (AppConfiguration.getInstance().getNeighbourType().equals("periodic")) {
                    //up
                    if (!subPhaseRegion.containsCell(boardToExplore[(x - 1 + xDimension) % xDimension][y])) {
                        this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                        continue;
                    }

                    //up-right corner
                    if (!subPhaseRegion.containsCell(boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension])) {
                        this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                        continue;
                    }

                    //right
                    if (!subPhaseRegion.containsCell(boardToExplore[x][(y + 1) % yDimension])) {
                        this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                        continue;
                    }

                    //down-right corner
                    if (!subPhaseRegion.containsCell(boardToExplore[(x + 1) % xDimension][(y + 1) % yDimension])) {
                        this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                        continue;
                    }

                    //down
                    if (!subPhaseRegion.containsCell(boardToExplore[(x + 1) % xDimension][y])) {
                        this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                        continue;
                    }
                    //down-left corner
                    if (!subPhaseRegion.containsCell(boardToExplore[(x + 1) % xDimension][(y - 1 + yDimension) % yDimension])) {
                        this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                        continue;
                    }

                    //left corner
                    if (!subPhaseRegion.containsCell(boardToExplore[x][(y - 1 + yDimension) % yDimension])) {
                        this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                        continue;
                    }

                    //up-left corner
                    if (!subPhaseRegion.containsCell(boardToExplore[(x - 1 + xDimension) % xDimension][(y - 1 + yDimension) % yDimension])) {
                        this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                        continue;
                    }
                } else if (AppConfiguration.getInstance().getNeighbourType().equals("absorbing")) {
                    //up
                    if (x - 1 > 0 && !subPhaseRegion.containsCell(boardToExplore[x - 1][y])) {
                        this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                        continue;
                    }

                    //up-right corner
                    if (x - 1 > 0 && y + 1 < yDimension && !subPhaseRegion.containsCell(boardToExplore[x - 1][y + 1])) {
                        this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                        continue;
                    }

                    //right
                    if (y + 1 < yDimension && !subPhaseRegion.containsCell(boardToExplore[x][y + 1])) {
                        this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                        continue;
                    }

                    //down-right corner
                    if (x + 1 < xDimension && y + 1 < yDimension && !subPhaseRegion.containsCell(boardToExplore[x + 1][y + 1])) {
                        this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                        continue;
                    }

                    //down
                    if (x + 1 < xDimension && !subPhaseRegion.containsCell(boardToExplore[x + 1][y])) {
                        this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                        continue;
                    }

                    //down-left corner
                    if (x + 1 < xDimension && y - 1 > 0 && !subPhaseRegion.containsCell(boardToExplore[x + 1][y - 1])) {
                        this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                        continue;
                    }

                    //left corner
                    if (y - 1 > 0 && !subPhaseRegion.containsCell(boardToExplore[x][y - 1 ])) {
                        this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                        continue;
                    }

                    //up-left corner
                    if (x - 1 > 0 && y - 1 > 0 && !subPhaseRegion.containsCell(boardToExplore[x - 1][y - 1])) {
                        this.cellMapFor2px.putIfAbsent(regionCell, new AbstractMap.SimpleEntry<>(regionCell.getId(), subPhaseRegion));
                        continue;
                    }
                }

            }
        }

        addAdditionalBoundariesOfImmutablePhaseToBoundaries();
    }

    private void addAdditionalBoundariesOfImmutablePhaseToBoundaries() {
        //GrainMap grainMap = GrainMap.getInstance();
        Cell[][] boardToExplore = GrainMap.getInstance().copyCurrentStep();

        int xDimension = boardToExplore.length;
        int yDimension = boardToExplore[0].length;

        for (int i = 0; i < GrainMap.getInstance().numberOfCellsAtX; i++) {
            for (int j = 0; j < GrainMap.getInstance().numberOfCellsAtY; j++) {

                if (boardToExplore[i][j].isImmutablePhase()) {
                    int x = i;
                    int y = j;

                    SubPhaseRegion s = SubPhase.getInstance().getSubPhaseRegionByCell(boardToExplore[i][j]);

                    if (AppConfiguration.getInstance().getNeighbourType().equals("periodic")) {
                        //up
                        if (!boardToExplore[(x - 1 + xDimension) % xDimension][y].isImmutablePhase()) {
                            this.cellMapFor2px.putIfAbsent(boardToExplore[i][j], new AbstractMap.SimpleEntry<>(boardToExplore[i][j].getId(), s));
                            continue;
                        }

                        //up-right corner
                        //if (!boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension].isImmutableOrEmptyOrInclusion()) {
                        if (!boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension].isImmutablePhase()) {
                            this.cellMapFor2px.putIfAbsent(boardToExplore[i][j], new AbstractMap.SimpleEntry<>(boardToExplore[i][j].getId(), s));
                            continue;
                        }

                        //right
                        if (!boardToExplore[x][(y + 1) % yDimension].isImmutablePhase()) {
                            this.cellMapFor2px.putIfAbsent(boardToExplore[i][j], new AbstractMap.SimpleEntry<>(boardToExplore[i][j].getId(), s));
                            continue;
                        }

                        //down-right corner
                        if (!boardToExplore[(x + 1) % xDimension][(y + 1) % yDimension].isImmutablePhase()) {
                            this.cellMapFor2px.putIfAbsent(boardToExplore[i][j], new AbstractMap.SimpleEntry<>(boardToExplore[i][j].getId(), s));
                            continue;
                        }

                        //down
                        if (!boardToExplore[(x + 1) % xDimension][y].isImmutablePhase()) {
                            this.cellMapFor2px.putIfAbsent(boardToExplore[i][j], new AbstractMap.SimpleEntry<>(boardToExplore[i][j].getId(), s));
                            continue;
                        }

                        //down-left corner
                        if (!boardToExplore[(x + 1) % xDimension][(y - 1 + yDimension) % yDimension].isImmutablePhase()) {
                            this.cellMapFor2px.putIfAbsent(boardToExplore[i][j], new AbstractMap.SimpleEntry<>(boardToExplore[i][j].getId(), s));
                            continue;
                        }

                        //left corner
                        if (!boardToExplore[x][(y - 1 + yDimension) % yDimension].isImmutablePhase()) {
                            this.cellMapFor2px.putIfAbsent(boardToExplore[i][j], new AbstractMap.SimpleEntry<>(boardToExplore[i][j].getId(), s));
                            continue;
                        }

                        //up-left corner
                        if (!boardToExplore[(x - 1 + xDimension) % xDimension][(y - 1 + yDimension) % yDimension].isImmutablePhase()) {
                            this.cellMapFor2px.putIfAbsent(boardToExplore[i][j], new AbstractMap.SimpleEntry<>(boardToExplore[i][j].getId(), s));
                            continue;
                        }
                    } else if (AppConfiguration.getInstance().getNeighbourType().equals("absorbing")) {
                        //up
                        if (x - 1 > 0 && !boardToExplore[x - 1][y].isImmutablePhase()) {
                            this.cellMapFor2px.putIfAbsent(boardToExplore[i][j], new AbstractMap.SimpleEntry<>(boardToExplore[i][j].getId(), s));
                            continue;
                        }

                        //up-right corner
                        if (x - 1 > 0 && y + 1 < yDimension && !boardToExplore[x - 1][y + 1].isImmutablePhase()) {
                            this.cellMapFor2px.putIfAbsent(boardToExplore[i][j], new AbstractMap.SimpleEntry<>(boardToExplore[i][j].getId(), s));
                            continue;
                        }

                        //right
                        if (y + 1 < yDimension && !boardToExplore[x][y + 1].isImmutablePhase()) {
                            this.cellMapFor2px.putIfAbsent(boardToExplore[i][j], new AbstractMap.SimpleEntry<>(boardToExplore[i][j].getId(), s));
                            continue;
                        }

                        //down-right corner
                        if (x + 1 < xDimension && y + 1 < yDimension && !boardToExplore[x + 1][y + 1].isImmutablePhase()) {
                            this.cellMapFor2px.putIfAbsent(boardToExplore[i][j], new AbstractMap.SimpleEntry<>(boardToExplore[i][j].getId(), s));
                            continue;
                        }

                        //down
                        if (x + 1 < xDimension && !boardToExplore[x + 1][y].isImmutablePhase()) {
                            this.cellMapFor2px.putIfAbsent(boardToExplore[i][j], new AbstractMap.SimpleEntry<>(boardToExplore[i][j].getId(), s));
                            continue;
                        }

                        //down-left corner
                        if (x + 1 < xDimension && y - 1 > 0 && !boardToExplore[x + 1][y - 1].isImmutablePhase()) {
                            this.cellMapFor2px.putIfAbsent(boardToExplore[i][j], new AbstractMap.SimpleEntry<>(boardToExplore[i][j].getId(), s));
                            continue;
                        }

                        //left corner
                        if (y - 1 > 0 && !boardToExplore[x][y - 1 ].isImmutablePhase()) {
                            this.cellMapFor2px.putIfAbsent(boardToExplore[i][j], new AbstractMap.SimpleEntry<>(boardToExplore[i][j].getId(), s));
                            continue;
                        }

                        //up-left corner
                        if (x - 1 > 0 && y - 1 > 0 && !boardToExplore[x - 1][y - 1].isImmutablePhase()) {
                            this.cellMapFor2px.putIfAbsent(boardToExplore[i][j], new AbstractMap.SimpleEntry<>(boardToExplore[i][j].getId(), s));
                            continue;
                        }
                    }


                }

            }
        }


    }

    private void findBoundariesOfParticularGrain(SubPhaseRegion subPhaseRegion) {
//        Cell[][] boardToExplore = GrainMap.getInstance().currentStep;
//        SubPhase subPhase = SubPhase.getInstance();
//        //subPhase.divideIntoRegions();
//        int xDimension = boardToExplore.length;
//        int yDimension = boardToExplore[0].length;
//        for (Cell regionCell : subPhaseRegion.getRegionCells()) {
//
//            int x = regionCell.getX();
//            int y = regionCell.getY();
//
//            //up
//            if (!subPhaseRegion.containsCell(boardToExplore[(x - 1 + xDimension) % xDimension][y])) {
//                cellMap.putIfAbsent(regionCell, regionCell.getId());
//                continue;
//            }
//
//            //up-right corner
//            if (!subPhaseRegion.containsCell(boardToExplore[(x - 1 + xDimension) % xDimension][(y + 1) % yDimension])) {
//                cellMap.putIfAbsent(regionCell, regionCell.getId());
//                continue;
//            }
//
//            //right
//            if (!subPhaseRegion.containsCell(boardToExplore[x][(y + 1) % yDimension])) {
//                cellMap.putIfAbsent(regionCell, regionCell.getId());
//                continue;
//            }
//
//            //down-right corner
//            if (!subPhaseRegion.containsCell(boardToExplore[(x + 1) % xDimension][(y + 1) % yDimension])) {
//                cellMap.putIfAbsent(regionCell, regionCell.getId());
//                continue;
//            }
//
//            //down
//            if (!subPhaseRegion.containsCell(boardToExplore[(x + 1) % xDimension][y])) {
//                cellMap.putIfAbsent(regionCell, regionCell.getId());
//                continue;
//            }
//            //down-left corner
//            if (!subPhaseRegion.containsCell(boardToExplore[(x + 1) % xDimension][(y - 1 + yDimension) % yDimension])) {
//                cellMap.putIfAbsent(regionCell, regionCell.getId());
//                continue;
//            }
//
//            //left corner
//            if (!subPhaseRegion.containsCell(boardToExplore[x][(y - 1 + yDimension) % yDimension])) {
//                cellMap.putIfAbsent(regionCell, regionCell.getId());
//                continue;
//            }
//
//            //up-left corner
//            if (!subPhaseRegion.containsCell(boardToExplore[(x - 1 + xDimension) % xDimension][(y - 1 + yDimension) % yDimension])) {
//                cellMap.putIfAbsent(regionCell, regionCell.getId());
//                continue;
//            }
//        }
    }

    private void markBoundaryCells() {

        for (Map.Entry<Cell, Map.Entry<Integer, SubPhaseRegion>> cellEntryEntry : cellMap.entrySet()) {
            Cell cell = cellEntryEntry.getKey();
            GrainMap.getInstance().getCurrentStep()[cell.getX()][cell.getY()].setId(-2);
        }
    }

    public void clearAllGrains() {
        Arrays.stream(GrainMap.getInstance().currentStep).flatMap(Stream::of).filter(x -> x.getId() != -2).forEach(x -> x.setId(-1));
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

    private synchronized void hideBoundaries() {
        for (Map.Entry<Cell, Map.Entry<Integer, SubPhaseRegion>> cellEntryEntry : cellMap.entrySet()) {
            Cell cell = cellEntryEntry.getKey();
            int prevId = cellEntryEntry.getValue().getKey();
            SubPhaseRegion subPhaseRegion = cellEntryEntry.getValue().getValue();

            //cell.setId(prevId);
            GrainMap.getInstance().getCurrentStep()[cell.getX()][cell.getY()].setId(prevId);

            if(subPhaseRegion != null && !subPhaseRegion.containsCell(cell)) {
            subPhaseRegion.getRegionCells().add(cell);
            }

        }

        cellMap = new HashMap<>();
    }

}

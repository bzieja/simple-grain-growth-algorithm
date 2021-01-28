package app.subphase;

import app.AppConfiguration;
import app.grid.Cell;
import app.grid.GrainMap;
import gui.CanvasPrinter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubPhase {
    static SubPhase instance;
    //GrainMap grainMap;
    int numberOfGrainsAtTheStart;
    int numberOfGrainsAtTheEnd;
    public List<SubPhaseRegion> subPhaseRegions;


    public SubPhase() {
        //this.grainMap = GrainMap.getInstance();
        this.subPhaseRegions = new ArrayList<>();
    }

    public SubPhase(List<SubPhaseRegion> subPhaseRegions, int start, int end) {
        //this.grainMap = grainMap;
        this.subPhaseRegions = subPhaseRegions;
        this.numberOfGrainsAtTheStart = start;
        this.numberOfGrainsAtTheEnd = end;
    }

    public synchronized void divideIntoRegions() {
        Arrays.stream(GrainMap.getInstance().currentStep).flatMap(Stream::of).forEach(x -> x.setChangedSecondTime(false));
        numberOfGrainsAtTheStart = CanvasPrinter.getInstance().getCellsRGB().length;
        numberOfGrainsAtTheEnd = numberOfGrainsAtTheStart + AppConfiguration.getInstance().getNumberOfGrainsInSubPhases();

        if (subPhaseRegions.size() == 0) { //if it's first creating
            for (int i = 0; i < numberOfGrainsAtTheStart; i++) {   //create region for each existing colour of cells
                if (GrainMap.getInstance().containsPhase(i)) {
                    subPhaseRegions.add(new SubPhaseRegion(i, GrainMap.getInstance()));  //init regions
                }
            }
        } else {
            List<SubPhaseRegion> newSubRegions = new ArrayList<>();;
            for (SubPhaseRegion subPhaseRegion : subPhaseRegions) {
                List<SubPhaseRegion> k = subPhaseRegion.divideOnSubregions();
                newSubRegions.addAll(k);
            }
            subPhaseRegions = newSubRegions;
        }

        CanvasPrinter.getInstance().increaseNumberOfInitialGrains(AppConfiguration.getInstance().getNumberOfGrainsInSubPhases());
        //CanvasPrinter.getInstance().increaseNumberOfInitialGrains(numberOfGrainsAtTheEnd);
    }

    public synchronized void generateNewGrains() {

        for (SubPhaseRegion subPhaseRegion : subPhaseRegions) {    //for each subregion

            Random random = new Random();
            for (int i = numberOfGrainsAtTheStart; i < numberOfGrainsAtTheEnd; i++) { //numeracja "nowych" ziaren

                Cell cell  = subPhaseRegion.getRegionCells().get(random.nextInt(subPhaseRegion.getRegionCells().size()));

                while (true) {
                    if (!cell.isChangedSecondTime()) {
                        break;
                    }
                    cell  = subPhaseRegion.getRegionCells().get(random.nextInt(subPhaseRegion.getRegionCells().size()));
                }
                cell.setId(i);
                cell.setChangedSecondTime(true);
            }
        }
    }

    public synchronized void nextSubPhaseStep() {
        for (SubPhaseRegion subPhaseRegion : subPhaseRegions) {
            subPhaseRegion.nextStep(GrainMap.getInstance());
        }
    }

    public boolean isSubStructureIncomplete() {
        return Stream.of(GrainMap.getInstance().currentStep).flatMap(Stream::of).anyMatch(x -> !x.isInclusion() && !x.isImmutablePhase() && !x.isChangedSecondTime());
    }

    public static SubPhase getInstance() {
        if (instance == null) {
            instance = new SubPhase();
        }
        return instance;
    }

    public static SubPhase replaceAndGetInstance(List<SubPhaseRegion> subPhaseRegions, int start, int end) {
        instance = new SubPhase(subPhaseRegions, start, end);
        return instance;
    }

    public void setConfigurables(List<SubPhaseRegion> subPhaseRegions, int start, int end) {
        this.subPhaseRegions = subPhaseRegions;
        this.numberOfGrainsAtTheStart = start;
        this.numberOfGrainsAtTheEnd = end;
    }

    public static boolean hasInstance() {
        if (instance == null) {
            return false;
        } else {
            return true;
        }
    }

    public SubPhaseRegion getSubPhaseRegionByCell(Cell cell1) {
        SubPhaseRegion subPhaseRegion1 = null;
        for (SubPhaseRegion subPhaseRegion : subPhaseRegions) {
            if (subPhaseRegion.getRegionCells().stream().anyMatch(x -> (x.getX() == cell1.getX() && x.getY() == cell1.getY()))) {
                subPhaseRegion1 = subPhaseRegion;
            }
        }

        return subPhaseRegion1;
    }

    public void changeRegionToImmutable(SubPhaseRegion subPhaseRegion1) {
        if (subPhaseRegion1 != null) {
            subPhaseRegion1.getRegionCells().forEach(x -> x.setId(-3));
            subPhaseRegions.remove(subPhaseRegion1);
        }

        //Stream.of(currentStep).flatMap(Stream::of).forEach(x -> x.setId(x.getId() == id ? -3 : x.getId()));
    }

    public static void clear(){
        instance = null;
    }

    private void deleteEmptyRegions(){
        List <SubPhaseRegion> emptyRegions = subPhaseRegions.stream().filter(x -> x.getRegionCells().size() == 0).collect(Collectors.toList());
        subPhaseRegions.removeAll(emptyRegions);
    }

    public List<SubPhaseRegion> getSubPhaseRegions() {
        return subPhaseRegions;
    }

    public static void setInstance(SubPhase instance1) {
        instance = null;
        instance = instance1;
    }

    public synchronized List<SubPhaseRegion> copySubRegions() {
        List<SubPhaseRegion> result = new ArrayList<>();

        for (SubPhaseRegion subPhaseRegion : this.getSubPhaseRegions()) {

            List<Cell> cells = new ArrayList<>();
            for (int i = 0; i < subPhaseRegion.cells.size(); i++) {
                cells.add(subPhaseRegion.cells.get(i).copy());
            }

            result.add(new SubPhaseRegion(subPhaseRegion.idOfCellsInRegion, cells));
        }

        return result;
    }

    public synchronized void setSubPhaseRegions(List<SubPhaseRegion> list) {
        divideIntoRegions();
        this.subPhaseRegions = list;
    }

    public int getNumberOfGrainsAtTheStart() {
        return numberOfGrainsAtTheStart;
    }

    public int getNumberOfGrainsAtTheEnd() {
        return numberOfGrainsAtTheEnd;
    }
}

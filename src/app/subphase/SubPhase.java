package app.subphase;

import app.AppConfiguration;
import app.grid.Cell;
import app.grid.GrainMap;
import gui.CanvasPrinter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubPhase {
    static SubPhase instance;
    GrainMap grainMap;
    int numberOfGrainsAtTheStart;
    int numberOfGrainsAtTheEnd;
    public List<SubPhaseRegion> subPhaseRegions;


    public SubPhase() {
        this.grainMap = GrainMap.getInstance();
        this.subPhaseRegions = new ArrayList<>();

    }

    public synchronized void divideIntoRegions() {
        Arrays.stream(grainMap.currentStep).flatMap(Stream::of).forEach(x -> x.setChangedSecondTime(false));
        numberOfGrainsAtTheStart = CanvasPrinter.getInstance().getCellsRGB().length;
        numberOfGrainsAtTheEnd = numberOfGrainsAtTheStart + AppConfiguration.getInstance().getNumberOfGrainsInSubPhases();

        if (subPhaseRegions.size() == 0) { //if it's first creating
            for (int i = 0; i < numberOfGrainsAtTheStart; i++) {   //create region for each existing colour of cells
                if (grainMap.containsPhase(i)) {
                    subPhaseRegions.add(new SubPhaseRegion(i, grainMap));  //init regions
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
            subPhaseRegion.nextStep(grainMap);
        }
    }

    public boolean isSubStructureIncomplete() {
        return Stream.of(grainMap.currentStep).flatMap(Stream::of).anyMatch(x -> !x.isInclusion() && !x.isImmutablePhase() && !x.isChangedSecondTime());
    }

    public static SubPhase getInstance() {
        if (instance == null) {
            instance = new SubPhase();
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

    public SubPhaseRegion getSubPhaseRegionByCell(Cell cell1) {
        SubPhaseRegion subPhaseRegion1 = null;
        for (SubPhaseRegion subPhaseRegion : subPhaseRegions) {
//            if (subPhaseRegion.getSubPhaseCells().contains(cell1)) {
//                return subPhaseRegion;
//            }

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
}

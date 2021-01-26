package app.grid;

import app.AppConfiguration;
import app.exploration.Neighborhood;
import app.inclusion.Inclusion;

import java.util.Random;
import java.util.stream.Stream;

public class GrainMap {
    public static int IdCounter = 0;
    //int emptyCellsCounter;

    private static GrainMap instance;
    public int numberOfInitialGrains;
    public int numberOfCellsAtX;
    public int numberOfCellsAtY;
    public String neighbourType;
    public Cell[][] currentStep;


    public GrainMap(AppConfiguration appConfiguration) {
        instance = this;
        this.numberOfInitialGrains = appConfiguration.getNumberOfInitialGrains();
        this.numberOfCellsAtX = appConfiguration.getNumberOfGrainsAtX();
        this.numberOfCellsAtY = appConfiguration.getNumberOfGrainsAtY();
        this.neighbourType = appConfiguration.getNeighbourType();


        this.currentStep = new Cell[numberOfCellsAtX][numberOfCellsAtY];

        for (int i = 0; i < numberOfCellsAtX; i++) {
            for (int j = 0; j < numberOfCellsAtY; j++) {
                this.currentStep[i][j] = new Cell(i, j);
                //this.emptyCellsCounter++;
            }
        }

        //initialize grains
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < numberOfInitialGrains; i++) {
            int randX;
            int randY;

            while (true) {
                randX = random.nextInt(numberOfCellsAtX);
                randY = random.nextInt(numberOfCellsAtY);
                if (this.currentStep[randX][randY].isEmpty()) {
                    break;
                }
            }
            this.currentStep[randX][randY].setId(IdCounter);
            //this.emptyCellsCounter--;
            IdCounter++;
        }

        //add Inclusions at the start
        if (appConfiguration.isStartWithInclusions()) {
            Inclusion inclusion = new Inclusion(appConfiguration, this.currentStep, this.hasEmptyCells());
            inclusion.add();
        }
    }

    public synchronized void nextStep() {
        Cell[][] nextStep = new Cell[numberOfCellsAtX][numberOfCellsAtY];

        for (int i = 0; i < numberOfCellsAtX; i++) {
            for (int j = 0; j < numberOfCellsAtY; j++) {
                nextStep[i][j]  = currentStep[i][j].copy();

                if (this.currentStep[i][j].isEmpty()) { //state of cell can be changed only if it's empty

                    Neighborhood neighborhood = new Neighborhood(this.neighbourType, this.currentStep, i, j, numberOfInitialGrains);
                    nextStep[i][j].setId(neighborhood.getNextState());

                }
            }
        }

        this.currentStep = nextStep;
    }

    public synchronized boolean hasEmptyCells() {
        return Stream.of(currentStep).flatMap(Stream::of).anyMatch(Cell::isEmpty);
    }

    public synchronized Cell[][] getCurrentStep() {
        return this.currentStep;
    }

    public synchronized boolean containsPhase(int phaseId) {
        return Stream.of(currentStep).flatMap(Stream::of).anyMatch(x -> x.getId() == phaseId);
    }

    public synchronized void changePhaseToImmutable(int id) {
        Stream.of(currentStep).flatMap(Stream::of).forEach(x -> x.setId(x.getId() == id ? -3 : x.getId()));
    }

    public void printCurrentStepForDebug() {
        for (int i = 0; i < numberOfCellsAtX; i++) {
            for (int j = 0; j < numberOfCellsAtY; j++) {
                System.out.print(this.currentStep[i][j].id + " ");
            }
            System.out.print("\n");
        }

    }

    public static GrainMap getInstance() {
        if (instance == null) {
            instance = new GrainMap(AppConfiguration.getInstance());
        }
        return instance;
    }
}

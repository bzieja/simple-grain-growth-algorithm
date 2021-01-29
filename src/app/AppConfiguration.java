package app;

public class AppConfiguration {
    static AppConfiguration instance;
    private final static int magicNumberForDifferentColors = 0;

    private int numberOfInitialGrains;
    private int numberOfGrainsAtX;
    private int numberOfGrainsAtY;
    private String neighbourType;
    private int numberOfInclusions;
    private String typeOfInclusion;
    private int sizeOfInclusion;
    boolean isStartWithInclusions;
    private int probabilityOfChange;
    private int numberOfGrainsInSubPhases;
    private String typeOfGrowth;

    private AppConfiguration() {}


    public static AppConfiguration getInstance() {
        if (AppConfiguration.instance == null) {
            AppConfiguration.instance = new AppConfiguration();
            return AppConfiguration.instance;
        } else {
            return AppConfiguration.instance;
        }
    }

    public int getNumberOfInitialGrains() {
        return this.numberOfInitialGrains;
    }

    public void setNumberOfInitialGrains(int numberOfInitialGrains) {
        this.numberOfInitialGrains = numberOfInitialGrains;
    }

    public int getNumberOfGrainsAtX() {
        return this.numberOfGrainsAtX;
    }

    public void setNumberOfGrainsAtX(int numberOfGrainsAtX) {
        this.numberOfGrainsAtX = numberOfGrainsAtX;
    }

    public int getNumberOfGrainsAtY() {
        return this.numberOfGrainsAtY;
    }

    public void setNumberOfGrainsAtY(int numberOfGrainsAtY) {
        this.numberOfGrainsAtY = numberOfGrainsAtY;
    }

    public String getNeighbourType() {
        return this.neighbourType;
    }

    public void setNeighbourType(String neighbourType) {
        this.neighbourType = neighbourType;
    }

    public int getNumberOfInclusions() {
        return numberOfInclusions;
    }

    public void setNumberOfInclusions(int numberOfInclusions) {
        this.numberOfInclusions = numberOfInclusions;
    }

    public String getTypeOfInclusion() {
        return typeOfInclusion;
    }

    public void setTypeOfInclusion(String typeOfInclusion) {
        this.typeOfInclusion = typeOfInclusion;
    }

    public int getSizeOfInclusion() {
        return sizeOfInclusion;
    }

    public void setSizeOfInclusion(int sizeOfInclusion) {
        this.sizeOfInclusion = sizeOfInclusion;
    }

    public boolean isStartWithInclusions() {
        return isStartWithInclusions;
    }

    public void setStartWithInclusions(boolean startWithInclusions) {
        isStartWithInclusions = startWithInclusions;
    }

    public int getProbabilityOfChange() {
        return probabilityOfChange;
    }

    public void setProbabilityOfChange(int probabilityOfChange) {
        this.probabilityOfChange = probabilityOfChange;
    }

    public int getNumberOfGrainsInSubPhases() {
        return numberOfGrainsInSubPhases;
    }

    public void setNumberOfGrainsInSubPhases(int numberOfGrainsInSubPhases) {
        this.numberOfGrainsInSubPhases = numberOfGrainsInSubPhases;
    }

    public String getTypeOfGrowth() {
        return typeOfGrowth;
    }

    public void setTypeOfGrowth(String typeOfGrowth) {
        this.typeOfGrowth = typeOfGrowth;
    }


}

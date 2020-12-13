package businesslogic;

public class GlobalData {
    private int numberOfInitialGrains;
    private int numberOfGrainsAtX;
    private int numberOfGrainsAtY;
    private String neighbourType;

    private int numberOfInclusions;
    private String typeOfInclusion;
    private int sizeOfInclusion;

    public GlobalData() {
        this.numberOfInclusions = 0;
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
}

package businesslogic;

public class GlobalData {
    static int numberOfInitialGrains;
    static int numberOfGrainsAtX;
    static int numberOfGrainsAtY;
    static  String neighbourType;

    public static int getNumberOfInitialGrains() {
        return numberOfInitialGrains;
    }

    public static void setNumberOfInitialGrains(int numberOfInitialGrains) {
        GlobalData.numberOfInitialGrains = numberOfInitialGrains;
    }

    public static int getNumberOfGrainsAtX() {
        return numberOfGrainsAtX;
    }

    public static void setNumberOfGrainsAtX(int numberOfGrainsAtX) {
        GlobalData.numberOfGrainsAtX = numberOfGrainsAtX;
    }

    public static int getNumberOfGrainsAtY() {
        return numberOfGrainsAtY;
    }

    public static void setNumberOfGrainsAtY(int numberOfGrainsAtY) {
        GlobalData.numberOfGrainsAtY = numberOfGrainsAtY;
    }

    public static String getNeighbourType() {
        return neighbourType;
    }

    public static void setNeighbourType(String neighbourType) {
        GlobalData.neighbourType = neighbourType;
    }
}

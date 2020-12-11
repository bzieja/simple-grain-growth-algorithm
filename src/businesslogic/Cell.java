package businesslogic;

public class Cell {
    int x;
    int y;
    int id;
    int[] rgb = new int [3];

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }










    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getRgb() {
        return rgb;
    }

    public void setRgb(int[] rgb) {
        this.rgb = rgb;
    }
}

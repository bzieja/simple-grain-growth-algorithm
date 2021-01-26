package app.grid;

public class Cell {
    int x;
    int y;
    int id; //-1 is empty cell
            //-2 is inclusion
            //-3 immutable phase
    boolean changedSecondTime;   //for sub-phase grain


    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.id = -1;
        this.changedSecondTime = false;
        //this.rgb = new int[]{255, 255, 255};
    }

    public Cell(int x, int y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
        //this.rgb = new int[]{255, 255, 255};
    }


    public Cell copy(){
        Cell cell = new Cell(x, y);
        cell.setId(id);
        return cell;
    }
    public boolean isEmptyOrInclusion() {
        if (isEmpty() || isInclusion()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmpty(){
        if (this.id == -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isInclusion(){
        if (this.id == -2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isImmutablePhase() {
        return id == -3;
    }

    public boolean isImmutableOrEmptyOrInclusion(){
        if (isEmpty() || isInclusion() || isImmutablePhase()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isChangedSecondTime() {
        return changedSecondTime;
    }

    public void setChangedSecondTime(boolean changedSecondTime) {
        this.changedSecondTime = changedSecondTime;
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
/*
    public int[] getRgb() {
        return rgb;
    }

    public void setRgb(int[] rgb) {
        this.rgb = rgb;
    }
    */
}

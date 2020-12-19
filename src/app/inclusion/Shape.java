package app.inclusion;

import app.grid.Cell;

public interface Shape {

    void draw(int x, int y, int sizeOfInclusion, Cell[][] board);
    boolean collisionWillNotOccur(int x, int y, int sizeOfInclusion, Cell[][] board);

}

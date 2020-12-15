package businesslogic.inclusion;

import businesslogic.Cell;

public interface Shape {

    void draw(int i, int j, int sizeOfInclusion, Cell[][] board);
    boolean collisionWillNotOccur(int i, int j, int sizeOfInclusion, Cell[][] board);

}

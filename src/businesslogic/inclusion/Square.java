package businesslogic.inclusion;

import businesslogic.Cell;

public class Square implements Shape {
    @Override
    public void draw(int i, int j, int sizeOfInclusion, Cell[][] board) {

    }

    @Override
    public boolean collisionWillNotOccur(int i, int j, int sizeOfInclusion, Cell[][] board) {
        return false;
    }
}

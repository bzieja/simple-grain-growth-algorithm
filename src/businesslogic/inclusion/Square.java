package businesslogic.inclusion;

import businesslogic.Cell;

public class Square implements Shape {
    @Override
    public void draw(int x, int y, int sizeOfInclusion, Cell[][] board) {

        int side = sizeOfInclusion;
        int startX = x - side / 2;
        int startY = y - side / 2;

        for (int i = startX; i < startX + side; i++){
            for (int j = startY; j < startY + side; j++) {
                board[i][j].setId(-2);
            }
        }

    }

    @Override
    public boolean collisionWillNotOccur(int x, int y, int sizeOfInclusion, Cell[][] board) {
        int side = sizeOfInclusion;
        int startX = x - side / 2;
        int startY = y - side / 2;

        if (x - side <= 0 || x + side >= board.length || y - side <= 0 || y + side >= board[0].length) {
            return false;
        }

        for (int i = startX; i < startX + side; i++){
            for (int j = startY; j < startY + side; j++) {
                if (board[i][j].isInclusion()) {
                    return false;
                }
            }
        }

        return true;
    }
}

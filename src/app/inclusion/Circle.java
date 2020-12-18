package app.inclusion;

import app.Cell;

public class Circle implements Shape {

    @Override
    public void draw(int x, int y, int sizeOfInclusion, Cell[][] board) {
        int r = sizeOfInclusion;

        for (int i = x - r; i <= x + r; i++) {
            for (int j = y - r; j <= y + r; j++) {
                if ((i - x) * (i - x) + (j - y) * (j - y) <= r * r) {
                    board[i][j].setId(-2);
                }
            }
        }
    }

    @Override
    public boolean collisionWillNotOccur(int x, int y, int sizeOfInclusion, Cell[][] board) {
        int r = sizeOfInclusion + 1;

        //radius checking
        if (x - r <= 0 || x + r >= board.length || y - r <= 0 || y + r >= board[0].length) {
            return false;
        }

        //containing with another circle inclusion
        for (int i = x - r; i <= x + r; i++) {
            for (int j = y - r; j <= y + r; j++) {
                if ((i - x) * (i - x) + (j - y) * (j - y) <= r * r) {
                    if (board[i][j].getId() == -2) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}

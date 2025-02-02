package app.inclusion;

import app.grid.Cell;
import app.AppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Inclusion {

    int numberOfInclusion;
    String typeOfInclusion;
    int sizeOfInclusion;
    Cell[][] board;
    boolean hasEmptyCells;
    Shape shape;

    public Inclusion(AppConfiguration appConfiguration, Cell[][] board, boolean hasEmptyCells) {
        this.board = board;
        this.numberOfInclusion = appConfiguration.getNumberOfInclusions();
        this.typeOfInclusion = appConfiguration.getTypeOfInclusion();
        this.sizeOfInclusion = appConfiguration.getSizeOfInclusion();
        this.hasEmptyCells = hasEmptyCells;

        switch (typeOfInclusion) {
            case "circle":
                this.shape = new Circle();
                break;
            case "square":
                this.shape = new Square();
                break;
            default:
                System.err.println("unknown type of inclusion shape!");
        }
    }

    public void add() {
        Random random = new Random(System.currentTimeMillis());

        if (hasEmptyCells) {    //add inclusion on start

            //randomize initial place and put it to method
            for (int i = 0; i < this.numberOfInclusion; i++) {
                int randX;
                int randY;

                while (true) {
                    randX = random.nextInt(board.length);
                    randY = random.nextInt(board[0].length);
                    if (board[randX][randY].isEmpty() && this.shape.collisionWillNotOccur(randX, randY, this.sizeOfInclusion, board)) {
                        break;
                    }
                }

                shape.draw(randX, randY, this.sizeOfInclusion, board);
            }

        } else {    //add inclusions at the end
            //find possible places on the boundaries, choose randomly and put to the method
            List<int[]> boundaryCells = getBoundaryCells();

            for (int i = 0; i < this.numberOfInclusion; i++) {
                int randX;
                int randY;

                while (true) {
                    int[] selectedCoordinates =  boundaryCells.get(random.nextInt(boundaryCells.size()));
                    randX = selectedCoordinates[0];
                    randY = selectedCoordinates[1];

                    if(this.shape.collisionWillNotOccur(randX, randY, this.sizeOfInclusion, board)) {
                        break;
                    }
                }
                shape.draw(randX, randY, this.sizeOfInclusion, board);

            }
        }
    }

    private List<int[]> getBoundaryCells() {
        List<int[]> boundaryCells = new ArrayList<int[]>();

        for (int i = 1; i < this.board.length - 1; i++) {
            for (int j = 1; j < this.board[0].length - 1; j++) {

                //up
                if(board[i][j].getId() != board[i - 1][j].getId() && !board[i][j].isInclusion() && !board[i - 1][j].isInclusion()) {
                    boundaryCells.add(new int[]{i, j});
                }

                else if(board[i][j].getId() != board[i][j + 1].getId() && !board[i][j].isInclusion() && !board[i][j + 1].isInclusion()) {
                    boundaryCells.add(new int[]{i, j});
                }

                else if(board[i][j].getId() != board[i + 1][j].getId() && !board[i][j].isInclusion() && !board[i + 1][j].isInclusion()) {
                    boundaryCells.add(new int[]{i, j});
                }

                else if(board[i][j].getId() != board[i][j - 1].getId() && !board[i][j].isInclusion() && !board[i][j - 1].isInclusion()) {
                    boundaryCells.add(new int[]{i, j});
                }


            }
        }

        return boundaryCells;

    }

}

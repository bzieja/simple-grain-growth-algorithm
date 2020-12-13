package businesslogic;

import java.util.Random;

public class Inclusion {

    int numberOfInclusion;
    String typeOfInclusion;
    int sizeOfInclusion;
    Cell[][] board;

    public Inclusion(int numberOfInclusion, String typeOfInclusion, int sizeOfInclusion, Cell[][] board, boolean hasEmptyCells) {
        this.board = board;
        this.numberOfInclusion = numberOfInclusion;
        this.typeOfInclusion = typeOfInclusion;
        this.sizeOfInclusion = sizeOfInclusion;


        Random random = new Random(System.currentTimeMillis());
        if (hasEmptyCells) {
            //randomize initial place and put it to method

            if (this.typeOfInclusion.equals("circle")) {

                for (int i = 0; i < this.numberOfInclusion; i++) {
                    int randX;
                    int randY;

                    while (true) {
                        randX = random.nextInt(board.length);
                        randY = random.nextInt(board[0].length);
                        if (board[randX][randY].isEmpty() && !willOccurCircleInclusionsConflict(randX, randY)) {
                            break;
                        }
                    }

                    addCircleInclusion(randX, randY);


                }


            } else if (this.typeOfInclusion.equals("square")){

            }



        } else {
            //find possible places on the boundaries, choose randomly and put to the method

        }

    }


    private void addCircleInclusion(int i, int j) {
        board[randX][randY].setId(-2);
    }

    private boolean willOccurCircleInclusionsConflict(int i, int j) {

    }

    private void addSquareInclusion(int i, int j) {

    }

    private boolean willOccurSquareInclusionsConflict(int i, int j, int sideLength) {

    }


}

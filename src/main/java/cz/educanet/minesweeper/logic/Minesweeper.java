package cz.educanet.minesweeper.logic;

import java.util.Random;

public class Minesweeper {

    private final int rowsCount;
    private final int columnsCount;
    private final Save[][] field;
    private int place;
    private boolean click;
    private final int bombCount;


    public Minesweeper(int rows, int columns) {
        field = new Save[columns][rows];
        this.rowsCount = rows;
        this.columnsCount = columns;

        bombCount = 15; // CHANGE BOMBS IN FIELD

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                field[i][j] = new Save();
            }
        }

        createBombsField(rows, columns);
        place = (rows * columns) - bombCount;
    }


    /**
     * 0 - Hidden
     * 1 - Visible
     * 2 - Flag
     * 3 - Question mark
     *
     * @param x X
     * @param y Y
     * @return field type
     */
    public int getField(int x, int y) {
        return field[x][y].getState();
    }

    /**
     * Toggles the field state, ie.
     * 0 -> 1,
     * 1 -> 2,
     * 2 -> 3 and
     * 3 -> 0
     *
     * @param x X
     * @param y Y
     */
    public void toggleFieldState(int x, int y) {
        if (field[x][y].getState() == 2){
            field[x][y].setState(0);
        }
        else {
            field[x][y].setState(2);
        }
    }

    /**
     * Reveals the field and all fields adjacent (with 0 adjacent bombs) and all fields adjacent to the adjacent fields... ect.
     *
     * @param x X
     * @param y Y
     */
    public void reveal(int x, int y) {
        if (field[x][y].getBomb()) {
            click = true;
        }
        ifReveal(x, y);
        place--;
    }

    private void ifReveal(int x, int y) {
        field[x][y].setState(1);

        if (getAdjacentBombCount(x, y) == 0) {

            boolean leftTopCorner = (x != 0) && y != 0;
            boolean rightTopCorner = (x != (columnsCount - 1)) && (y != 0);
            boolean leftBottomCorner = (x != 0) && (y != (rowsCount - 1));
            boolean rightBottomCorner = (x != (columnsCount - 1)) && (y != (rowsCount - 1));

            //RIGHT
            if ((rightTopCorner || rightBottomCorner) && !field[x + 1][y].getBomb() && field[x + 1][y].getState() != 1) {
                place--;
                ifReveal(x + 1, y);
            }

            //LEFT
            if ((leftBottomCorner || leftTopCorner) && !field[x - 1][y].getBomb() && field[x - 1][y].getState() != 1){
                place--;
                ifReveal(x - 1, y);
            }

            //TOP
            if ((leftTopCorner || rightTopCorner) && !field[x][y - 1].getBomb() && field[x][y - 1].getState() != 1){
                place--;
                ifReveal(x, y - 1);
            }

            //BOTTOM
            if ((rightBottomCorner || leftBottomCorner) && !field[x][y + 1].getBomb() && field[x][y + 1].getState() != 1){
                place--;
                ifReveal(x, y + 1);
            }

        }
    }

    /**
     * Returns the amount of adjacent bombs
     *
     * @param x X
     * @param y Y
     * @return number of adjacent bombs
     */
    public int getAdjacentBombCount(int x, int y) {
        int placesToBomb = 0;

        boolean leftTopCorner = (x != 0) && (y != 0);
        boolean rightTopCorner = (x != columnsCount - 1) && (y != 0);
        boolean leftBottomCorner = (x != 0) && (y != rowsCount - 1);
        boolean rightBottomCorner = (x != columnsCount - 1) && (y != rowsCount - 1);


        //RIGHT
        if ((rightTopCorner || rightBottomCorner) && field[x + 1][y].getBomb()){
            placesToBomb++;
        }
        //LEFT
        if ((leftBottomCorner || leftTopCorner) && field[x - 1][y].getBomb()){
            placesToBomb++;
        }
        //TOP GENERAL
        if ((leftTopCorner || rightTopCorner) && field[x][y - 1].getBomb()){
            placesToBomb++;
        }
        //TOP RIGHT CORNER
        if (rightTopCorner && field[x + 1][y - 1].getBomb()){
            placesToBomb++;
        }
        //TOP LEFT CORNER
        if (leftTopCorner && field[x - 1][y - 1].getBomb()){
            placesToBomb++;
        }
        //BOTTOM GENERAL
        if ((rightBottomCorner || leftBottomCorner) && field[x][y + 1].getBomb()){
            placesToBomb++;
        }
        //BOTTOM RIGHT CORNER
        if (rightBottomCorner && field[x + 1][y + 1].getBomb()){
            placesToBomb++;
        }
        //BOTTOM LEFT CORNER
        if (leftBottomCorner && field[x - 1][y + 1].getBomb()){
            placesToBomb++;
        }

        return placesToBomb;
    }



    public void createBombsField(int rows, int columns) {

        Random rand = new Random();
        int countDown = 0;
        while (countDown != bombCount) {

            int randRow = rand.nextInt(columns);
            int randCol = rand.nextInt(rows);

            while (field[randRow][randCol].getBomb()) {
                randRow = rand.nextInt(columns);
                randCol = rand.nextInt(rows);
            }
            field[randRow][randCol].setBomb(true);
            countDown++;
        }
    }


    /**
     * returns true if every flag is on a bomb, else false
     *
     * @return if player won
     */
    public boolean didWin() {
        return (place == 0);
    }

    /**
     * returns true if player revealed a bomb, else false
     *
     * @return if player lost
     */
    public boolean didLoose() {
        return click;
    }

    public int getRows() {
        return rowsCount;
    }

    public int getColumns() {
        return columnsCount;
    }

}
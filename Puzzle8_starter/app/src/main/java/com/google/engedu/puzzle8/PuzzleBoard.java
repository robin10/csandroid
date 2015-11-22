package com.google.engedu.puzzle8;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;


public class PuzzleBoard {

    private static final int NUM_TILES = 3;
    private static final int[][] NEIGHBOUR_COORDS = {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
    };
    private ArrayList<PuzzleTile> tiles;
    private int steps;
    private PuzzleBoard previousBoard;

    PuzzleBoard(Bitmap bitmap, int parentWidth) {
        previousBoard = null;
        this.steps = 0;

        tiles=new ArrayList<>();
        Bitmap[][] imageChunks = new Bitmap[NUM_TILES][NUM_TILES];
        int width = bitmap.getWidth() / NUM_TILES;
        int height = bitmap.getHeight() / NUM_TILES;

        for(int row = 0 ; row < NUM_TILES ; row++)
        {
            for(int col = 0 ; col < NUM_TILES ; col++)
            {
                if(!(row == NUM_TILES - 1 && col == NUM_TILES - 1))
                {
                    imageChunks[row][col] = Bitmap.createBitmap(bitmap , width*col , height*row , width , height);
                    PuzzleTile currTile = new PuzzleTile(imageChunks[row][col], row*NUM_TILES + col);
                    tiles.add(currTile);
                }
            }
        }
        tiles.add(null);
    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
        previousBoard = otherBoard;
        steps = otherBoard.steps + 1;
    }

    public void initializePreviousBoard(){
        previousBoard = null;
        steps = 0;
    }

    public PuzzleBoard getPreviousBoard(){
        return previousBoard;
    }
    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i % NUM_TILES, i / NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    public ArrayList<PuzzleBoard> neighbours() {
        ArrayList<PuzzleBoard> shuffledBoards = new ArrayList<>();

        int emptyTile = 0;
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null) {
                emptyTile = i;
                break;
            }
        }

        int tileX = emptyTile / NUM_TILES;
        int tileY = emptyTile % NUM_TILES;

        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES) {
                int currTile = nullX * NUM_TILES + nullY;
                swapTiles(emptyTile, currTile);
                PuzzleBoard anotherBoard = new PuzzleBoard(this);
                shuffledBoards.add(anotherBoard);
                swapTiles(emptyTile, currTile);
            }
        }
        return shuffledBoards;
    }

    public int priority() {
        int manhattanPriority = 0;
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                int tileX = tile.getNumber() / NUM_TILES;
                int tileY = tile.getNumber() % NUM_TILES;
                int goalX = i / NUM_TILES;
                int goalY = i % NUM_TILES;

                manhattanPriority = manhattanPriority + Math.abs(goalX - tileX) + Math.abs(goalY - tileY);
            }
        }
        return manhattanPriority + this.steps;
    }
}

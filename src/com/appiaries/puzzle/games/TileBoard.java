//
// Copyright (c) 2014 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.games;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Point;

public class TileBoard {
    private int xSize;
    private int ySize;
    private int[][] tiles;

    public int getXSize() {
        return this.xSize;
    }

    public int getYSize() {
        return this.ySize;
    }

    // Initialize Board with Size (e.g. 3x3)
    public TileBoard(int wSize, int hSize) {
        this.xSize = wSize;
        this.ySize = hSize;

        this.tiles = tileValuesForSize(this.xSize, this.ySize);
    }

    private boolean isCoordinateInBound(Point coor) {
        return (coor.x > 0 && coor.x <= this.xSize && coor.y > 0 && coor.y <= this.ySize);
    }

    private int[][] tileValuesForSize(int xSize, int ySize) {
        int value = 1;

        int[][] newTiles = new int[xSize][ySize];

        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                newTiles[i][j] = (value != xSize * ySize ? value++ : 0);
            }
        }

        return newTiles;
    }

    public int getTilesCount() {
        return tiles.length;
    }

    public void setTileAtCoordinate(Point coor, int value) {
        if (this.isCoordinateInBound(coor)) {
            this.tiles[coor.y - 1][coor.x - 1] = value;
        }
    }

    public int tileAtCoordinate(Point coord) {
        if (this.isCoordinateInBound(coord)) {
            return this.tiles[coord.y - 1][coord.x - 1];
        }

        return -1; // null
    }

    public boolean canMoveTile(Point coor) {
        return (this.tileAtCoordinate(new Point(coor.x, coor.y - 1)) == 0 || // upper neighbor
                this.tileAtCoordinate(new Point(coor.x + 1, coor.y)) == 0 || // right neighbor
                this.tileAtCoordinate(new Point(coor.x, coor.y + 1)) == 0 || // lower neighbor
                this.tileAtCoordinate(new Point(coor.x - 1, coor.y)) == 0 // left neighbor
        );
    }

    public Point shouldMove(boolean move, Point coord) {
        if (!this.canMoveTile(coord)) {
            return null;
        }

        Point lowerNeighbor = new Point(coord.x, coord.y + 1);
        Point rightNeighbor = new Point(coord.x + 1, coord.y);
        Point upperNeighbor = new Point(coord.x, coord.y - 1);
        Point leftNeighbor = new Point(coord.x - 1, coord.y);

        Point neighbor = null;

        if (this.tileAtCoordinate(lowerNeighbor) == 0) {
            neighbor = lowerNeighbor;
        } else if (this.tileAtCoordinate(rightNeighbor) == 0) {
            neighbor = rightNeighbor;
        } else if (this.tileAtCoordinate(upperNeighbor) == 0) {
            neighbor = upperNeighbor;
        } else if (this.tileAtCoordinate(leftNeighbor) == 0) {
            neighbor = leftNeighbor;
        }

        if (move) {
            // Swap current tile with blank tile
            int value = this.tileAtCoordinate(coord);
            this.setTileAtCoordinate(coord, this.tileAtCoordinate(neighbor));
            this.setTileAtCoordinate(neighbor, value);
        }

        return neighbor;
    }

    // Shuffle the TileBoard
    public void shuffle() {
        int times = 200;

        for (int t = 0; t < times; t++) {
            List<Point> validMoves = new ArrayList<>();

            for (int j = 1; j <= this.ySize; j++) {
                for (int i = 1; i <= this.xSize; i++) {
                    Point p = new Point(i, j);
                    if (this.canMoveTile(p)) {
                        validMoves.add(p);
                    }
                }
            }

            // Get random 1 valid move in arrays
            int randInt = new Random().nextInt(validMoves.size());

            Point coord = validMoves.get(randInt);
            this.shouldMove(true, coord);
        }
    }

    // Check if the order of all tiles are correct
    public boolean isAllTilesCorrect() {
        int v = 1;
        boolean correct = true;

        checkCorrect: {
            for (int i = 0; i < this.xSize; i++) {
                for (int j = 0; j < this.ySize; j++) {
                    // Check if the first tile is not equal 1
                    if (this.tiles[i][j] != v) {
                        correct = false;
                        break checkCorrect;
                    } else {
                        v = (v < ((this.xSize * this.ySize) - 1) ? v + 1: 0);
                    }
                }
            }
        }

        return correct;
    }

}

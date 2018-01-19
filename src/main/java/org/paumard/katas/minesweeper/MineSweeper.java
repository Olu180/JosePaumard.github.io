package org.paumard.katas.minesweeper;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

public class MineSweeper {

    private InputGrid inputGrid;

    public void init(String inputField) {
        String[] lines = inputField.split("\n");
        String[] firstLineSplit = lines[0].split(" ");
        int numberOfLines = Integer.parseInt(firstLineSplit[0].trim());
        int numberOfColumns = Integer.parseInt(firstLineSplit[1].trim());
        inputGrid = new InputGrid(numberOfLines, numberOfColumns, lines);
    }

    public String produceHintField() {
        ResultGrid resultGrid = inputGrid.createEmptyResult();
        for (GridPosition position: inputGrid) {
            if (inputGrid.containsAMineAt(position)) {
                resultGrid.setAMineAt(position);
                resultGrid.updateNeighborhood(position);
            }
        }
        return resultGrid.createFinalResult();
    }

    private static class ResultGrid {

        private final char[][] result;
        private final int numberOfLines;
        private final int numberOfColumns;

        public ResultGrid(int numberOfLines, int numberOfColumns) {
            this.numberOfLines = numberOfLines;
            this.numberOfColumns = numberOfColumns;
            this.result = createGrid(numberOfLines, numberOfColumns);
        }

        private char[][] createGrid(int numberOfLines, int numberOfColumns) {
            char[][] result = new char[numberOfLines][];
            for (int lineIndex = 0 ; lineIndex < numberOfLines ; lineIndex++) {
                this.result[lineIndex] = new char[numberOfColumns];
                for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
                    result[lineIndex][columnIndex] = '0';
                }
            }
            return result;
        }

        public void setAMineAt(GridPosition position) {
            this.result[position.getLine()][position.getColumn()] = '*';
        }

        public void updateNeighborhood(GridPosition position) {
            for (int deltaLine = -1; deltaLine <= 1; deltaLine++) {
                for (int deltaColumn = -1; deltaColumn <= 1; deltaColumn++) {
                    if (position.line + deltaLine >= 0 && position.line + deltaLine < numberOfLines &&
                        position.column + deltaColumn >= 0 && position.column + deltaColumn < numberOfColumns) {
                        if (result[position.line + deltaLine][position.column + deltaColumn] != '*') {
                            result[position.line + deltaLine][position.column + deltaColumn]++;
                        }
                    }
                }
            }
        }

        public String createFinalResult() {
            return Arrays.stream(result).map(String::new).collect(Collectors.joining("\r\n"));
        }
    }

    private static class InputGrid implements Iterable<GridPosition> {

        private final int numberOfLines;
        private final int numberOfColumns;
        private final char[][] inputField;

        public InputGrid(int numberOfLines, int numberOfColumns, String[] lines) {

            this.numberOfLines = numberOfLines;
            this.numberOfColumns = numberOfColumns;
            this.inputField = new char[numberOfLines][];
            for (int lineIndex = 0; lineIndex < numberOfLines; lineIndex++) {
                this.inputField[lineIndex] = lines[lineIndex + 1].trim().toCharArray();
            }
        }

        @Override
        public Iterator<GridPosition> iterator() {
            return new Iterator<GridPosition>() {
                private int columnIndex = 0;
                private int lineIndex = 0;

                @Override
                public boolean hasNext() {
                    return columnIndex < numberOfColumns && lineIndex < numberOfLines;
                }

                @Override
                public GridPosition next() {
                    GridPosition gridPosition = new GridPosition(lineIndex, columnIndex);
                    columnIndex++;
                    if (columnIndex == numberOfColumns) {
                        columnIndex = 0;
                        lineIndex++;
                    }
                    return gridPosition;
                }
            };
        }

        public boolean containsAMineAt(GridPosition position) {
            return inputField[position.line][position.column] == '*';
        }

        public ResultGrid createEmptyResult() {
            return new ResultGrid(this.numberOfLines, this.numberOfColumns);
        }
    }

    private static class GridPosition {
        private final int column;
        private final int line;

        public GridPosition(int line, int column) {
            this.line = line;
            this.column = column;
        }

        public int getColumn() {
            return this.column;
        }

        public int getLine() {
            return this.line;
        }
    }
}

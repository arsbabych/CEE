package org.arsen.cee.hard;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by arsen on 14.08.15.
 */
public class LabyrinthSolver {

    private Cell[][] labyrinth;
    List<String> rawLabyrinth = new LinkedList<String>();
    private int width;
    private int heigth = 1;

    private LabyrinthSolver initLabyrinth() {
        for (int i = 0; i < getWidth(); i++) {
            String rawLine = rawLabyrinth.get(i);

            for (int j = 0; j < getHeigth(); j++) {
                if (i == 0) {
                    if (rawLine.charAt(j) == ' ') {
                        getLabyrinth()[i][j] = new Cell(i, j, 'S');
                        continue;
                    }
                }
                if (i == getWidth() - 1) {
                    if (rawLine.charAt(j) == ' ') {
                        getLabyrinth()[i][j] = new Cell(i, j, 'F');
                        continue;
                    }
                }

                getLabyrinth()[i][j] = new Cell(i, j, rawLine.charAt(j));
            }
        }
        return this;
    }

    private void printLabyrinth() {
        for (int i = 0; i < getWidth(); i++) {
            StringBuffer sb = new StringBuffer();

            for (int j = 0; j < getHeigth(); j++) {
                sb.append(getLabyrinth()[i][j].getValue().getCharValue());
            }

            System.out.println(sb.toString());
        }
    }
    
    public void setLabyrinth(Cell[][] labyrinth) {
        this.labyrinth = labyrinth;        
    }
    
    public LabyrinthSolver createLabyrinthFromFile(String path) throws IOException {
        File file = new File(path);

        if (file.isFile()) {
            InputStream is = new FileInputStream(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            rawLabyrinth.add(line);
            setWidth(line.length());

            while ((line = br.readLine()) != null) {
                rawLabyrinth.add(line);
                setHeigth(getHeigth() + 1);
            }
        }

        setLabyrinth(new Cell[width][heigth]);
        initLabyrinth();

        return this;
    }    

    public static void main(String[] args) throws IOException {
        LabyrinthSolver labyrinthSolver = new LabyrinthSolver();
        labyrinthSolver.createLabyrinthFromFile(args[0]);

        AStarAlgorithm aStarAlgorithm = new AStarAlgorithm(labyrinthSolver);
        labyrinthSolver = aStarAlgorithm.solveLabyrinth();

        labyrinthSolver.printLabyrinth();
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

    public int getWidth() {
        return width;
    }

    public int getHeigth() {
        return heigth;
    }

    public Cell[][] getLabyrinth() {
        return labyrinth;
    }

    public void defineHs(Cell finishCell) {
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeigth(); j++) {
                if (getLabyrinth()[i][j].getValue().getCharValue() == '*') {
                    continue;
                }

                int value = Math.abs(finishCell.getX() - getLabyrinth()[i][j].getX()) + Math.abs(finishCell.getY() - getLabyrinth()[i][j].getY());
                getLabyrinth()[i][j].setH(value);
            }
        }
    }
}

class Cell {

    Cell(int x, int y, char c) {
        this.setX(x);
        this.setY(y);
        this.setValue(Value.initValue(c));
    }

    private int g;
    private int h;
    private int f;
    private int x;
    private int y;
    private Cell parent;
    private Value value;
    private boolean inOpenList;
    private boolean inClosedList;

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public Cell getParent() {
        return parent;
    }

    public void setParent(Cell parent) {
        this.parent = parent;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isInClosedList() {
        return inClosedList;
    }

    public void setInClosedList(boolean inClosedList) {
        this.inClosedList = inClosedList;
    }

    public boolean isInOpenList() {
        return inOpenList;
    }

    public void setInOpenList(boolean inOpenList) {
        this.inOpenList = inOpenList;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}

enum Value {
    IGNORED('*'), OPEN('O'), CLOSED('C'), EMPTY(' '), START('S'), FINISH('F'), PATH('+');

    private char charValue;

    Value(char charValue) {
        this.charValue = charValue;
    }

    public char getCharValue() {
        return this.charValue;
    }

    static Value initValue(char charValue) {
        switch (charValue) {
            case '*':
                return Value.IGNORED;
            case 'O':
                return Value.OPEN;
            case 'C':
                return Value.CLOSED;
            case ' ':
                return Value.EMPTY;
            case 'S':
                return Value.START;
            case 'F':
                return Value.FINISH;
            case '+':
                return Value.PATH;
            default:
                throw new IllegalArgumentException("Wrong symbol");
        }
    }
}

class AStarAlgorithm {

    private LabyrinthSolver labyrinthSolver;
    private Cell startCell;
    private Cell finishCell;
    private Cell workingCell;
    List<Cell> closedList = new LinkedList<Cell>();
    List<Cell> openedList = new LinkedList<Cell>();

    final int[] horizontal = new int[] {1, 0, -1, 0};
    final int[] vertical = new int[] {0, -1, 0, 1};

    AStarAlgorithm(LabyrinthSolver labyrinthSolver) {
        this.labyrinthSolver = labyrinthSolver;
        findStartCell();
        findFinishCell();
    }

    private void findStartCell() {
        int i = 0;

        Cell[][] labyrinth = labyrinthSolver.getLabyrinth();

        while(i < labyrinthSolver.getWidth()) {
            if (labyrinth[0][i].getValue().getCharValue() == 'S') {
                startCell = labyrinth[0][i];
                startCell.setParent(null);
                closedList.add(startCell);
                startCell.setInClosedList(true);
                startCell.setInOpenList(false);
                startCell.setG(0);
                startCell.setValue(Value.CLOSED);
                workingCell = startCell;
                break;
            }
            i++;
        }
    }

    private void findFinishCell() {
        int i = 0;

        Cell[][] labyrinth = labyrinthSolver.getLabyrinth();

        while(i < labyrinthSolver.getWidth()) {
            if (labyrinth[labyrinthSolver.getHeigth() - 1][i].getValue().getCharValue() == 'F') {
                finishCell = labyrinth[labyrinthSolver.getHeigth() - 1][i];
                break;
            }
            i++;
        }
    }

    public LabyrinthSolver solveLabyrinth() {

        initializeHValues();

        do {
            step();
        } while (!isSolved());

        findPath();

        return getLabyrinthSolver();
    }

    public void initializeHValues() {
        labyrinthSolver.defineHs(finishCell);
    }

    public boolean isSolved() {
        return finishCell == workingCell;

    }

    public void findPath() {

    }

    public void step() {
        addCellsToOpenList();
        updateWorkingCell();
    }

    private void addCellsToOpenList() {
        int xCoordinate;
        int yCoordinate;

        for (int i = 0; i < 4; i++) {
            xCoordinate = workingCell.getX() + horizontal[i];
            yCoordinate = workingCell.getY() + vertical[i];

            if (isCoordinatesInTheLabyrinth(xCoordinate, yCoordinate) &&
                !labyrinthSolver.getLabyrinth()[xCoordinate][yCoordinate].getValue().equals(Value.IGNORED) &&
                !labyrinthSolver.getLabyrinth()[xCoordinate][yCoordinate].isInClosedList() &&
                !labyrinthSolver.getLabyrinth()[xCoordinate][yCoordinate].isInOpenList()) {

                Cell targetCell = labyrinthSolver.getLabyrinth()[xCoordinate][yCoordinate];
                openedList.add(targetCell);

                targetCell.setG(workingCell.getG() + 10 * (horizontal[i] + vertical[i]));
                targetCell.setF(targetCell.getG() + targetCell.getH());
                targetCell.setParent(workingCell);
                targetCell.setValue(Value.OPEN);
            }
        }
    }

    private void updateWorkingCell() {

    }

    private boolean isCoordinatesInTheLabyrinth(int xCoordinate, int yCoordinate) {
        return xCoordinate > 0 && xCoordinate < labyrinthSolver.getWidth() &&
               yCoordinate > 0 && yCoordinate < labyrinthSolver.getHeigth();

    }

    public LabyrinthSolver getLabyrinthSolver() {
        return labyrinthSolver;
    }
}

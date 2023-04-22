import java.util.ArrayList;

public class Board extends Observable {

    private Cell[][] board = new Cell[5][5];
    private int sandLevel = 8;
    private double stormLevel = 2.0;

    public Board() {
        // random l'equipement
        this.board[0][0] = new Cell(CellType.empty, new ArrayList<Integer>(), null);
        this.board[0][1] = new Cell(CellType.empty, new ArrayList<Integer>(), null);
        this.board[0][2] = new Cell(CellType.sand1, new ArrayList<Integer>(), null);
        this.board[0][3] = new Cell(CellType.empty, new ArrayList<Integer>(), null);
        this.board[0][4] = new Cell(CellType.empty, new ArrayList<Integer>(), null);

        this.board[1][0] = new Cell(CellType.empty, new ArrayList<Integer>(), null);
        this.board[1][1] = new Cell(CellType.sand1, new ArrayList<Integer>(), null);
        this.board[1][2] = new Cell(CellType.empty, new ArrayList<Integer>(), null);
        this.board[1][3] = new Cell(CellType.sand1, new ArrayList<Integer>(), null);
        this.board[1][4] = new Cell(CellType.empty, new ArrayList<Integer>(), null);

        this.board[2][0] = new Cell(CellType.sand1, new ArrayList<Integer>(), null);
        this.board[2][1] = new Cell(CellType.empty, new ArrayList<Integer>(), null);
        this.board[2][2] = new Cell(CellType.eye, new ArrayList<Integer>(), null);
        this.board[2][3] = new Cell(CellType.empty, new ArrayList<Integer>(), null);
        this.board[2][4] = new Cell(CellType.sand1, new ArrayList<Integer>(), null);

        this.board[3][0] = new Cell(CellType.empty, new ArrayList<Integer>(), null);
        this.board[3][1] = new Cell(CellType.sand1, new ArrayList<Integer>(), null);
        this.board[3][2] = new Cell(CellType.empty, new ArrayList<Integer>(), null);
        this.board[3][3] = new Cell(CellType.sand1, new ArrayList<Integer>(), null);
        this.board[3][4] = new Cell(CellType.empty, new ArrayList<Integer>(), null);

        this.board[4][0] = new Cell(CellType.empty, new ArrayList<Integer>(), null);
        this.board[4][1] = new Cell(CellType.empty, new ArrayList<Integer>(), null);
        this.board[4][2] = new Cell(CellType.sand1, new ArrayList<Integer>(), null);
        this.board[4][3] = new Cell(CellType.empty, new ArrayList<Integer>(), null);
        this.board[4][4] = new Cell(CellType.empty, new ArrayList<Integer>(), null);
        this.notifyObservers();
    }

    public Cell[][] getBoard() {
        return this.board;
    }

    public double getStormLevel() {
        return this.stormLevel;
    }

    public void increaseStormLevel() {
        this.stormLevel += 0.5;
        this.notifyObservers();
    }

    public int getSandLevel() {
        return this.sandLevel;
    }

    public int updateSandLevel() {
        this.sandLevel = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                switch (this.board[i][j].getType()) {
                    case sand1:
                        sandLevel += 1;
                        break;
                    case sand2:
                        sandLevel += 2;
                        break;
                    default:
                        break;
                }
            }
        }
        this.notifyObservers();
        return this.sandLevel;
    }

    public Coord getOeil() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (this.board[i][j].getType() == CellType.eye) {
                    return new Coord(i, j);
                }
            }
        }
        return new Coord(-1, -1);
    }

    public Cell getCell(Coord c) {
        return this.board[c.x][c.y];
    }

    public void switchCases(Coord c1, Coord c2) {
        final Cell tmp = this.board[c1.x][c1.y];
        this.board[c1.x][c1.y] = this.board[c1.x][c1.y].copy();
        this.board[c2.x][c2.y] = tmp.copy();
    }

    public void sandstorm() {
        // le vent souffle
        final double dirP = Math.random();
        Coord oeilPos = this.getOeil();
        // 1 <= force <= 3
        final int force = (int) (Math.floor(Math.random()*2.9) + 1);

        System.out.print(force + " ");
        if (dirP < 0.25) {
            // sud
            System.out.print("sud\n");

            final int maxValue = Math.max((int) Math.floor(oeilPos.x-force), 0);

            while (oeilPos.x > maxValue) {
                this.switchCases(new Coord(oeilPos.x - 1, oeilPos.y), new Coord(oeilPos.x, oeilPos.y));
                oeilPos = this.getOeil();
            }
        } else if (dirP < 0.5) {
            // nord
            System.out.print("nord\n");

            final int maxValue = Math.min((int) Math.floor(oeilPos.x+force), 5 - 1);

            while (oeilPos.x < maxValue) {
                this.switchCases(new Coord(oeilPos.x + 1, oeilPos.y), new Coord(oeilPos.x, oeilPos.y));
                oeilPos = this.getOeil();
            }

        } else if (dirP < 0.75) {
            // est
            System.out.print("est\n");

            final int maxValue = Math.max((int) Math.floor(oeilPos.y-force), 0);

            while (oeilPos.y > maxValue) {
                this.switchCases(new Coord(oeilPos.x, oeilPos.y - 1), new Coord(oeilPos.x, oeilPos.y));
                oeilPos = this.getOeil();
            }

        } else {
            // est
            System.out.print("ouest\n");

            final int maxValue = Math.min((int) Math.floor(oeilPos.y+force), 5 - 1);

            while (oeilPos.y < maxValue) {
                this.switchCases(new Coord(oeilPos.x, oeilPos.y + 1), new Coord(oeilPos.x, oeilPos.y));
                oeilPos = this.getOeil();
            }
        }
        this.notifyObservers();
    }

    public void movePlayer(Coord coord1, Coord coord2, int pid) {
        if (coord1 != null) {
            final Cell c1 = this.getCell(coord1);
            c1.removePlayer(pid);
        }
        if (coord2 != null) {
            final Cell c2 = this.getCell(coord2);
            c2.addPlayer(pid);
        }
    }
}
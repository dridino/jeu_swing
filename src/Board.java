import java.util.ArrayList; 
import java.util.Collections;

public class Board extends Observable implements Observer {

    private Cell[][] board = new Cell[5][5];
    private int sandLevel = 8;
    private double stormLevel = 2.0;

    public Board(ArrayList<Player> p) {
        final ArrayList<CellContent> contents = new ArrayList<>();
        final ArrayList<ObjectType> objects = new ArrayList<>();
        final CellContent[] values1 = {
                CellContent.oasis,
                CellContent.oasis,
                CellContent.mirage,

                CellContent.crash,
                CellContent.tunnel,
                CellContent.tunnel,
                CellContent.tunnel,
                CellContent.equipment,
                CellContent.equipment,
                CellContent.equipment,
                CellContent.equipment,
                CellContent.equipment,
                CellContent.equipment,
                CellContent.equipment,
                CellContent.equipment,

                CellContent.takeoff,

                CellContent.clueRowEngine,
                CellContent.clueRowWheel,
                CellContent.clueRowEnergy,
                CellContent.clueRowRotor,

                CellContent.clueColumnEngine,
                CellContent.clueColumnWheel,
                CellContent.clueColumnEnergy,
                CellContent.clueColumnRotor,
        };

        Collections.addAll(contents, values1);
        final Deck<CellContent> deck = new Deck<CellContent>(contents, DeckType.cellContent);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i != 2 || j != 2) {
                    final CellType cellType = ((i == 0 && (j == 2)) || (i == 1 && (j == 1 || j == 3)) || (i == 2 && (j == 0 || j == 4)) || (i == 3 && (j == 1 || j == 3)) || (i == 4 && j == 2)) ? CellType.sand1 : CellType.empty;
                    final CellContent cellContent = deck.pick();
                    final ArrayList<Integer> players = new ArrayList<Integer>();
                    this.board[i][j] = new Cell(cellType, players, cellContent);
                } else {
                    this.board[i][j] = new Cell(CellType.eye, new ArrayList<Integer>(), CellContent.none);
                }
                this.board[i][j].addObserver(this);
            }
        }
        this.notifyObservers();
        /*
        this.board[0][0] = new Cell(CellType.empty, new ArrayList<Integer>(), contents);
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
         */
    }

    public void positionPlayers(ArrayList<Player> players) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                final Cell current = this.board[i][j];
                if (current.getContent() == CellContent.crash) {
                    for (Player p: players) {
                        current.addPlayer(p.getId());
                        p.setPosition(new Coord(i, j));
                    }
                    break;
                }
            }
        }
        notifyObservers();
    }

    public void removeSand(Coord c) {
        this.getCell(c).removeSand();
        notifyObservers();
    }

    public void update() {
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
        final Cell tmp = this.board[c1.x][c1.y].copy();
        this.board[c1.x][c1.y] = this.board[c2.x][c2.y].copy();
        this.board[c2.x][c2.y] = tmp;
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

    public void moveNorth(int n) {
        int i = 0;
        while (this.getOeil().x < 4 && i < n) {
            final Coord newCoord = new Coord(this.getOeil().x+1, this.getOeil().y);
            this.getCell(newCoord).addSand();
            this.switchCases(newCoord, this.getOeil());
            i++;
        }
    }

    public void moveSouth(int n) {
        int i = 0;
        while (this.getOeil().x > 0 && i < n) {
            final Coord newCoord = new Coord(this.getOeil().x-1, this.getOeil().y);
            this.getCell(newCoord).addSand();
            this.switchCases(newCoord, this.getOeil());
            i++;
        }
    }

    public void moveEast(int n) {
        int i = 0;
        while (this.getOeil().y > 0 && i < n) {
            final Coord newCoord = new Coord(this.getOeil().x, this.getOeil().y - 1);
            this.getCell(newCoord).addSand();
            this.switchCases(newCoord, this.getOeil());
            i++;
        }
    }

    public void moveWest(int n) {
        int i = 0;
        while (this.getOeil().y < 4 && i < n) {
            final Coord newCoord = new Coord(this.getOeil().x, this.getOeil().y + 1);
            this.getCell(newCoord).addSand();
            this.switchCases(newCoord, this.getOeil());
            i++;
        }
    }

    public void unleash() {
        this.stormLevel += 0.5;
    }

    public void handleStormEvents(StormAction action) {
        switch (action) {
            case north1:
                this.moveNorth(1);
                break;
            case north2:
                this.moveNorth(2);
                break;
            case north3:
                this.moveNorth(3);
                break;
            case south1:
                this.moveSouth(1);
                break;
            case south2:
                this.moveSouth(2);
                break;
            case south3:
                this.moveSouth(3);
                break;
            case west1:
                this.moveWest(1);
                break;
            case west2:
                this.moveWest(2);
                break;
            case west3:
                this.moveWest(3);
                break;
            case east1:
                this.moveEast(1);
                break;
            case east2:
                this.moveEast(2);
                break;
            case east3:
                this.moveEast(3);
                break;
            case unleash:
                this.unleash();
                break;
        }
        notifyObservers();
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

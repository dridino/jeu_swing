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
                    this.board[i][j] = new Cell(cellType, players, cellContent, false);
                } else {
                    this.board[i][j] = new Cell(CellType.eye, new ArrayList<Integer>(), CellContent.none, false);
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

    private boolean inLine(CellContent content, Coord c) {
        for (int j = 0; j < 5; j++) {
            if (this.getCell(new Coord(c.x, j)).getContent() == content) {
                return true;
            }
        }
        return false;
    }

    private boolean inColumn(CellContent content, Coord c) {
        for (int i = 0; i < 5; i++) {
            if (this.getCell(new Coord(i, c.y)).getContent() == content) {
                return true;
            }
        }
        return false;
    }

    public PieceType updatePieceCoord(Coord c) {
        final Cell currentCell = this.getCell(c);
        if (currentCell.getType() == CellType.empty && currentCell.isExplored()) {
            return null;
        }
        if (this.inLine(CellContent.clueRowEngine, c) && this.inColumn(CellContent.clueColumnEngine, c)) {
            return PieceType.engine;
        }
        if (this.inLine(CellContent.clueRowWheel, c) && this.inColumn(CellContent.clueColumnWheel, c)) {
            return PieceType.wheel;
        }
        if (this.inLine(CellContent.clueRowEnergy, c) && this.inColumn(CellContent.clueColumnEnergy, c)) {
            return PieceType.energy;
        }
        if (this.inLine(CellContent.clueRowRotor, c) && this.inColumn(CellContent.clueColumnRotor, c)) {
            return PieceType.rotor;
        }
        return null;
    }

    public void updatePiece() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                this.updatePieceCoord(new Coord(i, j));
            }
        }

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

    public void removeSand(Coord c, int n) {
        for (int i = 0; i < n; i++) {
            this.getCell(c).removeSand();
        }
        notifyObservers();
    }

    public void update() {
        this.notifyObservers();
    }

    public double getStormLevel() {
        return this.stormLevel;
    }

    public int getSandLevel() {
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
        notifyObservers();
    }


    public void moveNorth(int n, Player currentPlayer) {
        int i = 0;
        while (this.getOeil().x < 4 && i < n) {
            final Coord newCoord = new Coord(this.getOeil().x+1, this.getOeil().y);
            if (currentPlayer.getPosition().isEqual(newCoord)) {
                currentPlayer.setPosition(this.getOeil());
            }
            this.getCell(newCoord).addSand();
            this.switchCases(newCoord, this.getOeil());
            i++;
        }
    }

    public void moveSouth(int n, Player currentPlayer) {
        int i = 0;
        while (this.getOeil().x > 0 && i < n) {
            final Coord newCoord = new Coord(this.getOeil().x-1, this.getOeil().y);
            if (currentPlayer.getPosition().isEqual(newCoord)) {
                currentPlayer.setPosition(this.getOeil());
            }
            this.getCell(newCoord).addSand();
            this.switchCases(newCoord, this.getOeil());
            i++;
        }
    }

    public void moveEast(int n, Player currentPlayer) {
        int i = 0;
        while (this.getOeil().y > 0 && i < n) {
            final Coord newCoord = new Coord(this.getOeil().x, this.getOeil().y - 1);
            if (currentPlayer.getPosition().isEqual(newCoord)) {
                currentPlayer.setPosition(this.getOeil());
            }
            this.getCell(newCoord).addSand();
            this.switchCases(newCoord, this.getOeil());
            i++;
        }
    }

    public void moveWest(int n, Player currentPlayer) {
        int i = 0;
        while (this.getOeil().y < 4 && i < n) {
            final Coord newCoord = new Coord(this.getOeil().x, this.getOeil().y + 1);
            if (currentPlayer.getPosition().isEqual(newCoord)) {
                currentPlayer.setPosition(this.getOeil());
            }
            this.getCell(newCoord).addSand();
            this.switchCases(newCoord, this.getOeil());
            i++;
        }
    }

    public void unleash() {
        this.stormLevel += 0.5;
    }

    public void handleStormEvents(StormAction action, Player currentPlayer) {
        switch (action) {
            case north1:
                this.moveNorth(1, currentPlayer);
                break;
            case north2:
                this.moveNorth(2, currentPlayer);
                break;
            case north3:
                this.moveNorth(3, currentPlayer);
                break;
            case south1:
                this.moveSouth(1, currentPlayer);
                break;
            case south2:
                this.moveSouth(2, currentPlayer);
                break;
            case south3:
                this.moveSouth(3, currentPlayer);
                break;
            case west1:
                this.moveWest(1, currentPlayer);
                break;
            case west2:
                this.moveWest(2, currentPlayer);
                break;
            case west3:
                this.moveWest(3, currentPlayer);
                break;
            case east1:
                this.moveEast(1, currentPlayer);
                break;
            case east2:
                this.moveEast(2, currentPlayer);
                break;
            case east3:
                this.moveEast(3, currentPlayer);
                break;
            case DECHAINE:
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

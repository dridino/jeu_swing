import java.util.ArrayList; 
import java.util.List;

public class Cell extends Observable {
    private CellType type;
    private final ArrayList<Integer> playerIds;
    private CellContent content;

    private boolean isExplored;
    private boolean equipmentPicked = false;
    private boolean waterConsumed;
    private PieceType piece;

    public Cell(CellType type, ArrayList<Integer> players, CellContent content, boolean isExplored) {
        this.type = type;
        this.playerIds = players;
        this.content = content;
        this.isExplored = isExplored;
        this.waterConsumed = content != CellContent.oasis;
        notifyObservers();
    }

    public void consumeWater() {
        this.waterConsumed = true;
        notifyObservers();
    }

    public boolean isWaterConsumed() {
        return this.waterConsumed;
    }

    public Cell copy() {
        return new Cell(this.type, this.playerIds, this.content, this.isExplored);
    }

    public CellType getType() {
        return this.type;
    }

    public boolean isExplored() {
        return this.isExplored;
    }

    public PieceType getPiece() {
        return this.piece;
    }

    public void explore() {
        this.isExplored = true;
        notifyObservers();
    }

    public CellContent getContent() {
        return this.content;
    }

    public List<Integer> getPlayerIds() {
        return this.playerIds;
    }

    public void addPlayer(int id) {
        this.playerIds.add(id);
        notifyObservers();
    }

    public void removePlayer(int id) {
        this.playerIds.removeIf(p -> p == id);
        notifyObservers();
    }

    public int addSand() {
        switch (this.type) {
            case empty:
                this.type = CellType.sand1;
                notifyObservers();
                return 1;
            case sand1:
                this.type = CellType.sand2;
                notifyObservers();
                return 1;
            default:
                return 0;
        }
    }

    public int removeSand() {
        switch (this.type) {
            case sand1:
                this.type = CellType.empty;
                notifyObservers();
                return -1;
            case sand2:
                this.type = CellType.sand1;
                notifyObservers();
                return -1;
            default:
                return 0;
        }
    }
}

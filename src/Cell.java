import java.util.ArrayList; 
import java.util.List;

public class Cell extends Observable {
    private CellType type;
    private ArrayList<Integer> playerIds = new ArrayList<Integer>();
    private CellContent content;

    private boolean isExplored = false;
    private boolean equipmentPicked = false;

    public Cell(CellType type, ArrayList<Integer> players, CellContent content) {
        this.type = type;
        this.playerIds.addAll(players);
        this.content = content;
        notifyObservers();
    }

    public boolean is(Cell c) {
        return this.type == c.type && this.playerIds == c.playerIds && this.content == c.content;
    }

    public Cell copy() {
        return new Cell(this.type, this.playerIds, this.content);
    }

    public CellType getType() {
        return this.type;
    }

    public boolean isExplored() {
        return this.isExplored;
    }

    public void explore() {
        this.isExplored = true;
        notifyObservers();
    }

    public void setType(CellType t) {
        this.type = t;
        notifyObservers();
    }

    public boolean isEquipmentPicked() {
        return this.equipmentPicked;
    }

    public CellContent getContent() {
        return this.content;
    }

    public void pickEquipment() {
        this.equipmentPicked = true;
        notifyObservers();
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

    public boolean containsEquipment() {
        return this.content == CellContent.equipment || this.content == CellContent.crash || this.content == CellContent.tunnel;
    }
}

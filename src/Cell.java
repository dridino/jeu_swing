import java.util.ArrayList;
import java.util.List;

public class Cell extends Observable {
    private CellType type;
    private ArrayList<Integer> playerIds = new ArrayList<Integer>();
    private CellContent content;
    private ObjectType objectType;
    private boolean equipmentPicked = false;

    public Cell(CellType type, ArrayList<Integer> players, CellContent content, ObjectType objectType) {
        this.type = type;
        this.playerIds.addAll(players);
        this.content = content;
        this.objectType = objectType;
        System.out.println(this.playerIds);
        notifyObservers();
    }

    public Cell copy() {
        return new Cell(this.type, this.playerIds, this.content, this.objectType);
    }

    public CellType getType() {
        return this.type;
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

    public ObjectType getObjectType() {
        return this.objectType;
    }

    public void pickEquipment() {
        this.equipmentPicked = true;
    }

    public List<Integer> getPlayerIds() {
        return this.playerIds;
    }

    public void addPlayer(int id) {
        this.playerIds.add(id);
    }

    public void removePlayer(int id) {
        this.playerIds.removeIf(p -> p == id);
    }

    public int addSand() {
        switch (this.type) {
            case empty:
                this.type = CellType.sand1;
                return 1;
            case sand1:
                this.type = CellType.sand2;
                return 1;
            default:
                return 0;
        }
    }

    public int removeSand() {
        switch (this.type) {
            case sand1:
                this.type = CellType.empty;
                return -1;
            case sand2:
                this.type = CellType.sand1;
                return -1;
            default:
                return 0;
        }
    }
}

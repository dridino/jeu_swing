import java.util.List;

public class Cell {
    private CellType type;
    private List<Integer> playerIds;
    private Equipment equipment;
    private boolean equipmentPicked = false;

    public Cell(CellType type, List<Integer> playerIds, Equipment equipment) {
        this.type = type;
        this.playerIds = playerIds;
        this.equipment = equipment;
    }

    public Cell copy() {
        return new Cell(this.type, this.playerIds, this.equipment);
    }

    public CellType getType() {
        return this.type;
    }


    public void setType(CellType t) {
        this.type = t;
    }

    public boolean isEquipmentPicked() {
        return this.equipmentPicked;
    }
    public Equipment getEquipment() {
        return this.equipment;
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

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

public class Player extends Observable {
    private static Color[] colors = {Color.green, Color.pink, Color.cyan, Color.red, Color.blue};
    private int id;
    private String pseudo;
    private double waterLevel;
    private Coord position;
    private PlayerType type;
    private int actionsDone;
    private int maxActions;
    private int maxDistance;
    private int maxWater;
    private int maxSandRemove;
    private int maxSandDeplacement;
    private DeplacementType deplacementType;
    private boolean canRemoveStormCard;
    private Color color;

    private ArrayList<Equipment> objects = new ArrayList<Equipment>();

    public Player(int id, String pseudo, Coord position, PlayerType type) {
        this.id = id;
        this.pseudo = pseudo;
        this.position = position;
        this.type = type;
        this.color = colors[id];

        this.waterLevel = 4;
        this.actionsDone = 0;
        this.maxActions = 4;
        this.maxDistance = 1;
        this.maxSandRemove = 1;
        this.maxSandDeplacement = 1;
        this.maxWater = 4;
        this.deplacementType = DeplacementType.straight;
        this.canRemoveStormCard = false;

        switch (this.type) {
            case archeo:
                this.maxSandRemove = 2;
                break;
            case alpinist:
                this.maxSandDeplacement = 2;
                break;
            case explo:
                this.deplacementType = DeplacementType.diagonal;
                break;
            case meteo:
                this.canRemoveStormCard = true;
                break;
            case navig:
                break;
            case water:
                this.maxWater = 5;
                this.waterLevel = 5;
                break;
        }
    }

    public int getId() {
        return this.id;
    }

    public Color getColor() {
        return this.color;
    }
    public String getPseudo() {
        return this.pseudo;
    }

    public double getWaterLevel() {
        return this.waterLevel;
    }

    public int getRemainingActions() {
        return this.maxActions - this.actionsDone;
    }

    public ArrayList<Equipment> getEquipment() {
        return this.objects;
    }

    public double increaseWaterLevel(int v) {
        this.waterLevel += v;
        return this.waterLevel;
    }

    public double decreaseWaterLevel(double v) {
        this.waterLevel -= v;
        return this.waterLevel;
    }

    public Coord getPosition() {
        return this.position;
    }

    public void setPosition(Coord c) {
        this.position = c;
    }

    private boolean praticableCell(CellType cellType, PlayerAction action) {
        if (action == PlayerAction.move) {
            return (cellType == CellType.sand1 || cellType == CellType.empty || (this.type == PlayerType.alpinist && cellType == CellType.sand2));
        } else {
            return (cellType == CellType.sand1 || cellType == CellType.sand2);
        }
    }

    private ArrayList<Coord> straightCoords(Board b, PlayerAction action) {
        final ArrayList<Coord> arr = new ArrayList<Coord>();
        if (this.position.x > 0) {
            final Coord tmp = new Coord(this.position.x -1, this.position.y);
            if (this.praticableCell(b.getCell(tmp).getType(), action)) {
                arr.add(tmp);
            }
        }
        if (this.position.x < 4) {
            final Coord tmp = new Coord(this.position.x + 1, this.position.y);
            if (this.praticableCell(b.getCell(tmp).getType(), action)) {
                arr.add(tmp);
            }
        }
        if (this.position.y > 0) {
            final Coord tmp = new Coord(this.position.x, this.position.y - 1);
            if (this.praticableCell(b.getCell(tmp).getType(), action)) {
                arr.add(tmp);
            }
        }
        if (this.position.y < 4) {
            final Coord tmp = new Coord(this.position.x, this.position.y + 1);
            if (this.praticableCell(b.getCell(tmp).getType(), action)) {
                arr.add(tmp);
            }
        }
        return arr;
    }

    private ArrayList<Coord> diagCoords(Board b, PlayerAction action) {
        final ArrayList<Coord> arr = new ArrayList<Coord>();
        if (this.position.x > 0 && this.position.y > 0) {
            final Coord tmp = new Coord(this.position.x -1, this.position.y - 1);
            if (this.praticableCell(b.getCell(tmp).getType(), action)) {
                arr.add(tmp);
            }
        }
        if (this.position.x > 0 && this.position.y < 4) {
            final Coord tmp = new Coord(this.position.x - 1, this.position.y + 1);
            if (this.praticableCell(b.getCell(tmp).getType(), action)) {
                arr.add(tmp);
            }
        }
        if (this.position.x < 4 && this.position.y > 0) {
            final Coord tmp = new Coord(this.position.x + 1, this.position.y - 1);
            if (this.praticableCell(b.getCell(tmp).getType(), action)) {
                arr.add(tmp);
            }
        }
        if (this.position.x < 4 && this.position.y < 4) {
            final Coord tmp = new Coord(this.position.x + 1, this.position.y + 1);
            if (this.praticableCell(b.getCell(tmp).getType(), action)) {
                arr.add(tmp);
            }
        }
        return arr;
    }

    public int getMaxSandRemove() {
        return maxSandRemove;
    }

    public ArrayList<Coord> getAvailableCells(Board b, PlayerAction action) {
        final ArrayList<Coord> arr = new ArrayList<Coord>();
        arr.addAll(this.straightCoords(b, action));

        if (this.type == PlayerType.explo) {
            arr.addAll(this.diagCoords(b, action));
        }

        if (action == PlayerAction.removeSand && (b.getCell(this.position).getType() == CellType.sand1 || b.getCell(this.position).getType() == CellType.sand2)) {
            arr.add(this.position);
        }

        if (action == PlayerAction.move && b.getCell(this.position).isExplored() && b.getCell(this.position).getContent() == CellContent.tunnel) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (i != this.position.x || j != this.position.y) {
                        if (b.getCell(new Coord(i, j)).isExplored() && b.getCell(new Coord(i, j)).getContent() == CellContent.tunnel) {
                            arr.add(new Coord(i, j));
                        }
                    }
                }
            }
        }

        return arr;
    }

    public boolean isCellAvailable(Board b, PlayerAction action, Coord coord) {
        for (Coord c: this.getAvailableCells(b, action)) {
            if (c.x == coord.x && c.y == coord.y) {
                return true;
            }
        }
        return false;
    }

    public PlayerType getType() {
        return this.type;
    }

    public int getActionsDone() {
        return this.actionsDone;
    }

    public void resetActions() {
        this.actionsDone = 0;
    }

    public int increaseActions(PlayerAction action) {
        this.actionsDone += 1;
        return this.actionsDone;
    }

    public int getMaxActions() {
        return this.maxActions;
    }

    public int getMaxDistance() {
        return this.maxDistance;
    }

    public int getMaxWater() {
        return this.maxWater;
    }

    public ArrayList<Equipment> getObjects() {
        return this.objects;
    }

    public void addObject(Equipment equipment) {
        this.objects.add(equipment);
    }

    public void removeObject(Equipment equipment) {
        this.objects.removeIf(e -> e.getType() == equipment.getType());
    }
}

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

public class Player extends Observable {
    private static Color[] colors = {Color.green, Color.pink, Color.cyan, Color.red, Color.blue};
    private int id;
    private String pseudo;
    private int waterLevel;
    private Coord position;
    private PlayerType type;
    private int actionsDone;
    private int maxActions;
    private int maxDistance;
    private int maxWater;
    private Color color;

    private ArrayList<Equipment> objects = new ArrayList<Equipment>();

    public Player(int id, String pseudo, Coord position, PlayerType type, int maxActions, int maxDistance, int maxWater, Color c) {
        this.id = id;
        this.pseudo = pseudo;
        this.waterLevel = maxWater;
        this.position = position;
        this.type = type;
        this.actionsDone = 0;
        this.maxActions = maxActions;
        this.maxDistance = maxDistance;
        this.maxWater = maxWater;
        this.color = c;
    }

    public Player(int id, String pseudo, Coord position, PlayerType type) {
        this.id = id;
        this.pseudo = pseudo;
        this.position = position;
        this.type = type;
        this.actionsDone = 0;
        this.waterLevel = 4;
        this.maxActions = 4;
        this.maxDistance = 1;
        this.maxWater = 4;
        this.color = colors[id];
        // TODO : implémenter différents roles
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

    public int getWaterLevel() {
        return this.waterLevel;
    }

    public int getRemainingActions() {
        return this.maxActions - this.actionsDone;
    }

    public ArrayList<Equipment> getEquipment() {
        return this.objects;
    }

    public int increaseWaterLevel(int v) {
        this.waterLevel += v;
        return this.waterLevel;
    }

    public int decreaseWaterLevel(int v) {
        this.waterLevel -= v;
        return this.waterLevel;
    }

    public Coord getPosition() {
        return this.position;
    }

    public void setPosition(Coord c) {
        this.position = c;
    }

    public ArrayList<Coord> getAvailableCells(Board b, PlayerAction action) {
        final ArrayList<Coord> arr = new ArrayList<Coord>();
        if (action == PlayerAction.move) {
            if (this.position.x > 0) {
                final Coord tmp = new Coord(this.position.x -1, this.position.y);
                if (tmp != b.getOeil()) {
                    arr.add(tmp);
                }
            }
            if (this.position.x < 4) {
                final Coord tmp = new Coord(this.position.x + 1, this.position.y);
                if (tmp != b.getOeil()) {
                    arr.add(tmp);
                }
            }
            if (this.position.y > 0) {
                final Coord tmp = new Coord(this.position.x, this.position.y - 1);
                if (tmp != b.getOeil()) {
                    arr.add(tmp);
                }
            }
            if (this.position.y < 4) {
                final Coord tmp = new Coord(this.position.x, this.position.y + 1);
                if (tmp != b.getOeil()) {
                    arr.add(tmp);
                }
            }
        } else if (action == PlayerAction.removeSand) {
            if (this.position.x > 0) {
                final Coord tmp = new Coord(this.position.x -1, this.position.y);
                if (tmp != b.getOeil()) {
                    arr.add(tmp);
                }
            }
            if (this.position.x < 4) {
                final Coord tmp = new Coord(this.position.x + 1, this.position.y);
                if (tmp != b.getOeil()) {
                    arr.add(tmp);
                }
            }
            if (this.position.y > 0) {
                final Coord tmp = new Coord(this.position.x, this.position.y - 1);
                if (tmp != b.getOeil()) {
                    arr.add(tmp);
                }
            }
            if (this.position.y < 4) {
                final Coord tmp = new Coord(this.position.x, this.position.y + 1);
                if (tmp != b.getOeil()) {
                    arr.add(tmp);
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

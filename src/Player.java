import java.awt.*;
import java.util.ArrayList;

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
        return new ArrayList<Coord>();
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

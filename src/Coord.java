public class Coord {
    public int x;
    public int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isEqual(Coord c) {
        return this.x == c.x && this.y == c.y;
    }

    public String toString() {
        return "Coord(x: " + this.x + ", y: " + this.y + ")";
    }
}

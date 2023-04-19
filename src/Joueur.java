import java.awt.*;

public class Joueur {

    private final int id;
    private final String pseudo;
    private int niveauEau;
    private Color color;

    public Joueur(int id, String p, int n, Color c) {
        this.id = id;
        this.pseudo = p;
        this.niveauEau = n;
        this.color = c;
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

    public int getNiveauEau() {
        return this.niveauEau;
    }
}

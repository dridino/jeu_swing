import java.util.ArrayList;

enum CaseType {
    vide,
    sable1,
    sable2,
    oeil,
}

public class Case {
    private CaseType caseType;
    private ArrayList<Joueur> joueurs;

    public Case(CaseType t, ArrayList<Joueur> j) {
        if (j!=null) {
            this.joueurs = j;
        } else {
            this.joueurs = new ArrayList<Joueur>();
        }
        this.caseType = t;
    }

    public CaseType getType() {
        return this.caseType;
    }

    public String toString() {
        return "Case(type: " + this.caseType + ", joueurs: " + this.joueurs.toString() + ")";
    }

    public void setType(CaseType newType) {
        this.caseType = newType;
    }

    public Case copy() {
        return new Case(this.caseType, this.joueurs);
    }

    public void setAttributes(Case c2) {
        this.caseType = c2.caseType;
        this.joueurs = c2.joueurs;
    }

    public void addSand() {
        switch (this.caseType) {
            case vide:
                this.caseType = CaseType.sable1;
                break;
            case sable1:
                this.caseType = CaseType.sable2;
                break;
            default:
                break;
        }
    }

    public Joueur getJoueur(int id) {
        for (Joueur j: this.joueurs) {
            if (j.getId() == id) {
                return j;
            }
        }
        return null;
    }

    public ArrayList<Joueur> getJoueurs() {
        return this.joueurs;
    }

    public void removeJoueur(int id) {
        this.joueurs.removeIf((j) -> j.getId() == id);
    }

    public void addJoueur(Joueur j) {
        this.joueurs.add(j);
    }

    public boolean contientJoueur(int id) {
        for (Joueur j: this.joueurs) {
            if (j.getId() == id) {
                return true;
            }
        }
        return false;
    }
}

import java.awt.*;
import java.util.ArrayList;

enum Direction {
    nord,
    sud,
    est,
    ouest,
}

public class Plateau  extends Observable {
    private final ArrayList<ArrayList<Case>> plateau = new ArrayList<ArrayList<Case>>();
    private double niveauTempete = 2.0;
    private int sableCount = 8;

    public Plateau() {
        ArrayList<Case> tmp = new ArrayList<Case>();
        tmp.add(new Case(CaseType.vide, null));
        tmp.add(new Case(CaseType.vide, null));
        tmp.add(new Case(CaseType.sable1, null));
        tmp.add(new Case(CaseType.vide, null));
        tmp.add(new Case(CaseType.vide, null));
        this.plateau.add(tmp);

        tmp = new ArrayList<Case>();
        tmp.add(new Case(CaseType.vide, null));
        tmp.add(new Case(CaseType.sable1, null));
        tmp.add(new Case(CaseType.vide, null));
        tmp.add(new Case(CaseType.sable1, null));
        tmp.add(new Case(CaseType.vide, null));
        this.plateau.add(tmp);

        tmp = new ArrayList<Case>();
        tmp.add(new Case(CaseType.sable1, null));
        tmp.add(new Case(CaseType.vide, null));
        tmp.add(new Case(CaseType.oeil, null));
        tmp.add(new Case(CaseType.vide, null));
        tmp.add(new Case(CaseType.sable1, null));
        this.plateau.add(tmp);

        final ArrayList<Joueur> arr = new ArrayList<Joueur>();
        arr.add(new Joueur(1, "Adrien", 2, Color.cyan));
        arr.add(new Joueur(2, "Maël", 2, Color.red));

        tmp = new ArrayList<Case>();
        tmp.add(new Case(CaseType.vide, null));
        tmp.add(new Case(CaseType.sable1, null));
        tmp.add(new Case(CaseType.vide, null));
        tmp.add(new Case(CaseType.sable1, arr));
        tmp.add(new Case(CaseType.vide, null));
        this.plateau.add(tmp);

        tmp = new ArrayList<Case>();
        tmp.add(new Case(CaseType.vide, null));
        tmp.add(new Case(CaseType.vide, null));
        tmp.add(new Case(CaseType.sable1, null));
        tmp.add(new Case(CaseType.vide, null));
        tmp.add(new Case(CaseType.vide, null));
        this.plateau.add(tmp);
    }

    public String toString() {
        return "niveauTempete : " + niveauTempete + " plateau : " + plateau.toString();
    }

    public int size() {
        return this.plateau.size();
    }

    public void updateCase(int i, int j, CaseType newType) {
        this.plateau.get(i).get(j).setType(newType);
        this.notifyObservers();
    }

    private void updateSableCount() {
        this.sableCount = 0;
        for (int i = 0; i < this.plateau.size(); i++) {
            for (int j = 0; j < this.plateau.size(); j++) {
                if (this.getCase(i, j).getType() == CaseType.sable1) {
                    this.sableCount += 1;
                } else if (this.getCase(i, j).getType() == CaseType.sable2) {
                    this.sableCount += 2;
                }
            }
        }
    }

    public Case getCase(int i, int j) {
        return this.plateau.get(i).get(j);
    }

    public ArrayList<Integer> getOeil() {
        for (int i = 0; i < this.plateau.size(); i++) {
            for (int j = 0; j < this.plateau.size(); j++) {
                if (this.getCase(i, j).getType() == CaseType.oeil) {
                    final ArrayList<Integer> arr = new ArrayList<Integer>();
                    arr.add(i);
                    arr.add(j);
                    return arr;
                }
            }
        }
        final ArrayList<Integer> arr = new ArrayList<Integer>();
        arr.add(-1);
        arr.add(-1);
        return arr;
    }

    public double getNiveauTempete() {
        return this.niveauTempete;
    }

    public int getSableCount() {
        return this.sableCount;
    }

    private void switchCases(int x1, int y1, int x2, int y2) {
        /// (x2, y2) -> l'oeil
        /// Intervertit les deux cases en ajoutant du sable sur la première
        this.getCase(x1, y1).addSand();
        final Case tmp = this.getCase(x2, y2).copy();
        this.getCase(x2, y2).setAttributes(this.getCase(x1, y1));
        this.getCase(x1, y1).setAttributes(tmp);
    }

    public void avanceTour() {
        for (int i = 0; i < this.niveauTempete; i++) {
            final double p = Math.random();
            // 1 chance sur 2 de faire souffler le vent
            if (p < 0.5) {
                // le vent souffle
                final double dirP = Math.random();
                ArrayList<Integer> oeilPos = this.getOeil();
                // 1 <= force <= 3
                final int force = (int) (Math.floor(Math.random()*2.9) + 1);

                System.out.print(force + " ");
                if (dirP < 0.25) {
                    // sud
                    System.out.print("sud\n");

                    final int maxValue = Math.max((int) Math.floor(oeilPos.get(0)-force), 0);

                    while (oeilPos.get(0) > maxValue) {
                        this.switchCases(oeilPos.get(0) - 1, oeilPos.get(1), oeilPos.get(0), oeilPos.get(1));
                        oeilPos = this.getOeil();
                    }
                } else if (dirP < 0.5) {
                    // nord
                    System.out.print("nord\n");

                    final int maxValue = Math.min((int) Math.floor(oeilPos.get(0)+force), this.plateau.size() - 1);

                    while (oeilPos.get(0) < maxValue) {
                        this.switchCases(oeilPos.get(0) + 1, oeilPos.get(1), oeilPos.get(0), oeilPos.get(1));
                        oeilPos = this.getOeil();
                    }

                } else if (dirP < 0.75) {
                    // est
                    System.out.print("est\n");

                    final int maxValue = Math.max((int) Math.floor(oeilPos.get(1)-force), 0);

                    while (oeilPos.get(1) > maxValue) {
                        this.switchCases(oeilPos.get(0), oeilPos.get(1) - 1, oeilPos.get(0), oeilPos.get(1));
                        oeilPos = this.getOeil();
                    }

                } else {
                    // est
                    System.out.print("ouest\n");

                    final int maxValue = Math.min((int) Math.floor(oeilPos.get(1)+force), this.plateau.size() - 1);

                    while (oeilPos.get(1) < maxValue) {
                        this.switchCases(oeilPos.get(0), oeilPos.get(1) + 1, oeilPos.get(0), oeilPos.get(1));
                        oeilPos = this.getOeil();
                    }
                }

            } else {
                this.niveauTempete += 0.5;
            }
        }
        this.updateSableCount();
        notifyObservers();
        if (this.sableCount >= 43) {
            // écrit en rouge
            System.out.println("\u001B[31m! Fin du jeu par ensablement ! \u001B[0m");
        } else if (this.niveauTempete > 7) {
            System.out.println("\u001B[31m! Fin du jeu, la tempête est trop puissante ! \u001B[0m");
        }
    }

    private ArrayList<Integer> getCaseJoueur(int id) {
        for (int i = 0; i < this.plateau.size(); i++) {
            for (int j = 0; j < this.plateau.size(); j++) {
                if (this.getCase(i, j).contientJoueur(id)) {
                    final ArrayList<Integer> arr = new ArrayList<Integer>();
                    arr.add(i);
                    arr.add(j);
                    return arr;
                }
            }
        }
        final ArrayList<Integer> arr = new ArrayList<Integer>();
        arr.add(-1);
        arr.add(-1);
        return arr;
    }

    public void bougeJoueur(int id, Direction dir) {
        final ArrayList<Integer> coords = this.getCaseJoueur(id);
        switch (dir) {
            case nord:
                if (coords.get(0) > 0) {
                    final Joueur j1 = this.getCase(coords.get(0), coords.get(1)).getJoueur(id);
                    this.getCase(coords.get(0), coords.get(1)).removeJoueur(id);
                    this.getCase(coords.get(0) - 1, coords.get(1)).addJoueur(j1);
                }
                break;
            case sud:
                if (coords.get(0) < this.plateau.size() - 1) {
                    final Joueur j1 = this.getCase(coords.get(0), coords.get(1)).getJoueur(id);
                    this.getCase(coords.get(0), coords.get(1)).removeJoueur(id);
                    this.getCase(coords.get(0) + 1, coords.get(1)).addJoueur(j1);
                }
                break;
            case est:
                if (coords.get(1) < this.plateau.size() - 1) {
                    final Joueur j1 = this.getCase(coords.get(0), coords.get(1)).getJoueur(id);
                    this.getCase(coords.get(0), coords.get(1)).removeJoueur(id);
                    this.getCase(coords.get(0), coords.get(1) + 1).addJoueur(j1);
                }
                break;
            case ouest:
                if (coords.get(1) > 0) {
                    final Joueur j1 = this.getCase(coords.get(0), coords.get(1)).getJoueur(id);
                    this.getCase(coords.get(0), coords.get(1)).removeJoueur(id);
                    this.getCase(coords.get(0), coords.get(1) - 1).addJoueur(j1);
                }
                break;
        }
    }
}
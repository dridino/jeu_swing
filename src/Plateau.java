import java.util.ArrayList;
import java.util.Random;

public class Plateau  extends Observable {
    private final ArrayList<ArrayList<Case>> plateau = new ArrayList<ArrayList<Case>>();
    private double niveauTempete = 2.0;

    public Plateau() {
        ArrayList<Case> tmp = new ArrayList<Case>();
        tmp.add(new Case(CaseType.vide));
        tmp.add(new Case(CaseType.vide));
        tmp.add(new Case(CaseType.sable1));
        tmp.add(new Case(CaseType.vide));
        tmp.add(new Case(CaseType.vide));
        this.plateau.add(tmp);

        tmp = new ArrayList<Case>();
        tmp.add(new Case(CaseType.vide));
        tmp.add(new Case(CaseType.sable1));
        tmp.add(new Case(CaseType.vide));
        tmp.add(new Case(CaseType.sable1));
        tmp.add(new Case(CaseType.vide));
        this.plateau.add(tmp);

        tmp = new ArrayList<Case>();
        tmp.add(new Case(CaseType.sable1));
        tmp.add(new Case(CaseType.vide));
        tmp.add(new Case(CaseType.oeil));
        tmp.add(new Case(CaseType.vide));
        tmp.add(new Case(CaseType.sable1));
        this.plateau.add(tmp);

        tmp = new ArrayList<Case>();
        tmp.add(new Case(CaseType.vide));
        tmp.add(new Case(CaseType.sable1));
        tmp.add(new Case(CaseType.vide));
        tmp.add(new Case(CaseType.sable1));
        tmp.add(new Case(CaseType.vide));
        this.plateau.add(tmp);

        tmp = new ArrayList<Case>();
        tmp.add(new Case(CaseType.vide));
        tmp.add(new Case(CaseType.vide));
        tmp.add(new Case(CaseType.sable1));
        tmp.add(new Case(CaseType.vide));
        tmp.add(new Case(CaseType.vide));
        this.plateau.add(tmp);
    }

    public String toString() {
        return "niveauTempete : " + niveauTempete + " plateau : " + plateau.toString();
    }

    public int size() {
        return this.plateau.size();
    }

    public void updateCase(int i, int j, CaseType newType) {
        this.plateau.get(i).get(j).changeType(newType);
        this.notifyObservers();
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

    public void avanceTour() {
        for (int i = 0; i < this.niveauTempete; i++) {
            final double p = Math.random();
            // 1 chance sur 2 de faire souffler le vent
            if (p < 0.5) {
                // le vent souffle
                final double dirP = Math.random();
                final int direction;

                ArrayList<Integer> oeilPos = this.getOeil();

                System.out.print(oeilPos);

                // 1 <= force <= 3
                final int force = (int) (Math.floor(Math.random()*2.9) + 1);

                if (dirP < 0.25) {
                    // nord
                    System.out.print("nord\n");
                    direction = -1;

                    int j = oeilPos.get(0);
                    final int maxVal = Math.max(0, oeilPos.get(0) - force)+1;

                    while (j >= maxVal) {
                        // permutation
                        final Case tmp = this.plateau.get(oeilPos.get(0)-1).get(oeilPos.get(1));
                        this.plateau.get(oeilPos.get(0)-1).set(oeilPos.get(1), this.plateau.get(oeilPos.get(0)).get(oeilPos.get(1)));
                        this.plateau.get(oeilPos.get(0)).set(oeilPos.get(1), tmp);
                        this.getCase(oeilPos.get(0), oeilPos.get(1)).addSand();
                        oeilPos = this.getOeil();
                        j--;
                    }

                } else if (dirP < 0.5) {
                    // sud
                    System.out.print("sud\n");
                    direction = 1;

                    int j = oeilPos.get(0);
                    final int maxVal = Math.min(this.plateau.size(), oeilPos.get(0) + force)-1;

                    while (j <= maxVal) {
                        // permutation
                        final Case tmp = this.plateau.get(oeilPos.get(0)+1).get(oeilPos.get(1));
                        this.plateau.get(oeilPos.get(0)+1).set(oeilPos.get(1), this.plateau.get(oeilPos.get(0)).get(oeilPos.get(1)));
                        this.plateau.get(oeilPos.get(0)).set(oeilPos.get(1), tmp);
                        this.getCase(oeilPos.get(0), oeilPos.get(1)).addSand();
                        oeilPos = this.getOeil();
                        j++;
                    }
                } else if (dirP < 0.75) {
                    // ouest
                    System.out.print("ouest\n");
                    direction = 2;

                    int j = oeilPos.get(1);
                    final int maxVal = Math.min(this.plateau.size(), oeilPos.get(1) + force)-1;

                    while (j <= maxVal) {
                        // permutation
                        final Case tmp = this.plateau.get(oeilPos.get(0)).get(oeilPos.get(1)+1);
                        this.plateau.get(oeilPos.get(0)).set(oeilPos.get(1)+1, this.plateau.get(oeilPos.get(0)).get(oeilPos.get(1)));
                        this.plateau.get(oeilPos.get(0)).set(oeilPos.get(1), tmp);
                        this.getCase(oeilPos.get(0), oeilPos.get(1)).addSand();
                        oeilPos = this.getOeil();
                        j++;
                    }
                } else {
                    // est
                    System.out.print("est\n");
                    direction = -2;
                    int j = oeilPos.get(1);
                    final int maxVal = Math.max(0, oeilPos.get(1) - force)+1;

                    while (j >= maxVal) {
                        // permutation
                        final Case tmp = this.plateau.get(oeilPos.get(0)).get(oeilPos.get(1)-1);
                        this.plateau.get(oeilPos.get(0)).set(oeilPos.get(1)-1, this.plateau.get(oeilPos.get(0)).get(oeilPos.get(1)));
                        this.plateau.get(oeilPos.get(0)).set(oeilPos.get(1), tmp);
                        this.getCase(oeilPos.get(0), oeilPos.get(1)).addSand();
                        oeilPos = this.getOeil();
                        j--;
                    }
                }

            } else {
                this.niveauTempete += 0.5;
            }
            notifyObservers();
        }
    }
}
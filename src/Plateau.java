import java.util.ArrayList;

public class Plateau  extends Observable {
    private final ArrayList<ArrayList<Case>> plateau = new ArrayList<ArrayList<Case>>();

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
        return plateau.toString();
    }

    public void updateCase(int i, int j, CaseType newType) {
        this.plateau.get(i).get(j).changeType(newType);
        this.notifyObservers();
    }

    public Case getCase(int i, int j) {
        return this.plateau.get(i).get(j);
    }
}
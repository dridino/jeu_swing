
enum CaseType {
    vide,
    sable1,
    sable2,
    oeil,
}

public class Case {
    private CaseType caseType;

    public Case(CaseType t) {
        this.caseType = t;
    }

    public CaseType getType() {
        return this.caseType;
    }

    public String toString() {
        return "Case(type: " + this.caseType + ")";
    }

    public void setType(CaseType newType) {
        this.caseType = newType;
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
}

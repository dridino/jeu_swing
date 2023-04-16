
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

    public void changeType(CaseType newType) {
        this.caseType = newType;
    }
}

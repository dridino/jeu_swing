public class Equipment {
    private String name;
    private ObjectType type;

    public Equipment(ObjectType type) {
        this.type = type;

        switch (type) {
            case BOUCLIER:
                this.name = "Shield";
                break;
            case BLASTER:
                this.name = "Blaster";
                break;
            case JETPACK:
                this.name = "JetPack";
                break;
            case X_RAY:
                this.name = "XRay";
                break;
            case ACCELERATEUR_DE_TEMPS:
                this.name = "Time";
                break;
            case RESERVE_D_EAU:
                this.name = "Water";
                break;
        }
    }

    public ObjectType getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }
}

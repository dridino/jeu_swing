public class Equipment {
    private String name;
    private ObjectType type;

    public Equipment(ObjectType type) {
        this.type = type;

        switch (type) {
            case shield:
                this.name = "Shield";
                break;
            case blaster:
                this.name = "Blaster";
                break;
            case jetpack:
                this.name = "JetPack";
                break;
            case xRay:
                this.name = "XRay";
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

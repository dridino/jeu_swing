public class Role {
    private final PlayerType playerType;
    private final String name;
    private final String description;

    public Role(PlayerType type) {
        this.playerType = type;
        switch (type) {
            case EXPLORATEUR:
                this.name = "Explorateur";
                this.description = "Je suis un explorateur";
                break;
            case ALPINISTE:
                this.name = "Alpiniste";
                this.description = "Je suis un alpiniste";
                break;
            case ARCHEOLOGUE:
                this.name = "Archéologue";
                this.description = "Je suis un archéologue";
                break;
            case METEOROLOGUE:
                this.name = "Météorologue";
                this.description = "Je suis un météorologue";
                break;
            case NAVIGATEUR:
                this.name = "Navigateur";
                this.description = "Je suis un navigateur";
                break;
            default:
                this.name = "Porteuse d'eau";
                this.description = "Je suis une porteuse d'eau";
                break;
        }
    }

    public String getDescription() {
        return this.description;
    }
}

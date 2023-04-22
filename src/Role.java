public class Role {
    private final PlayerType playerType;
    private final String name;
    private final String description;

    public Role(PlayerType type) {
        this.playerType = type;
        switch (type) {
            case explo:
                this.name = "Explorateur";
                this.description = "Je suis un explorateur";
                break;
            case alpinist:
                this.name = "Alpiniste";
                this.description = "Je suis un alpiniste";
                break;
            case archeo:
                this.name = "Archéologue";
                this.description = "Je suis un archéologue";
                break;
            case meteo:
                this.name = "Météorologue";
                this.description = "Je suis un météorologue";
                break;
            case navig:
                this.name = "Navigateur";
                this.description = "Je suis un navigateur";
                break;
            default:
                this.name = "Porteuse d'eau";
                this.description = "Je suis une porteuse d'eau";
                break;
        }
    }

    public String getName() {
        return this.name;
    }

    public PlayerType getPlayerType() {
        return this.playerType;
    }

    public String getDescription() {
        return this.description;
    }
}

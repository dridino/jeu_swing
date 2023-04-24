enum CellType {empty, sand1, sand2, eye}

enum CellContent {oasis, mirage, tunnel, crash, takeoff, clueRowEngine, clueRowWheel, clueRowEnergy, clueRowRotor, clueColumnEngine, clueColumnWheel, clueColumnEnergy, clueColumnRotor, engine, wheel, energy, rotor, equipment, none};
enum PieceType {engine, wheel, energy, rotor}
enum DeplacementType {straight, diagonal}

enum PlayerType {ARCHEOLOGUE, ALPINISTE, EXPLORATEUR, METEOROLOGUE, NAVIGATEUR, PORTEUSE_D_EAU}

enum ObjectType {JETPACK, BOUCLIER, BLASTER, X_RAY, ACCELERATEUR_DE_TEMPS, RESERVE_D_EAU, none}

enum PlayerAction {removeSand, move, discover, pickUp, giveWater}

enum StormAction {east1, east2, east3, west1, west2, west3, north1, north2, north3, south1, south2, south3, VAGUE_CHALEUR, DECHAINE}

enum Turn {player, storm, equipment};

enum ScreenType {
    numberOfPlayers,
    playerSelection,
    game,
    end,
}

enum DeckType {
    playerType,
    stormAction,
    equipmentType,
    cellContent,
    defausse,
}

enum GameResults {win, loose, none}
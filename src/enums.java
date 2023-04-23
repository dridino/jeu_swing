enum CellType {empty, sand1, sand2, eye}

enum CellContent {oasis, mirage, tunnel, crash, takeoff, clueRowEngine, clueRowWheel, clueRowEnergy, clueRowRotor, clueColumnEngine, clueColumnWheel, clueColumnEnergy, clueColumnRotor, equipment, none};

enum PlayerType {archeo, alpinist, explo, meteo, navig, water}

enum ObjectType {jetpack, shield, blaster, xRay, time, water, none}

enum PlayerAction {removeSand, move, discover, pickUp}

enum StormAction {east1, east2, east3, west1, west2, west3, north1, north2, north3, south1, south2, south3, heatwave, unleash}

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
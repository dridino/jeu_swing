enum CellType {empty, sand1, sand2, eye}

enum CellContent {oasis, mirage, tunnel, crash, takeoff, clueRow, clueColumn, equipment, none};

enum PlayerType {archeo, alpinist, explo, meteo, navig, water}

enum ObjectType {jetpack, shield, blaster, xRay, time, water, none}

enum PlayerAction {removeSand, move, discover, pickUp}

enum StormAction {sandStorm, unleash, heatWave}

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
}
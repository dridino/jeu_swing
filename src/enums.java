enum CellType {empty, sand1, sand2, eye, oasis, mirage, tunnel, crash, takeoff, clue}

enum PlayerType {archeo, alpinist, explo, meteo, navig, water}

enum ObjectType {jetpack, shield, blaster, xRay}

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
}
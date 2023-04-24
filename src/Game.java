import java.util.ArrayList; 
import java.util.Collections;

public class Game extends Observable implements Observer {
    private Board board;
    private ArrayList<Player> players = new ArrayList<Player>();
    private int currentPlayer = 0;
    private int numberOfPlayers = 0;
    private int pickedCards = 0;
    private ScreenType currentScreen = ScreenType.numberOfPlayers;
    private Turn turn = Turn.player;
    private final Deck<StormAction> defausse = new Deck<StormAction>(new ArrayList<StormAction>(), DeckType.defausse);
    private final Deck<ObjectType> equipmentDeck;
    private GameResults gameResults = GameResults.none;
    private PlayerType playerType;
    private String playerPseudo;
    private final ArrayList<PieceType> piecesPicked = new ArrayList<PieceType>();
    private final Deck<PlayerType> playerTypeDeck;

    public Game(ArrayList<Player> players) {
        for (Player p: players) {
            p.addObserver(this);
        }
        final ArrayList<PlayerType> arr1 = new ArrayList<PlayerType>();
        Collections.addAll(arr1, PlayerType.values());
        this.playerTypeDeck = new Deck<PlayerType>(arr1, DeckType.playerType);
        final ArrayList<ObjectType> arr = new ArrayList<ObjectType>();
        Collections.addAll(arr, ObjectType.BLASTER,
                ObjectType.BLASTER,
                ObjectType.BLASTER,

                ObjectType.JETPACK,
                ObjectType.JETPACK,
                ObjectType.JETPACK,

                ObjectType.BOUCLIER,
                ObjectType.BOUCLIER,

                ObjectType.X_RAY,
                ObjectType.X_RAY,

                ObjectType.ACCELERATEUR_DE_TEMPS,

                ObjectType.RESERVE_D_EAU);
        this.equipmentDeck = new Deck<ObjectType>(arr, DeckType.equipmentType);
        this.board = new Board(players);
        this.board.addObserver(this);
        this.players = players;
        this.defausse.addObserver(this);
    }

    @Override
    public void update() {
        this.notifyObservers();
    }

    public Deck<StormAction> getDefausse() {
        return defausse;
    }

    public void increasePickCards() {
        this.pickedCards++;
        notifyObservers();
    }

    public Deck<PlayerType> getPlayerTypeDeck() {
        return this.playerTypeDeck;
    }

    public int getPickedCards() {
        return this.pickedCards;
    }

    public void resetPickedCards() {
        this.pickedCards = 0;
        notifyObservers();
    }

    public ScreenType getCurrentScreen() {
        return this.currentScreen;
    }

    public void nextScreen() {
        switch (this.currentScreen) {
            case numberOfPlayers:
                this.currentScreen = ScreenType.playerSelection;
                break;
            case playerSelection:
                this.board.positionPlayers(this.players);
                this.currentScreen = ScreenType.game;
                break;
            case game:
                this.currentScreen = ScreenType.end;
                break;
            default:
                break;
        }
        notifyObservers();
    }

    private void updateGameResult() {
        if (this.board.getSandLevel() > 43 || this.board.getStormLevel() >= 7) {
            this.gameResults = GameResults.loose;
        }
        for (Player p: this.players) {
            if (p.getWaterLevel() == 0) {
                this.gameResults = GameResults.loose;
                break;
            }
        }

        if (this.gameResults == GameResults.loose) {
            this.nextScreen();
        } else {
            if (this.victory()) {
                this.gameResults = GameResults.win;
                this.nextScreen();
            }
        }
        this.notifyObservers();
    }

    public GameResults getGameResults() {
        return this.gameResults;
    }

    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    public void setNumberOfPlayers(int newVal) {
        this.numberOfPlayers = newVal;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public void addPlayer(Player p) {
        this.players.add(p);
        if (p.getPosition() != null) {
            this.board.getCell(p.getPosition()).addPlayer(p.getId());
        }
        p.addObserver(this);
    }

    public Coord movePlayer(int id, Coord c) {
        final Player player = this.players.get(id);
        final Coord oldPos = player.getPosition();
        if (player.getRemainingActions() == 0) {
            return oldPos;
        }
        player.setPosition(c);
        player.increaseActions(PlayerAction.move);
        this.board.movePlayer(oldPos, c, id);
        if (player.getRemainingActions() == 0) {
            this.endOfTurn();
        }
        this.updateGameResult();
        notifyObservers();
        return c;
    }

    public void removeSand(Coord c) {
        final Player player = this.players.get(this.currentPlayer);
        if (player.getRemainingActions() == 0) {
            return;
        }
        player.increaseActions(PlayerAction.removeSand);
        this.board.removeSand(c, player.getMaxSandRemove());
        if (player.getRemainingActions() == 0) {
            this.endOfTurn();
        }
        this.updateGameResult();
        notifyObservers();
    }

    public Board getBoard() {
        return this.board;
    }

    public int getCurrentPlayerId() {
        return this.currentPlayer;
    }

    public Player getCurrentPlayer() {
        return this.players.get(this.currentPlayer);
    }

    public void endOfTurn() {
        this.turn = Turn.storm;
        this.pickedCards = 0;
        notifyObservers();
    }

    public boolean isStormTurn() {
        return this.turn == Turn.storm;
    }

    public boolean isPlayerTurn() {
        return this.turn == Turn.player;
    }

    public boolean isEquipmentTurn() {
        return this.turn == Turn.equipment;
    }

    public void heatWave() {
        for (Player p: this.players) {
            final Coord pCoord = p.getPosition();
            if (this.board.getCell(pCoord).getContent() != CellContent.tunnel || !this.board.getCell(pCoord).isExplored()) {
                p.decreaseWaterLevel(0.5);
            }
        }
        this.updateGameResult();
    }

    public void handleStormEvents(StormAction action) {
        this.defausse.add(action);
        this.increasePickCards();
        if (action == StormAction.VAGUE_CHALEUR) {
            this.heatWave();
        }
        this.board.handleStormEvents(action, this.getCurrentPlayer());
        this.updateGameResult();
        this.notifyObservers();
        if (this.pickedCards == Math.floor(this.board.getStormLevel())) {
            this.newTurn();
        }
    }

    public void newTurn() {
        this.players.get(this.currentPlayer).resetActions();
        this.currentPlayer = (this.currentPlayer + 1)%this.numberOfPlayers;
        this.turn = Turn.player;
        notifyObservers();
    }

    public PieceType canPickPiece() {
        return this.board.updatePieceCoord(this.getCurrentPlayer().getPosition());
    }

    public void addWater() {
        final Cell currentCell = this.board.getCell(this.getCurrentPlayer().getPosition());
        if (currentCell.isExplored() && currentCell.getContent() == CellContent.oasis) {
            for (Player p: this.players) {
                if (!currentCell.isWaterConsumed() || this.getCurrentPlayer().getType() == PlayerType.PORTEUSE_D_EAU) {
                    p.addWater();
                }
            }
            currentCell.consumeWater();
        }
    }

    public CellContent explore() {
        final Player player = this.players.get(this.currentPlayer);
        this.board.getCell(player.getPosition()).explore();
        player.increaseActions(PlayerAction.discover);
        if (player.getRemainingActions() == 0) {
            this.endOfTurn();
        }
        this.board.updatePiece();
        this.addWater();
        this.updateGameResult();
        notifyObservers();
        return this.board.getCell(player.getPosition()).getContent();
    }

    public PieceType pick() {
        final PieceType piece = this.canPickPiece();
        if (piece != null) {
            this.piecesPicked.add(piece);
            this.getCurrentPlayer().addPiece(piece);
            notifyObservers();
            return piece;
        }
        return null;
    }

    public boolean victory() {
        Coord takeOffCoord = new Coord(0, 0);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (this.board.getCell(new Coord(i, j)).getType() != CellType.empty) {
                    return false;
                }
                if (this.board.getCell(new Coord(i, j)).getContent() == CellContent.takeoff) {
                    takeOffCoord = new Coord(i, j);
                    break;
                }
            }
        }

        for (Player p: this.players) {
            if (!p.getPosition().isEqual(takeOffCoord)) {
                return false;
            }
        }

        return this.piecesPicked.size() == 4;
    }

    public void setTurn(Turn v) {
        this.turn = v;
        notifyObservers();
    }

    public Deck<ObjectType> getEquipmentDeck() {
        return this.equipmentDeck;
    }

    public void resetDefausse() {
        this.defausse.reset();
        notifyObservers();
    }

    public Equipment pickEquipment() {
        if (this.equipmentDeck.isNotEmpty()) {
            final Equipment equipment = new Equipment(this.equipmentDeck.pick());
            final Player player = this.players.get(this.currentPlayer);
            player.addEquipment(equipment);
            this.turn = player.getRemainingActions() == 0 ? Turn.storm : Turn.player;
            notifyObservers();
            return equipment;
        }
        return null;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public void setPlayerPseudo(String playerPseudo) {
        this.playerPseudo = playerPseudo;
    }

    public PlayerType getPlayerType() {
        return this.playerType;
    }

    public String getPlayerPseudo() {
        return this.playerPseudo;
    }
}
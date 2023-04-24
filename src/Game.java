import java.util.ArrayList; 
import java.util.Collections;
import java.util.List;

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

    public Game(ArrayList<Player> players) {
        for (Player p: players) {
            p.addObserver(this);
        }
        final ArrayList<ObjectType> arr = new ArrayList<ObjectType>();
        Collections.addAll(arr, ObjectType.blaster,
                ObjectType.blaster,
                ObjectType.blaster,

                ObjectType.jetpack,
                ObjectType.jetpack,
                ObjectType.jetpack,

                ObjectType.shield,
                ObjectType.shield,

                ObjectType.xRay,
                ObjectType.xRay,

                ObjectType.time,

                ObjectType.water);
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
            boolean samePos = true;
            for (Player p: this.players) {
                if (this.board.getCell(p.getPosition()).getContent() != CellContent.takeoff) {
                    samePos = false;
                    break;
                }
            }
            if (samePos) {
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
        notifyObservers();
        this.updateGameResult();
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

    /* public void endOfTurn() {
        for (int i = 0; i < this.board.getStormLevel(); i++) {
            final double p = Math.random();
            // 1 chance sur 2 de faire souffler le vent
            if (p < 0.5) {
                this.board.sandstorm();
            } else {
                this.board.increaseStormLevel();
            }
        }
        this.board.updateSandLevel();
        notifyObservers();
        if (this.board.getSandLevel() >= 43) {
            // écrit en rouge
            System.out.println("\u001B[31m! Fin du jeu par ensablement ! \u001B[0m");
        } else if (this.board.getStormLevel() > 7) {
            System.out.println("\u001B[31m! Fin du jeu, la tempête est trop puissante ! \u001B[0m");
        }
    }*/

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
        if (action == StormAction.heatwave) {
            this.heatWave();
        }
        this.board.handleStormEvents(action);
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

    private boolean inLine(CellContent content) {
        for (int j = 0; j < 5; j++) {
            if (this.board.getCell(new Coord(this.getCurrentPlayer().getPosition().x, j)).getContent() == content) {
                return true;
            }
        }
        return false;
    }

    private boolean inColumn(CellContent content) {
        for (int i = 0; i < 5; i++) {
            if (this.board.getCell(new Coord(i, this.getCurrentPlayer().getPosition().y)).getContent() == content) {
                return true;
            }
        }
        return false;
    }

    public CellContent explore() {
        final Player player = this.players.get(this.currentPlayer);
        this.board.getCell(player.getPosition()).explore();
        player.increaseActions(PlayerAction.discover);
        if (player.getRemainingActions() == 0) {
            this.endOfTurn();
        }
        notifyObservers();
        return this.board.getCell(player.getPosition()).getContent();
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
            player.addObject(equipment);
            this.turn = player.getRemainingActions() == 0 ? Turn.storm : Turn.player;
            notifyObservers();
            return equipment;
        }
        return null;
    }
}
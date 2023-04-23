import java.util.ArrayList;
import java.util.List;

public class Game extends Observable implements Observer {
    private Board board;
    private ArrayList<Player> players = new ArrayList<Player>();
    private int currentPlayer = 0;

    private int numberOfPlayers = 0;

    private ScreenType currentScreen = ScreenType.numberOfPlayers;

    public Game(ArrayList<Player> players) {
        for (Player p: players) {
            p.addObserver(this);
        }
        this.board = new Board(players);
        this.board.addObserver(this);
        this.players = players;
    }

    @Override
    public void update() {
        this.notifyObservers();
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

    // TODO: implémenter
    public Coord movePlayer(int id, Coord c) {
        final Player player = this.players.get(id);
        final Coord oldPos = player.getPosition();
        if (player.getRemainingActions() == 0) {
            return oldPos;
        }
        player.setPosition(c);
        player.increaseActions(PlayerAction.move);
        this.board.movePlayer(oldPos, c, id);
        notifyObservers();
        return c;
    }

    public void removeSand(Coord c) {
        final Player player = this.players.get(this.currentPlayer);
        if (player.getRemainingActions() == 0) {
            return;
        }
        player.increaseActions(PlayerAction.removeSand);
        this.board.getCell(c).removeSand();
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
    }
}
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

interface Observer {
    public void update();
}

abstract class Observable {
    private final ArrayList<Observer> observers;

    public Observable() {
        this.observers = new ArrayList<Observer>();
    }

    public void addObserver(Observer o) {
        this.observers.add(o);
    }

    public void notifyObservers() {
        for (Observer o: observers) {
            o.update();
        }
    }
}



public class Vue implements Observer {
    public static final Integer TAILLE = 5;

    private final JFrame frame;
    private Game game;

    public Vue(Game g) {
        this.game = g;
        g.addObserver(this);
        this.frame = new JFrame();
        this.frame.setFocusable(true);
        this.frame.setTitle("Le desert interdit");
        this.frame.setLayout(new GridBagLayout());
        this.build();
        this.frame.pack();
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setMinimumSize(new Dimension(1200, 800));
        frame.getContentPane().setBackground(Color.white);
        this.frame.setVisible(true);
    }

    private void build() {
        switch (this.game.getCurrentScreen()) {
            case numberOfPlayers:
                this.frame.getContentPane().add(new FirstScreen(this.game), new GridBagConstraints());
                break;
            case playerSelection:
                this.frame.getContentPane().add(new SecondScreen(this.game), new GridBagConstraints());
                break;
            case game:
                this.frame.getContentPane().add(new ThirdScreen(this.game), new GridBagConstraints());
                break;
            case end:
                this.frame.getContentPane().add(new FinalScreen(this.game), new GridBagConstraints());
                break;
        }
    }

    public void update() {
        this.frame.getContentPane().removeAll();
        this.build();
        this.frame.revalidate();
        this.frame.repaint();
    }
}

class FirstScreen extends JPanel {
    private final Game game;

    public FirstScreen(Game g) {
        this.game = g;
        this.build();
    }

    public void build() {
        this.setLayout(new FlowLayout());
        this.setBackground(Color.white);

        final JTextArea text = new JTextArea("Nombre de joueurs : ");
        text.setEditable(false);
        this.add(text);

        final JSlider slider = new JSlider(2, 5, 2);
        slider.setMajorTickSpacing(1);
        slider.setPaintTrack(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setBackground(Color.white);
        this.add(slider);

        final JButton button = new JButton("Suivant");
        final ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.setNumberOfPlayers(slider.getValue());
                game.nextScreen();
            }
        };
        button.addActionListener(actionListener);
        this.add(button);
    }
}

class SecondScreen extends JPanel {
    private final Game game;
    private String pseudo;

    private JTextArea text;
    private JTextField textField;
    private DeckLayout<PlayerType> deckLayout;
    private JButton button;
    private PlayerType playerType;

    public SecondScreen(Game g) {
        this.game = g;
        this.build();
    }

    public void build() {
        this.setLayout(new FlowLayout());
        this.setBackground(Color.white);

        // TEXT
        this.text = new JTextArea("Nom Joueur : ");
        this.text.setEditable(false);
        this.add(text);

        // TEXTFIELD
        this.textField = new JTextField(game.getPlayerPseudo());
        this.textField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                game.setPlayerPseudo(textField.getText());
            }
            public void removeUpdate(DocumentEvent e) {
                game.setPlayerPseudo(textField.getText());
            }
            public void insertUpdate(DocumentEvent e) {
                game.setPlayerPseudo(textField.getText());
            }
        });
        this.textField.setPreferredSize(new Dimension(200, 25));
        this.textField.setMaximumSize(new Dimension(200, 25));
        this.add(this.textField);

        // DECK
        final MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);
                if (game.getPlayerType() == null) {
                    game.setPlayerType(game.getPlayerTypeDeck().pick());
                    final Role role = new Role(game.getPlayerType());
                    JOptionPane.showMessageDialog(null, role.getDescription(), "Pioche", JOptionPane.INFORMATION_MESSAGE);
                    game.update();
                }
            }
        };
        this.deckLayout = new DeckLayout<PlayerType>(this.game.getPlayerTypeDeck(), mouseAdapter, "Paquet", "Aventurier");
        this.add(this.deckLayout);

        // BUTTON
        final JButton button = new JButton("Suivant");
        button.setEnabled(this.game.getPlayerType() != null);
        final ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.addPlayer(new Player(game.getPlayers().size(), game.getPlayerPseudo(), null, game.getPlayerType()));
                if (game.getPlayers().size() < game.getNumberOfPlayers()) {
                    game.setPlayerPseudo(null);
                    game.setPlayerType(null);
                    game.update();
                } else {
                    game.nextScreen();
                }
            }
        };
        button.addActionListener(actionListener);
        this.add(button);
    }
}

class DeckLayout<E> extends JPanel {
    private final Deck<E> deck;
    private final Color color;
    private final String title1;
    private final String title2;

    public DeckLayout(Deck<E> deck, MouseAdapter mouseAdapter, String title1, String title2) {
        this.deck = deck;
        switch (deck.getType()) {
            case playerType:
                this.color = Color.green;
                break;
            case equipmentType:
                this.color = Color.blue;
                break;
            case stormAction:
                this.color = Color.red;
                break;
            default:
                this.color = Color.black;
                break;
        }
        this.title1 = title1;
        this.title2 = title2;
        addMouseListener(mouseAdapter);
        this.setPreferredSize(new Dimension(100 + 4, 150 + 4));
    }

    public void paintComponent(Graphics g) {
        final Graphics2D g2 = (Graphics2D) g;
        float thickness = 4;
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(thickness));

        g2.setColor(Color.white);
        g2.fillRoundRect(2, 2, 100, 150, 5, 5);

        g2.setColor(this.color);
        g2.drawRoundRect(2, 2, 100, 150, 5, 5);

        g2.setStroke(oldStroke);

        g2.setColor(Color.black);
        g2.setFont(new Font("default", Font.BOLD, 12));
        g2.drawString(this.title1, 2 + 4, 18);
        g2.drawString(this.title2, 2 + 4, 38);
    }
}

class ThirdScreen extends JPanel {
    private final Game game;

    public ThirdScreen(Game game) {
        this.game = game;
        this.build();
    }

    private void build() {
        this.setLayout(new FlowLayout());
        this.setBackground(Color.white);
        this.add(new ThirdScreenLeftPanel(this.game));
        this.add(new ThirdScreenMiddlePanel(this.game));
        this.add(new ThirdScreenRightPanel(this.game));
    }
}

class GridPart extends JPanel {
    /** On maintient une référence vers le modèle. */
    private final Game game;
    /** Définition d'une taille (en pixels) pour l'affichage des cellules. */
    private final static int TAILLE = 100;

    /** Constructeur. */
    public GridPart(Game g) {
        this.game = g;
        /**
         * Définition et application d'une taille fixe pour cette zone de
         * l'interface, calculée en fonction du nombre de cellules et de la
         * taille d'affichage.
         */
        Dimension dim = new Dimension(TAILLE*Vue.TAILLE,
                TAILLE*Vue.TAILLE);
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (game.isPlayerTurn()) {
                    final Coord coord = new Coord(e.getPoint().y / TAILLE, e.getPoint().x / TAILLE);
                    if (e.getModifiersEx() == 0) {
                        if (game.getCurrentPlayer().isCellAvailable(game.getBoard(), PlayerAction.move, coord)) {
                            game.movePlayer(game.getCurrentPlayerId(), coord);
                        }
                    } else if (e.getModifiersEx() == 256) {
                        if (game.getCurrentPlayer().isCellAvailable(game.getBoard(), PlayerAction.removeSand, coord)) {
                            game.removeSand(coord);
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        this.setPreferredSize(dim);
    }

    /**
     * Les éléments graphiques comme [JPanel] possèdent une méthode
     * [paintComponent] qui définit l'action à accomplir pour afficher cet
     * élément. On la redéfinit ici pour lui confier l'affichage des cellules.
     *
     * La classe [Graphics] regroupe les éléments de style sur le dessin,
     * comme la couleur actuelle.
     */
    public void paintComponent(Graphics g) {
        super.repaint();
        /** Pour chaque cellule... */
        for(int i=0; i<Vue.TAILLE; i++) {
            for(int j=0; j<Vue.TAILLE; j++) {
                /**
                 * ... Appeler une fonction d'affichage auxiliaire.
                 * On lui fournit les informations de dessin [g] et les
                 * coordonnées du coin en haut à gauche.
                 */
                paint(g, game.getBoard().getCell(new Coord(i, j)), new Coord(i, j), (j)*TAILLE, (i)*TAILLE);
            }
        }
    }
    /**
     * Fonction auxiliaire de dessin d'une cellule.
     * Ici, la classe [Cellule] ne peut être désignée que par l'intermédiaire
     * de la classe [CModele] à laquelle elle est interne, d'où le type
     * [CModele.Cellule].
     * Ceci serait impossible si [Cellule] était déclarée privée dans [CModele].
     */
    private void paint(Graphics g, Cell c, Coord coord, int x, int y) {
        /** Sélection d'une couleur. */
        final Color col;
        switch (c.getType()) {
            case sand1 :
                col = Color.yellow;
                break;
            case sand2 :
                col = Color.orange;
                break;
            case eye :
                col = Color.black;
                break;
            default:
                col = new Color(240, 240, 240);
                break;
        }
        g.setColor(col);
        /** Coloration d'un rectangle. */
        g.fillRect(x, y, TAILLE, TAILLE);
        g.setColor(Color.white);
        g.drawRect(x, y, TAILLE, TAILLE);
        if (this.game.getCurrentPlayer().isCellAvailable(this.game.getBoard(), PlayerAction.move, coord)) {
            g.setColor(Color.green);
            g.drawRect(x + 1, y + 1, TAILLE - 2, TAILLE - 2);
        }

        if (this.game.getCurrentPlayer().isCellAvailable(this.game.getBoard(), PlayerAction.removeSand, coord)) {
            g.setColor(Color.blue);
            g.drawRect(x + 1, y + 1, TAILLE - 2, TAILLE - 2);
        }

        if (c.getContent() == CellContent.mirage || c.getContent() == CellContent.oasis) {
            if (c.isWaterConsumed() && c.getContent() == CellContent.oasis) {
                g.setColor(new Color(25, 35, 120));
            } else {
                g.setColor(Color.blue);
            }
            g.fillOval(x + TAILLE - 20, y + 5, 15, 15);
        }

        if (c.isExplored()) {
            g.setColor(Color.black);
            g.drawString(c.getContent().toString(), x + 5, y + TAILLE/2);
        }

        // joueur
        final int t = 10;
        int i = 0;
        for (int pid: c.getPlayerIds()) {
            final Player p = game.getPlayers().get(pid);
            g.setColor(p.getColor());
            g.fillRect(x+5 + i*(t+5), y+TAILLE-5-t, t, t);
            i++;
        }

        // PIECE
        if (c.getPiece() != null) {
            switch (c.getPiece()) {
                case engine:
                    this.add(new EngineLogo());
                    break;
                case wheel:
                    this.add(new WheelLogo());
                    break;
                case energy:
                    this.add(new EnergyLogo());
                    break;
                case rotor:
                    this.add(new RotorLogo());
                    break;
            }
        }
    }
}

class ThirdScreenLeftPanel extends JPanel {
    private final Game game;

    public ThirdScreenLeftPanel(Game game) {
        this.game = game;
        this.build();
    }

    public void build() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(Color.white);

        // DECK
        final MouseAdapter deckMouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);
                if (game.isEquipmentTurn()) {
                    final Equipment equipment = game.pickEquipment();
                    JOptionPane.showMessageDialog(null, equipment.getName(), "Equipement", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };
        this.add(new DeckLayout<ObjectType>(game.getEquipmentDeck(), deckMouseAdapter, "Paquet", "Equipement"));

        // TEXT
        final JTextArea sandText = new JTextArea("Total Sable : " + this.game.getBoard().getSandLevel());
        sandText.setEditable(false);
        this.add(sandText);

        // PROGRESS BAR
        final JPanel stormLevel = new JPanel();
        stormLevel.setBackground(Color.white);
        stormLevel.setLayout(new FlowLayout());

        final JTextArea stormLevelText = new JTextArea("Niveau de la tempête");
        stormLevelText.setEditable(false);
        stormLevel.add(stormLevelText);

        final JProgressBar progressBar = new JProgressBar(1, 7);
        progressBar.setValue((int) this.game.getBoard().getStormLevel());
        progressBar.setString("" + this.game.getBoard().getStormLevel() + "/7");
        progressBar.setStringPainted(true);
        progressBar.setBackground(Color.white);
        stormLevel.add(progressBar);

        this.add(stormLevel);

        // PLAYERS
        final JPanel playerPanel = new JPanel();
        playerPanel.setBackground(Color.white);
        playerPanel.setLayout(new FlowLayout());

        final JPanel playerPanel1 = new JPanel();
        playerPanel1.setBackground(Color.white);
        playerPanel1.setLayout(new BoxLayout(playerPanel1, BoxLayout.PAGE_AXIS));

        final JPanel playerPanel2 = new JPanel();
        playerPanel2.setBackground(Color.white);
        playerPanel2.setLayout(new BoxLayout(playerPanel2, BoxLayout.PAGE_AXIS));

        int i = 0;
        for (Player p: this.game.getPlayers()) {
            if (i < 3) {
                playerPanel1.add(new PlayerCard(this.game, p));
            } else {
                playerPanel2.add(new PlayerCard(this.game, p));
            }
            i++;
        }
        playerPanel.add(playerPanel1);
        playerPanel.add(playerPanel2);
        this.add(playerPanel);
    }
}

class ThirdScreenMiddlePanel extends JPanel {
    private final Game game;

    public ThirdScreenMiddlePanel(Game game) {
        this.game = game;
        this.build();
    }

    private void build() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.white);

        // TOP
        final JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.white);
        topPanel.setLayout(new FlowLayout());

        final JPanel descPanel = new JPanel();
        descPanel.setBackground(Color.white);
        descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));

        final JTextArea desc1 = new JTextArea("Trouver les 4 pièces de la machine volante cachées dans le desert");
        desc1.setEditable(false);
        final JTextArea desc2 = new JTextArea("puis échappez vous du désert par la piste de décollage");
        desc2.setEditable(false);
        descPanel.add(desc1);
        descPanel.add(desc2);

        topPanel.add(descPanel);

        // RIGHT
        final JPanel rightPart = new JPanel();
        rightPart.setLayout(new BoxLayout(rightPart, BoxLayout.Y_AXIS));
        rightPart.setBackground(Color.white);

        final JPanel piece1 = new JPanel();
        piece1.setLayout(new FlowLayout());
        piece1.setBackground(Color.white);
        final JTextArea text1 = new JTextArea("Moteur");
        text1.setEditable(false);
        piece1.add(text1);
        piece1.add(new EngineLogo());
        rightPart.add(piece1);


        final JPanel piece2 = new JPanel();
        piece2.setLayout(new FlowLayout());
        piece2.setBackground(Color.white);
        final JTextArea text2 = new JTextArea("Gouvernail");
        text2.setEditable(false);
        piece2.add(text2);
        piece2.add(new WheelLogo());
        rightPart.add(piece2);


        final JPanel piece3 = new JPanel();
        piece3.setLayout(new FlowLayout());
        piece3.setBackground(Color.white);
        final JTextArea text3 = new JTextArea("Energie");
        text3.setEditable(false);
        piece3.add(text3);
        piece3.add(new EnergyLogo());
        rightPart.add(piece3);

        final JPanel piece4 = new JPanel();
        piece4.setLayout(new FlowLayout());
        piece4.setBackground(Color.white);
        final JTextArea text4 = new JTextArea("Helice");
        text4.setEditable(false);
        piece4.add(text4);
        piece4.add(new RotorLogo());
        rightPart.add(piece4);

        topPanel.add(rightPart);


        this.add(topPanel);
        // GRID
        this.add(new GridPart(this.game));
    }
}

class ThirdScreenRightPanel extends JPanel {
    private final Game game;
    private final Deck<StormAction> deck;

    public ThirdScreenRightPanel(Game game) {
        this.game = game;
        final ArrayList<StormAction> arr = new ArrayList<StormAction>();
        final StormAction[] values = {
            StormAction.north1,
            StormAction.north1,
            StormAction.north1,
            StormAction.north2,
            StormAction.north2,
            StormAction.north3,

            StormAction.south1,
            StormAction.south1,
            StormAction.south1,
            StormAction.south2,
            StormAction.south2,
            StormAction.south3,

            StormAction.east1,
            StormAction.east1,
            StormAction.east1,
            StormAction.east2,
            StormAction.east2,
            StormAction.east3,

            StormAction.west1,
            StormAction.west1,
            StormAction.west1,
            StormAction.west2,
            StormAction.west2,
            StormAction.west3,

            StormAction.VAGUE_CHALEUR,
            StormAction.VAGUE_CHALEUR,
            StormAction.VAGUE_CHALEUR,
            StormAction.VAGUE_CHALEUR,

            StormAction.DECHAINE,
            StormAction.DECHAINE,
            StormAction.DECHAINE,
        };
        Collections.addAll(arr, values);
        this.deck = new Deck<StormAction>(arr, DeckType.stormAction);
        this.build();
    }

    private void build() {
        this.setBackground(Color.white);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setBackground(Color.white);

        final MouseAdapter stormAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);
                if (game.isStormTurn()) {
                    if (deck.isNotEmpty()) {
                        if (game.getPickedCards() < Math.floor(game.getBoard().getStormLevel())) {
                            final StormAction action = deck.pick();
                            game.handleStormEvents(action);
                        }
                    } else {
                        deck.addAll(game.getDefausse().getAll());
                        game.resetDefausse();
                        if (game.getPickedCards() < Math.floor(game.getBoard().getStormLevel())) {
                            final StormAction action = deck.pick();
                            game.handleStormEvents(action);
                        }
                    }
                }
            }
        };

        topPanel.add(new DeckLayout<StormAction>(this.deck, stormAdapter, "Paquet", "Tempête"));

        final MouseAdapter defausseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);
                game.resetPickedCards();
                String message = "<html>";
                for (StormAction sa: game.getDefausse().getAll()) {
                    message += sa.toString() + "<br>";
                }
                message += "</html>";
                JOptionPane.showMessageDialog(null, message, "Défausse", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        topPanel.add(new DeckLayout<StormAction>(this.deck, defausseAdapter, "Défausse", "" + (game.getDefausse().isNotEmpty() ? game.getDefausse().getFirst() : "")));

        this.add(topPanel);

        final JButton exploreButton = new JButton("Exploration");
        exploreButton.setEnabled(this.game.getBoard().getCell(this.game.getCurrentPlayer().getPosition()).getType() == CellType.empty && this.game.isPlayerTurn());
        exploreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("explore");
                if (!game.getBoard().getCell(game.getCurrentPlayer().getPosition()).isExplored()) {
                    final CellContent content = game.explore();
                    if (content == CellContent.equipment || content == CellContent.crash || content == CellContent.tunnel) {
                        game.setTurn(Turn.equipment);
                    }
                }
            }
        });
        this.add(exploreButton);

        final JButton takeButton = new JButton("Prendre une pièce");
        takeButton.setEnabled(this.game.canPickPiece() != null);
        takeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("prendre pièce");
                final PieceType piece = game.pick();
                if (piece != null) {
                    JOptionPane.showMessageDialog(null, piece.toString(), "Nouvelle pièce !", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        this.add(takeButton);

        final JButton endTurnButton = new JButton("Fin de Tour");
        endTurnButton.setEnabled(this.game.isPlayerTurn());
        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("fin de tour");
                if (game.isPlayerTurn()) {
                    game.endOfTurn();
                } else {
                    game.newTurn();
                }
            }
        });
        this.add(endTurnButton);

        final JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new FlowLayout());

        final JTextArea playerArea = new JTextArea("Tour du joueur : " + this.game.getCurrentPlayer().getPseudo());
        playerArea.setEditable(false);
        playerPanel.add(playerArea);

        playerPanel.add(new PlayerColor(this.game.getCurrentPlayer()));
        playerPanel.setBackground(Color.white);
        this.add(playerPanel);

        if (this.game.isPlayerTurn()) {
            final JTextArea text1 = new JTextArea("Phase d'action, vous pouvez faire 4");
            text1.setEditable(false);
            final JTextArea text2 = new JTextArea("actions parmis :");
            text2.setEditable(false);
            final JTextArea text3 = new JTextArea("- Déplacement de 1 case (clic gauche)");
            text3.setEditable(false);
            final JTextArea text4 = new JTextArea("- Désensabler 1 case (clic droit)");
            text4.setEditable(false);
            final JTextArea text5 = new JTextArea("- Explorer une case");
            text5.setEditable(false);
            final JTextArea text6 = new JTextArea("- Prendre une pièce");
            text6.setEditable(false);

            this.add(text1);
            this.add(text2);
            this.add(text3);
            this.add(text4);
            this.add(text5);
            this.add(text6);
        } else if (game.isStormTurn()) {
            final JTextArea text1 = new JTextArea("Phase de la tempête");
            text1.setEditable(false);
            final JTextArea text2 = new JTextArea("Piochez un nombre de cartes de");
            text2.setEditable(false);
            final JTextArea text3 = new JTextArea("tempête égal niveau de la tempête");
            text3.setEditable(false);
            final JTextArea text4 = new JTextArea("Restant à piocher : " + (int) (Math.floor(this.game.getBoard().getStormLevel()) - this.game.getPickedCards()) + " carte(s)");
            text4.setEditable(false);

            this.add(text1);
            this.add(text2);
            this.add(text3);
            this.add(text4);
        } else {
            final JTextArea text1 = new JTextArea("Phase Equipement");
            text1.setEditable(false);
            final JTextArea text2 = new JTextArea("Piochez une carte équipement.");
            text2.setEditable(false);

            this.add(text1);
            this.add(text2);
        }

    }
}

class FinalScreen extends JPanel {
    final Game game;
    public FinalScreen(Game game) {
        this.game = game;
        this.setBackground(Color.white);
        this.setLayout(new FlowLayout());
        final JTextArea text;
        if (this.game.getGameResults() == GameResults.win) {
            text = new JTextArea("VICTOIRE !");
        } else {
            text = new JTextArea("DEFAITE...");
        }
        text.setEditable(false);
        this.add(text);
    }
}

class EngineLogo extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        this.setBackground(Color.white);
        super.paintComponent(g);
        g.setColor(Color.gray);
        g.fillRect(0, 0, 15, 15);
    }
}

class WheelLogo extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.white);
        g.setColor(Color.red);
        g.drawRoundRect(1, 2, 7, 7, 4, 4);
    }
}

class EnergyLogo extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.white);
        g.setColor(Color.yellow);
        g.fillPolygon(new int[]{0, 8, 4}, new int[]{8, 8, 0}, 3);
        g.fillRect(0, 8, 8, 2);
    }
}

class RotorLogo extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.white);
        g.setColor(Color.red);
        g.drawLine(4, 0, 4, 8);
        g.drawLine(0, 4, 8, 4);
    }
}

class PlayerCard extends JPanel {
    private final Game game;
    private final Player player;

    public PlayerCard(Game game, Player player) {
        this.game = game;
        this.player = player;
        this.build();
    }

    private void build() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.white);
        this.setBorder(BorderFactory.createLineBorder(this.player.getColor(), 4));
        this.setPreferredSize(new Dimension(180, 160));

        // JOUEUR
        final JTextArea playerArea = new JTextArea("Joueur : " + this.player.getPseudo());
        playerArea.setEditable(false);
        playerArea.setAlignmentX(0.5f);
        this.add(playerArea);

        // CLASSE
        final JPanel classPanel = new JPanel();
        classPanel.setLayout(new FlowLayout());

        final JTextArea classArea = new JTextArea("Classe : " + this.player.getType());
        classArea.setEditable(false);
        classPanel.add(classArea);

        classPanel.add(new PlayerColor(this.player));
        classPanel.setBackground(Color.white);
        this.add(classPanel);

        // ACTION
        final JTextArea actionArea = new JTextArea("Actions : " + this.player.getRemainingActions());
        actionArea.setEditable(false);
        this.add(actionArea);

        // WATER
        final JTextArea waterArea = new JTextArea("Niveau d'eau : " + this.player.getWaterLevel());
        waterArea.setEditable(false);
        this.add(waterArea);
        final JTextArea waterArea2 = new JTextArea("(max d'eau: " + this.player.getMaxWater() + ")");
        waterArea2.setEditable(false);
        this.add(waterArea2);

        final ActionListener equipmentListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = "<html>";
                for (Equipment equip: player.getEquipment()) {
                    message += (equip.getName() + "<br>");
                }
                message += "</html>";
                JOptionPane.showMessageDialog(null, message, "Equipement", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        final JButton equipmentButton = new JButton("Inventaire (" + this.player.getEquipment().size() + ")");
        equipmentButton.addActionListener(equipmentListener);
        this.add(equipmentButton);

        final ActionListener pieceListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = "<html>";
                for (PieceType piece: player.getPieces()) {
                    message += (piece.toString() + "<br>");
                }
                message += "</html>";
                JOptionPane.showMessageDialog(null, message, "Pièces", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        final JButton pieceButton = new JButton("Pièces (" + this.player.getPieces().size() + ")");
        pieceButton.addActionListener(pieceListener);
        if (player.getPieces().size() > 0) {
            this.add(equipmentButton);
        }
    }
}


class PlayerColor extends JPanel {
    private final Player player;

    public PlayerColor(Player player) {
        this.player = player;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(this.player.getColor());
        /** Coloration d'un rectangle. */
        g.fillRect(0, 0, 20, 20);
    }
}

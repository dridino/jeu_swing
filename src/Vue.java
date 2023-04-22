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
            default:
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

        final JTextArea text = new JTextArea("Nombre de joueurs : ");
        text.setEditable(false);
        this.add(text);

        final JSlider slider = new JSlider(2, 5, 2);
        slider.setMajorTickSpacing(1);
        slider.setPaintTrack(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
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
    private final Deck<PlayerType> deck;
    private String pseudo;

    private JTextArea text;
    private JTextField textField;
    private DeckLayout<PlayerType> deckLayout;
    private JButton button;
    private PlayerType playerType;
    private int i = 0;

    public SecondScreen(Game g) {
        this.game = g;
        final ArrayList<PlayerType> types = new ArrayList<>();
        Collections.addAll(types, PlayerType.values());
        this.deck = new Deck<PlayerType>(types, DeckType.playerType);
        this.build();
    }

    public void build() {
        this.setLayout(new FlowLayout());

        // TEXT
        this.text = new JTextArea("Nom Joueur : ");
        this.text.setEditable(false);
        this.add(text);

        // TEXTFIELD
        this.textField = new JTextField("");
        this.textField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                pseudo = textField.getText();
            }
            public void removeUpdate(DocumentEvent e) {
                pseudo = textField.getText();
            }
            public void insertUpdate(DocumentEvent e) {
                pseudo = textField.getText();
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
                if (playerType == null) {
                    playerType = deck.pick();
                    final Role role = new Role(playerType);
                    JOptionPane.showMessageDialog(null, role.getDescription(), "Pioche", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };
        this.deckLayout = new DeckLayout<PlayerType>(this.deck, mouseAdapter);
        this.add(this.deckLayout);

        // BUTTON
        final JButton button = new JButton("Suivant");
        final ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.addPlayer(new Player(i, pseudo, null, playerType));
                if (i < game.getNumberOfPlayers() - 1) {
                    textField.setText("");
                    playerType = null;
                    i++;
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

    public DeckLayout(Deck<E> deck, MouseAdapter mouseAdapter) {
        this.deck = deck;
        switch (deck.getType()) {
            case playerType:
                this.color = Color.green;
                break;
            default:
                this.color = Color.red;
                break;
        }
        addMouseListener(mouseAdapter);
        this.setPreferredSize(new Dimension(100 + 4, 150 + 4));
    }

    public void paintComponent(Graphics g) {
        super.repaint();
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
        g2.drawString("Paquet", 2 + 4, 18);
        g2.drawString("Aventurier", 2 + 4, 38);
    }
}

class ThirdScreen extends JPanel {
    /** On maintient une référence vers le modèle. */
    private final Game game;
    /** Définition d'une taille (en pixels) pour l'affichage des cellules. */
    private final static int TAILLE = 80;

    /** Constructeur. */
    public ThirdScreen(Game g) {
        this.game = g;
        /**
         * Définition et application d'une taille fixe pour cette zone de
         * l'interface, calculée en fonction du nombre de cellules et de la
         * taille d'affichage.
         */
        Dimension dim = new Dimension(TAILLE*Vue.TAILLE,
                TAILLE*Vue.TAILLE);
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
                paint(g, game.getBoard().getCell(new Coord(i, j)), (j)*TAILLE, (i)*TAILLE);
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
    private void paint(Graphics g, Cell c, int x, int y) {
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
                col = Color.white;
                break;
        }
        g.setColor(col);
        /** Coloration d'un rectangle. */
        g.fillRect(x, y, TAILLE, TAILLE);

        // joueur
        final int t = 10;
        int i = 0;
        for (int pid: c.getPlayerIds()) {
            final Player p = game.getPlayers().get(pid);
            g.setColor(p.getColor());
            g.fillRect(x+5 + i*(t+5), y+TAILLE-5-t, t, t);
            i++;
        }
    }
}

class VueTexte extends JTextPane implements Observer {
    /** On maintient une référence vers le modèle. */

    private Game game;

    /** Constructeur. */
    public VueTexte(Game g) {
        this.game = g;
        /** On enregistre la vue [this] en tant qu'observateur de [modele]. */
        this.game.addObserver(this);

        this.setText("Quantité totale de sable : " + g.getBoard().getSandLevel());
        this.setEditable(false);
    }



    /**
     * L'interface [Observer] demande de fournir une méthode [update], qui
     * sera appelée lorsque la vue sera notifiée d'un changement dans le
     * modèle. Ici on se content de réafficher toute la grille avec la méthode
     * prédéfinie [repaint].
     */
    public void update() {
        this.setText("Quantité totale de sable : " + this.game.getBoard().getSandLevel());
    }
}


/**
 * Une classe pour représenter la zone contenant le bouton.
 *
 * Cette zone n'aura pas à être mise à jour et ne sera donc pas un observateur.
 * En revanche, comme la zone précédente, celle-ci est un panneau [JPanel].
 */
class VueCommandes extends JPanel {
    /**
     * Pour que le bouton puisse transmettre ses ordres, on garde une
     * référence au modèle.
     */
    private final Game game;

    /** Constructeur. */
    public VueCommandes(Game g) {
        this.game = g;
        /**
         * On crée un nouveau bouton, de classe [JButton], en précisant le
         * texte qui doit l'étiqueter.
         * Puis on ajoute ce bouton au panneau [this].
         */
        JButton finDeTour = new JButton("Fin de tour");
        this.add(finDeTour);
        /**
         * Le bouton, lorsqu'il est cliqué par l'utilisateur, produit un
         * événement, de classe [ActionEvent].
         *
         * On a ici une variante du schéma observateur/observé : un objet
         * implémentant une interface [ActionListener] va s'inscrire pour
         * "écouter" les événements produits par le bouton, et recevoir
         * automatiquements des notifications.
         * D'autres variantes d'auditeurs pour des événements particuliers :
         * [MouseListener], [KeyboardListener], [WindowListener].
         *
         * Cet observateur va enrichir notre schéma Modèle-Vue d'une couche
         * intermédiaire Contrôleur, dont l'objectif est de récupérer les
         * événements produits par la vue et de les traduire en instructions
         * pour le modèle.
         * Cette strate intermédiaire est potentiellement riche, et peut
         * notamment traduire les mêmes événements de différentes façons en
         * fonction d'un état de l'application.
         * Ici nous avons un seul bouton réalisant une seule action, notre
         * contrôleur sera donc particulièrement simple. Cela nécessite
         * néanmoins la création d'une classe dédiée.
         */
        Controleur ctrl = new Controleur(this.game);
        /** Enregistrement du contrôleur comme auditeur du bouton. */
        finDeTour.addActionListener(ctrl);

        /**
         * Variante : une lambda-expression qui évite de créer une classe
         * spécifique pour un contrôleur simplissime.
         *
         JButton boutonAvance = new JButton(">");
         this.add(boutonAvance);
         boutonAvance.addActionListener(e -> { modele.avance(); });
         *
         */

    }
}
/** Fin de la vue. */

/**
 * Classe pour notre contrôleur rudimentaire.
 *
 * Le contrôleur implémente l'interface [ActionListener] qui demande
 * uniquement de fournir une méthode [actionPerformed] indiquant la
 * réponse du contrôleur à la réception d'un événement.
 */
class Controleur implements ActionListener {
    /**
     * On garde un pointeur vers le modèle, car le contrôleur doit
     * provoquer un appel de méthode du modèle.
     * Remarque : comme cette classe est interne, cette inscription
     * explicite du modèle est inutile. On pourrait se contenter de
     * faire directement référence au modèle enregistré pour la classe
     * englobante [VueCommandes].
     */
    private final Game game;
    public Controleur(Game g) { this.game = g; }
    /**
     * Action effectuée à réception d'un événement : appeler la
     * méthode [avance] du modèle.
     */
    public void actionPerformed(ActionEvent e) {
        System.out.println("Fin de tour");
        game.endOfTurn();
    }
}

class DeplacementListener extends KeyAdapter {
    @Override
    public void keyTyped(KeyEvent evt) {
        System.out.print(evt.getKeyCode());
        if (evt.getKeyCode() == 104) {
            // 8 -> deplacement vers le haut
            System.out.print("haut");
        } else if (evt.getKeyCode() == 102) {
            // 6 -> deplacement vers la droite
            System.out.print("droite");
        } else if (evt.getKeyCode() == 100) {
            // 4 -> deplacement vers la gauche
            System.out.print("gauche");
        } else if (evt.getKeyCode() == 98) {
            // 2 -> deplacement vers le bas
            System.out.print("bas");
        }
    }
}
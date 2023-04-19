import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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



public class Vue {
    public static final Integer TAILLE = 5;

    private final JFrame frame;

    private final VueGrille grille;
    private final VueTexte totalSable;
    private final VueCommandes finDeTour;

    public Vue(Plateau p) {
        frame = new JFrame();
        frame.setFocusable(true);
        frame.addKeyListener(new KeyListener(){
            @Override
            public void keyPressed(KeyEvent evt) {
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

            @Override
            public void keyTyped(KeyEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyReleased(KeyEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        frame.setTitle("Le desert interdit");
        frame.setLayout(new FlowLayout());

        grille = new VueGrille(p);
        frame.add(grille);
        totalSable = new VueTexte(p);
        frame.add(totalSable);
        finDeTour = new VueCommandes(p);
        frame.add(finDeTour);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class VueGrille extends JPanel implements Observer {
    /** On maintient une référence vers le modèle. */
    private final Plateau plateau;
    /** Définition d'une taille (en pixels) pour l'affichage des cellules. */
    private final static int TAILLE = 80;

    /** Constructeur. */
    public VueGrille(Plateau p) {
        this.plateau = p;
        /** On enregistre la vue [this] en tant qu'observateur de [modele]. */
        p.addObserver(this);
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
     * L'interface [Observer] demande de fournir une méthode [update], qui
     * sera appelée lorsque la vue sera notifiée d'un changement dans le
     * modèle. Ici on se content de réafficher toute la grille avec la méthode
     * prédéfinie [repaint].
     */
    public void update() { repaint(); }

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
                paint(g, plateau.getCase(i, j), (j)*TAILLE, (i)*TAILLE);
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
    private void paint(Graphics g, Case c, int x, int y) {
        /** Sélection d'une couleur. */
        final Color col;
        switch (c.getType()) {
            case sable1 :
                col = Color.yellow;
                break;
            case sable2 :
                col = Color.orange;
                break;
            case oeil :
                col = Color.black;
                break;
            default:
                col = Color.white;
                break;
        }
        g.setColor(col);
        /** Coloration d'un rectangle. */
        g.fillRect(x, y, TAILLE, TAILLE);

        final int t = 10;
        int i = 0;
        for (Joueur j: c.getJoueurs()) {
            g.setColor(j.getColor());
            g.fillRect(x+5 + i*(t+5), y+TAILLE-5-t, t, t);
            i++;
        }
    }
}

class VueTexte extends JTextPane implements Observer {
    /** On maintient une référence vers le modèle. */

    private Plateau plateau;

    /** Constructeur. */
    public VueTexte(Plateau p) {
        this.plateau = p;
        /** On enregistre la vue [this] en tant qu'observateur de [modele]. */
        p.addObserver(this);

        this.setText("Quantité totale de sable : " + p.getSableCount());
        this.setEditable(false);
    }



    /**
     * L'interface [Observer] demande de fournir une méthode [update], qui
     * sera appelée lorsque la vue sera notifiée d'un changement dans le
     * modèle. Ici on se content de réafficher toute la grille avec la méthode
     * prédéfinie [repaint].
     */
    public void update() {
        this.setText("Quantité totale de sable : " + this.plateau.getSableCount());
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
    private final Plateau plateau;

    /** Constructeur. */
    public VueCommandes(Plateau p) {
        this.plateau = p;
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
        Controleur ctrl = new Controleur(plateau);
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
    Plateau plateau;
    public Controleur(Plateau p) { this.plateau = p; }
    /**
     * Action effectuée à réception d'un événement : appeler la
     * méthode [avance] du modèle.
     */
    public void actionPerformed(ActionEvent e) {
        System.out.println("Fin de tour");
        plateau.avanceTour();
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
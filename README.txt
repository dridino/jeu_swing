Parties du sujet traitées :
    - Sélection du nombre de joueur
    - Renseignement des pseudos et attribution des rôles
    - Jeu en tant que tel :
        - grille
        - affichage des cases accessibles en déplacement et au désensablage
        - phase du joueur (déplacements, désensablement, exploration, pioche equipement, fin de tour anticipée)
        - phase de la tempête (pioche des cartes et actions réalisées, défausse fonctionnelle)
        - gestion de victoire ou défaite
        - explorateur qui se déplace en diagonale
        - archéologue peut enlever 2 tonnes de sable en une action
        - porteuse d'eau qui commence avec 5 portions d'eau
        - alpiniste peut se rendre sur une case couverte par 2 tonnes de sable
        - tunnels communiquants
        - oasis fontionnelles (avec le comportement de la porteuse d'eau adapté)
        - récupération des pièces avec les indices ligne / colonne


Parties du sujet non-traitées:
    - Le fait que la météorologue puisse enlever une carte du dessus du paquet pour la mettre en dessous
    - Le fait que l'alpiniste puisse emporter quelqu'un avec lui
    - Le fait que la navigatrice puisse déplacer un autre joueur
    - Utilisation des équipements

Choix d'achitecture :
    - Architecture réfléchie sur papier avant de coder
    - Une classe mère `Game` qui contient le plateau, les joueurs et gère les différents états du jeu
    - Une classe `Board` représentant le plateau du jeu
    - Une classe `Player` représentant les joueurs
    - `Game` est la seule classe observée par l'UI et observe à la fois les joueurs et le plateau (pour des raisons de simplifications)

Difficultés rencontrées :
    - Comprendre les règles du jeu
    - Créer une interface correspondant plus ou moins à celle du programme exemple
    - Bien prendre en main Swing et ses éléments
    - Prendre en main, au début, ce système de Observer - Observable et bien gérer les changements d'états


Répartition des rôles :
    Adrien s'est principalement chargé de la partie modèle tandis que Maël lui s'occupait plus de l'aspect graphique.
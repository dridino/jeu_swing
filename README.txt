Parties du sujet traitées :
    - Sélection du nombre de joueur
    - Renseignement des pseudos et attribution des rôles
    - Jeu en tant que tel :
        - grille
        - phase du joueur (déplacements, désensablement, exploration, pioche equipement, fin de tour anticipée)
        - phase de la tempête (pioche des cartes et actions réalisées, défausse fonctionnelle)
        - gestion de victoire ou défaite
        - explorateur qui se déplace en diagonale
        - porteuse d'eau qui commence avec 5 portions d'eau
        - tunnels communiquants

Parties du sujet non-traitées:
    - Le fait que la météorologue puisse enlever une carte du dessus du paquet pour la mettre en dessous
    - Le fait que l'alpiniste puisse emporter quelqu'un avec lui
    - Le fait que la navigatrice puisse déplacer un autre joueur
    - Utilisation des objets

Choix d'achitecture :
    - Une classe mère `Game` qui contient le plateau, les joueurs et gère les différents états du jeu
    - Une classe `Board` représentant le plateau du jeu
    - Une classe `Player` représentant les joueurs
    - `Game` est la seule classe observée par l'UI et observe à la fois les joueurs et le plateau (pour des raisons de simplifications)

Répartition des rôles :

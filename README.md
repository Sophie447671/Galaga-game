# Explication des fonctionnalités réalisées :
1. Formation et comportement des ennemis 
- Les ennemis se déplacent en formation. Pour les niveaux 1 et 2, ils sont chargés à partir des fichiers levelN.lvl.

- Pour le niveau 3, la formation est plus complexe : les ennemis se déplacent plus rapidement, tirent plus fréquemment.

- Les ennemis en bas de formation attaquent un par un. 

- Dès que l'ennemi qui attaque est mort, un autre est choisi aléatoirement parmi ceux en bas de formation pour continuer l'attaque.

- Les BEE se déplacent en zigzag lorsqu'ils attaquent, tandis que les autres se déplacent en ligne droite.

- Les ennemis en bas de formation tirent des missiles en fonction des cooldowns définis dans les fichiers de niveaux.
Pour le niveau 3, le tir est plus régulier. 

- Si un missile ennemi touche le joueur, celui-ci perd une vie, en sachant que le joueur commence avec 3 vies à chaque niveau.

# 2. Joueur et interactions
- Le joueur peut se déplacer à gauche et à droite.

- Il peut tirer des missiles avec une limite de 3 missiles affichés simultanément.

- Les collisions entre les missiles du joueur et les ennemis entraînent la destruction de l'ennemi et l'incrémentation du score.

# 3. Scores et vies 
- Le nombre de vies du joueur est affiché et mis à jour en temps réel.

- Le score du joueur augmente en fonction des ennemis détruits.

- Le meilleur score est récupéré depuis le fichier ressources/highscore/highscore.sc et mis à jour si le joueur le dépasse.

# 4. Gestion de fin de partie
- Le jeu affiche Game Over si le joueur a perdu toutes ses vies, soit en étant touché par des missiles ennemis, soit en étant capturé par un MOTH.

- Le joueur gagne la partie s'il termine le niveau 3 avec au moins une vie restante et que tous les ennemis sont morts.

# 5. Graphismes et sprites 
- Les sprites du joueur et des ennemis sont chargés directement depuis les fichiers correspondants dans ressources/sprites.

- L'affichage graphique est réalisé grâce à la librairie StdDraw, pour gérer le rendu des sprites, des projectiles ainsi que des textes : scores, vies, 
fin de partie.

# Guide d'exécution du projet :
Lancer le projet en exécutant la classe game.Game depuis VS Code avec engine.app.

Vérifier que le dossier ressources, contenant les fichiers de niveaux et les sprites, se trouve dans le même répertoire que le projet.

La documentation JavaDoc est accessible via le fichier docs/index.html.

Description de l'interface pour jouer avec votre projet :

#### 1. Commandes
- Flèche gauche et droite pour faire avancer le personnage

- Espace pour que le joueur tire des missiles (avec une limite de 3 affichés simultanément sur l'écran)

- Espace pour redémarrer une partie (après Game Over ou si le joueur a gagné la partie)
#### 2. Affichage
- En bas à gauche, on peut voir le nombre de vie restantes (maximum 3).

- En haut : le score et le meilleur score mis à jour en temps réel.

package game;


import Graphics.InterfaceGraphique;
import engine.StdDraw;
import game.actors.Ennemi;
import game.actors.FormationEnnemi; 
import game.actors.Joueur;

/**
 * Classe du jeu principal.
 * Gère la création de l'espace de jeu et la boucle de jeu en temps réel.
 */
public class Game {
    public Joueur player; 
    public Ennemi ennemi;
    public FormationEnnemi ennemis;
    public InterfaceGraphique interfaceGraphique;
    public Level level;
    public boolean gameOver = false;
    public boolean partieFinie = false;
    public boolean espacePresse = false;



    /**
     * Créé un jeu avec tous les éléments qui le composent
     */
    public Game() {
        player = new Joueur(0.5, 0.16, 0.05, true, 0.01, 0);
        level = new Level(1);
        ennemis = new FormationEnnemi(0.08, 0.1, 0.4, 0.88, level, player);
        interfaceGraphique = new InterfaceGraphique(player, level, ennemis);
        level.afficherNomLevel();

    }

    /**
     * Initialise l'espace de jeu
     */
    private void init() {
        StdDraw.setCanvasSize(900, 900);
        StdDraw.enableDoubleBuffering();
    }

    /**
     * Initialise le jeu et lance la boucle de jeu en temps réel
     */
    public void launch() {
        init();

        while (true) {
            //StdDraw.setPenColor(StdDraw.BLACK);
            
            StdDraw.clear(StdDraw.BLACK); // On efface tous ce qu'il y a sur l'interface
            if(isGameRunning()){
                update(); // on met a jour les attributs de chaque éléments
            }
            else{
                if(StdDraw.isKeyPressed(32)){
                    rejouer();
                }
            }
            draw(); // on dessine chaques éléments

            StdDraw.show(); // on montre l'interface
            StdDraw.pause(30); // on attend 30 milisecondes avant de recommencer
        }
    }

    /**
     * Condition d'arrêt du jeu
     * 
     * @return true si la partie n'est pas terminée
     */
    private boolean isGameRunning() {
        return !partieFinie;
    }

    /**
     * Dessin tous les éléments du jeu
     */
    public void draw() {
        player.draw();
        ennemis.draw();
        interfaceGraphique.draw();
    }

    public void nextLevel(){
        level.setLevel();
        player.resetPosition();
        player.resetMissiles();
        player.setNbVies(3);
        ennemis.resetFormation();
        level.afficherNomLevel();
    }

    public void rejouer(){
        level.resetLevel();
        level.afficherNomLevel();
        player.resetPosition();
        player.resetMissiles();
        player.resetScore();
        player.setNbVies(3);
        ennemis.resetFormation();
        partieFinie = false;
    }

    /**
     * Met a jour les attributs de tous les éléments du jeu
     */
    private void update() {
        player.update();
        player.checkCollisionsWithEnnemy(ennemis);
        player.checkCollisionsProjectiles(ennemis.getProjectiles());
        ennemis.update();
        interfaceGraphique.updateMeilleurScore();
        level.updateLevel();

       if((level.getLevel() == 3 && (player.getNbVies() <= 0 || ennemis.ennemisTousMorts())) || player.getNbVies() <= 0){
            partieFinie = true;
            return;
       }

       if(ennemis.ennemisTousMorts()){
           nextLevel();
       }

       if(partieFinie){
            if(StdDraw.isKeyPressed(32)){
                espacePresse = true;
                rejouer();
            }
            if(!StdDraw.isKeyPressed(32)){
                espacePresse = false;
            }
       }
    }

}

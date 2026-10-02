package game.actors;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import Graphics.Sprite;
import engine.StdDraw;
import game.Projectile;

/**
 * Classe représentant le joueur.
 */
public class Joueur extends Entite {
    protected double xInit;
    protected double yInit;
    protected double vitesse;
    protected int nbVies = 3;
    protected int maxProjectiles = 3;
    protected ArrayList<Projectile> projectiles;
    protected boolean canShoot = true;
    protected int score;
    protected Sprite vaisseau;

    /**
     * Créé un joueur.
     * 
     * @param x      postion du joueur sur l'axe des abscisses
     * @param y      position du joueur sur l'axe des ordonnées
     * @param length largeur du joueur
     */
    public Joueur(double x, double y, double largeur, boolean vivant, double vitesse, int score) {
        super(x, y, largeur, vivant);
        this.xInit = x;
        this.yInit = y;
        this.vitesse = vitesse;
        projectiles = new ArrayList<>();
        this.score = score;
        this.vaisseau = new Sprite(Path.of("ressources/sprites/ship.spr"), 0.006);
    }

    /**
     * 
     * @return le score actuel du joueur
     */
    public int getScore(){
        return score;
    }

    /**
     * modifie le score actuel du joueur
     * @param score     score actuel du joueur
     */
    public void setScore(int score){
        this.score = score;
    }

    /**
     * 
     * @return le nombre de vies restantes du joueur 
     */
    public int getNbVies(){
        return nbVies;
    }

    /**
     * met à jour le nombre de vies du joueur
     * @param nbVies
     */
    public void setNbVies(int nbVies){
        this.nbVies = nbVies;
    }

    /**
     * 
     * @return le Sprite du joueur
     */

    public Sprite getSprite(){
        return vaisseau;
    }

    /**
     * réintialise le score du joueur
     */
    public void resetScore(){
        score = 0;
    }

    /**
     * réintialise la position du joueur
     */
    public void resetPosition(){
        x = xInit;
        y = yInit;
    }

    /**
     * réintialise le nombre de missiles du joueur (3) et supprime tous ses projectiles
     */
    public void resetMissiles(){
        maxProjectiles = 3;
        if (!projectiles.isEmpty()){
            projectiles.clear();

        }
    }


    /**
     * dessine le joueur à sa position initiale, ainsi que ses projectiles
     */
    public void draw() {
        vaisseau.draw(x, y);
        for(Projectile p : projectiles){
            p.draw();
        }

    }

    /**
     * Met à jour la position du joueur en fonction des touches préssé.
     */
    public void update() {
        double speed = 0.01; // vitesse de déplacement du joueur
        // Si la flèche gauche est préssé
        if (StdDraw.isKeyPressed(37)) {
            x -= speed;
        }

        // Si la flèche droite est préssé
        if (StdDraw.isKeyPressed(39)) {
            x += speed;
        }
        if (x > 1 - largeur / 2) {
            x = 1 - largeur / 2;
        }
        if (x < largeur/2) {
            x = largeur/2;
        }
        if(StdDraw.isKeyPressed(32) && canShoot){
            if(canShoot){
                shoot(); 
                canShoot = false;
            }
        }
        else if(!StdDraw.isKeyPressed(32)){
            canShoot = true;
        }  
        
        for(Projectile p : projectiles){
            p.update();
        }
        
    }


    /**
     * Fait tirer un projectile au joueur
     */
    public void shoot() {
        if(projectiles.size() < maxProjectiles){
            Projectile projectile = new Projectile(x, y+0.1, 0.001, true, 0.01, 1, true, null);
            projectiles.add(projectile);
        }
    }



    /**
     * Vérifie les collisions entre les projectiles du joueur et les ennemis vivants
     * @param formation l'ensemble des ennemis sur l'écra
     */
    public void checkCollisionsWithEnnemy(FormationEnnemi formation){
        List<Ennemi> ennemis = formation.getEnnemis();
        List<Projectile> projectilestoErase = new ArrayList<>();
        for(Projectile p : projectiles){
            for(Ennemi e : ennemis){
                if(e.isVivant() && p.isVivant() && e.isInCollisionWithProjectile(p)){
                    e.setVivant(false);
                    p.setVivant(false);
                    score += e.getPoints();
                }
            }
            if(!p.isVivant()){
                projectilestoErase.add(p);
            }
        }
        projectiles.removeAll(projectilestoErase);
    }

    /**
     * Vérifie si un projectile ennemi touche le joueur
     * @param p le projectile à tester
     * @return true si le projectile est en collision avec le joueur, false sinon
     */
    public boolean isInCollisionWithPlayer(Projectile p){ 
        return Math.abs(p.getX() - x) < 0.01 && Math.abs(p.getY() - y) < 0.01; 
    }

    /**
     * Vérifie les collisions entre le joueur et une liste de projectiles ennemis
     * @param projectiles la liste de projectiles à tester
     */

    public void checkCollisionsProjectiles(List<Projectile> projectiles){
        List<Projectile> projectilestoErase = new ArrayList<>();
        for(Projectile p : projectiles){
            if(p.isVivant() && isInCollisionWithPlayer(p)){
                nbVies--;
                resetPosition();
                p.setVivant(false);
                projectilestoErase.add(p);
            }
        }
        projectiles.removeAll(projectilestoErase);

    } 
}

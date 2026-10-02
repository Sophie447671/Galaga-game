package game;

import engine.StdDraw;
import game.actors.Ennemi;
import game.actors.Entite;

/**
 * Classe permettant de gérer la logique d'un projectile
 */
public class Projectile extends Entite {
    private double vitesse;
    private int direction;
    private boolean fromPlayer;
    private Ennemi tireur;

    /**
     * 
     * @param x             la position du projectile sur l'axe x
     * @param y             la position du projectile sur l'axe y
     * @param largeur       la largeur du projectile
     * @param vivant        l'état de vie du projectile
     * @param vitesse       la vitesse du projectile
     * @param direction     la direction du projectile
     * @param fromPlayer    le projectile provient du joueur
     */
    public Projectile(double x, double y, double largeur, boolean vivant, double vitesse, int direction, boolean fromPlayer, Ennemi tireur) {
        super(x, y, largeur, vivant);
        this.vitesse = vitesse;
        this.direction = direction;
        this.fromPlayer = fromPlayer;
        this.tireur = tireur;
    }

    /**
     * 
     * @return true si le tir provient du joueur, false sinon
     */
    public boolean fromPlayer(){
        return fromPlayer;
    }

    /**
     * 
     * @return l'état de vie du projectile
     */
    public boolean getVivant(){
        return vivant;
    }

    /**
     * 
     * @return l'ennemi à l'origine du tir
     */
    public Ennemi getTireur(){
        return tireur;
    }

    /**
     * Met à jour le projectile sur l'écran en fonction de s'il provient du joueur ou de l'ennemi
     */
    public void update() {
        if(vivant){
            if(fromPlayer){
                direction = 1; 
                y += direction * vitesse;
            }
            else{
                direction = -1; 
                y += direction * vitesse;
            }
        }
        if(y >= 0.91 || y <= 0.1){
            vivant = false;       
        }
    }

    /**
     * Dessine le projectile en fonction de s'il provient du joueur ou de l'ennemi
     */
    @Override
    public void draw() {
        if(vivant){
            if(fromPlayer){
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.filledRectangle(x, y, 0.001, 0.01);
            }
            else{
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.filledRectangle(x, y, 0.001, 0.01);
            }
        }
    }
    
}

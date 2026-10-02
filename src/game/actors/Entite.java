package game.actors;

/**
* Classe abstraite représentant une entité du jeu, elle sert de parent pour : 
* - Ennemi
* - Joueur
* - Projectile
*/

public abstract class Entite {
    protected double x;
    protected double y;
    protected double largeur;
    protected boolean vivant;

    /**
     * 
     * @param x
     * @param y
     * @param largeur
     * @param vivant
     */

    public Entite(double x, double y, double largeur, boolean vivant) {
        this.x = x;
        this.y = y;
        this.largeur = largeur;
        this.vivant = vivant;
    }

    /**
     * 
     * @return la position de l'entité sur l'axe x
     */
    public double getX() {
        return x;
    }

    /**
     * modifie la position de l'entité sur l'axe x
     * @param x
     */
    public void setX(double x) {
        this.x = x;
    }
  
    /**
     * 
     * @return la position de l'entité sur l'axe y
     */
    public double getY() {
        return y;
    }

    /**
     * modifie la position de l'entité sur l'axe y
     * @param y
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * 
     * @return la largeur de l'entité 
     */
    public double getLargeur() {
        return largeur;
    }

    /**
     * modifie la largeur de l'entité
     * @param largeur
     */
    public void setLargeur(double largeur) {
        this.largeur = largeur;
    }

    /**
     * 
     * @return true si l'entité est vivante, false sinon
     */
    public boolean isVivant() {
        return vivant;
    }

    /**
     * modifie l'état de vie de l'entité
     * @param vivant
     */
    public void setVivant(boolean vivant) {
        this.vivant = vivant;
    }

    /**
     * Méthode abstraite mettant à jour l'état de l'entité
     */
    public abstract void update();


    /**
     * Méthode abstraite dessinant l'entité à l'écran
     */
    public abstract void draw();


}

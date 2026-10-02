package game.actors;

import java.nio.file.Path;

import Graphics.Sprite;
import game.Level;
import game.Projectile;
import game.actors.FormationEnnemi.TypeEnnemi;

/**
* Classe permettant de créer un ennemi.
*/

public class Ennemi extends Entite {

    private TypeEnnemi type;
    private int points;
    private double xInit; 
    private double yInit;
    private double vitesse;
    private int direction;
    private boolean enAttaque;
    private Sprite sprite;
    private int coolDownTir = 0;
    private long lastShotTime = 0;
    private int currentTime = 0;
    private Level level;
    private boolean missileEnCours;

    /**
     * Constructeur permettant d'initialiser un ennemi
     * @param type
     * @param points
     * @param vitesse
     * @param x
     * @param y
     * @param largeur
     * @param vivant
     * @param direction
     * @param canAttack
     * @param enAttaque
     * @param level
     */

    public Ennemi(TypeEnnemi type, int points, double vitesse, double x, double y, double largeur, boolean vivant, int direction, boolean enAttaque, Level level, boolean missileEnCours) {
        super(x, y, largeur, vivant);
        this.xInit= x;
        this.yInit = y;
        this.type = type;
        this.points = points;
        this.vitesse = vitesse;
        this.direction = direction;
        this.enAttaque = enAttaque;
        this.sprite = new Sprite(Path.of("ressources/sprites/" + type.name().toLowerCase()+ ".spr"), 0.004);
        this.missileEnCours = missileEnCours;
        this.level = level;
    }

    /**
     * 
     * @return la vitesse de l'ennemi
     */
    public double getVitesse(){
        return vitesse;
    }

    /**
     * 
     * @return un booléen indiquant si l'ennemi est en attaque
     */
    public boolean getEnAttaque(){
        return enAttaque;
    }

    /**
     * 
     * @return la position intiale de l'ennemi sur l'axe x
     */

    public double getxInitial(){
        return xInit;
    }

    /**
     * 
     * @return la position initial de l'ennemi sur l'axe y
     */

    public double getyInitial(){
        return yInit;  
    }

    /**
     * modifie la position intiale de l'ennemi sur l'axe x
     * @param xIn
     */
    public void setxInitial(double xIn){
        xInit = xIn;
    }

    /**
     * modifie la position de l'ennemi sur l'axe y
     * @param yIn
     */

    public void setYInitial(double yIn){
        yInit = yIn;
    }

    /**
     * renvoie un booléen indiquant si l'ennemi est vivant
     * @return la valeur du booléen estVivant
     */

    public boolean getEstVivant(){
        return vivant;
    }

    /**
     * setter modifiant l'état de l'attaque de l'ennemi
     */

    public void setEnAttaque(boolean enAttaque){
        this.enAttaque = enAttaque;
    }

    /**
     * revoie la direction de l'ennemi 
     * @return 1 pour un déplacement vers la droite, -1 vers la gauche
     */

    public int getDirection(){
        return direction;
    }

    /**
     * modifie la direction de l'ennemi
     * @param direction
     */
    public void setDirection(int direction){
        this.direction = direction;
    }

    /**
     * Vérifie si l'ennemi peut tirer un missile
     * @return true si l'ennemi peut tirer, false sinon
     */
    public boolean canShoot(Level level){
        long currentTime = System.currentTimeMillis();
        if(level.getLevel()<=2){
            if(lastShotTime == 0) return true; 
            return (currentTime - lastShotTime) >= level.getCoolDownMissile();  
        }
        else{
            return isVivant() && !missileEnCours;

        }
    }

    /**
     * Indique si un missile est actuellement utilisé par l'ennemi (niveau 3)
     * @param b true si le missile est actif, false sinon
     */
    public void setMissileEnCours(boolean b){
        missileEnCours = b;
    }


    /**
     * met à jour le dernier temps de tir
     */
    public void resetCoolDown(){
        lastShotTime = System.currentTimeMillis();
    }

    /**
     * 
     * @return le type de l'ennemi
     */

    public TypeEnnemi getTypeEnnemi(){
        return type;
    }

    /**
     * réintialise la position de l'ennemi à sa position initiale
     */

    public void resetPosition(){
        x = xInit;
        y = yInit;
    }

    /**
     * 
     * @return la valeur de l'ennemi
     */
    public int getPoints(){
        return points;
    }

    /**
     * 
     * @return le temps de recharge du tir
     */
    public int getCoolDown(){
        return coolDownTir;
    }

    /**
     * met à jour la position de l'ennemi à chaque frame 
     */
    @Override
    public void update() {
        if(!enAttaque){
            x += direction * vitesse; 
        }
    }

    /**
     * modifie le temps actuel
     * @param time
     */
    public void setCurrentTime(int time){
        this.currentTime = time; 
    }

    /**
     * Dessiner l'ennemi à sa position actuelle
     */
    @Override
    public void draw() {
        sprite.draw(x, y);

    }

    /**
     * Vérifie si un ennemi est touché par un projectile
     * @param p
     * @return true si l'ennemi est en collision, false sinon
     */
    public boolean isInCollisionWithProjectile(Projectile p){
        double posCentre = largeur / 2;
        return p.getX() >= x - posCentre && p.getX() <= x + posCentre
        && p.getY() >= y - posCentre && p.getY() <= y + posCentre;
    }

    /**
     * Vérfie si un ennemi est en collision avec le joueur (utile pour la capture du vaisseau par Moth)
     * @param joueur
     * @return true si l'ennemi est en collision avec le joueur, false sinon
     */

    public boolean isInCollisionWithPlayer(Joueur joueur) {
        double dx = Math.abs(joueur.getX() - x);
        double dy = Math.abs(joueur.getY() - y);
        double distance = Math.max(largeur, joueur.getLargeur()) / 2;
        return dx < distance && dy < distance;
    }

    /**
     * si l'ennemi (Moth) a capturé le joueur, modification de l'état de ce dernier
     * @param joueur
     */
    public void capturerJoueur(Joueur joueur) {
        joueur.setNbVies(joueur.getNbVies() - 1);
        joueur.setVivant(false);
    }
}

package game.actors;

import java.util.ArrayList;
import java.util.List;

import game.Level;
import game.Projectile;

/**
 * Classe permettant de créer une formation d'ennemi
 */

public class FormationEnnemi {

    /**
     * Énumération représentant les différents type d'ennemis : BEE, BUTTERFLY, MOTH
     */
    public enum TypeEnnemi {
        BEE, 
        BUTTERFLY, 
        MOTH
    }

    private List<Ennemi> ennemis;
    private double espacementX;
    private double espacementY;
    List<Ennemi> derniereLigne = new ArrayList<>();

    private Level level;

    private double departX;
    private double departY;
    private int directionGroupe;
    private List<Projectile> projectiles;
    private double vitesseGroupe;
    private List<List<TypeEnnemi>> formation = new ArrayList<>();
    private int timerMissileAleatoire = 0;
    private Ennemi ennemiEnAttaque = null;
    private Joueur joueur;           
    private Joueur joueurCapture; 
    //private int gameTime = 0;   

    /**
     * 
     * @param espacementX   espacement entre les ennemis sur l'axe x pour le niveau 3
     * @param espacementY   espacement entre les ennemis sur l'axe y pour le niveau 3 
     * @param departX       posiition de départ des ennemis sur l'axe x pour le niveau 3
     * @param departY       position de départ des ennemis sur l'axe y pour le niveau 3
     * @param level         niveau actuel du jeu
     * @param joueur        instance du joueur
     */
    public FormationEnnemi(double espacementX, double espacementY, double departX, double departY, Level level, Joueur joueur){
        this.espacementX = espacementX; 
        this.espacementY = espacementY; 
        this.departX = departX; 
        this.departY = departY;
        this.level = level;
        this.directionGroupe = -1;
        ennemis = new ArrayList<Ennemi>();
        projectiles = new ArrayList<>();
        String[] ligneLevel = level.chargement_ligne(0);
        this.vitesseGroupe = Double.parseDouble(ligneLevel[1]);
        this.joueur = joueur;
        this.joueurCapture = null;
        initListEnnemis(level.getLevel());
    }

    /**
     * Initialise la formation d'ennemis à partir du fichier levelN.lvl pour un niveau N donné
     * @param Nlevel niveau actuel du jeu
     */

    public void initListEnnemis(int Nlevel){
        List<String> listLevel = level.lecture_niveau(Nlevel);
        for(int i = 1; i < listLevel.size(); i++){
            String[] ligneLevel = level.chargement_ligne(i);
            TypeEnnemi type = TypeEnnemi.valueOf(ligneLevel[0].toUpperCase()); 
            double x = Double.parseDouble(ligneLevel[1]);
            double y = Double.parseDouble(ligneLevel[2]);
            double taille = Double.parseDouble(ligneLevel[3]);
            int points = Integer.parseInt(ligneLevel[4]);
            double vitesse = Double.parseDouble(ligneLevel[5]);
            Ennemi e = new Ennemi(type, points, vitesse, x, y, taille, true, -1, false, level, false);
            ennemis.add(e);
        }
    }

    public void initFormationLevel3Boss(){
        formation.add(List.of(TypeEnnemi.MOTH, TypeEnnemi.MOTH, TypeEnnemi.MOTH, TypeEnnemi.MOTH));
        formation.add(List.of(TypeEnnemi.BUTTERFLY, TypeEnnemi.BUTTERFLY, TypeEnnemi.BUTTERFLY, TypeEnnemi.BUTTERFLY,
        TypeEnnemi.BUTTERFLY, TypeEnnemi.BUTTERFLY, TypeEnnemi.BUTTERFLY, TypeEnnemi.BUTTERFLY));
        formation.add(List.of(TypeEnnemi.BEE, TypeEnnemi.BEE, TypeEnnemi.BEE, TypeEnnemi.BEE, TypeEnnemi.BEE,
        TypeEnnemi.BEE, TypeEnnemi.BEE, TypeEnnemi.BEE, TypeEnnemi.BEE, TypeEnnemi.BEE));
        formation.add(List.of(TypeEnnemi.BEE, TypeEnnemi.BEE, TypeEnnemi.BEE, TypeEnnemi.BEE, TypeEnnemi.BEE,
        TypeEnnemi.BEE, TypeEnnemi.BEE, TypeEnnemi.BEE, TypeEnnemi.BEE, TypeEnnemi.BEE));
    }

    /**
     * Initialisation  de la formation d'ennemis pour le niveau 3 
     */

    public void initListEnnemisLevel3Boss(){
        for (int i = 0; i < formation.size(); i++) {
            List<TypeEnnemi> ligne = formation.get(i);
            int nbEnnemis = ligne.size();
            double largeurLigne = (nbEnnemis - 1) * espacementX;
            for (int j = 0; j < nbEnnemis; j++) {
                int points = 0;
                TypeEnnemi type = ligne.get(j);
                double x = (departX - largeurLigne   /2) + j * espacementX;
                double y = departY - i * espacementY;
                if(type == TypeEnnemi.BEE){
                    points = 300;
                }
                else if(type == TypeEnnemi.BUTTERFLY){
                    points = 500;
                }
                else{
                    points = 750;
                }
                Ennemi e = new Ennemi(type, points, 0.01, x, y, 0.05, true, -1, false, level, false);
                e.setxInitial(x);
                e.setYInitial(y);
                ennemis.add(e);
            }
        }
    }

    /**
     * 
     * @return liste des ennemis
     */
    public List<Ennemi> getEnnemis(){
        return ennemis;
    }

    public List<Projectile> getProjectiles(){
        return projectiles;
    }

    /**
     * réintialise complètement la formation d'ennemis
     */
    public void resetFormation(){
        ennemis.clear();
        projectiles.clear();
        ennemiEnAttaque = null; 
        derniereLigne.clear();
        directionGroupe = -1;
        joueurCapture = null;
        if(level.getLevel() == 3){
            initFormationLevel3Boss();
            initListEnnemisLevel3Boss();
        }
        else{
            initListEnnemis(level.getLevel());
        }
    }

    /**
     * 
     * @return la liste des ennemis se situant sur la dernière ligne de la formation
     */

    private List<Ennemi> getBasFormation(){
        derniereLigne.clear();
        double yMin = Double.MAX_VALUE;
        for(Ennemi e : ennemis){
            if(e.isVivant()){
                yMin = Math.min(yMin, e.getY());
            }
        }
        for(Ennemi e : ennemis){
            if(e.isVivant() && Math.abs(e.getY() - yMin) <= 0.001){
                derniereLigne.add(e);
            }
        }
        return derniereLigne;
    }

    /**
     * Choisit aléatoirement un ennemi par la liste des ennemis en dernière ligne de formation
     */
    private void choisirEnnemiBasFormation(){
        getBasFormation();
        if (ennemiEnAttaque == null){
            if (!derniereLigne.isEmpty()) {
                int indexAleatoire = (int)(Math.random() * derniereLigne.size());
                Ennemi e = derniereLigne.get(indexAleatoire);
                e.setEnAttaque(true);
                ennemiEnAttaque  = e;
            }
        }
    }

    /**
     * Permet de tirer un projectile par un ennemi si possible
     * @param e l'ennemi qui tire
     */

    private void tirEnnemi(Ennemi e){
        if(e.canShoot(level)){
            Projectile p = new Projectile(e.getX(), e.getY() - e.getLargeur()/2, 0.8, true, 0.01, 1, false, e);
            projectiles.add(p);
            e.setMissileEnCours(true);
            e.resetCoolDown();
        }
    }

    /**
     * Gérer l'apparition des missiles par ennemi au niveau 3
     */
    private void timerMissileNiveau3(){
        if(level.getLevel() == 3){
            if(timerMissileAleatoire <= 0){
                List<Ennemi> vivants = new ArrayList<>();
                for(Ennemi e : ennemis){
                    if(e.isVivant() && derniereLigne.contains(e) && e.canShoot(level)){
                        vivants.add(e);
                    }
                }
                if(!vivants.isEmpty()){
                    int index = (int) (Math.random() * vivants.size());
                    Ennemi e = vivants.get(index);
                    Projectile missile = new Projectile(e.getX(), e.getY() - e.getLargeur() / 2, 1.5, true, 0.01, 1, false, e);
                    projectiles.add(missile);
                    e.setMissileEnCours(true);
                    timerMissileAleatoire = 300;
                }
            }
            else{
                timerMissileAleatoire--;
            }
        }
    }

    /**
     * Met à jour la dicrection de la formation lorsque les ennemis atteignent les bords de la fenêtre
     */
    private void updateDirectionGroupe() {
        boolean changementDirection = false;
        for(Ennemi e : ennemis){
            if(e.isVivant() && !e.getEnAttaque()){
                if(directionGroupe > 0 && e.getX() + e.getLargeur() / 2 >= 1){
                    changementDirection = true;
                    break;
                }            
                else if(directionGroupe < 0 && e.getX() - e.getLargeur() / 2 <= 0){
                    changementDirection = true;
                    break;
                }
            }
        }

        if(changementDirection){
            directionGroupe *= -1;
            for(Ennemi e : ennemis){
                if(e.isVivant() && !e.getEnAttaque()){
                    e.setDirection(directionGroupe);
                }
            }
        }
    }

    /** 
     * Met à jour les projectiles de la formation, en supprimant ceux qui sont morts
     */
    private void updateProjectiles() {
        for(int i = projectiles.size() - 1; i >= 0; i--){
            Projectile p = projectiles.get(i);
            p.update();
            if(!p.isVivant()){
                Ennemi e = p.getTireur();
                if(e!=null) e.setMissileEnCours(false);
                projectiles.remove(i);
            }
        }
    }

    /**
     * 
     * @return true si tous les ennemis sont morts, sinon false
     */

    public boolean ennemisTousMorts(){
        for(Ennemi ennemi : ennemis){
            if(ennemi.isVivant()){
                return false;
            }
        }
        return true;
    }


    /**
     * Dessine tous les ennemis et leurs projectiles sur la fenêtre 
     */
    public void draw(){
        for(Ennemi e : ennemis){
            if(e.getEstVivant()){
                e.draw();
            }
        }
        for(Projectile p : projectiles){
            p.draw();
        }
    }

    /**
     * Applique la trjectoire spécifique des ennemis de type BEE qui se déplacent en zigzag
     * @param e l'ennemi BEE
     */

    private void trajectoireBEE(Ennemi e){
        e.setX(e.getX() + e.getDirection() * e.getVitesse());

        if(e.getX() + e.getLargeur()/2 >= 1 || e.getX() - e.getLargeur()/2 <= 0){
            e.setDirection(-e.getDirection());
        }
        e.setY(e.getY() - e.getVitesse());
        if(e.getY() + e.getLargeur() / 2 <= 0.15){
            e.setVivant(false);
        }
    }

    /**
     * Applique la trajectoire des ennemis de type Butterfly et Moth qui se déplacent en ligne droite
     * @param e
     */
    private void trajectoireEnnemi(Ennemi e){
        e.setY(e.getY() - e.getVitesse());

        if(e.getY() + e.getLargeur() / 2 <= 0.15){
            e.setVivant(false);
        }
    }


    /**
     * Met à jour l'état de la formation
     */
    public void update(){
        updateDirectionGroupe();
        getBasFormation();

        if(ennemiEnAttaque != null && !ennemiEnAttaque.getEstVivant()){
            ennemiEnAttaque = null;
        }

        for(Ennemi e : ennemis){
            if(e.getEstVivant()){
                if(e.getEnAttaque()){
                    if(e.getTypeEnnemi() == TypeEnnemi.BEE){
                        trajectoireBEE(e);
                    }
                    else{
                        trajectoireEnnemi(e);
                    }
                }

                if (e.getTypeEnnemi() == TypeEnnemi.MOTH && joueurCapture == null && joueur != null ){
                    if (e.isInCollisionWithPlayer(joueur) && level.canEnnemiAttaque()) {
                        e.capturerJoueur(joueur);
                        level.resetAttaqueCoolDown();
                        joueurCapture = joueur;
                        joueurCapture.setVivant(false);
                        joueurCapture.setNbVies(joueurCapture.getNbVies() - 1);
                    }
                } 
                if(!e.getEnAttaque()){
                    e.setDirection(directionGroupe);
                    e.setX(e.getX() + directionGroupe * vitesseGroupe);
                }

                if(e.getEnAttaque() || derniereLigne.contains(e)){
                    tirEnnemi(e);
                }
                e.update(); 
            }
        }
        updateProjectiles();
        timerMissileNiveau3();
        level.updateCoolDowns();

        if(ennemiEnAttaque == null || !ennemiEnAttaque.getEstVivant()){
            choisirEnnemiBasFormation();
        }
    }
}

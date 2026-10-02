package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant le chargement et l'initialisation d'un niveau 
 */
public class Level {
    private int numerolevel;
    private long startTime;
    private boolean afficheNom;
    private int coolDownAttaque;
    private int coolDownMissile;
    private int timerAttaque = 0; 
    private int timerMissile = 0;
    private static int MAX_LEVEL = 3;

    /**
     * Constructeur pour initialiser un niveau à partir de son numéro, ainsi que les cooldowns.
     * @param numerolevel le numéro du niveau 
     */

    public Level(int numerolevel){
        this.numerolevel = numerolevel;
        this.startTime = System.currentTimeMillis();
        this.afficheNom = true;
        lireCooldowns();
    }

    /**
     * Affiche le nom du niveau pendant 2 secondes
     */
    public void afficherNomLevel(){
        startTime = System.currentTimeMillis();
        afficheNom = true;
    }

    /**
     * Renvoie le temps de recharge entre les tirs de missiles ennemis
     * @return le cooldown des missiles
     */
    public int getCoolDownMissile(){
        return coolDownMissile;
    }

    /**
     * Indique si le nom du niveau doit s'afficher
     * @return true si le nom doit être affiché, false sinon
     */
    public boolean getAfficheNom(){
        return afficheNom && (System.currentTimeMillis() - startTime < 2000);
    }

    /**
     * Met à jour l'affichage du niveau
     */
    public void updateLevel(){
        if(System.currentTimeMillis() - startTime >= 2000){
            afficheNom = false;
        }
    }

    /**
     * Réintialise le niveau à 1 et remet à 0 tous les timers
     */
    public void resetLevel(){
        numerolevel = 1;
        startTime = System.currentTimeMillis();
        afficheNom = true;
        lireCooldowns();
        timerAttaque = 0;
        timerMissile = 0;
    }

    /**
     * Renvoie le numéro actuel du niveau
     * @return le numéro du niveau
     */
    public int getLevel(){
        return numerolevel;
    }

    /**
     * Passe au niveau supérieur
     */
    public void setLevel(){
        if(numerolevel < MAX_LEVEL){
            numerolevel++;
        }
    }

    /**
     * Lit un fichier de niveau et renvoie la liste des lignes
     * @param Nlevel le numéro du niveau à lire
     * @return la liste des lignes du fichier de niveau
     */
    public List lecture_niveau(int Nlevel){
        String numLevel = "level" + Nlevel;
        List<String> listLevel = new ArrayList<>();
        Path cheminFichier = Path.of("ressources/levels/" + numLevel + ".lvl");
        try (BufferedReader reader = Files.newBufferedReader(cheminFichier)) {
            String line; 
            while ((line = reader.readLine()) != null){
                listLevel.add(line);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return listLevel;
    }


    /**
     *
     * @param i indice de la line
     * @return la ligne correspondant à l'indice i sous forme d'une liste 
     */
    public String[] chargement_ligne(int i){
        List<String> listLevel = lecture_niveau(numerolevel);
        return listLevel.get(i).split(" ");
    }

    /**
     * Lit les cooldowns d'attaque et de missile fournit par le fichier de niveau sur la première ligne
     */
    private void lireCooldowns(){
        String[] ligneLevel = chargement_ligne(0);
        this.coolDownAttaque = Integer.parseInt(ligneLevel[2]);
        this.coolDownMissile = Integer.parseInt(ligneLevel[3]);
    }

    /**
     * Détermine si un ennemi peut attaquer à partir de son cooldown
     * @return true si l'ennemi peut attaquer, false sinon
     */
    public boolean canEnnemiAttaque(){
        return coolDownAttaque == -1 || timerAttaque == 0;
    }

    /**
     * Réintialise le cooldown d'attaque
     */
    public void resetAttaqueCoolDown(){
        if(coolDownAttaque > 0){
            timerAttaque = coolDownAttaque;
        }
    }

    /**
     * Détermine s'il est possible de tirer un missile
     * @return true s'il est possible de tirer un missile, false sinon
     */
    public boolean canMissileShoot(){
        return coolDownMissile == -1 || timerMissile == 0;
    }

    /**
     * Réintialise le cooldown des missiles
     */
    public void resetMissileCooldown(){
        if(coolDownMissile > 0){
            timerMissile = coolDownMissile;
        }
    }

    /**
     * Met à jour les cooldowns à chaque frame
     */
    public void updateCoolDowns(){
        if(timerAttaque > 0){
            timerAttaque --;
        }
        if(timerMissile > 0){
            timerMissile--;
        }
    }
}
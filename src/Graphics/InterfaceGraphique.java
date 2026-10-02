package Graphics;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import engine.StdDraw;
import game.actors.FormationEnnemi;
import game.actors.Joueur;
import game.Level;


/**
 * Classe représentant l'interface graphique du jeu : score, high score, vies restantes, niveau
 */
public class InterfaceGraphique {
    private Joueur joueur;
    private FormationEnnemi ennemis;
    private int meilleurScore;
    private Level level;

    /**
     * 
     * @param joueur
     * @param level
     * @param ennemis
     */
    public InterfaceGraphique(Joueur joueur, Level level, FormationEnnemi ennemis){
        this.joueur = joueur;
        this.meilleurScore = getMeilleurScore();
        this.level = level;
        this.ennemis = ennemis;
    }

    /**
     * lit le highscore depuis le fichier fournni
     * @return le meilleur score enregistré
     */

    public int getMeilleurScore(){
        Path cheminFichier = Path.of("ressources/highscore/highscore.sc");

        try (BufferedReader reader = Files.newBufferedReader(cheminFichier)) {
            String ligne = reader.readLine();
            if(ligne !=null){
                meilleurScore = Integer.parseInt(ligne);
            }

        } catch (IOException e) {
            e.printStackTrace();;
        }
        return meilleurScore;
    }

    /**
     * Mise à jour du high score si le joueur fait un meilleur score et est enregistré dans le fichier
     */
    public void updateMeilleurScore(){
        Path cheminFichier = Path.of("ressources/highscore/highscore.sc");
        int score = joueur.getScore();

        if(score > meilleurScore){
            meilleurScore = score;

            try (BufferedWriter writer = Files.newBufferedWriter(cheminFichier)) {
                writer.write(String.valueOf(meilleurScore));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Dessine tous les éléments liés à l'interface graphique
     */

    public void draw(){
        Font font = new Font("Arial", Font.BOLD, 25);
        StdDraw.setFont(font);



        if(level.getAfficheNom()){
            if(level.getLevel() <= 3){
                StdDraw.setPenColor(Color.RED);
                StdDraw.text(0.5, 0.5, "LEVEL"+level.getLevel());
            }
        }

        boolean gameOver = joueur.getNbVies() <= 0;
        boolean partieFinie = (level.getLevel() == 3 && ennemis.ennemisTousMorts() && (joueur.getNbVies() > 0));


        if(gameOver){
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(0.5, 0.5, "Game Over... Espace pour rejouer ! ");
        }

        if(partieFinie){
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(0.5, 0.5, "Bravo ! Espace pour rejouer ! ");
        }

        
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(0.05, 0.95, ""+joueur.getScore());

        StdDraw.setPenColor(Color.RED);
        StdDraw.text(0.5, 0.98, "High Score");

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(0.5, 0.94, ""+meilleurScore);

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.line(0, 0.92, 1.0, 0.92);

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.line(0, 0.1, 1.0, 0.1);
        Sprite vaisseau;
        Path cheminFichierVaisseau = Path.of("ressources/sprites/ship.spr");
        vaisseau = new Sprite(cheminFichierVaisseau, 0.003);

        int nbVies = joueur.getNbVies();
        for(int i = 0; i < nbVies; i++){
            double x = 0.05 + i * 0.06;
            vaisseau.draw(x, 0.05);
        }

    }

}

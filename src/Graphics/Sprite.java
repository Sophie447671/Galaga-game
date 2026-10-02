package Graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import engine.StdDraw;
import java.awt.Color;

/**
 * Représente et dessine un sprite à partir d'un fichier texte 
 */


public class Sprite {
    private List<String> sprite;
    private double pixelSize;
    private int width;
    private int height;

    /**
     * 
     * @param cheminFichier
     * @param pixelSize
     */

    public Sprite(Path cheminFichier, double pixelSize){
        this.sprite = readSprite(cheminFichier);
        this.pixelSize = pixelSize;
        this.height = sprite.size();
        this.width = sprite.get(0).length();
    }

    /**
     * Lit un fichier texte correspondant au sprite et retourne chaque ligne dans une ArrayList
     * @param cheminFichier
     * @return
     */
    public static List readSprite(Path cheminFichier){ 
            List<String> sprite = new ArrayList<>();
            try (BufferedReader reader = Files.newBufferedReader(cheminFichier)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sprite.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();;
            }
            return sprite;
    }

    /**
     * 
     * @return le nombre de caractères par ligne
     */

    public int getWidth(){
        return width;
    }

    /**
     * 
     * @return le nombre de lignes
     */
    public int getHeight(){
        return height;
    }

    /**
     * 
     * @return la taille d'un pixel 
     */

    public double getPixelSize(){
        return pixelSize;
    }

    /**
     * Retourne la couleur associée à un caractère du sprite
     * @param c
     * @return la couleur correspondante ou null si le caractère n'existe pas
     */
    public Color getColor(char c){
        if(c == 'R'){
            return StdDraw.RED;
        }
        else if(c == 'B'){
            return StdDraw.BLUE;
        }
        else if(c == 'G'){
            return StdDraw.GREEN;
        }
        else if(c == 'Y'){
            return StdDraw.YELLOW;
        }
        else if(c == 'W'){
            return StdDraw.WHITE;
        }
        else if(c=='N'){
            return StdDraw.BLACK;
        }
        return null;
    }

    /**
     * Dessine le sprite à l'écran
     * @param x la position du sprite sur l'axe x
     * @param y la position du sprite sur l'axe y
     */

    public void draw(double x, double y){
        double hauteur = sprite.size();
        double largeur = sprite.get(0).length();
        for(int i = 0; i < sprite.size(); i++){
            String ligne = sprite.get(i);
            for(int j = 0; j < ligne.length(); j++){
                Color color = getColor(ligne.charAt(j));
                StdDraw.setPenColor(color);
                double drawX = x - largeur/2 * pixelSize + j * pixelSize;
                double drawY = y + hauteur/2 * pixelSize - i * pixelSize;
                StdDraw.filledRectangle(drawX, drawY, pixelSize/2, pixelSize/2);
                StdDraw.filledRectangle(drawX, drawY, pixelSize/2, pixelSize/2);
            }
        }
    }

}


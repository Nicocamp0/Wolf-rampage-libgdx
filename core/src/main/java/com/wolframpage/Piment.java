package com.wolframpage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

/**
 * Classe représentant un piment dans le jeu
 * Cette entité est un objet qui peut être ramassé par le joueur
 */
public class Piment extends Entity {
    /** État d'activation du piment */
    private boolean isActive = true;

    /**
     * Constructeur du piment
     * @param x Position initiale X
     * @param y Position initiale Y 
     * @param minX Limite minimale X de la zone de réapparition
     * @param maxX Limite maximale X de la zone de réapparition
     * @param minY Limite minimale Y de la zone de réapparition
     * @param maxY Limite maximale Y de la zone de réapparition
     */
    public Piment(float x, float y, float minX, float maxX, float minY, float maxY) {
        super(x, y, minX, maxX, minY, maxY); // constructeur clase Entity
    }

    /**
     * Retourne l'état d'activation du piment
     * @return true si le piment est actif, false sinon
     */
    public boolean isActive() {
        return isActive;
    }
    
    @Override
    public void disparaitre() {
        estVivant = false;
        tempsRestant = MathUtils.random(10.0f, 20.0f);
    }

    @Override
    public void mettreAJour(float delta, Vector2 playerPosition) {
        if (!estVivant()) {
            super.mettreAJour(delta); // Appeler la méthode de réapparition
        }
    }
}

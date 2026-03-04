package com.wolframpage;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Classe représentant un mouton dans le jeu
 * Cette entité est un objet qui peut disparaître et réapparaître
 */
public class Mouton extends Entity {

    // Constructeur qui passe les paramètres nécessaires à la classe parente Entity
    public Mouton(float x, float y, float minX, float maxX, float minY, float maxY) {
        super(x, y, minX, maxX, minY, maxY); // Appel du constructeur de la classe Entity
    }

    // Implémentation de la méthode abstraite mettreAJour
    @Override
    public void mettreAJour(float delta, Vector2 playerPosition) {
        if (!estVivant()) {
            super.mettreAJour(delta); // Appeler la méthode de réapparition si nécessaire
        }
    }
}

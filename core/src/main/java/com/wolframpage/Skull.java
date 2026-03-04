package com.wolframpage;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Classe représentant un crâne dans le jeu
 * Cette entité est un ennemi qui peut disparaître et réapparaître
 */
public class Skull extends Entity {

    // Constructeur qui passe les paramètres nécessaires à la classe parente Entity
    public Skull(float x, float y, float minX, float maxX, float minY, float maxY) {
        super(x, y, minX, maxX, minY, maxY); // Appel du constructeur de la classe Entity
    }

    // Implémentation de la méthode abstraite mettreAJour
    @Override
    public void mettreAJour(float delta, Vector2 playerPosition) {
        if (!estVivant()) {
            super.mettreAJour(delta); // Appeler la méthode de réapparition si nécessaire
        }
    }

    /**
     * Fait disparaître le crâne avec un temps de réapparition plus long
     * que les autres entités (entre 4 et 9 secondes)
     */
    @Override
    public void disparaitre() {
        estVivant = false;
        tempsRestant = MathUtils.random(4.0f, 9.0f);
    }
}
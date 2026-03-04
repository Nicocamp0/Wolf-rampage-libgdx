package com.wolframpage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

/**
 * Classe abstraite représentant une entité dans le jeu
 */
public abstract class Entity {
    /** Position de l'entité */
    protected Vector2 position;
    /** État de vie de l'entité */
    protected boolean estVivant;
    /** Temps restant avant la réapparition */
    protected float tempsRestant;

    // Définition de la zone de réapparition
    /** Coordonnée X minimum de la zone de réapparition */
    protected float minX;
    /** Coordonnée X maximum de la zone de réapparition */
    protected float maxX;
    /** Coordonnée Y minimum de la zone de réapparition */
    protected float minY;
    /** Coordonnée Y maximum de la zone de réapparition */
    protected float maxY;

    /**
     * Constructeur de l'entité
     * @param x Position initiale X
     * @param y Position initiale Y
     * @param minX Limite minimale X de la zone de réapparition
     * @param maxX Limite maximale X de la zone de réapparition
     * @param minY Limite minimale Y de la zone de réapparition
     * @param maxY Limite maximale Y de la zone de réapparition
     */
    public Entity(float x, float y, float minX, float maxX, float minY, float maxY) {
        position = new Vector2(x, y);
        estVivant = true;
        tempsRestant = 0;

        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    /**
     * Fait disparaître l'entité et initialise un temps de réapparition aléatoire
     */
    public void disparaitre() {
        estVivant = false;
        tempsRestant = MathUtils.random(1.0f, 6.0f);
    }

    /**
     * Met à jour l'état de l'entité
     * @param delta Temps écoulé depuis la dernière mise à jour
     */
    public void mettreAJour(float delta) {
        if (!estVivant) {
            tempsRestant -= delta;  // Décrémenter le temps restant
            if (tempsRestant <= 0) {
                // Réapparaître à une position aléatoire dans la zone spécifiée
                float x = MathUtils.random(minX, maxX);
                float y = MathUtils.random(minY, maxY);
                reaparaitre(x, y);
            }
        }
    }

    /**
     * Fait réapparaître l'entité à une position donnée
     * @param x Nouvelle position X
     * @param y Nouvelle position Y
     */
    public void reaparaitre(float x, float y) {
        position.set(x, y);
        estVivant = true;
        tempsRestant = 0;
    }

    /**
     * @return La position actuelle de l'entité
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * @return true si l'entité est vivante, false sinon
     */
    public boolean estVivant() {
        return estVivant;
    }

    /**
     * @return Le temps restant avant la réapparition
     */
    public float getTempsRestant() {
        return tempsRestant;
    }

    // Méthode abstraite pour que chaque sous-classe l'implémente selon ses spécifications
    public abstract void mettreAJour(float delta, Vector2 playerPosition);
}
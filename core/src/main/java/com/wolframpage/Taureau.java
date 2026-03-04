package com.wolframpage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Classe représentant un taureau dans le jeu
 * Cette entité est un ennemi qui charge le joueur quand il est dans son champ de vision
 */
public class Taureau extends Entity {
    private String direction;
    private Texture texture;
    private float speed; // Vitesse du taureau
    private float initialX, initialY; // Variables pour stocker la position initiale du taureau

    public Taureau(float x, float y, String direction) {
        super(x, y, 0, 0, 0, 0);
        this.initialX = x; // Initialisation de la position initiale
        this.initialY = y;
        this.direction = direction;

        // Choisir la texture en fonction de la direction
        switch (direction) {
            case "north":
                this.texture = new Texture("bull1.png");
                break;
            case "east":
                this.texture = new Texture("bull4.png");
                break;
            case "south":
                this.texture = new Texture("bull3.png");
                break;
            case "west":
                this.texture = new Texture("bull2.png");
                break;
        }

        this.speed = 150; 
        increaseSpeed(); // Appliquer la vitesse multipliée par 4
    }

    // Augmenter la vitesse du taureau pour qu'il aille 4 fois plus vite
    public void increaseSpeed() {
        this.speed *= 4; // Multiplie la vitesse par 4
    }

    public Texture getTexture() {
        return texture;
    }

    @Override
    public void mettreAJour(float delta, Vector2 playerPosition) {
        if (!estVivant()) {
            super.mettreAJour(delta); // Appeler la méthode de réapparition
            return;
        }

        float speedDelta = speed * delta; // Calcul de la vitesse basée sur delta

        // Logique pour déplacer le taureau en fonction de sa direction
        switch (direction) {
            case "north":
                if (playerPosition.x > position.x && playerPosition.x < position.x + 32 && playerPosition.y > position.y) {
                    position.y += speedDelta; // Le taureau fonce vers le joueur si le joueur est devant
                }
                break;
            case "east":
                if (playerPosition.x > position.x && playerPosition.y > position.y && playerPosition.y < position.y + 32) {
                    position.x += speedDelta; // Le taureau fonce vers le joueur si le joueur est devant
                }
                break;
            case "south":
                if (playerPosition.x > position.x && playerPosition.x < position.x + 32 && playerPosition.y < position.y) {
                    position.y -= speedDelta; // Le taureau fonce vers le joueur si le joueur est devant
                }
                break;
            case "west":
                if (playerPosition.x < position.x && playerPosition.y > position.y && playerPosition.y < position.y + 32) {
                    position.x -= speedDelta; // Le taureau fonce vers le joueur si le joueur est devant
                }
                break;
        }
    }

    // Méthode pour téléporter le taureau à sa position initiale
    public void teleportToInitialPosition() {
        position.set(initialX, initialY);
    }

    // Méthode pour libérer la texture lorsque l'objet n'est plus utilisé
    public void dispose() {
        if (texture != null) {
            texture.dispose(); // Libérer la ressource de la texture
        }
    }
}

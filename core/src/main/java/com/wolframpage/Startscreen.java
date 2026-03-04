package com.wolframpage;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Screen;

/**
 * Classe représentant l'écran de démarrage du jeu
 * Permet de choisir entre commencer une partie ou quitter le jeu
 */
public class Startscreen implements Screen {
    private final Main game;
    private Texture startImage;
    private SpriteBatch batch;

    // Variables pour stocker la position de l'image
    private float imageX, imageY;

    /**
     * Constructeur de l'écran de démarrage
     * @param game Référence vers le jeu principal
     */
    public Startscreen(Main game) {
        this.game = game;  // Référence au jeu principal
    }

    @Override
    public void show() {
        // Charger l'image de démarrage
        startImage = new Texture("start.png");
        batch = new SpriteBatch();

        // Calculer la position de l'image pour qu'elle soit centrée au début
        updateImagePosition();
    }

    @Override
    public void render(float delta) {
        // Effacer l'écran
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Dessiner l'image centrée à la position (imageX, imageY)
        batch.begin();
        batch.draw(startImage, imageX, imageY);
        batch.end();

        // Vérifier la position du clic
        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();  // Position X du clic

            // Si le clic est à gauche, arrêter le jeu
            if (touchX < Gdx.graphics.getWidth() / 2) {
                game.setScreen(new GameScreen(game,"map1.tmx"));  // Passer à l'écran de jeu
            } else {
                Gdx.app.exit();  // Arrêter le jeu
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        // Rien à faire ici, l'image reste fixe
    }

    // Méthode pour calculer la position de l'image centrée une fois au début
    private void updateImagePosition() {
        float windowWidth = Gdx.graphics.getWidth();
        float windowHeight = Gdx.graphics.getHeight();

        // Centrer l'image dans la fenêtre sans la redimensionner
        imageX = (windowWidth - startImage.getWidth()) / 2f;
        imageY = (windowHeight - startImage.getHeight()) / 2f;
    }

    @Override
    public void hide() {
        startImage.dispose();  // Libérer les ressources de l'image
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        startImage.dispose();  // Libérer les ressources
        batch.dispose();       // Libérer le batch
    }
}
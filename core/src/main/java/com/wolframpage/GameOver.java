package com.wolframpage;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Screen;

/**
 * Classe représentant l'écran de fin de jeu (Game Over)
 * Permet de choisir entre recommencer une partie ou quitter le jeu
 */
public class GameOver implements Screen {
    private final Main game;
    private Texture gameOverImage;
    private SpriteBatch batch;

    // Variables pour stocker la position de l'image
    private float imageX, imageY;

    /**
     * Constructeur de l'écran de fin de jeu
     * @param game Référence vers le jeu principal
     */
    public GameOver(Main game) {
        this.game = game;  // Référence au jeu principal
    }

    @Override
    public void show() {
        // Charger l'image de fin de jeu
        gameOverImage = new Texture("gameover.png");
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
        batch.draw(gameOverImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Vérifier la position du clic
        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();  // Position X du clic

            // Si le clic est à gauche, recommencer le jeu
            if (touchX < Gdx.graphics.getWidth() / 2) {
                game.setScreen(new GameScreen(game, "map1.tmx"));  // Passer à l'écran de jeu
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
        imageX = (windowWidth - gameOverImage.getWidth()) / 2f;
        imageY = (windowHeight - gameOverImage.getHeight()) / 2f;
    }

    @Override
    public void hide() {
        gameOverImage.dispose();  // Libérer les ressources de l'image
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        gameOverImage.dispose();  // Libérer les ressources
        batch.dispose();          // Libérer le batch
    }
}

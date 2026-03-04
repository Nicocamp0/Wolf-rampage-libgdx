package com.wolframpage;

import com.badlogic.gdx.Game;

/**
 * Classe principale du jeu qui hérite de Game
 */
public class Main extends Game {

    @Override
    public void create() {
        this.setScreen(new Startscreen(this));  // Démarrer avec l'écran de démarrage
    }
}

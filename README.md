# Wolf Rampage – LibGDX Game Prototype

2D game prototype developed with **LibGDX**.

The project explores core concepts of game development such as:

- entity systems
- collision handling
- tiled map rendering
- game state management
- real-time update loops

---

# Overview

In **Wolf Rampage**, the player controls a wolf moving through different maps populated with various entities such as animals and obstacles.

The project focuses on implementing the core architecture of a small 2D game using LibGDX.

---

# Features

- Player movement and entity interactions
- Multiple game screens (start screen, gameplay, game over)
- Tile-based maps using **Tiled (.tmx)**
- Multiple entities (wolf, sheep, bull, etc.)
- Sprite rendering and animation
- Collision detection
- Game state management

---

# Technologies

- **Java**
- **LibGDX**
- **LWJGL3 backend**
- **Tiled Map Editor**

---

# Project Structure

```
core/       main game logic and entities
lwjgl3/     desktop launcher
assets/     sprites, maps and textures
```

Important classes include:

- `Main.java` – game entry point
- `GameScreen.java` – main gameplay loop
- `Startscreen.java` – start menu
- `GameOver.java` – game over screen
- `Entity.java` – base class for game entities

---

# Running the project

From the project root:

```
./gradlew lwjgl3:run
```

or on Windows:

```
gradlew.bat lwjgl3:run
```

This launches the desktop version of the game.

---

# Assets

Maps are created using **Tiled** and stored in `.tmx` format.  
Sprites and tilesets are located in the `assets/` directory.

---

# Author

Nicolas Ventadoux  
Master's student in Computer Science – Image & 3D  
University of Strasbourg

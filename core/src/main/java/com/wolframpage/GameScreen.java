// Classe principale du jeu qui gère l'affichage et la logique du jeu
package com.wolframpage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject; 
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Color;

public class GameScreen implements Screen {
    // Instance du jeu principal
    private final Main game;
    // Carte du jeu et son rendu
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    
    // Dimensions de la carte
    private float mapWidth, mapHeight;
    // Texture et position du joueur
    private Texture playerTexture;
    private Vector2 playerPosition;
    private float speed;

    // Tableaux des différents éléments du jeu
    private Array<Rectangle> collisionAreas;
    private Array<Mouton> moutons;
    private Array<Skull> skulls;
    private Array<Piment> piments;
    private Array<Taureau> taureaux;
    private BitmapFont font;
    private int score;
    private float timer;

    // Gestion de la pause
    private boolean isPaused;
    private Texture pauseTexture;

    // Gestion des zones de boue et de la vitesse
    private Array<Polygon> bouePolygons;
    private float normalSpeed = 200f;
    private float speedModifier = 200f;
    private float mudTimer = 0f;
    private float speedMultiplierDuration =0f;

    // Fichier de la carte courante
    private String currentMapFile;

    public GameScreen(Main game, String mapFile) {
        this.game = game;
        this.currentMapFile = mapFile;
        loadMap(mapFile);
    }
    
    public String getMapfile() {
    	return currentMapFile;
    }

    private void loadMap(String mapFile) {
        tiledMap = new TmxMapLoader().load(mapFile);
        mapWidth = tiledMap.getProperties().get("width", Integer.class) * tiledMap.getProperties().get("tilewidth", Integer.class);
        mapHeight = tiledMap.getProperties().get("height", Integer.class) * tiledMap.getProperties().get("tileheight", Integer.class);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, mapWidth, mapHeight);  // La caméra doit être dimensionnée sur la taille de la carte

        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        batch = new SpriteBatch();

        playerTexture = new Texture("wolf.png");

        font = new BitmapFont();
        font.setColor(Color.WHITE);

        score = 0;
        timer = 60;  

        // Initialiser la gestion de la pause
        isPaused = false;
        pauseTexture = new Texture("pause.png");

        loadEntities();

        // Charger les zones de boue
        bouePolygons = new Array<>();
        MapLayer mudLayer = tiledMap.getLayers().get("boue");
        if (mudLayer != null) {
            for (MapObject object : mudLayer.getObjects()) {
                if (object instanceof PolygonMapObject) {
                    Polygon polygon = ((PolygonMapObject) object).getPolygon();
                    bouePolygons.add(polygon);
                }
            }
        }
    }

    private void loadEntities() {
        collisionAreas = new Array<>();
        moutons = new Array<>();
        taureaux = new Array<>();
        skulls = new Array<>();
        piments= new Array<>();

        // Chargement des zones de collision
        MapLayer collisionLayer = tiledMap.getLayers().get("collision");
        if (collisionLayer != null) {
            for (MapObject object : collisionLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
                    collisionAreas.add(rect);
                }
            }
        }

        // Chargement des moutons avec la zone polygone
        MapLayer sheepLayer = tiledMap.getLayers().get("entités");
        if (sheepLayer != null) {
            for (MapObject object : sheepLayer.getObjects()) {
                if (object instanceof PolygonMapObject) {
                    Polygon polygon = ((PolygonMapObject) object).getPolygon();
                    float[] vertices = polygon.getTransformedVertices();
                    float minX = Float.MAX_VALUE, maxX = Float.MIN_VALUE;
                    float minY = Float.MAX_VALUE, maxY = Float.MIN_VALUE;

                    // Calcul des coordonnées min et max
                    for (int i = 0; i < vertices.length; i += 2) {
                        minX = Math.min(minX, vertices[i]);
                        maxX = Math.max(maxX, vertices[i]);
                        minY = Math.min(minY, vertices[i + 1]);
                        maxY = Math.max(maxY, vertices[i + 1]);
                    }

                    // Créer des moutons dans la zone polygone
                    for (int i = 0; i < 15; i++) {
                        float randomX = MathUtils.random(minX, maxX);
                        float randomY = MathUtils.random(minY, maxY);

                        if (polygon.contains(randomX, randomY)) {
                            moutons.add(new Mouton(randomX, randomY, minX, maxX, minY, maxY));
                        }
                    }
                }
            }
        }
        
        MapLayer skullLayer = tiledMap.getLayers().get("entités");
        if (skullLayer != null) {
            for (MapObject object : skullLayer.getObjects()) {
                if (object instanceof PolygonMapObject) {
                    Polygon polygon = ((PolygonMapObject) object).getPolygon();
                    float[] vertices = polygon.getTransformedVertices();
                    float minX = Float.MAX_VALUE, maxX = Float.MIN_VALUE;
                    float minY = Float.MAX_VALUE, maxY = Float.MIN_VALUE;

                    // Calcul des coordonnées min et max
                    for (int i = 0; i < vertices.length; i += 2) {
                        minX = Math.min(minX, vertices[i]);
                        maxX = Math.max(maxX, vertices[i]);
                        minY = Math.min(minY, vertices[i + 1]);
                        maxY = Math.max(maxY, vertices[i + 1]);
                    }

                    // Créer des moutons dans la zone polygone
                    for (int i = 0; i < 4; i++) {
                        float randomX = MathUtils.random(minX, maxX);
                        float randomY = MathUtils.random(minY, maxY);

                        if (polygon.contains(randomX, randomY)) {
                            skulls.add(new Skull(randomX, randomY, minX, maxX, minY, maxY));
                        }
                    }
                }
            }
        }

        MapLayer pimentLayer = tiledMap.getLayers().get("entités");
        if (pimentLayer != null) {
            for (MapObject object : pimentLayer.getObjects()) {
                if (object instanceof PolygonMapObject) {
                    Polygon polygon = ((PolygonMapObject) object).getPolygon();
                    float[] vertices = polygon.getTransformedVertices();
                    float minX = Float.MAX_VALUE, maxX = Float.MIN_VALUE;
                    float minY = Float.MAX_VALUE, maxY = Float.MIN_VALUE;

                    // Calcul des coordonnées min et max
                    for (int i = 0; i < vertices.length; i += 2) {
                        minX = Math.min(minX, vertices[i]);
                        maxX = Math.max(maxX, vertices[i]);
                        minY = Math.min(minY, vertices[i + 1]);
                        maxY = Math.max(maxY, vertices[i + 1]);
                    }

                    // Créer des piments dans la zone polygone
                    for (int i = 0; i < 2; i++) {  // Ajouter 2 piments par zone
                        float randomX = MathUtils.random(minX, maxX);
                        float randomY = MathUtils.random(minY, maxY);

                        if (polygon.contains(randomX, randomY)) {
                            piments.add(new Piment(randomX, randomY, minX, maxX, minY, maxY));
                        }
                    }
                }
            }
        }
        // Chargement des taureaux
        MapLayer bullLayer = tiledMap.getLayers().get("entités");
        if (bullLayer != null) {
            for (MapObject object : bullLayer.getObjects()) {
            	if ("bull".equals(object.getProperties().get("type", String.class))) {
	                if (object instanceof RectangleMapObject) {
	                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
	                    String direction = object.getProperties().get("direction", String.class);
	                    taureaux.add(new Taureau(rect.x, rect.y, direction));
	                }
            	}
            }
        }

        // Chargement du joueur
        MapLayer entitiesLayer = tiledMap.getLayers().get("entités");
        if (entitiesLayer != null) {
            for (MapObject object : entitiesLayer.getObjects()) {
                if ("player".equals(object.getProperties().get("type", String.class))) {
                    if (object instanceof RectangleMapObject) {
                        Rectangle rect = ((RectangleMapObject) object).getRectangle();
                        playerPosition = new Vector2(rect.x, rect.y);
                    }
                }
            }
        }

        if (playerPosition == null) {
            throw new IllegalStateException("Aucun joueur trouvé dans la couche 'entités' !");
        }
    }

    private boolean isBlocked(float x, float y) {
        for (Rectangle rect : collisionAreas) {
            if (rect.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCollision(Vector2 playerPosition, Mouton mouton) {
        float moutonX = mouton.getPosition().x;
        float moutonY = mouton.getPosition().y;

        // Détecter la collision entre le joueur et le mouton
        return playerPosition.x < moutonX + 32 && playerPosition.x + 32 > moutonX &&
               playerPosition.y < moutonY + 32 && playerPosition.y + 32 > moutonY;
    }

    private boolean isCollision(Vector2 playerPosition, Taureau taureau) {
        float taureauX = taureau.getPosition().x;
        float taureauY = taureau.getPosition().y;

        // Détecter la collision entre le joueur et le taureau
        return playerPosition.x < taureauX + 32 && playerPosition.x + 32 > taureauX &&
               playerPosition.y < taureauY + 32 && playerPosition.y + 32 > taureauY;
    }
    
    private boolean isCollision(Vector2 playerPosition, Skull skull) {
        float skullX = skull.getPosition().x;
        float skullY = skull.getPosition().y;
        return playerPosition.x < skullX + 32 && playerPosition.x + 32 > skullX &&
               playerPosition.y < skullY + 32 && playerPosition.y + 32 > skullY;
    }
    
    private boolean isCollision(Vector2 playerPosition, Piment piment) {
        float pimentX = piment.getPosition().x;
        float pimentY = piment.getPosition().y;
        return playerPosition.x < pimentX + 32 && playerPosition.x + 32 > pimentX &&
               playerPosition.y < pimentY + 32 && playerPosition.y + 32 > pimentY;
    }

    @Override
    public void render(float delta) {
    	if (speedMultiplierDuration > 0) {
            speedMultiplierDuration -= delta;  // Réduire la durée de l'effet
    	}
    	else if (speedMultiplierDuration <= 0 && mudTimer <=0) {
    			Gdx.app.log("speed", "reset");
                speed = normalSpeed;  // Réinitialiser la vitesse après la durée
                speedMultiplierDuration = 4f;  // Réinitialiser la durée pour les prochains piments
            }
    	
        // Mettre à jour le timer de la boue
        if (mudTimer > 0) {
            mudTimer -= delta; // Diminuer le timer de la boue
        } else if (speedMultiplierDuration<=0) {
            speed = normalSpeed; // Réinitialiser la vitesse si le timer est écoulé
        }

        // Si le joueur appuie sur ESC, on met le jeu en pause
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = !isPaused; // Bascule entre pause et jeu
        }

        if (isPaused) {
            ScreenUtils.clear(0, 0, 0, 1); // Efface l'écran en noir
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            batch.draw(pauseTexture, mapWidth / 2 - pauseTexture.getWidth() / 2, mapHeight / 2 - pauseTexture.getHeight() / 2);

            // Vérifier où l'utilisateur clique
            if (Gdx.input.isTouched()) {
                float touchX = Gdx.input.getX();
                float touchY = Gdx.input.getY();
                touchY = mapHeight - touchY; // Inverser l'axe Y pour correspondre à la coordonnée de l'écran

                
                if (touchY > mapHeight / 3) {
                    isPaused = false; // Reprendre le jeu
                }
                
                else if (touchY < mapHeight / 3 * 2 && touchX < mapWidth / 1.2) {
                    restartLevel(); // Réinitialiser le niveau
                }
                //Sinon on quitte le jeu
                else {
                    Gdx.app.exit();
                }
            }

            batch.end();
            return; // Ne pas mettre à jour le jeu pendant la pause
        }

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        handlePlayerMovement(delta);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(playerTexture, playerPosition.x, playerPosition.y, 32, 32);

        for (Mouton mouton : moutons) {
            if (mouton.estVivant() && isCollision(playerPosition, mouton)) {
                mouton.disparaitre();
                score += 10;
            }
            mouton.mettreAJour(delta);
            if (mouton.estVivant()) {
                batch.draw(new Texture("sheep.png"), mouton.getPosition().x, mouton.getPosition().y, 32, 32);
            }
        }
        
        for (Skull skull : skulls) {
            if (skull.estVivant() && isCollision(playerPosition, skull)) {
                skull.disparaitre();
                score -= 10;
            }
            skull.mettreAJour(delta);
            if (skull.estVivant()) {
                batch.draw(new Texture("skull.png"), skull.getPosition().x, skull.getPosition().y, 32, 32);
            }
        }

        for (Piment piment : piments) {
            if (piment.estVivant() && isCollision(playerPosition, piment)) {
            	speed = 300f;
                speedMultiplierDuration=4f;
                Gdx.app.log("piment touche", "speed: " + speed);
                Gdx.app.log("duré modif", "dure " + speedMultiplierDuration);
                piment.disparaitre();

            }
            piment.mettreAJour(delta, playerPosition);
            if (piment.estVivant()) {
                batch.draw(new Texture("pepper.png"), piment.getPosition().x, piment.getPosition().y, 32, 32);
            }
        }
        // Update and render bulls
        for (Taureau taureau : taureaux) {
            taureau.mettreAJour(delta, playerPosition);
            if (isCollision(playerPosition, taureau)) {
                taureau.teleportToInitialPosition(); // Téléporter le taureau en cas de collision
                if (score >= 20) {
                    score -= 20;
                } else {
                    score = 0;
                }
            }
            batch.draw(taureau.getTexture(), taureau.getPosition().x, taureau.getPosition().y, 32, 32);
        }

        // Afficher le score
        font.draw(batch, "Score: " + score, mapWidth - 150, mapHeight - 20);

        // Afficher le timer
        font.draw(batch, "Time: " + (int) timer, mapWidth / 2 - 30, mapHeight - 20);

        batch.end();

        // Mettre à jour le timer
        timer -= delta;
        if (timer <= 50) {
            if (currentMapFile.equals("map4.tmx")) {
                game.setScreen(new GameOver(game));
            } else if (score >= 20) {
                switchToNextMap();
            } else {
                game.setScreen(new GameOver(game));
            }
        }
    }

    private void handlePlayerMovement(float delta) {
        // Si le joueur est dans la boue, réduire la vitesse
        if (isInMud(playerPosition.x, playerPosition.y)) {
            if (speed >= 150f) {
                speed = normalSpeed/2;
                mudTimer = 1f;  // Timer pour la durée du ralentissement
            }
        } else {
            if (speed != normalSpeed && mudTimer==0 && speedMultiplierDuration==0) {
                speed = normalSpeed;  // Réinitialiser la vitesse à la normale si le joueur sort de la boue
            }
        }

        Vector2 newPosition = new Vector2(playerPosition);
        Gdx.app.log("speed", "Current speed: " + speed);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            newPosition.x -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            newPosition.x += speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            newPosition.y += speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            newPosition.y -= speed * delta;
        }

        // Vérification de la collision avec les obstacles
        if (!isBlocked(newPosition.x, newPosition.y)) {
            playerPosition.set(newPosition);
        }
    }

    // Vérification si le joueur est dans une zone de boue
    private boolean isInMud(float x, float y) {
        for (Polygon polygon : bouePolygons) {
            if (polygon.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    private void restartLevel() {
        // Recharger la carte et réinitialiser le joueur et les moutons
        tiledMap = new TmxMapLoader().load(getMapfile());s
        loadEntities(); // Recharge les entités (joueur, moutons, etc.)
        score = 0;
        timer = 60;  // Réinitialiser le timer
        isPaused = false; // Quitter la pause après le reset
    }
    private void switchToNextMap() {
        String nextMap = getNextMapFile(currentMapFile);
        if(nextMap=="map4.tmx") {
        	game.setScreen(new GameOver(game));
        }
        if (nextMap != null) {
            game.setScreen(new GameScreen(game, nextMap));
        } else {
            Gdx.app.log("GameScreen", "No next map found! Ending game.");
        }
    }

    private String getNextMapFile(String currentMap) {
        String baseName = currentMap.replace(".tmx", "");
        if (baseName.matches(".*\\d+")) {
            int currentNumber = Integer.parseInt(baseName.replaceAll("\\D", ""));
            String nextMap = baseName.replaceAll("\\d+", "") + (currentNumber + 1) + ".tmx";
            return Gdx.files.internal(nextMap).exists() ? nextMap : null;
        }
        return null;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void resume() {
        isPaused = false;
    }
    
    @Override
    public void show() {}

    @Override
    public void dispose() {
        // Libérer les ressources utilisées
        if (playerTexture != null) {
            playerTexture.dispose();
        }
        if (pauseTexture != null) {
            pauseTexture.dispose();
        }
        if (mapRenderer != null) {
            mapRenderer.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }
        if (font != null) {
            font.dispose();
        }

        for (Taureau taureau : taureaux) {
            taureau.dispose();
        }
    }

}

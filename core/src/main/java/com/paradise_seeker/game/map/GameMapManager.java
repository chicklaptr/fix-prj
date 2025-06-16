package com.paradise_seeker.game.map;

import java.util.List;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paradise_seeker.game.entity.player.Player;

public class GameMapManager {
    private GameMap currentMap;
    private final Player player;
    private final List<GameMap> maps = new ArrayList<>();
    private int currentMapIndex = 0;

    // List of music paths (make sure order matches maps list)
    private final String[] mapMusicPaths = {
        "music/map1.mp3", // Map 1
        "music/map2.mp3", // Map 2
        "music/map3.mp3", // Map 3
        "music/map4.mp3", // Map 4
        "music/map5.mp3"  // Map 5
    };

    public GameMapManager(Player player) {
        this.player = player;

        // No player in constructor!
        maps.add(new GameMap1());  // Map 1
        maps.add(new GameMap2());  // Map 2
        maps.add(new GameMap3());  // Map 3
        maps.add(new GameMap4());  // Map 4
        maps.add(new GameMap5());  // Map 5

        // Start with the first map
        setCurrentMap(0);
    }

    public String getCurrentMapMusic() {
        return mapMusicPaths[currentMapIndex];
    }

    public GameMap getCurrentMap() { return currentMap; }

    public void update(float delta) { currentMap.update(delta); }

    public void render(SpriteBatch batch) { currentMap.render(batch); }

    public void switchToNextMap() {
        int nextIndex = (currentMapIndex + 1) % maps.size();
        setCurrentMap(nextIndex);
    }

    public void switchToPreviousMap() {
        int prevIndex = (currentMapIndex - 1 + maps.size()) % maps.size();
        setCurrentMap(prevIndex);
    }

    // -- The crucial method: sets current map and loads player spawn from Tiled!
    private void setCurrentMap(int index) {
        currentMapIndex = index;
        currentMap = maps.get(currentMapIndex);
        currentMap.setPlayer(player);

        currentMap.loadSpawnPoints(player);
        
        currentMap.setCollisionSystem(new com.paradise_seeker.game.collision.CollisionSystem());
    }

	public int getCurrentMapIndex() {
		return currentMapIndex;
	}
}

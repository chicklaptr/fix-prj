package com.paradise_seeker.game.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.npc.Gipsy;
import com.paradise_seeker.game.map.GameMap;
import com.paradise_seeker.game.object.Book;

/**
 * Implementation của PlayerInputHandler
 * Chuyên trách xử lý tất cả input của Player
 */
public class PlayerInputHandlerManager implements PlayerInputHandler {
    public boolean showInteractMessage = false;

    public boolean isShowInteractMessage() {
        return showInteractMessage;
    }

    @Override
    public void handleInput(Player player, float deltaTime, GameMap gameMap) {
        if (player.isPaused() || player.isAttacking || player.isDead) return;

        // Check for interaction opportunities and set showInteractMessage
        checkForInteractions(player, gameMap);

        handleMovement(player, deltaTime, gameMap);
        handleDash(player, gameMap);
        handleAttack(player, gameMap);
        handleSkills(player);
        handleNPCInteraction(player, gameMap);
    }
    private void checkForInteractions(Player player, GameMap gameMap) {
        showInteractMessage = false; // Reset first

        if (gameMap == null) return;

        // Check for NPCs
        for (Gipsy npc : gameMap.getNPCs()) {
            float distance = calculateDistance(player, npc);
            if (distance <= 2.5f) {
                showInteractMessage = true;
                return; // Found an interaction, no need to check further
            }
        }

        // Check for books
        Book book = gameMap.getBook();
        if (book != null && book.isPlayerInRange(player) && !book.isOpened()) {
            showInteractMessage = true;
        }
    }
    @Override
    public void handleMovement(Player player, float deltaTime, GameMap gameMap) {
        float dx = 0, dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) dy += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;

        float len = (float) Math.sqrt(dx * dx + dy * dy);
        player.setMoving(len > 0);
        if (len > 0) {
            dx /= len;
            dy /= len;
            float moveX = dx * player.speed * deltaTime;
            float moveY = dy * player.speed * deltaTime;
            float nextX = player.getBounds().x + moveX;
            float nextY = player.getBounds().y + moveY;
            Rectangle nextBounds = new Rectangle(nextX, nextY, player.getBounds().width, player.getBounds().height);
            boolean blocked = false;
            if (gameMap != null && gameMap.collidables != null) {
                for (Collidable c : gameMap.collidables) {
                    if (c != player && nextBounds.overlaps(c.getBounds())) {
                    	 c.onCollision(player);  // Gọi để xử lý phụ (mở rương...)
                    	 if (c.isSolid()) {
                    		    player.blockMovement();
                    		    blocked = true;
                    		    break;
                    		}
                    }
                }
            }
            if (!blocked) {
                player.getBounds().x = nextX;
                player.getBounds().y = nextY;
                // Cập nhật hướng di chuyển
                if (Math.abs(dx) > Math.abs(dy)) {
                    player.setDirection(dx > 0 ? "right" : "left");
                } else {
                    player.setDirection(dy > 0 ? "up" : "down");
                }
            } else {
                player.setMoving(false);
            }
        }
        clampToMapBounds(player, gameMap);
    }
    @Override
    public void handleDash(Player player, GameMap gameMap) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) && player.getDashTimer() <= 0 && player.isMoving()) {
            float dx = 0, dy = 0;

            // Xác định hướng dash
            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) dy += 1;
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= 1;
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;

            float len = (float) Math.sqrt(dx * dx + dy * dy);

            if (len > 0) {
                float stepSize = 0.1f;
                float totalDash = 0f;
                float maxDash = player.getDashDistance();
                float prevX = player.getBounds().x;
                float prevY = player.getBounds().y;

                // Try to move in increments until hit something or finished full dash
                while (totalDash < maxDash) {
                    float nextX = player.getBounds().x + (dx / len) * stepSize;
                    float nextY = player.getBounds().y + (dy / len) * stepSize;
                    Rectangle nextBounds = new Rectangle(nextX, nextY, player.getBounds().width, player.getBounds().height);

                    if (gameMap == null || !gameMap.isBlocked(nextBounds)) {
                        player.getBounds().x = nextX;
                        player.getBounds().y = nextY;
                        totalDash += stepSize;
                    } else {
                        break;
                    }
                }
                player.setDashTimer(player.getDashCooldown());
                player.addSmoke(prevX, prevY);
            }
            clampToMapBounds(player, gameMap);
        }
    }

    public void damageMonstersInRange(float x, float y, float radius, float damage, GameMap gameMap) {
        for (Monster m : gameMap.getMonsters()) {
            if (!m.isDead() && isInRange(x, y, m.getBounds(), radius)) m.takeHit(damage);
        }
    }

    public boolean isInRange(float x, float y, Rectangle bounds, float radius) {
        float centerX = bounds.x + bounds.width / 2;
        float centerY = bounds.y + bounds.height / 2;
        float dx = centerX - x;
        float dy = centerY - y;
        return dx * dx + dy * dy <= radius * radius;
    }

    @Override
    public void handleAttack(Player player, GameMap gameMap) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.setAttacking(true);
    //        player.resetStateTime();

            if (gameMap != null) {
                float centerX = player.getBounds().x + player.getBounds().width / 2;
                float centerY = player.getBounds().y + player.getBounds().height / 2;
                damageMonstersInRange(centerX, centerY, 5f, player.getAtk(), gameMap);
            }
        }
    }


    @Override
    public void handleSkills(Player player) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            if (player.getMp() >= 2 && player.getPlayerSkill1().canUse(System.currentTimeMillis())) {
                player.setMp(player.getMp() - 2);
                player.getPlayerSkill1().castSkill(player.getAtk(), player.getBounds(), player.getDirection());
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            if (player.getMp() >= 2 && player.getPlayerSkill2().canUse(System.currentTimeMillis())) {
                player.setMp(player.getMp() - 2);
                player.getPlayerSkill2().castSkill(player.getAtk(), player.getBounds(), player.getDirection());
            }
        }
    }

    @Override
    public void handleNPCInteraction(Player player, GameMap gameMap) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F) && showInteractMessage && gameMap != null) {
            // Tìm NPC gần nhất để tương tác
            Gipsy nearestNPC = null;
            float minDistance = Float.MAX_VALUE;

            for (Gipsy npc : gameMap.getNPCs()) {
                float distance = calculateDistance(player, npc);
                if (distance <= 2.5f && distance < minDistance) {
                    minDistance = distance;
                    nearestNPC = npc;
                }
            }

            if (nearestNPC != null) {
                nearestNPC.openChest();
                // Có thể thêm các logic tương tác khác ở đây
            }
        }
    }

    private float calculateDistance(Player player, Gipsy npc) {
        return (float) Math.sqrt(
            Math.pow(player.getBounds().x + player.getBounds().width/2 - (npc.getBounds().x + npc.getBounds().width/2), 2) +
            Math.pow(player.getBounds().y + player.getBounds().height/2 - (npc.getBounds().y + npc.getBounds().height/2), 2)
        );
    }

    private void clampToMapBounds(Player player, GameMap gameMap) {
        if (gameMap == null) return;

        float minX = 0;
        float minY = 0;
        float maxX = gameMap.getMapWidth() - player.getBounds().width;
        float maxY = gameMap.getMapHeight() - player.getBounds().height;

        player.getBounds().x = Math.max(minX, Math.min(player.getBounds().x, maxX));
        player.getBounds().y = Math.max(minY, Math.min(player.getBounds().y, maxY));
    }
}

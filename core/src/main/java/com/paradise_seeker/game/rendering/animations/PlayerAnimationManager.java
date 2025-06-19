package com.paradise_seeker.game.rendering.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Implementation của PlayerAnimationManager
 * Chuyên trách loading và quản lý animation
 */
public class PlayerAnimationManager implements AnimationManager {

    private Animation<TextureRegion> runUp, runDown, runLeft, runRight;
    private Animation<TextureRegion> idleUp, idleDown, idleLeft, idleRight;
    private Animation<TextureRegion> attackUp, attackDown, attackLeft, attackRight;
    private Animation<TextureRegion> hitUp, hitDown, hitLeft, hitRight;
    private Animation<TextureRegion> deathAnimation;
    private Animation<TextureRegion> smokeAnimation;

    public void setAnimations() {
        // Load animation di chuyển
        runDown = loadAnimation("images/Entity/characters/player/char_run_down_anim_strip_6.png");
        runUp = loadAnimation("images/Entity/characters/player/char_run_up_anim_strip_6.png");
        runLeft = loadAnimation("images/Entity/characters/player/char_run_left_anim_strip_6.png");
        runRight = loadAnimation("images/Entity/characters/player/char_run_right_anim_strip_6.png");

        // Load animation đứng yên
        idleDown = loadAnimation("images/Entity/characters/player/char_idle_down_anim_strip_6.png");
        idleUp = loadAnimation("images/Entity/characters/player/char_idle_up_anim_strip_6.png");
        idleLeft = loadAnimation("images/Entity/characters/player/char_idle_left_anim_strip_6.png");
        idleRight = loadAnimation("images/Entity/characters/player/char_idle_right_anim_strip_6.png");

        // Load animation tấn công
        attackDown = loadAnimation("images/Entity/characters/player/attack_down_new.png");
        attackUp = loadAnimation("images/Entity/characters/player/attack_up_new.png");
        attackLeft = loadAnimation("images/Entity/characters/player/attack_left_new.png");
        attackRight = loadAnimation("images/Entity/characters/player/attack_right_new.png");


        // Load hit animations
        hitUp = loadAnimation("images/Entity/characters/player/char_hit_up_anim_strip_3.png", 3);
        hitDown = loadAnimation("images/Entity/characters/player/char_hit_down_anim_strip_3.png", 3);
        hitLeft = loadAnimation("images/Entity/characters/player/char_hit_left_anim_strip_3.png", 3);
        hitRight = loadAnimation("images/Entity/characters/player/char_hit_right_anim_strip_3.png", 3);

        // Load death animation
        deathAnimation = loadAnimation("images/Entity/characters/player/char_death_all_dir_anim_strip_10.png", 10);

        // Load smoke animation
        Texture smokeSheet = new Texture(Gdx.files.internal("images/spritesheet_smoke.png"));
        TextureRegion[] smokeFrames = TextureRegion.split(smokeSheet, smokeSheet.getWidth() / 6, smokeSheet.getHeight())[0];
        smokeAnimation = new Animation<>(0.08f, smokeFrames);
    }

    public Animation<TextureRegion> loadAnimation(String filePath) {
        Texture sheet = new Texture(Gdx.files.internal(filePath));
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / 6, sheet.getHeight());
        return new Animation<>(0.07f, tmp[0]);
    }

    public Animation<TextureRegion> loadAnimation(String filePath, int frameCount) {
        Texture sheet = new Texture(Gdx.files.internal(filePath));
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / frameCount, sheet.getHeight());
        return new Animation<>(0.07f, tmp[0]);
    }

    @Override
    public Animation<TextureRegion> getRunAnimation(String direction) {
        switch (direction) {
            case "up": return runUp;
            case "down": return runDown;
            case "left": return runLeft;
            case "right": return runRight;
            default: return runDown;
        }
    }

    @Override
    public Animation<TextureRegion> getIdleAnimation(String direction) {
        switch (direction) {
            case "up": return idleUp;
            case "down": return idleDown;
            case "left": return idleLeft;
            case "right": return idleRight;
            default: return idleDown;
        }
    }

    @Override
    public Animation<TextureRegion> getAttackAnimation(String direction) {
        switch (direction) {
            case "up": return attackUp;
            case "down": return attackDown;
            case "left": return attackLeft;
            case "right": return attackRight;
            default: return attackDown;
        }
    }

    @Override
    public Animation<TextureRegion> getHitAnimation(String direction) {
        switch (direction) {
            case "up": return hitUp;
            case "down": return hitDown;
            case "left": return hitLeft;
            case "right": return hitRight;
            default: return hitDown;
        }
    }

    @Override
    public Animation<TextureRegion> getDeathAnimation(String direction) {
        return deathAnimation;
    }

    public Animation<TextureRegion> getSmokeAnimation() {
        return smokeAnimation;
    }

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}


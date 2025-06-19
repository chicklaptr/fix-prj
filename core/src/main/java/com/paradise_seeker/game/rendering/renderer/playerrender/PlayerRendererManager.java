package com.paradise_seeker.game.rendering.renderer.playerrender;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.rendering.animations.PlayerAnimationManager;

public class PlayerRendererManager implements PlayerRenderer {
	public PlayerAnimationManager animationManager;

    public PlayerRendererManager(PlayerAnimationManager animationManager) {
        this.animationManager = animationManager;
    }

    @Override
    public void render(Player player, SpriteBatch batch) {
        if (player.statusManager.isDead()) {
            renderDeath(player, batch);
            return;
        }

        if (player.statusManager.isHit()) {
            renderHit(player, batch);
            return;
        }

        if (player.statusManager.isAttacking()) {
            renderAttack(player, batch);
        } else if (player.statusManager.isMoving()) {
            renderMovement(player, batch);
        } else {
            renderIdle(player, batch);
        }

        renderSmoke(player, batch);
    }

    @Override
    public void renderMovement(Player player, SpriteBatch batch) {
        Animation<TextureRegion> runAnimation = animationManager.getRunAnimation(player.statusManager.getDirection());
        TextureRegion currentFrame = runAnimation.getKeyFrame(player.statusManager.getStateTime(), true);
        batch.draw(currentFrame, player.getBounds().x, player.getBounds().y,
            player.getBounds().width, player.getBounds().height);
    }

    @Override
    public void renderIdle(Player player, SpriteBatch batch) {
        Animation<TextureRegion> idleAnimation = animationManager.getIdleAnimation(player.statusManager.getDirection());
        TextureRegion currentFrame = idleAnimation.getKeyFrame(player.statusManager.getStateTime(), true);
        batch.draw(currentFrame, player.getBounds().x, player.getBounds().y,
            player.getBounds().width, player.getBounds().height);
    }

    @Override
    public void renderAttack(Player player, SpriteBatch batch) {
        Animation<TextureRegion> attackAnimation = animationManager.getAttackAnimation(player.statusManager.getDirection());
        TextureRegion currentFrame = attackAnimation.getKeyFrame(player.statusManager.getStateTime(), false);

        // Phóng to khi tấn công
        float scaledWidth = player.getBounds().width * 3.0f;
        float scaledHeight = player.getBounds().height * 3.0f;
        float drawX = player.getBounds().x - (scaledWidth - player.getBounds().width) / 2f;
        float drawY = player.getBounds().y - (scaledHeight - player.getBounds().height) / 2f;

        batch.draw(currentFrame, drawX, drawY, scaledWidth, scaledHeight);
    }

    @Override
    public void renderHit(Player player, SpriteBatch batch) {
        Animation<TextureRegion> hitAnimation = animationManager.getHitAnimation(player.statusManager.getDirection());
        TextureRegion currentFrame = hitAnimation != null
            ? hitAnimation.getKeyFrame(player.statusManager.getStateTime(), false)
            : null;

        if (hitAnimation != null && hitAnimation.isAnimationFinished(player.statusManager.getStateTime())) {
            player.statusManager.setHit(false) ; // Reset trạng thái khi hoạt ảnh kết thúc
        }
        if (currentFrame == null) {
            System.out.println("WARNING: Player renderHit frame NULL! Using idle frame.");
            currentFrame = animationManager.getIdleAnimation(player.statusManager.getDirection()).getKeyFrame(0, true);
        }
        batch.draw(currentFrame, player.getBounds().x, player.getBounds().y,
            player.getBounds().width, player.getBounds().height);
    }

    @Override
    public void renderDeath(Player player, SpriteBatch batch) {

    }

    @Override
    public void renderSmoke(Player player, SpriteBatch batch) {
        player.smokeManager.render(batch, animationManager);
    }
}

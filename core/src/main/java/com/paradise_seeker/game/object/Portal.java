package com.paradise_seeker.game.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.collision.Collidable;
import com.paradise_seeker.game.entity.player.Player;
import com.badlogic.gdx.Gdx;

public class Portal extends GameObject {
    public Rectangle innerBounds;  // Vùng thật để trigger
    public Animation<TextureRegion> animation;
    public float stateTime = 0f;
    public boolean activated = false;


    public Portal(float x, float y) {
    	super(x, y, 3f, 3f, "images/objects/portal/portal1_frame_1.png");
        this.bounds = new Rectangle(x, y, 3f , 3f );  // Kích thước to cho hình ảnh
        this.innerBounds = new Rectangle(x + 1.25f, y + 0.75f, 0.5f, 0.5f);  // Kích thước nhỏ để trigger chính xác
        this.animation = loadAnimation("images/objects/portal/portal1_frame_", 7, 0.1f);
    }

    private Animation<TextureRegion> loadAnimation(String pathPrefix, int frameCount, float frameDuration) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            Texture texture = new Texture(pathPrefix + (i + 1) + ".png");
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(frameDuration, frames);
    }

    public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    @Override
    public Rectangle getBounds() {
        return innerBounds;  // Sử dụng vùng trigger thật khi kiểm tra va chạm
    }
    @Override
    public void onCollision(Collidable other) {
		if (other instanceof Player) {
			activated = true;
		}
	}

	@Override
	public boolean isSolid() {
		// TODO Auto-generated method stub
		return false;
	}

}

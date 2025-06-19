package com.paradise_seeker.game.object.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.rendering.Renderable;
import com.paradise_seeker.game.collision.Collidable;
import com.paradise_seeker.game.entity.player.Player;

public abstract class Item implements Collidable, Renderable {
    protected Rectangle bounds;
    protected Texture texture;
    protected String name;
    protected String type;
    protected String description;
    protected boolean active = true;
    protected boolean stackable;
    protected int maxStackSize = 1;
    protected int count = 1;


    public Item(float x, float y, float size, String texturePath) {
        this.bounds = new Rectangle(x, y, size, size);
        this.texture = new Texture(texturePath);
        this.stackable = false;
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (active) {
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    public boolean isActive() {
        return active;
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

    public Texture getTexture() { return texture; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getCount() { return count; }

    // Phương thức trừu tượng để các lớp con triển khai
    @Override
    public void onCollision(Collidable other) {   
    }
    public void onCollision(Player player) {
		if (active) {
			player.addItemToInventory(this);
			active = false; // Đánh dấu item đã được lấy
		}
	}
    
    public abstract void isUsed(Player player);
    
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isStackable() {
        return stackable;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }
    public void setCount(int count) {
        this.count = Math.min(count, maxStackSize);
    }
    public String getName1() {
		return name;
	}

    public boolean canStackWith(Item other) {
        return this.stackable &&
               other != null &&
               this.getClass() == other.getClass() &&
               this.count < this.maxStackSize;
    }

}

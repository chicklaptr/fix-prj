package com.paradise_seeker.game.entity.skill;

import com.paradise_seeker.game.entity.Character;
import com.badlogic.gdx.math.Rectangle;

public interface Skill {
    boolean canUse(long now);
    void execute(Character user);
    void update(long now);
    void castSkill(float atk, Character target);
    void castSkill(float atk, float x, float y);  // Để bắn ở vị trí x, y
    void castSkill(float atk, float x, float y, String direction);  // Bắn theo hướng
    void castSkill(float atk, Rectangle playerBounds, String direction); // Bắn theo hướng từ player

    float getManaCost();
}

/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import static fr.ubx.poo.view.image.ImageResource.*;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.decor.bonus.*;
import fr.ubx.poo.model.go.*;
import fr.ubx.poo.model.decor.explosives.*;
import fr.ubx.poo.model.go.character.*;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;


public final class SpriteFactory {

    public static Sprite createDecor(Pane layer, Position position, Decor decor) {
        ImageFactory factory = ImageFactory.getInstance();
        if (decor instanceof Stone)
            return new SpriteDecor(layer, factory.get(STONE), position);
        if (decor instanceof Tree)
            return new SpriteDecor(layer, factory.get(TREE), position);
        if (decor instanceof Scarecrow)
            return new SpriteScarecrow(layer, factory.get(SCARECROW), position, (Scarecrow) decor);
        if (decor instanceof Door)
            return new SpriteDoor(layer, factory.get(DOOR_CLOSED), position, (Door) decor);
        if (decor instanceof Princess)
            return new SpriteDecor(layer, factory.get(PRINCESS), position);
        if (decor instanceof Key)
            return new SpriteBonus(layer, factory.get(KEY), position, (Bonus) decor);
        if (decor instanceof Heart)
            return new SpriteBonus(layer, factory.get(HEART), position, (Bonus) decor);
        if (decor instanceof BombNumberInc)
            return new SpriteBonus(layer, factory.get(BONUS_BOMB_INC), position, (Bonus) decor);
        if (decor instanceof BombNumberDec)
            return new SpriteBonus(layer, factory.get(BONUS_BOMB_DEC), position, (Bonus) decor);
        if (decor instanceof BombRangeDec)
            return new SpriteBonus(layer, factory.get(BONUS_BOMB_RANGE_DEC), position, (Bonus) decor);
        if (decor instanceof BombRangeInc)
            return new SpriteBonus(layer, factory.get(BONUS_BOMB_RANGE_INC), position, (Bonus) decor);
        if (decor instanceof Landminer)
            return new SpriteBonus(layer, factory.get(BONUS_LANDMINE), position, (Bonus) decor);
        if (decor instanceof BonusScarecrow)
            return new SpriteBonus(layer, factory.get(BONUS_SCARECROW), position, (Bonus) decor);
        if (decor instanceof Explosion)
            return new SpriteExplosion(layer, factory.get(EXPLOSION), position, (Explosion) decor);
        if (decor instanceof Bomb)
            return new SpriteBomb(layer, position, (Bomb) decor);
        if (decor instanceof Landmine)
            return new SpriteLandmine(layer, position, (Landmine) decor);

        return null;
    }
    public static Sprite createMonster(Pane layer, Monster monster){
        return new SpriteMonster(layer, monster) ;
    }
    public static Sprite createBox(Pane layer, Box box){
        return new SpriteBox(layer, box) ;
    }
    public static Sprite createPlayer(Pane layer, Player player) {
        return new SpritePlayer(layer, player);
    }
}

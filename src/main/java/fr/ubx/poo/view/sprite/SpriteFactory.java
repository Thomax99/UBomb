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
            return new SpriteDecor(layer, factory.get(STONE), position, decor);
        if (decor instanceof Tree)
            return new SpriteDecor(layer, factory.get(TREE), position, decor);
        if (decor instanceof Scarecrow)
            return new SpriteDecor(layer, factory.get(SCARECROW), position, decor);
        if (decor instanceof Door)
            return new SpriteDoor(layer, factory.get(DOOR_CLOSED), position, (Door) decor);
        if (decor instanceof Princess)
            return new SpriteDecor(layer, factory.get(PRINCESS), position, decor);
        if (decor instanceof Key)
            return new SpriteDecor(layer, factory.get(KEY), position, decor);
        if (decor instanceof Heart)
            return new SpriteDecor(layer, factory.get(HEART), position, decor);
        if (decor instanceof BombNumberInc)
            return new SpriteDecor(layer, factory.get(BONUS_BOMB_INC), position, decor);
        if (decor instanceof BombNumberDec)
            return new SpriteDecor(layer, factory.get(BONUS_BOMB_DEC), position, decor);
        if (decor instanceof BombRangeDec)
            return new SpriteDecor(layer, factory.get(BONUS_BOMB_RANGE_DEC), position, decor);
        if (decor instanceof BombRangeInc)
            return new SpriteDecor(layer, factory.get(BONUS_BOMB_RANGE_INC), position, decor);
        if (decor instanceof Landminer)
            return new SpriteDecor(layer, factory.get(BONUS_LANDMINE), position, decor);
        if (decor instanceof BonusScarecrow)
            return new SpriteDecor(layer, factory.get(BONUS_SCARECROW), position, decor);
        if (decor instanceof Explosion)
            return new SpriteDecor(layer, factory.get(EXPLOSION), position,  decor);
        if (decor instanceof Bomb)
            return new SpriteBomb(layer, position, (Bomb) decor);
        if (decor instanceof Landmine)
            return new SpriteDecor(layer,factory.get(LANDMINE), position, decor);

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

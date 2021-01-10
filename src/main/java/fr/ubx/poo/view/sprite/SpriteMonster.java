/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

public class SpriteMonster extends SpriteGameObject {
    public SpriteMonster(Pane layer, Monster monster) {
        super(layer, null, monster);
        updateImage();
    }

    @Override
    public void updateImage() {
        Monster monster = (Monster) go;
        if (monster.hasToBeRemoved()) setImage(null) ;
        else if (monster.isExplosing()) setImage(ImageFactory.getInstance().getMonsterExplosion(monster.getMonsterType()));
        else setImage(ImageFactory.getInstance().getMonster(monster.getMonsterType(), monster.getDirection()));
    }
}

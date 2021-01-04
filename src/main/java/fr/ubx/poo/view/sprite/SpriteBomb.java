package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.decor.explosives.Bomb;
import fr.ubx.poo.view.image.ImageFactory;
import fr.ubx.poo.game.Position;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;


public class SpriteBomb extends SpriteDecor{
    private Bomb bomb ;
    public SpriteBomb(Pane layer, Position pos, Bomb bomb) {
        super(layer, ImageFactory.getInstance().getBomb(bomb.getState() >= 0 ? bomb.getState() : 0), pos) ;
        this.bomb = bomb ;
    }

    @Override
    public void updateImage() {
        if (bomb.isExplosing() || bomb.hasToBeRemoved())
            setImage(null) ;
        else
            setImage(ImageFactory.getInstance().getBomb(bomb.getState() >= 0 ? bomb.getState() : 0));
    }
}

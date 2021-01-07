package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.decor.explosives.Bomb;
import fr.ubx.poo.view.image.ImageFactory;
import fr.ubx.poo.game.Position;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;


public class SpriteBomb extends SpriteDecor{
    public SpriteBomb(Pane layer, Position pos, Bomb bomb) {
        super(layer, ImageFactory.getInstance().getBomb(bomb.getState() >= 0 ? bomb.getState() : 0), pos, bomb) ; //we verify that the state of the bomb is not smaller than 0
    }

    @Override
    public void updateImage() {
        Bomb bomb = (Bomb) getDecor() ;
        if (bomb.isExplosing())
            setImage(null) ; //we remove the image
        else
            setImage(ImageFactory.getInstance().getBomb(bomb.getState() >= 0 ? bomb.getState() : 0)); //we do carefully the getState (if it is too small, we're going to have an
            // index problem)
        super.updateImage();
    }
}

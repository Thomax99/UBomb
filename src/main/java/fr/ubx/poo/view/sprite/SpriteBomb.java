package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;


public class SpriteBomb extends SpriteGameObject{
    private final ColorAdjust effect = new ColorAdjust();

    public SpriteBomb(Pane layer, Bomb bomb) {
        super(layer, null, bomb);
        updateImage();
    }

    @Override
    public void updateImage() {
        Bomb bomb = (Bomb) go;
        if (bomb.isExplosing() || bomb.hasExplosed()) setImage(null) ;
        else setImage(ImageFactory.getInstance().getBomb(bomb.getState()));
    }
}

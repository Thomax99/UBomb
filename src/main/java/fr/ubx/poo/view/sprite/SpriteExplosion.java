package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.Box;
import fr.ubx.poo.model.decor.Explosion;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import fr.ubx.poo.game.Position;

public class SpriteExplosion extends SpriteDecor{
    private Explosion exp ;

    public SpriteExplosion(Pane layer, Image image, Position position, Explosion exp) {
        super(layer, image, position);
        this.exp = exp ;
        updateImage();
    }

    @Override
    public void updateImage() {
        if (exp.hasToBeRemoved()) setImage(null) ;
        else setImage(ImageFactory.getInstance().getExplosion()) ;
    }
}

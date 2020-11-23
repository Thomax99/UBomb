package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.Box;
import fr.ubx.poo.model.go.Explosion;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;

public class SpriteExplosion extends SpriteGameObject{
    private final ColorAdjust effect = new ColorAdjust();

    public SpriteExplosion(Pane layer, Explosion exp) {
        super(layer, null, exp);
        updateImage();
    }

    @Override
    public void updateImage() {
        Explosion exp = (Explosion) go;
        if (!exp.isExisting()) setImage(null) ;
        else setImage(ImageFactory.getInstance().getExplosion()) ;
    }
}

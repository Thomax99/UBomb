package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.Box;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;

public class SpriteBox extends SpriteGameObject{
    public SpriteBox(Pane layer, Box box) {
        super(layer, null, box);
        updateImage();
    }

    @Override
    public void updateImage() {
        Box box = (Box) go;
        if (box.hasToBeRemoved()) setImage(null) ;
        else setImage(ImageFactory.getInstance().getBox());
    }
}

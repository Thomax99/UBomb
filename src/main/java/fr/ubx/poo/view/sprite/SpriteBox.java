package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.Box;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;
import static fr.ubx.poo.view.image.ImageResource.*;

public class SpriteBox extends SpriteGameObject{
    public SpriteBox(Pane layer, Box box) {
        super(layer, ImageFactory.getInstance().get(BOX), box);
        updateImage();
    }

    @Override
    public void updateImage() {
        Box box = (Box) go;
        if (box.hasToBeRemoved()) setImage(null) ;
    }
}

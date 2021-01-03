package fr.ubx.poo.view.sprite;

import static fr.ubx.poo.view.image.ImageResource.*;

import fr.ubx.poo.model.go.Landmine;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;


public class SpriteLandmine extends SpriteGameObject{
    public SpriteLandmine(Pane layer, Landmine landmine) {
        super(layer, null, landmine);
        updateImage();
    }

    @Override
    public void updateImage() {
        Landmine landmine = (Landmine) go;
        if (landmine.hasToBeRemoved()) setImage(null) ;
        else setImage(ImageFactory.getInstance().get(LANDMINE));
    }
}

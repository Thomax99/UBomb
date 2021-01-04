package fr.ubx.poo.view.sprite;

import static fr.ubx.poo.view.image.ImageResource.*;

import fr.ubx.poo.game.Position;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import fr.ubx.poo.model.decor.explosives.Landmine;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;


public class SpriteLandmine extends SpriteDecor{
    private Landmine landmine ;
    public SpriteLandmine(Pane layer, Position pos, Landmine landmine) {
        super(layer, ImageFactory.getInstance().get(LANDMINE), pos) ;
        this.landmine = landmine ;
    }

    @Override
    public void updateImage() {
        if (landmine.hasToBeRemoved()) setImage(null) ;
        else setImage(ImageFactory.getInstance().get(LANDMINE));
    }
}

package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import fr.ubx.poo.model.decor.Scarecrow;
import fr.ubx.poo.model.decor.bonus.*;


public class SpriteScarecrow extends Sprite {
    private Position position;
    private Scarecrow scarecrow ;

    public SpriteScarecrow(Pane layer, Image image, Position position, Scarecrow scarecrow) {
        super(layer, image);
        this.position = position;
        this.scarecrow = scarecrow ;
    }

    @Override
    public void updateImage() {
        if(scarecrow.hasToBeRemoved()) setImage(null) ;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}

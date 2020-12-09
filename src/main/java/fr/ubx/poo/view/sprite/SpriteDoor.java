package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import fr.ubx.poo.model.decor.Door;
import fr.ubx.poo.view.image.*;
import static fr.ubx.poo.view.image.ImageResource.*;
import fr.ubx.poo.view.image.ImageFactory;



public class SpriteDoor extends SpriteDecor {
    private Door door ;

    public SpriteDoor(Pane layer, Image image, Position position, Door door) {
        super(layer, image, position);
        this.door = door ;
        updateImage();
    }

    @Override
    public void updateImage() {
        if(!door.isClosed()) setImage(ImageFactory.getInstance().get(DOOR_OPENED)) ;
    }
}

package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import fr.ubx.poo.model.decor.DoorNext;
import fr.ubx.poo.view.image.*;
import static fr.ubx.poo.view.image.ImageResource.*;
import fr.ubx.poo.view.image.ImageFactory;



public class SpriteNextDoor extends SpriteDecor {
    private DoorNext door ;

    public SpriteNextDoor(Pane layer, Image image, Position position, DoorNext door) {
        super(layer, image, position);
        this.door = door ;
    }

    @Override
    public void updateImage() {
        if(!door.isClosed()) setImage(ImageFactory.getInstance().get(DOOR_OPENED)) ;
    }
}

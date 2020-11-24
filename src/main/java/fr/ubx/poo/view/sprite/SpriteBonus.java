package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import fr.ubx.poo.model.decor.bonus.*;


public class SpriteBonus extends Sprite {
    private Position position;
    private Bonus bonus ;

    public SpriteBonus(Pane layer, Image image, Position position, Bonus bonus) {
        super(layer, image);
        this.position = position;
        this.bonus = bonus ;
    }

    @Override
    public void updateImage() {
        if(bonus.hasToBeRemoved()) setImage(null) ;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}

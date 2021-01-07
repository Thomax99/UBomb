package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Decor;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;


public class SpriteDecor extends Sprite {
    private Position position;
    private Decor decor ;

    public SpriteDecor(Pane layer, Image image, Position position, Decor decor) {
        super(layer, image);
        this.decor = decor ;
        this.position = position;
    }

    @Override
    public void updateImage() {
        if(decor.hasToBeRemoved()) setImage(null) ;
    }

    @Override
    public Position getPosition() {
        return position;
    }
    protected Decor getDecor(){
        return decor ;
    }
}

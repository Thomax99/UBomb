/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;

public class SpritePlayer extends SpriteGameObject {
    private final ColorAdjust effect = new ColorAdjust();

    public SpritePlayer(Pane layer, Player player) {
        super(layer, null, player);
        render();
    }

    @Override
    public void updateImage() {
        Player player = (Player) go;
        setImage(ImageFactory.getInstance().getPlayer(player.getDirection()));
        effect.setSaturation( player.hasAnInfection() ? 0.8 : 0) ; //managing the saturation effect when the player is infected
        effect.setBrightness( player.isInvincible() ? 0.5 : 0) ; //managing the transparency effect when the player is invincibl
        imageView.setEffect(effect) ;
    }
}

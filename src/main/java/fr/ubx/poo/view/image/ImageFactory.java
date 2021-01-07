/*
 * Copyright (c) 2020. Laurent Réveillère
 */
//test
package fr.ubx.poo.view.image;

import fr.ubx.poo.game.Direction;
import javafx.scene.image.Image;

import static fr.ubx.poo.view.image.ImageResource.*;

public final class ImageFactory {
    private final Image[] images;

    private final ImageResource[] directions = new ImageResource[]{
            // Direction { N, E, S, W }
            PLAYER_UP, PLAYER_RIGHT, PLAYER_DOWN, PLAYER_LEFT,
    };
    private final ImageResource[] directions_monsters_classicals = new ImageResource[]{
        // Direction { N, E, S, W }
        MONSTER_UP, MONSTER_RIGHT, MONSTER_DOWN, MONSTER_LEFT,
    };
    private final ImageResource[][] directions_monsters = new ImageResource[][]{
        // Direction { N, E, S, W }
        {MONSTER_0_UP, MONSTER_0_RIGHT, MONSTER_0_DOWN, MONSTER_0_LEFT},
        {MONSTER_1_UP, MONSTER_1_RIGHT, MONSTER_1_DOWN, MONSTER_1_LEFT},
        {MONSTER_2_UP, MONSTER_2_RIGHT, MONSTER_2_DOWN, MONSTER_2_LEFT},
        {MONSTER_3_UP, MONSTER_3_RIGHT, MONSTER_3_DOWN, MONSTER_3_LEFT},

    };
    private final ImageResource[] stateBomb = new ImageResource[]{
        // State {0, 1, 2, 3}
         BOMB_3, BOMB_2, BOMB_1, BOMB_0,
    };

    private final ImageResource[] digits = new ImageResource[]{
            DIGIT_0, DIGIT_1, DIGIT_2, DIGIT_3, DIGIT_4,
            DIGIT_5, DIGIT_6, DIGIT_7, DIGIT_8, DIGIT_9,
    };

    private ImageFactory() {
        images = new Image[ImageResource.values().length];
    }

    /**
     * Access point for the unique singleton instance
     * @return a singleton instance
     */
    public static ImageFactory getInstance() {
        return Holder.instance;
    }

    private Image loadImage(String file) {
        return new Image(getClass().getResource("/images/" + file).toExternalForm());
    }

    public void load() {
        for (ImageResource img : ImageResource.values()) {
            images[img.ordinal()] = loadImage(img.getFileName());
        }
    }

    public Image get(ImageResource img) {
        return images[img.ordinal()];
    }

    public Image getDigit(int i) {
        if (i < 0 || i > 9)
            throw new IllegalArgumentException();
        return get(digits[i]);
    }

    public Image getPlayer(Direction direction) {
        return get(directions[direction.ordinal()]);
    }
    /**
     * 
     * @param monster_type the type of the monster
     * @param direction the direction in which the monster is going
     * @return an image according to the type of the monster and the direction
     */
    public Image getMonster(int monster_type, Direction direction) {
        if (monster_type >= directions_monsters.length) return get(directions_monsters_classicals[direction.ordinal()]); //we return the default image of monsters
        return get(directions_monsters[monster_type][direction.ordinal()]);
    }
    public Image getBox() {
        return get(BOX);
    }
    public Image getExplosion(){
        return get(EXPLOSION) ;
    }

    public Image getBomb(int state) {
        return get(stateBomb[state]);
    }

    /**
     * Holder
     */
    private static class Holder {
        /**
         * Instance unique non préinitialisée
         */
        private final static ImageFactory instance = new ImageFactory();
    }

}

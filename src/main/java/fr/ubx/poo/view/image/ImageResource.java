package fr.ubx.poo.view.image;


public enum ImageResource {
    BANNER_BOMB ("banner_bomb.png"),
    BANNER_RANGE ("banner_range.png"),
    BANNER_LANDMINE("banner_landmine.png"),
    BANNER_SCARECROW("banner_scarecrow.png"),
    HEART("heart.png"),
    KEY("key.png"),
    DIGIT_0 ("banner_0.jpg"),
    DIGIT_1 ("banner_1.jpg"),
    DIGIT_2 ("banner_2.jpg"),
    DIGIT_3 ("banner_3.jpg"),
    DIGIT_4 ("banner_4.jpg"),
    DIGIT_5 ("banner_5.jpg"),
    DIGIT_6 ("banner_6.jpg"),
    DIGIT_7 ("banner_7.jpg"),
    DIGIT_8 ("banner_8.jpg"),
    DIGIT_9 ("banner_9.jpg"),
    BOMB_0 ("bomb4.png"),
    BOMB_1 ("bomb3.png"),
    BOMB_2 ("bomb2.png"),
    BOMB_3 ("bomb1.png"),
    EXPLOSION ("explosion.png"),
    PLAYER_DOWN("player_down.png"),
    PLAYER_LEFT("player_left.png"),
    PLAYER_RIGHT("player_right.png"),
    PLAYER_UP("player_up.png"),
    MONSTER_DOWN("monster_0_down.png"),
    MONSTER_LEFT("monster_0_left.png"),
    MONSTER_RIGHT("monster_0_right.png"),
    MONSTER_UP("monster_0_up.png"),
    MONSTER_0_DOWN("monster_0_down.png"),
    MONSTER_0_LEFT("monster_0_left.png"),
    MONSTER_0_RIGHT("monster_0_right.png"),
    MONSTER_0_UP("monster_0_up.png"),
    MONSTER_1_DOWN("monster_1_down.png"),
    MONSTER_1_LEFT("monster_1_left.png"),
    MONSTER_1_RIGHT("monster_1_right.png"),
    MONSTER_1_UP("monster_1_up.png"),
    MONSTER_2_DOWN("monster_2_down.png"),
    MONSTER_2_LEFT("monster_2_left.png"),
    MONSTER_2_RIGHT("monster_2_right.png"),
    MONSTER_2_UP("monster_2_up.png"),
    MONSTER_3_DOWN("monster_3_down.png"),
    MONSTER_3_LEFT("monster_3_left.png"),
    MONSTER_3_RIGHT("monster_3_right.png"),
    MONSTER_3_UP("monster_3_up.png"),
    STONE("stone.png"),
    TREE("tree.png"),
    BOX("box.png"),
    SCARECROW("scarecrow.png"),
    DOOR_CLOSED("door_closed.png"),
    DOOR_OPENED("door_opened.png"),
    PRINCESS("bomberwoman.png"),
    BONUS_BOMB_INC("bonus_bomb_nb_inc.png"),
    BONUS_BOMB_DEC("bonus_bomb_nb_dec.png"),
    BONUS_BOMB_RANGE_DEC("bonus_bomb_range_dec.png"),
    BONUS_BOMB_RANGE_INC("bonus_bomb_range_inc.png"),
    LANDMINE("landmine.png"),
    BONUS_LANDMINE("bonus_landmine.png"),
    BONUS_SCARECROW("bonus_scarecrow.png"),
    ;

    private final String FileName;

    ImageResource(String fileName) {
        this.FileName = fileName;
    }

    public String getFileName() {
        return FileName;
    }
}

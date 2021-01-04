package fr.ubx.poo.model.decor.explosives;


/**
 * The Landmine is a type of bomb which explode only when there is something pushed on
 */
public class Landmine extends Explosive {
    public Landmine(int range, int level) {
        super(range, level);
    }
    @Override
    public String toString() {
        return "Landmine";
    }
}
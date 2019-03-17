package com.bbaker.discord.xwing.die;

public class AttackDie extends BasicDie {

    private static final String[] SIDES = new String[] {
     "hit", "hit", "hit", "crit", "focus", "focus", "blank"
    };

    private String face;

    public AttackDie() {
        int roll = (int) (Math.random() * SIDES.length);
        face = SIDES[roll];
    }

    @Override
    public DieType getType() {
        return DieType.ATTACK;
    }

    @Override
    String getSide() {
        return face;
    }

}

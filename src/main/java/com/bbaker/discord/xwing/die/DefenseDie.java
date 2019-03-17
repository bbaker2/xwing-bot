package com.bbaker.discord.xwing.die;

public class DefenseDie extends BasicDie {

    private static final String[] SIDES = new String[] {
            "evade", "evade", "evade", "focus", "focus", "blank", "blank", "blank"
    };

    private String face;

    public DefenseDie() {
        int roll = (int) (Math.random() * SIDES.length);
        face = SIDES[roll];
    }

    @Override
    public DieType getType() {
        return DieType.DEFENSE;
    }

    @Override
    String getSide() {
        return face;
    }

}

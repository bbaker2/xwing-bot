package com.bbaker.discord.xwing.die;

public abstract class BasicDie implements Die{

    @Override
    public String getFace() {
        String emoji = getType() + "_" + getSide();
        return emoji.toLowerCase();
    }

    @Override
    public int compareTo(Die d) {
        return getFace().compareTo(d.getFace());
    }

    abstract String getSide();

}

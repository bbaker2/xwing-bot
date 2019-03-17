package com.bbaker.discord.xwing.die;

public interface Die extends Comparable<Die> {

    String getFace();

    DieType getType();

}

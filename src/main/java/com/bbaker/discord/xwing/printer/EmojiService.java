package com.bbaker.discord.xwing.printer;

import com.bbaker.discord.xwing.die.Die;

public interface EmojiService {

    String findEmoji(Die die);

    String findEmoji(String name);

}

package com.bbaker.discord.xwing;

import java.util.Properties;

import org.javacord.api.AccountType;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;

import com.bbaker.discord.xwing.database.DatabaseService;
import com.bbaker.discord.xwing.database.JdbiService;
import com.bbaker.discord.xwing.exception.SetupException;
import com.bbaker.discord.xwing.ffg.FFGReader;
import com.bbaker.discord.xwing.printer.EmojiServiceImpl;
import com.bbaker.discord.xwing.roller.DiceRoller;

import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JavacordHandler;

public class WhistlerTester {
    public static void main(String[] args) throws SetupException {
        FallbackLoggerConfiguration.setDebug(true);
        FallbackLoggerConfiguration.setTrace(true);

        // Prepare Database
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        DatabaseService dbService = new JdbiService(new Properties());
        dbService.createTables();

        // Start up bot
        String token = args[0];

        DiscordApiBuilder dab = new DiscordApiBuilder().setAccountType(AccountType.BOT).setToken(token);
        DiscordApi api = dab.login().join();
        EmojiServiceImpl emojiService = new EmojiServiceImpl();
        emojiService.setApi(api);


        CommandHandler ch = new JavacordHandler(api);
        ch.setDefaultPrefix("?");
        ch.registerCommand(new DiceRoller(emojiService));
        ch.registerCommand(new FFGReader());

//        api.getInviteByCode("UaFgJk").thenAccept(a->a.getChannelName());
        System.out.println(api.createBotInvite());

    }
}

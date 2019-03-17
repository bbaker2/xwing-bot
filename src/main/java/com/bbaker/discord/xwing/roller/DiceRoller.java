package com.bbaker.discord.xwing.roller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.javacord.api.entity.message.Message;

import com.bbaker.arg.parser.exception.BadArgumentException;
import com.bbaker.arg.parser.text.TextArgumentEvaluator;
import com.bbaker.arg.parser.text.TextArgumentParser;
import com.bbaker.discord.xwing.die.AttackDie;
import com.bbaker.discord.xwing.die.DefenseDie;
import com.bbaker.discord.xwing.die.Die;
import com.bbaker.discord.xwing.printer.EmojiService;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class DiceRoller implements CommandExecutor {

    private EmojiService emojies;

    public DiceRoller(EmojiService emojies) {
        this.emojies = emojies;
    }

    @Command(aliases = {"r", "roll"}, description = "Rolls dice", usage = "!roll")
    public String handleRoll(Message message) throws BadArgumentException {
        List<String> tokens = getList(message.getContent());

        TextArgumentParser parser = new TextArgumentParser();

        List<Die> dice = new ArrayList<>();

        parser.processArguments(tokens.iterator(), DiceRoller::isDie, (t,l,r)->{
            int total = TextArgumentEvaluator.getTotal(l, r);
            for(int i = 0; i < total; i++) {
                dice.add(findDie(t));
            }
            return true;
        });

        Collections.sort(dice);
        List<String> faces = dice.stream().sorted().map(d -> emojies.findEmoji(d)).collect(Collectors.toList());
        return String.join(" ", faces);
    }

    private List<String> getList(String message){
        String[] args = message.split("\\s+");
        List<String> tokens = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));
        return new ArrayList<String>(tokens);
    }

    private static boolean isDie(String token) {
        return findDie(token) != null;
    }

    private static Die findDie(String token) {
        switch(token.toLowerCase()) {
            case "red":
            case "r":
            case "attack":
            case "a":
                return new AttackDie();
            case "green":
            case "g":
            case "defense":
            case "d":
                return new DefenseDie();
        }
        return null;
    }

}

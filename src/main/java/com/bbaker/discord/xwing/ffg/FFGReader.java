package com.bbaker.discord.xwing.ffg;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;

import com.bbaker.arg.parser.exception.BadArgumentException;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class FFGReader implements CommandExecutor {

    private String url = "https://www.fantasyflightgames.com/en/rss/?tags=x-wing-second-edition";
    private boolean notStarted = true;
    private FFGListener ffg = new FFGListener();

    @Command(aliases = {"ffg"}, description = "Get the latest article", usage = "!roll")
    public String handleFFG(Message message) throws BadArgumentException {
        if(notStarted) {
            notStarted = false;
            ffg.addChannel(message.getChannel());
            startup();
            return message.getChannel().getId() + " has been registered";
        }
        return "Already registered";

    }

    public static void main(String...args) {
        String url = "https://www.fantasyflightgames.com/en/rss/?tags=x-wing-second-edition";
        try {
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(url)));
            System.out.println(feed.getEntries().get(0).getLink());
        } catch (IllegalArgumentException | FeedException | IOException e) {
            e.printStackTrace();
        }

    }

    public Optional<SyndEntry> publishArticle() {
        try {
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(url)));
            SyndEntry entry = feed.getEntries().get(0);

            return Optional.of(entry);
        } catch (IllegalArgumentException | FeedException | IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void startup() {
        new Thread(ffg).start();
    }

    public class FFGListener implements Runnable {
        private String lastGUID = "";
        private Set<TextChannel> listeners = new ConcurrentSkipListSet<>();

        @Override
        public void run() {
            try {
                while(true) {
                    System.out.println("waiting...");
                    Thread.sleep(90 * 1000);
                    System.out.println("pingFFG");
                    pingFFG();

                }
            } catch (InterruptedException e) {
                return;
            }

        }

        public void addChannel(TextChannel channel) {
            listeners.add(channel);
        }

        private void pingFFG() {
            publishArticle().ifPresent(entry -> {
                String link = entry.getLink();
                if(!lastGUID.equals(link)) {
                    lastGUID = link;
                    for(TextChannel channel : listeners) {
                        new MessageBuilder()
                        .append(entry.getTitle())
                        .appendNewLine()
                        .append(entry.getLink())
                        .send(channel);
                    }
                }

            });
        }

    }

}

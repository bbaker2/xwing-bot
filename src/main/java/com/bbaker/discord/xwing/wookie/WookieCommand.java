package com.bbaker.discord.xwing.wookie;

import java.io.IOException;
import java.net.URLEncoder;

import org.javacord.api.entity.message.Message;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.bbaker.arg.parser.exception.BadArgumentException;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class WookieCommand  implements CommandExecutor {

    @Command(aliases = {"s", "search"}, description = "Searches Wookiepedia", usage = "!search")
    public String search(Message message) throws BadArgumentException {
        String msg = message.getContent();

        if(StringUtil.isBlank(msg)) {
            return "Nothing to search?";
        }

        return getUrl(msg);
    }

    public String getUrl(String query) {
        try {
            String q = URLEncoder.encode(query, "UTF-8");
            String url = String.format("https://starwars.fandom.com/wiki/Special:Search?search=%s", q);
            Document doc = Jsoup.connect(url).get();
            Elements results = doc.select("ul.Results a.result-link");

            if(results.size() > 0) {
                return results.get(0).attr("href");
            } else {
                return "No luck";
            }
        } catch (IOException e) {
            return "Something went wrong. Unable to search.";
        }

    }

    public static void main(String...args) {
        WookieCommand c = new WookieCommand();
        System.out.println(c.getUrl("bbbbbbbbbbbbbbbbbbbbbbb"));
    }

}

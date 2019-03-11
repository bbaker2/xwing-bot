package com.bbaker.discord.xwing.database;

import java.util.Properties;
import java.util.regex.Pattern;

import org.h2.util.StringUtils;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.h2.H2DatabasePlugin;

import com.bbaker.discord.xwing.exception.SetupException;

public class JdbiService implements DatabaseService {

    private Jdbi jdbi;
    private Properties dbProps;
    private String prefix;

    public static final String TABLE_ROLL = "ROLL";
    public static final String TABLE_DESTINY = "DESTINY";
    public static final String TABLE_INIT = "INIT";
    public static final String TABLE_INIT_TRACKER = "INIT_TRACKER";

    public JdbiService(Properties p) throws SetupException {
        dbProps = new Properties();
        dbProps.put("url", 		"jdbc:h2:./discord_swrpg");
        dbProps.put("port", 	"9138");
        dbProps.put("password", "tk421");
        dbProps.put("user", 	"su");
        dbProps.put("prefix", 	"swrpgab_"); // star wars rpg assistant bot
        dbProps.putAll(p);

        prefix = dbProps.getProperty("prefix");

        if(StringUtils.isNullOrEmpty(prefix)) {
            throw new SetupException("Missing the prefix for the database tables. Make sure 'prefix' is correctly populated in the properties.");
        }

        if(!Pattern.matches("[a-zA-Z]+_", prefix)) {
            throw new SetupException("'%s' is not an acceptable prefix. Must only contain characters and ends with one underscore", prefix);
        }

        this.jdbi = Jdbi.create(dbProps.getProperty("url"), dbProps);
        this.jdbi.installPlugin(new H2DatabasePlugin());
    }

    @Override
    public void createTables() {
        // TODO Auto-generated method stub

    }

}

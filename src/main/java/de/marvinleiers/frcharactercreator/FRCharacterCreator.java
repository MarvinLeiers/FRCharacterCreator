package de.marvinleiers.frcharactercreator;

import de.marvinleiers.frcharactercreator.commands.CharacterCommand;
import de.marvinleiers.frcharactercreator.listeners.CharacterCreateListener;
import de.marvinleiers.frcharactercreator.listeners.ChatListener;
import de.marvinleiers.frcharactercreator.listeners.JoinListener;
import de.marvinleiers.frcharactercreator.utils.MySQL;
import de.marvinleiers.mpluginapi.mpluginapi.MPlugin;

public final class FRCharacterCreator extends MPlugin
{
    private static MySQL mySQL;

    @Override
    protected void onStart()
    {
        this.prepareMySQL();

        saveResource("config.yml", false);

        new CharacterCommand(this, "character", "mplugin.characters.character");

        getServer().getPluginManager().registerEvents(new CharacterCreateListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

    @Override
    protected void onStop()
    {
        mySQL.close();
    }

    private void prepareMySQL()
    {
        mySQL = new MySQL(getConfig().getString("mysql-host"), getConfig().getString("mysql-database"),
                getConfig().getString("mysql-user"), getConfig().getString("mysql-password"));
        mySQL.connect();

        mySQL.createTable("characters",
                "(id INTEGER AUTO_INCREMENT PRIMARY KEY," +
                        "name TEXT," +
                        "age INT," +
                        "description TEXT);");

        mySQL.createTable("player_characters",
                "(id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                        "owner TEXT, " +
                        "char_1_id INTEGER, " +
                        "char_2_id INTEGER, " +
                        "char_3_id INTEGER," +
                        "FOREIGN KEY (char_1_id) REFERENCES characters(id)," +
                        "FOREIGN KEY (char_2_id) REFERENCES characters(id)," +
                        "FOREIGN KEY (char_3_id) REFERENCES characters(id)" +
                        ");");
    }

    public static FRCharacterCreator getInstance()
    {
        return getPlugin(FRCharacterCreator.class);
    }

    public static MySQL getMySQL()
    {
        return mySQL;
    }
}

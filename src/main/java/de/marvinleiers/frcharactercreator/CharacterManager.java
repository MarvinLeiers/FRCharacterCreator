package de.marvinleiers.frcharactercreator;

import de.marvinleiers.frcharactercreator.utils.CharacterIdentifier;
import de.marvinleiers.frcharactercreator.utils.MySQL;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class CharacterManager
{
    private final HashMap<UUID, Character> characters = new HashMap<>();
    private static final MySQL mySQL = FRCharacterCreator.getMySQL();
    private static CharacterManager instance;

    private CharacterManager()
    {
    }

    public void selectCharacter(Player player, Character character)
    {
        player.setPlayerListName(character.getName());
        characters.put(player.getUniqueId(), character);
    }

    public boolean isInCharacter(OfflinePlayer player)
    {
        return characters.containsKey(player.getUniqueId());
    }

    public void deleteCharacter(OfflinePlayer player, CharacterIdentifier identifier, int id)
    {
        if (isInCharacter(player))
        {
            Character currentCharacter = getCharacters().get(player.getUniqueId());

            if (currentCharacter.compareTo(getCharacter(player, identifier)) == 0)
            {
                ((Player) player).setPlayerListName(player.getName());
                ((Player) player).setDisplayName(player.getName());

                characters.remove(player.getUniqueId());
            }
        }

        mySQL.update("UPDATE player_characters SET " + identifier.name().toLowerCase() + "_id = NULL WHERE owner ='" + player.getUniqueId().toString() +"';");
    }

    public Character getCharacter(OfflinePlayer player, CharacterIdentifier identifier)
    {
        int id = getCharacterId(player, identifier);

        String query = "SELECT id, name, age, description FROM characters WHERE id = '" + id + "';";

        try
        {
            Connection connection = mySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                int age = resultSet.getInt("age");
                int characterId = resultSet.getInt("id");

                resultSet.close();
                preparedStatement.close();
                connection.close();

                return new Character(name, description, String.valueOf(age), characterId);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return Character.EMPTY_CHARACTER();
    }

    public int getCharacterId(OfflinePlayer player, CharacterIdentifier identifier)
    {
        String query = "SELECT " + identifier.name().toLowerCase() + "_id FROM player_characters WHERE owner = '" + player.getUniqueId().toString() + "';";

        try
        {
            Connection connection = mySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                int characterId = resultSet.getInt(identifier.name().toLowerCase() + "_id");

                resultSet.close();
                preparedStatement.close();
                connection.close();

                return characterId;
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return -1;
    }

    public void create(OfflinePlayer player, Character character)
    {
        CharacterIdentifier characterIdentifier = CharacterIdentifier.CHAR_1;

        if (hasCharacter(player, CharacterIdentifier.CHAR_1))
            characterIdentifier = CharacterIdentifier.CHAR_2;

        if (hasCharacter(player, CharacterIdentifier.CHAR_2))
            characterIdentifier = CharacterIdentifier.CHAR_3;

        if (hasCharacter(player, CharacterIdentifier.CHAR_3))
            return;

        String name = character.getName();
        String description = character.getDescription();
        int age = Integer.parseInt(character.getAge());
        int id = getNextId();

        mySQL.update("INSERT INTO characters (name, age, description) VALUES ('" + name + "', '" + age + "', '" + description + "');");

        if (!mySQL.exists(player, "player_characters"))
        {
            mySQL.update("INSERT INTO player_characters (owner, " + characterIdentifier.name().toLowerCase() + "_id) VALUES (" +
                    "'" + player.getUniqueId().toString() + "', " +
                    id +
                    ");");
        }
        else
        {
            mySQL.update("UPDATE player_characters SET " + characterIdentifier.name().toLowerCase() + "_id = '" + id + "' WHERE " +
                    "owner = '" + player.getUniqueId().toString() + "';");
        }
    }

    public boolean hasCharacter(OfflinePlayer player, CharacterIdentifier characterIdentifier)
    {
        String query = "SELECT " + characterIdentifier.name().toLowerCase() + "_id FROM player_characters WHERE owner = '" + player.getUniqueId().toString() + "';";

        try
        {
            Connection connection = mySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                int result = resultSet.getInt(characterIdentifier.name().toLowerCase() + "_id");

                resultSet.close();
                preparedStatement.close();
                connection.close();

                return result != 0;
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    private int getNextId()
    {
        String query = "SELECT id FROM characters ORDER BY id DESC LIMIT 1;";
        int id = 1;

        try
        {
            Connection connection = mySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                id = resultSet.getInt("id");

                resultSet.close();
                preparedStatement.close();
                connection.close();

                return id + 1;
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return id;
    }

    public HashMap<UUID, Character> getCharacters()
    {
        return characters;
    }

    public static CharacterManager getInstance()
    {
        if (instance == null)
            instance = new CharacterManager();

        return instance;
    }
}

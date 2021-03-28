package de.marvinleiers.frcharactercreator.listeners;

import de.marvinleiers.frcharactercreator.Character;
import de.marvinleiers.frcharactercreator.CharacterManager;
import de.marvinleiers.frcharactercreator.FRCharacterCreator;
import de.marvinleiers.frcharactercreator.menus.CharacterCreateMenu;
import de.marvinleiers.mpluginapi.mpluginapi.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;

public class CharacterCreateListener implements Listener
{
    public static final HashMap<OfflinePlayer, Character> createCharacter = new HashMap<>();
    private static final CharacterManager characterManager = CharacterManager.getInstance();

    @EventHandler
    public void onCreate(AsyncPlayerChatEvent event)
    {
        Messages messages = FRCharacterCreator.getInstance().getMessages();
        Player player = event.getPlayer();

        if (!createCharacter.containsKey(player))
            return;

        event.setCancelled(true);

        Character character = createCharacter.get(player);
        String msg = event.getMessage();

        if (msg.equalsIgnoreCase("#"))
        {
            createCharacter.remove(player);
            Bukkit.getScheduler().cancelTask(CharacterCreateMenu.tasks.get(player));
            return;
        }

        if (character.getName().isEmpty())
        {
            String name = msg.split(" ")[0];

            if (!name.matches("[a-zA-Z]{5,}"))
            {
                player.sendMessage("§cError: §eName must consist of letters only and has to be at least 5 characters!");
                return;
            }

            character.setName(name);
            player.sendMessage(messages.get("message-enter-description"));
        }
        else if (character.getDescription().isEmpty())
        {
            if (msg.toCharArray().length < 16)
            {
                player.sendMessage("§cError: §eDescription has to be at least 16 characters!");
                return;
            }

            if (!msg.matches("[a-zA-Z0-9,.!?:)(\\-_# ]+"))
            {
                player.sendMessage("§cError: §eDescription must consist of letters, numbers and ',.!?:)(-_#' only!");
                return;
            }

            character.setDescription(msg);
            player.sendMessage(messages.get("message-enter-age"));
        }
        else if (character.getAge().isEmpty())
        {
            String age = msg.split(" ")[0];

            if (age.matches("[0-9]+"))
            {
                if (!(Integer.parseInt(age) > 0 && Integer.parseInt(age) < 100))
                {
                    player.sendMessage("§cError: §eAge must be between 1-99");
                    return;
                }

                character.setAge(age);
                characterManager.create(player, character);
                createCharacter.remove(player);
                Bukkit.getScheduler().cancelTask(CharacterCreateMenu.tasks.get(player));
                player.sendMessage(messages.get("message-character-creation-confirmation"));
            }
            else
            {
                player.sendMessage("§cError: §eAge must be a number and between 1-99");
            }
        }
    }
}

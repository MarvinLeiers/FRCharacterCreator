package de.marvinleiers.frcharactercreator.listeners;

import de.marvinleiers.frcharactercreator.CharacterManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener
{
    private static final CharacterManager characterManager = CharacterManager.getInstance();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();

        if (!characterManager.isInCharacter(player))
            return;

        String name = characterManager.getCharacters().get(player.getUniqueId()).getName();

        player.setDisplayName(name);
    }
}

package de.marvinleiers.frcharactercreator.listeners;

import de.marvinleiers.frcharactercreator.Character;
import de.marvinleiers.frcharactercreator.CharacterManager;
import de.marvinleiers.frcharactercreator.FRCharacterCreator;
import de.marvinleiers.mpluginapi.mpluginapi.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener
{
    private final Messages messages = FRCharacterCreator.getInstance().getMessages();
    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        CharacterManager characterManager = CharacterManager.getInstance();
        Player player = event.getPlayer();

        if (characterManager.isInCharacter(player))
        {
            Character character = characterManager.getCharacters().get(player.getUniqueId());

            characterManager.selectCharacter(player, character);
            player.sendMessage(messages.get("message-playing-as-character", character.getName()));
        }
    }
}

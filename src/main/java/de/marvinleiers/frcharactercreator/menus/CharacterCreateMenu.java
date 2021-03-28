package de.marvinleiers.frcharactercreator.menus;

import de.marvinleiers.frcharactercreator.Character;
import de.marvinleiers.frcharactercreator.CharacterManager;
import de.marvinleiers.frcharactercreator.FRCharacterCreator;
import de.marvinleiers.frcharactercreator.listeners.CharacterCreateListener;
import de.marvinleiers.frcharactercreator.utils.CharacterIdentifier;
import de.marvinleiers.menuapi.Menu;
import de.marvinleiers.menuapi.MenuAPI;
import de.marvinleiers.menuapi.MenuUserInformation;
import de.marvinleiers.mpluginapi.mpluginapi.utils.ItemFactory;
import de.marvinleiers.mpluginapi.mpluginapi.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class CharacterCreateMenu extends Menu implements Listener
{
    public static final HashMap<OfflinePlayer, Integer> tasks = new HashMap<>();
    private final FRCharacterCreator plugin = FRCharacterCreator.getInstance();
    private final CharacterManager characterManager = CharacterManager.getInstance();

    public CharacterCreateMenu(MenuUserInformation menuUserInformation)
    {
        super(menuUserInformation);
    }

    @Override
    public String getTitle()
    {
        return "Create new character | " + player.getName();
    }

    @Override
    public int getSlots()
    {
        return 9;
    }

    @Override
    public void setItems()
    {
        for (int i = 3; i <= 5; i++)
        {
            CharacterIdentifier identifier = CharacterIdentifier.getFromIdentifier(i - 2);
            Material material = characterManager.hasCharacter(player, identifier) ? Material.PLAYER_HEAD : Material.SKELETON_SKULL;

            Character character = characterManager.getCharacter(player, identifier);

            ItemStack icon;

            if (material == Material.PLAYER_HEAD)
            {
                icon = new ItemFactory(plugin, material)
                        .setDisplayName("§6§l" + character.getName())
                        .addLore("", "§7Age: §o§e" + character.getAge(), "", "§7" + character.getDescription())
                        .addPersistentDataEntry("mplugin.characters.id", identifier.name())
                        .getItem();
            }
            else
            {
                icon = new ItemFactory(plugin, material)
                        .setDisplayName("§7Empty")
                        .getItem();
            }

            inventory.setItem(i, icon);
        }
    }

    @Override
    public void handleClickActions(InventoryClickEvent inventoryClickEvent)
    {
        if (inventoryClickEvent.getCurrentItem() == null)
            return;

        ItemStack item = inventoryClickEvent.getCurrentItem();
        Material material = item.getType();

        if (material == Material.PLAYER_HEAD)
        {
            PersistentDataContainer persistentDataContainer = item.getItemMeta().getPersistentDataContainer();
            String persistentDataEntry  = persistentDataContainer.get(new NamespacedKey(plugin, "mplugin.characters.id"), PersistentDataType.STRING);
            CharacterIdentifier identifier = CharacterIdentifier.valueOf(persistentDataEntry);
            Character character = characterManager.getCharacter(player, identifier);

            player.closeInventory();

            new CharacterInfoMenu(character, identifier, MenuAPI.getMenuUserInformation(player)).open();
        }
        else if (material == Material.SKELETON_SKULL)
        {
            CharacterCreateListener.createCharacter.put(player, Character.EMPTY_CHARACTER());
            player.closeInventory();

            BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(FRCharacterCreator.getInstance(), () -> {
                final Messages messages = FRCharacterCreator.getInstance().getMessages();

                Character character = CharacterCreateListener.createCharacter.get(player);

                if (!player.isOnline())
                {
                    Bukkit.getScheduler().cancelTask(tasks.get(player));
                    return;
                }

                if (character.getName().isEmpty())
                {
                    player.sendMessage(messages.get("message-enter-name"));
                }
                else if (character.getDescription().isEmpty())
                {
                    player.sendMessage(messages.get("message-enter-description"));
                }
                else if (character.getAge().isEmpty())
                {
                    player.sendMessage(messages.get("message-enter-age"));
                }
            }, 0, 20 * 10);

            tasks.put(player, task.getTaskId());
        }
    }
}

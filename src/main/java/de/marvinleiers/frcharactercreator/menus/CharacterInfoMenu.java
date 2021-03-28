package de.marvinleiers.frcharactercreator.menus;

import de.marvinleiers.frcharactercreator.Character;
import de.marvinleiers.frcharactercreator.CharacterManager;
import de.marvinleiers.frcharactercreator.FRCharacterCreator;
import de.marvinleiers.frcharactercreator.utils.CharacterIdentifier;
import de.marvinleiers.menuapi.Menu;
import de.marvinleiers.menuapi.MenuUserInformation;
import de.marvinleiers.mpluginapi.mpluginapi.utils.ItemFactory;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CharacterInfoMenu extends Menu
{
    private static final CharacterManager characterManager = CharacterManager.getInstance();
    private static final FRCharacterCreator plugin = FRCharacterCreator.getInstance();
    private final CharacterIdentifier identifier;
    private final Character character;

    public CharacterInfoMenu(Character character, CharacterIdentifier identifier, MenuUserInformation menuUserInformation)
    {
        super(menuUserInformation);

        this.character = character;
        this.identifier = identifier;
    }

    @Override
    public String getTitle()
    {
        return character.getName() + " | Overview";
    }

    @Override
    public int getSlots()
    {
        return 9 * 3;
    }

    @Override
    public void setItems()
    {
        this.FILLER_GLASS = new ItemFactory(plugin, Material.GRAY_STAINED_GLASS_PANE)
                .setDisplayName(" ")
                .getItem();

        setFillerGlass();

        inventory.setItem(12, new ItemFactory(plugin, Material.GREEN_STAINED_GLASS_PANE)
                .setDisplayName("§a§lPlay as " + character.getName())
                .getItem());

        inventory.setItem(14, new ItemFactory(plugin, Material.RED_STAINED_GLASS_PANE)
                .setDisplayName("§c§lDelete " + character.getName())
                .getItem());
    }

    @Override
    public void handleClickActions(InventoryClickEvent inventoryClickEvent)
    {
        ItemStack item = inventoryClickEvent.getCurrentItem();

        if (item.getType() == Material.RED_STAINED_GLASS_PANE)
        {
            characterManager.deleteCharacter(player, identifier, character.getId());
            player.sendMessage(plugin.getMessages().get("message-character-deleted"));
            new CharacterCreateMenu(menuUserInformation).open();
        }
        else if (item.getType() == Material.GREEN_STAINED_GLASS_PANE)
        {
            characterManager.selectCharacter(player, character);
            player.closeInventory();

            player.sendMessage(plugin.getMessages().get("message-playing-as-character", character.getName()));
        }
    }
}

package de.marvinleiers.frcharactercreator.commands;

import de.marvinleiers.frcharactercreator.menus.CharacterCreateMenu;
import de.marvinleiers.menuapi.MenuAPI;
import de.marvinleiers.mpluginapi.mpluginapi.commands.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CharacterCommand extends Command
{
    public CharacterCommand(JavaPlugin plugin, String name, String permission)
    {
        super(plugin, name, permission);
    }

    @Override
    public void onPlayerExecute(Player player, String[] strings)
    {
        new CharacterCreateMenu(MenuAPI.getMenuUserInformation(player)).open();
    }

    @Override
    public void onExecute(CommandSender commandSender, String[] strings)
    {

    }
}

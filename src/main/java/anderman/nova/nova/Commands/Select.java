package anderman.nova.nova.Commands;

import anderman.nova.nova.Nova;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class Select implements CommandExecutor {

    private Nova plugin;

    public Select(Nova plugin) {
        this.plugin = plugin;
        plugin.getCommand("novaselect").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Only players can select Nova abilities.");
            return true;
        }

        HashMap<UUID, String> abilties = plugin.active_abilities;
        HashMap<UUID, Integer> level = plugin.levels;
        HashMap<UUID, String> trims = plugin.trims;
        Player p = (Player) commandSender;
        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + "No Ability Was Selected");
            return true;
        }

        String selectedAbility = args[0].toLowerCase();
        int playerLevel = level.getOrDefault(p.getUniqueId(), 0);
        String trim = trims.get(p.getUniqueId());
        if (trim == null) {
            p.sendMessage(ChatColor.RED + "You do not have a trim selected yet.");
            return true;
        }

        if (trims.get(p.getUniqueId()).equalsIgnoreCase("Wind Trim")) {
            if (selectedAbility.equals("levitate")) {
                selectAbility(p, abilties, "Levitate", "Levitation", 2, playerLevel);
            } else if (selectedAbility.equals("slowfall")) {
                selectAbility(p, abilties, "Slow_Falling", "Slow Falling", 3, playerLevel);
            } else if (selectedAbility.equals("launch")) {
                selectAbility(p, abilties, "Launch", "Launch", 4, playerLevel);
            } else if (selectedAbility.equals("jumpboost")) {
                selectAbility(p, abilties, "DoubleJump", "Double Jump", 5, playerLevel);
            } else if (selectedAbility.equals("blackhole")) {
                selectAbility(p, abilties, "BlackHole", "Black Hole", 6, playerLevel);
            } else {
                p.sendMessage(ChatColor.RED + "That ability is not available for Wind Trim.");
            }
        } else if (trims.get(p.getUniqueId()).equalsIgnoreCase("Water Trim")) {
            if (selectedAbility.equals("suffocate")) {
                selectAbility(p, abilties, "Suffocation", "Suffocation", 5, playerLevel);
            } else if (selectedAbility.equals("water")) {
                selectAbility(p, abilties, "Water", "Water Placing", 5, playerLevel);
            } else if (selectedAbility.equals("jerk")) {
                selectAbility(p, abilties, "Jerk", "Jerk", 6, playerLevel);
            } else {
                p.sendMessage(ChatColor.RED + "That ability is not available for Water Trim.");
            }
        } else if (trims.get(p.getUniqueId()).equalsIgnoreCase("Fire Trim")) {
            if (selectedAbility.equals("dash")) {
                selectAbility(p, abilties, "Dashing", "Dashing", 3, playerLevel);
            } else if (selectedAbility.equals("deflect")) {
                selectAbility(p, abilties, "Deflecting", "Deflecting", 4, playerLevel);
            } else if (selectedAbility.equals("fireball")) {
                selectAbility(p, abilties, "FireBall", "Fire Ball", 5, playerLevel);
            } else if (selectedAbility.equals("firecircle")) {
                selectAbility(p, abilties, "FireCircle", "Fire Circle", 6, playerLevel);
            } else {
                p.sendMessage(ChatColor.RED + "That ability is not available for Fire Trim.");
            }
        } else if (trims.get(p.getUniqueId()).equalsIgnoreCase("Time Trim")) {
            if (selectedAbility.equals("slowness")) {
                selectAbility(p, abilties, "Slowness", "Slowness", 3, playerLevel);
            } else if (selectedAbility.equals("slowmining")) {
                selectAbility(p, abilties, "Mining_Fatigue", "Mining Fatigue", 4, playerLevel);
            } else if (selectedAbility.equals("slowtime")) {
                selectAbility(p, abilties, "SlowTime", "Slow Time", 6, playerLevel);
            } else {
                p.sendMessage(ChatColor.RED + "That ability is not available for Time Trim.");
            }
        } else if (trims.get(p.getUniqueId()).equalsIgnoreCase("Power Trim")) {
            p.sendMessage(ChatColor.RED + "Power Trim abilities are passive and cannot be selected.");
        }
        return true;
    }

    private void selectAbility(Player player, HashMap<UUID, String> abilities, String abilityKey, String displayName, int requiredLevel, int playerLevel) {
        if (playerLevel >= requiredLevel) {
            abilities.put(player.getUniqueId(), abilityKey);
            player.sendMessage(ChatColor.GREEN + displayName + " selected.");
        } else {
            player.sendMessage(ChatColor.RED + displayName + " requires level " + requiredLevel + ".");
        }
    }
}

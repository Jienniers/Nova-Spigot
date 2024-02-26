package anderman.nova.nova.Commands;

import anderman.nova.nova.Nova;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
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
        HashMap<UUID, String> abilties = plugin.active_abilities;
        HashMap<UUID, Integer> level = plugin.levels;
        HashMap<UUID, String> trims = plugin.trims;
        Player p = (Player) commandSender;
        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + "No Ability Was Selected");
        }
        if (trims.get(p.getUniqueId()).equalsIgnoreCase("Wind Trim")) {
            if (args[0].equalsIgnoreCase("levitate")) {
                if (level.get(p.getUniqueId()) >= 2) {
                    abilties.put(p.getUniqueId(), "Levitate");
                    p.sendMessage(ChatColor.GREEN + "Your ability Levitation has been select from Trim " + trims.get(p.getUniqueId()));
                } else {
                    p.sendMessage(ChatColor.RED + "You don't have that level which has that ability");
                }
            } else if (args[0].equalsIgnoreCase("slowfall")) {
                if (level.get(p.getUniqueId()) >= 3) {
                    abilties.put(p.getUniqueId(), "Slow_Falling");
                    p.sendMessage(ChatColor.GREEN + "Your ability Slow Falling has been select from Trim " + trims.get(p.getUniqueId()));
                }
            } else if (args[0].equalsIgnoreCase("launch")) {
                if (level.get(p.getUniqueId()) >= 4) {
                    abilties.put(p.getUniqueId(), "Launch");
                    p.sendMessage(ChatColor.GREEN + "Your ability Launch has been select from Trim " + trims.get(p.getUniqueId()));
                }
            } else if (args[0].equalsIgnoreCase("jumpboost")) {
                if (level.get(p.getUniqueId()) >= 5) {
                    abilties.put(p.getUniqueId(), "DoubleJump");
                    p.sendMessage(ChatColor.GREEN + "Your ability Double Jump has been select from Trim " + trims.get(p.getUniqueId()));
                }
            } else if (args[0].equalsIgnoreCase("blackhole")) {
                if (level.get(p.getUniqueId()) == 6) {
                    abilties.put(p.getUniqueId(), "BlackHole");
                    p.sendMessage(ChatColor.GREEN + "Your ability Black Hole has been select from Trim " + trims.get(p.getUniqueId()));
                }
            }
        } else if (trims.get(p.getUniqueId()).equalsIgnoreCase("Water Trim")) {
            if (args[0].equalsIgnoreCase("suffocate")) {
                if (level.get(p.getUniqueId()) >= 5) {
                    abilties.put(p.getUniqueId(), "Suffocation");
                    p.sendMessage(ChatColor.GREEN + "Your ability Suffocation has been select from Trim " + trims.get(p.getUniqueId()));
                }
            } else if (args[0].equalsIgnoreCase("water")) {
                if (level.get(p.getUniqueId()) >= 5) {
                    abilties.put(p.getUniqueId(), "Water");
                    p.sendMessage(ChatColor.GREEN + "Your ability Water Placing has been select from Trim " + trims.get(p.getUniqueId()));
                }
            } else if (args[0].equalsIgnoreCase("jerk")) {
                if (level.get(p.getUniqueId()) == 6) {
                    abilties.put(p.getUniqueId(), "Jerk");
                    p.sendMessage(ChatColor.GREEN + "Your ability Jerking has been select from Trim " + trims.get(p.getUniqueId()));
                }
            }
        } else if (trims.get(p.getUniqueId()).equalsIgnoreCase("Fire Trim")) {
            if (args[0].equalsIgnoreCase("dash")) {
                if (level.get(p.getUniqueId()) >= 3) {
                    abilties.put(p.getUniqueId(), "Dashing");
                    p.sendMessage(ChatColor.GREEN + "Your ability Dashing has been select from Trim " + trims.get(p.getUniqueId()));
                } else if (args[0].equalsIgnoreCase("deflect")) {
                    if (level.get(p.getUniqueId()) >= 4) {
                        abilties.put(p.getUniqueId(), "Deflecting");
                        p.sendMessage(ChatColor.GREEN + "Your ability Deflecting has been select from Trim " + trims.get(p.getUniqueId()));
                    }
                }
                if (level.get(p.getUniqueId()) >= 5) {
                    if (args[0].equalsIgnoreCase("fireball")) {
                        abilties.put(p.getUniqueId(), "FireBall");
                        p.sendMessage(ChatColor.GREEN + "Your ability Fire Ball has been select from Trim " + trims.get(p.getUniqueId()));
                    }
                }
                if (level.get(p.getUniqueId()) == 6) {
                    if (args[0].equalsIgnoreCase("firecircle")) {
                        abilties.put(p.getUniqueId(), "FireCircle");
                        p.sendMessage(ChatColor.GREEN + "Your ability Fire Circle has been select from Trim " + trims.get(p.getUniqueId()));
                    }
                }
            } else if (trims.get(p.getUniqueId()).equalsIgnoreCase("Time Trim")) {
                if (level.get(p.getUniqueId()) >= 4) {
                    if (args[0].equalsIgnoreCase("slowness")) {
                        abilties.put(p.getUniqueId(), "Slowness");
                        p.sendMessage(ChatColor.GREEN + "Your ability Slowness has been select from Trim " + trims.get(p.getUniqueId()));
                    } else if (args[0].equalsIgnoreCase("slowmining")) {
                        abilties.put(p.getUniqueId(), "Mining_Fatigue");
                        p.sendMessage(ChatColor.GREEN + "Your ability Mining Fatigue has been select from Trim " + trims.get(p.getUniqueId()));
                    }
                    if (level.get(p.getUniqueId()) == 6) {
                        if (args[0].equalsIgnoreCase("slowtime")) {
                            abilties.put(p.getUniqueId(), "SlowTime");
                            p.sendMessage(ChatColor.GREEN + "Your ability SlowTime has been select from Trim " + trims.get(p.getUniqueId()));
                        }
                    }
                }
            }
        }
        return true;
    }
}
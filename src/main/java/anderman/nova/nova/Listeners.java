package anderman.nova.nova;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;


public class Listeners implements Listener {
    private static Nova plugin;
    private final Set<UUID> playersWhoHaveJoined = new HashSet<>();
    private HashMap<UUID, Boolean> Jump_cooldown = new HashMap<UUID, Boolean>();
    private HashMap<UUID, Boolean> levitating_cooldown = new HashMap<UUID, Boolean>();
    private HashMap<UUID, Boolean> slowfalling = new HashMap<UUID, Boolean>();
    private HashMap<UUID, Boolean> launch_cooldown = new HashMap<UUID, Boolean>();
    private HashMap<UUID, Boolean> suffo = new HashMap<UUID, Boolean>();
    private HashMap<UUID, Boolean> moreHearts = new HashMap<UUID, Boolean>();
    private HashMap<UUID, Boolean> slowness = new HashMap<UUID, Boolean>();
    private HashMap<UUID, Boolean> mining_fatigue = new HashMap<UUID, Boolean>();
    private HashMap<UUID, Boolean> slowtime = new HashMap<UUID, Boolean>();

    private HashMap<UUID, Boolean> delfect_cooldown = new HashMap<UUID, Boolean>();
    private HashMap<UUID, Boolean> Circle_Damage = new HashMap<UUID, Boolean>();
    private HashMap<UUID, Boolean> fireball_cooldown = new HashMap<UUID, Boolean>();

    private HashMap<UUID, Player> deflect = new HashMap<UUID, Player>();

    private HashSet<Player> playerSet = new HashSet<>();
    private HashSet<Player> Jerk_Radius = new HashSet<>();

    HashMap<Player, Location> Black_Hole = new HashMap<Player, Location>();

    HashMap<UUID, Boolean> Black_Hole_Cooldown = new HashMap<UUID, Boolean>();

    private Location Water_place_center;

    public Listeners(Nova plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        HashMap<UUID, Integer> level = plugin.levels;
        HashMap<UUID, String> trims = plugin.trims;
        Player player = e.getPlayer();
        UUID playerUniqueId = player.getUniqueId();
        if (playersWhoHaveJoined.contains(playerUniqueId)) {
            player.sendMessage("Your Trim Was been selected to: " + trims.get(player.getUniqueId()));
            return;
        }
        playersWhoHaveJoined.add(playerUniqueId);
        if (!level.containsKey(playerUniqueId)) {
            level.put(playerUniqueId, 3);
            player.sendMessage("Your Level is: " + level.get(playerUniqueId));
        }
        String[] items = {"Wind Trim", "Water Trim", "Fire Trim", "Time Trim", "Power Trim"};
        Random random = new Random();
        int randomIndex = random.nextInt(items.length);
        String selectedItem = items[randomIndex];
        trims.put(playerUniqueId, selectedItem);
        player.sendMessage("Your Trim has been selected to: "+selectedItem);
        Jump_cooldown.put(playerUniqueId, false);
        levitating_cooldown.put(player.getUniqueId(), false);
        slowfalling.put(player.getUniqueId(), false);
        launch_cooldown.put(player.getUniqueId(), false);
        suffo.put(player.getUniqueId(), false);
        moreHearts.put(player.getUniqueId(), false);
        slowness.put(playerUniqueId, false);
        mining_fatigue.put(playerUniqueId, false);
        slowtime.put(playerUniqueId, false);
        delfect_cooldown.put(playerUniqueId, false);
        Circle_Damage.put(playerUniqueId, false);
        fireball_cooldown.put(playerUniqueId, false);
        Black_Hole_Cooldown.put(playerUniqueId, false);
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent e) {
        HashMap<UUID, Integer> level = plugin.levels;
        //Set<String> banned_players = plugin.bannedPlayers;
        //banned_players = new HashSet<>();
        Player p = e.getEntity();
        Player killer = p.getKiller();
        if (p instanceof Player) {
            level.put(p.getUniqueId(), level.get(p.getUniqueId()) - 1);
            if (p.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                p.removePotionEffect(PotionEffectType.FAST_DIGGING);
            }
            if (p.hasPotionEffect(PotionEffectType.SPEED)) {
                p.removePotionEffect(PotionEffectType.SPEED);
            }
            if (level.get(p.getUniqueId()) <= 0) {
                playerSet.add(p);
                p.getServer().getBanList(BanList.Type.NAME).addBan(p.getName(), "Reached level 0", null, null);
                p.kickPlayer("You Have Been Banned For Reaching Level 0");
            }
            p.sendMessage(ChatColor.RED + "Your Current level is decreased to : " + level.get(p.getUniqueId()));
        }
        if (killer instanceof Player) {
            if (level.get(killer.getUniqueId()) < 6) {
                level.put(killer.getUniqueId(), level.get(killer.getUniqueId()) + 1);
                killer.sendMessage(ChatColor.GREEN + "Your Current level is increased to : " + level.get(killer.getUniqueId()));
            } else {
                killer.sendMessage(ChatColor.GREEN + "You have the Max Level!");
            }
        }
        try {
            if (Jerk_Radius.isEmpty()) {
                placeWaterAroundPlayer(Water_place_center, 9, Material.AIR);
            } else {
                Jerk_Radius.remove(p);
            }
        }catch(Exception error){
            return;
        }
    }

    @EventHandler
    public void InventoryArmor(InventoryClickEvent e) {
        HashMap<UUID, Integer> level = plugin.levels;
        Player player = (Player) e.getWhoClicked();
        HashMap<UUID, String> trims = plugin.trims;
        Inventory clickedInventory = e.getClickedInventory();
        if (clickedInventory != null && e.getView().getTitle().equals("Banned Players")) {
            e.setCancelled(true); // Prevents the items from being moved or taken

            // Handle the clicked item
            ItemStack clickedItem = e.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.PLAYER_HEAD) {
                SkullMeta skullMeta = (SkullMeta) clickedItem.getItemMeta();
                if (skullMeta != null && skullMeta.hasOwner()) {
                    String playerName = skullMeta.getOwningPlayer().getName();
                    if (playerName != null) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
                        if (offlinePlayer != null) {
                            if (offlinePlayer.hasPlayedBefore()) {
                                player.sendMessage("Unbanning player: " + offlinePlayer.getName());
                                Bukkit.getBanList(BanList.Type.NAME).pardon(offlinePlayer.getName());

                                level.put(offlinePlayer.getUniqueId(),3);
                                // Remove player from the set
                                playerSet.remove(offlinePlayer);

                                // Update the GUI
                                updateBannedPlayersGUI(player);

                            } else {
                                player.sendMessage("Player not found or never played before.");
                            }
                        }
                    }
                }
            }
        }
        try{
        if (e.getSlotType() == InventoryType.SlotType.ARMOR) {
            ItemStack helmet = player.getInventory().getHelmet();
            ItemStack chestplate = player.getInventory().getChestplate();
            ItemStack leggings = player.getInventory().getLeggings();
            ItemStack boots = player.getInventory().getBoots();
            ArmorMeta HelmetArmor = (ArmorMeta) helmet.getItemMeta();
            ArmorMeta ChestplateArmor = (ArmorMeta) chestplate.getItemMeta();
            ArmorMeta LeggingsArmor = (ArmorMeta) leggings.getItemMeta();
            ArmorMeta BootsArmor = (ArmorMeta) boots.getItemMeta();
            if (helmet != null || chestplate != null || leggings != null || boots != null) {
                if (trims.get(player.getUniqueId()).equals("Wind Trim")) {
                    ArmorTrim helmet_trim1 = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.VEX);
                    ArmorTrim chestplate_trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.VEX);
                    ArmorTrim leggings_trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.VEX);
                    ArmorTrim boots_trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.VEX);
                    HelmetArmor.setTrim(helmet_trim1);
                    ChestplateArmor.setTrim(chestplate_trim);
                    LeggingsArmor.setTrim(leggings_trim);
                    BootsArmor.setTrim(boots_trim);
                } else if (trims.get(player.getUniqueId()).equals("Water Trim")) {
                    ArmorTrim helmet_trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.EYE);
                    ArmorTrim chestplate_trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.EYE);
                    ArmorTrim leggings_trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.EYE);
                    ArmorTrim boots_trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.EYE);
                    HelmetArmor.setTrim(helmet_trim);
                    ChestplateArmor.setTrim(chestplate_trim);
                    LeggingsArmor.setTrim(leggings_trim);
                    BootsArmor.setTrim(boots_trim);
                } else if (trims.get(player.getUniqueId()).equals("Fire Trim")) {
                    ArmorTrim helmet_trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.WILD);
                    ArmorTrim chestplate_trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.WILD);
                    ArmorTrim leggings_trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.WILD);
                    ArmorTrim boots_trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.WILD);
                    HelmetArmor.setTrim(helmet_trim);
                    ChestplateArmor.setTrim(chestplate_trim);
                    LeggingsArmor.setTrim(leggings_trim);
                    BootsArmor.setTrim(boots_trim);
                } else if (trims.get(player.getUniqueId()).equals("Time Trim")) {
                    ArmorTrim helmet_trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.DUNE);
                    ArmorTrim chestplate_trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.DUNE);
                    ArmorTrim leggings_trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.DUNE);
                    ArmorTrim boots_trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.DUNE);
                    HelmetArmor.setTrim(helmet_trim);
                    ChestplateArmor.setTrim(chestplate_trim);
                    LeggingsArmor.setTrim(leggings_trim);
                    BootsArmor.setTrim(boots_trim);
                } else if (trims.get(player.getUniqueId()).equals("Power Trim")) {
                    ArmorTrim helmet_trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.RIB);
                    ArmorTrim chestplate_trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.RIB);
                    ArmorTrim leggings_trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.RIB);
                    ArmorTrim boots_trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.RIB);
                    HelmetArmor.setTrim(helmet_trim);
                    ChestplateArmor.setTrim(chestplate_trim);
                    LeggingsArmor.setTrim(leggings_trim);
                    BootsArmor.setTrim(boots_trim);
                }
                helmet.setItemMeta(HelmetArmor);
                chestplate.setItemMeta(ChestplateArmor);
                leggings.setItemMeta(LeggingsArmor);
                boots.setItemMeta(BootsArmor);
            }
            }
        }catch (Exception error){
            return;
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        HashMap<UUID, String> abilties = plugin.active_abilities;
        HashMap<UUID, Integer> level = plugin.levels;
        HashMap<UUID, String> trims = plugin.trims;
        //Set<String> banned_players = plugin.bannedPlayers;

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack heldItem = p.getInventory().getItemInMainHand();
            if (heldItem.getType().equals(Material.ECHO_SHARD) && heldItem.getItemMeta().getDisplayName().equalsIgnoreCase("Level Upgrader")) {
                if (level.get(p.getUniqueId()) < 6) {
                    level.put(p.getUniqueId(), level.get(p.getUniqueId()) + 1);
                    p.sendMessage(ChatColor.GREEN + "Your Current level is increased to : " + level.get(p.getUniqueId()));
                    if (heldItem != null && heldItem.getType() != Material.AIR) {
                        heldItem.setAmount(heldItem.getAmount() - 1);
                        p.getInventory().setItemInMainHand(heldItem);
                    }
                } else {
                    p.sendMessage(ChatColor.GREEN + "You have the Max Level!");
                }
            }

            if (heldItem.getType().equals(Material.PURPLE_GLAZED_TERRACOTTA) && heldItem.getItemMeta().getDisplayName().equalsIgnoreCase("Trim Swapper")) {
                String[] items = {"Wind Trim", "Water Trim", "Fire Trim", "Time Trim", "Power Trim"};
                Random random = new Random();
                int randomIndex = random.nextInt(items.length);
                String selectedItem = items[randomIndex];
                trims.put(p.getUniqueId(), selectedItem);
                level.put(p.getUniqueId(), 3);
                p.sendMessage("Your Trim has been selected to: " + trims.get(p.getUniqueId()));
                p.sendMessage("Your level is: " + level.get(p.getUniqueId()));
                for (PotionEffect effect : p.getActivePotionEffects()){
                    p.removePotionEffect(effect.getType());
                }
                updateArmorEnchantments(p);
                abilties.remove((p.getUniqueId()));
                if (heldItem != null && heldItem.getType() != Material.AIR) {
                    heldItem.setAmount(heldItem.getAmount() - 1);
                    p.getInventory().setItemInMainHand(heldItem);
                }
            }

            if (heldItem.getType().equals(Material.BEACON) && heldItem.getItemMeta().getDisplayName().equalsIgnoreCase("Revive Beacon")) {
                int slots;
                if (playerSet.size() < 9){
                    slots = 9;
                }else{
                    slots = playerSet.size() * 2;
                }
                Inventory gui = Bukkit.createInventory(p, slots, "Banned Players");
                for (Player target : playerSet) {
                    ItemStack playerHead = createPlayerHead(target.getName());
                    gui.addItem(playerHead);
                }
                p.openInventory(gui);
                if (heldItem != null && heldItem.getType() != Material.AIR) {
                    heldItem.setAmount(heldItem.getAmount() - 1);
                    p.getInventory().setItemInMainHand(heldItem);
                }
            }

            Material[] swordMaterials = {
                    Material.WOODEN_SWORD,
                    Material.STONE_SWORD,
                    Material.IRON_SWORD,
                    Material.GOLDEN_SWORD,
                    Material.DIAMOND_SWORD,
                    Material.NETHERITE_SWORD
            };
            boolean isSword = false;
            for (Material swordMaterial : swordMaterials) {
                if (heldItem.getType() == swordMaterial) {
                    isSword = true;
                    break;
                }
            }
            if (isSword) {
                int player_level = level.get(p.getUniqueId());
                ItemStack helmet = p.getInventory().getHelmet();
                ItemStack chestplate = p.getInventory().getChestplate();
                ItemStack leggings = p.getInventory().getLeggings();
                ItemStack boots = p.getInventory().getBoots();
                if (helmet == null && chestplate == null && leggings == null && boots == null) {
                    return;
                }
                ArmorMeta HelmetArmor = (ArmorMeta) helmet.getItemMeta();
                ArmorMeta ChestplateArmor = (ArmorMeta) chestplate.getItemMeta();
                ArmorMeta LeggingsArmor = (ArmorMeta) leggings.getItemMeta();
                ArmorMeta BootsArmor = (ArmorMeta) boots.getItemMeta();
                if (trims.get(p.getUniqueId()).equalsIgnoreCase("Wind Trim")) {
                    if (HelmetArmor.getTrim().getMaterial().equals(TrimMaterial.GOLD) && HelmetArmor.getTrim().getPattern().equals(TrimPattern.VEX)
                            && ChestplateArmor.getTrim().getMaterial().equals(TrimMaterial.GOLD) && ChestplateArmor.getTrim().getPattern().equals(TrimPattern.VEX)
                            && LeggingsArmor.getTrim().getMaterial().equals(TrimMaterial.GOLD) && LeggingsArmor.getTrim().getPattern().equals(TrimPattern.VEX)
                            && BootsArmor.getTrim().getMaterial().equals(TrimMaterial.GOLD) && BootsArmor.getTrim().getPattern().equals(TrimPattern.VEX)) {
                        if (level.get(p.getUniqueId()) == 2) {
                            Levitaion(p);
                        } else if (level.get(p.getUniqueId()) == 3) {
                            if (abilties.containsKey(p.getUniqueId()) && abilties.get(p.getUniqueId()).equalsIgnoreCase("Levitate")) {
                                Levitaion(p);
                            } else {
                                SlowFalling(p);
                            }
                        } else if (level.get(p.getUniqueId()) == 4) {
                            if (abilties.containsKey(p.getUniqueId())) {
                                if (abilties.get(p.getUniqueId()).equalsIgnoreCase("Launch")) {
                                    Launch(p);
                                } else if (abilties.get(p.getUniqueId()).equalsIgnoreCase("Slow_Falling")) {
                                    SlowFalling(p);
                                } else if (abilties.get(p.getUniqueId()).equalsIgnoreCase("Levitate")) {
                                    Levitaion(p);
                                } else if (abilties.get(p.getUniqueId()).equalsIgnoreCase("Launch")) {
                                    Launch(p);
                                }
                            } else {
                                Launch(p);
                            }
                        } else if (level.get(p.getUniqueId()) == 5) {
                            if (abilties.containsKey(p.getUniqueId())) {
                                if (abilties.get(p.getUniqueId()).equalsIgnoreCase("Launch")) {
                                    Launch(p);
                                } else if (abilties.get(p.getUniqueId()).equalsIgnoreCase("Slow_Falling")) {
                                    SlowFalling(p);
                                } else if (abilties.get(p.getUniqueId()).equalsIgnoreCase("Levitate")) {
                                    Levitaion(p);
                                } else if (abilties.get(p.getUniqueId()).equalsIgnoreCase("DoubleJump")) {
                                    DoubleJump(p);
                                }
                            } else {
                                DoubleJump(p);
                            }
                        } else if (level.get(p.getUniqueId()) == 6) {
                            if (abilties.containsKey(p.getUniqueId())) {
                                if (abilties.get(p.getUniqueId()).equalsIgnoreCase("Launch")) {
                                    Launch(p);
                                } else if (abilties.get(p.getUniqueId()).equalsIgnoreCase("Slow_Falling")) {
                                    SlowFalling(p);
                                } else if (abilties.get(p.getUniqueId()).equalsIgnoreCase("Levitate")) {
                                    Levitaion(p);
                                } else if (abilties.get(p.getUniqueId()).equalsIgnoreCase("DoubleJump")) {
                                    DoubleJump(p);
                                } else if (abilties.get(p.getUniqueId()).equalsIgnoreCase("BlackHole")) {
                                    BlackHole(p);
                                }
                            } else {
                                BlackHole(p);
                            }
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "You are not wearing Wind trim armor");
                    }
                } else if (trims.get(p.getUniqueId()).equalsIgnoreCase("Water Trim")) {
                    if (HelmetArmor.getTrim().getMaterial().equals(TrimMaterial.GOLD) && HelmetArmor.getTrim().getPattern().equals(TrimPattern.EYE)
                            && ChestplateArmor.getTrim().getMaterial().equals(TrimMaterial.GOLD) && ChestplateArmor.getTrim().getPattern().equals(TrimPattern.EYE)
                            && LeggingsArmor.getTrim().getMaterial().equals(TrimMaterial.GOLD) && LeggingsArmor.getTrim().getPattern().equals(TrimPattern.EYE)
                            && BootsArmor.getTrim().getMaterial().equals(TrimMaterial.GOLD) && BootsArmor.getTrim().getPattern().equals(TrimPattern.EYE)) {
                        if (level.get(p.getUniqueId()) == 1) {
                            if (p.getInventory().getHelmet() != null) {
                                p.getInventory().getHelmet().addEnchantment(Enchantment.WATER_WORKER, 1);
                            }
                        } else if (level.get(p.getUniqueId()) == 2) {
                            if (p.getInventory().getHelmet() != null) {
                                p.getInventory().getHelmet().addEnchantment(Enchantment.WATER_WORKER, 1);
                            }
                            PotionEffect waterBreathingEffect = new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 1, true, false);
                            p.addPotionEffect(waterBreathingEffect);
                        } else if (level.get(p.getUniqueId()) == 3) {
                            if (p.getInventory().getHelmet() != null) {
                                p.getInventory().getHelmet().addEnchantment(Enchantment.WATER_WORKER, 1);
                            }
                            PotionEffect waterBreathingEffect = new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 1, true, false);
                            p.addPotionEffect(waterBreathingEffect);
                            PotionEffect dolphinsGraceEffect = new PotionEffect(PotionEffectType.DOLPHINS_GRACE, Integer.MAX_VALUE, 0, true, false);
                            p.addPotionEffect(dolphinsGraceEffect);
                        } else if (level.get(p.getUniqueId()) == 4) {
                            if (p.getInventory().getHelmet() != null) {
                                p.getInventory().getHelmet().addEnchantment(Enchantment.WATER_WORKER, 1);
                            }
                            PotionEffect waterBreathingEffect = new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 1, true, false);
                            p.addPotionEffect(waterBreathingEffect);
                            PotionEffect dolphinsGraceEffect = new PotionEffect(PotionEffectType.DOLPHINS_GRACE, Integer.MAX_VALUE, 0, true, false);
                            p.addPotionEffect(dolphinsGraceEffect);
                            suffocation(p);
                        } else if (level.get(p.getUniqueId()) == 5) {
                            if (p.getInventory().getHelmet() != null) {
                                p.getInventory().getHelmet().addEnchantment(Enchantment.WATER_WORKER, 1);
                            }
                            PotionEffect waterBreathingEffect = new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 1, true, false);
                            p.addPotionEffect(waterBreathingEffect);
                            PotionEffect dolphinsGraceEffect = new PotionEffect(PotionEffectType.DOLPHINS_GRACE, Integer.MAX_VALUE, 0, true, false);
                            p.addPotionEffect(dolphinsGraceEffect);
                            if (abilties.containsKey(p.getUniqueId())) {
                                if (abilties.get(p.getUniqueId()).equalsIgnoreCase("Suffocation")) {
                                    suffocation(p);
                                } else if (abilties.get(p.getUniqueId()).equals("Water")) {
                                    placewater(p);
                                }
                            } else {
                                placewater(p);
                            }
                        } else if (level.get(p.getUniqueId()) == 6) {
                            if (abilties.containsKey(p.getUniqueId())) {
                                if (abilties.get(p.getUniqueId()).equalsIgnoreCase("Suffocation")) {
                                    suffocation(p);
                                } else if (abilties.get(p.getUniqueId()).equals("Water")) {
                                    placewater(p);
                                } else if (abilties.get(p.getUniqueId()).equals("Jerk")) {
                                    Jerk_around(p);
                                }
                            } else {
                                Jerk_around(p);
                            }
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "You are not wearing Water trim armor");
                    }
                } else if (trims.get(p.getUniqueId()).equalsIgnoreCase("Fire Trim")) {
                    if (HelmetArmor.getTrim().getMaterial().equals(TrimMaterial.GOLD) && HelmetArmor.getTrim().getPattern().equals(TrimPattern.WILD)
                            && ChestplateArmor.getTrim().getMaterial().equals(TrimMaterial.GOLD) && ChestplateArmor.getTrim().getPattern().equals(TrimPattern.WILD)
                            && LeggingsArmor.getTrim().getMaterial().equals(TrimMaterial.GOLD) && LeggingsArmor.getTrim().getPattern().equals(TrimPattern.WILD)
                            && BootsArmor.getTrim().getMaterial().equals(TrimMaterial.GOLD) && BootsArmor.getTrim().getPattern().equals(TrimPattern.WILD)) {
                        if (level.get(p.getUniqueId()) == 1) {
                            PotionEffect waterBreathingEffect = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, true, false);
                            p.addPotionEffect(waterBreathingEffect);
                        } else if (player_level == 2) {
                            createFireCircle(p, 5);
                        } else if (player_level == 3) {
                            PotionEffect waterBreathingEffect = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, true, false);
                            p.addPotionEffect(waterBreathingEffect);
                            dash(p);
                        } else if (player_level == 4) {
                            if (abilties.containsKey(p.getUniqueId())) {
                                if (abilties.get(p.getUniqueId()).equals("Dashing")) {
                                    dash(p);
                                } else if (abilties.get(p.getUniqueId()).equals("Deflecting")) {
                                    deflect.put(p.getUniqueId(), p);
                                }
                            } else {
                                if (!delfect_cooldown.get(p.getUniqueId())) {
                                    deflect.put(p.getUniqueId(), p);
                                    delfect_cooldown.put(p.getUniqueId(), true);
                                }
                                Bukkit.getScheduler().runTaskLater(plugin, () -> delfect_cooldown.put(p.getUniqueId(), false), 35 * 20);
                            }
                        } else if (player_level == 5) {
                            if (abilties.containsKey(p.getUniqueId())) {
                                if (abilties.get(p.getUniqueId()).equals("Dashing")) {
                                    dash(p);
                                } else if (abilties.get(p.getUniqueId()).equals("Deflecting")) {
                                    deflect.put(p.getUniqueId(), p);
                                } else if (abilties.get(p.getUniqueId()).equals("FireBall")) {
                                    Fireball(p);
                                }
                            } else {
                                Fireball(p);
                            }
                        } else if (player_level == 6) {
                            if (abilties.containsKey(p.getUniqueId())) {
                                if (abilties.get(p.getUniqueId()).equals("Dashing")) {
                                    dash(p);
                                } else if (abilties.get(p.getUniqueId()).equals("Deflecting")) {
                                    deflect.put(p.getUniqueId(), p);
                                } else if (abilties.get(p.getUniqueId()).equals("FireBall")) {
                                    Fireball(p);
                                } else if (abilties.get(p.getUniqueId()).equals("FireCircle")) {
                                    FireCircleDamage(p, 5);
                                }
                            } else {
                                FireCircleDamage(p, 5);
                            }
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "You are not wearing Fire trim armor");
                    }
                } else if (trims.get(p.getUniqueId()).equalsIgnoreCase("Time Trim")) {
                    if (HelmetArmor.getTrim().getMaterial().equals(TrimMaterial.GOLD) && HelmetArmor.getTrim().getPattern().equals(TrimPattern.DUNE)
                            && ChestplateArmor.getTrim().getMaterial().equals(TrimMaterial.GOLD) && ChestplateArmor.getTrim().getPattern().equals(TrimPattern.DUNE)
                            && LeggingsArmor.getTrim().getMaterial().equals(TrimMaterial.GOLD) && LeggingsArmor.getTrim().getPattern().equals(TrimPattern.DUNE)
                            && BootsArmor.getTrim().getMaterial().equals(TrimMaterial.GOLD) && BootsArmor.getTrim().getPattern().equals(TrimPattern.DUNE)) {
                        if (player_level == 2) {
                            PotionEffect Fast_Dig = new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1, true, false);
                            p.addPotionEffect(Fast_Dig);
                        } else if (player_level == 3) {
                            PotionEffect Fast_Dig = new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1, true, false);
                            p.addPotionEffect(Fast_Dig);
                            Slowness(p);
                        } else if (player_level == 4) {
                            PotionEffect Fast_Dig = new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1, true, false);
                            p.addPotionEffect(Fast_Dig);
                            if (abilties.containsKey(p.getUniqueId())) {
                                if (abilties.get(p.getUniqueId()).equals("Slowness")) {
                                    Slowness(p);
                                } else if (abilties.get(p.getUniqueId()).equals("Mining_Fatigue")) {
                                    Mining_Fatigue(p);
                                }
                            } else {
                                Mining_Fatigue(p);
                            }
                        } else if (player_level == 5) {
                            PotionEffect Fast_Dig = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, true, false);
                            p.addPotionEffect(Fast_Dig);
                        } else if (player_level == 6) {
                            PotionEffect Fast_Dig = new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1, true, false);
                            p.addPotionEffect(Fast_Dig);
                            if (abilties.containsKey(p.getUniqueId())) {
                                if (abilties.get(p.getUniqueId()).equals("Slowness")) {
                                    Slowness(p);
                                } else if (abilties.get(p.getUniqueId()).equals("Mining_Fatigue")) {
                                    Mining_Fatigue(p);
                                } else if (abilties.get(p.getUniqueId()).equals("SlowTime")) {
                                    SlowTime(p);
                                }
                            } else {
                                SlowTime(p);
                            }
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "You are not wearing Time trim armor");
                    }
                } else if (trims.get(p.getUniqueId()).equalsIgnoreCase("Power Trim")) {
                    if (HelmetArmor.getTrim().getMaterial().equals(TrimMaterial.GOLD) && HelmetArmor.getTrim().getPattern().equals(TrimPattern.RIB)
                            && ChestplateArmor.getTrim().getMaterial().equals(TrimMaterial.GOLD) && ChestplateArmor.getTrim().getPattern().equals(TrimPattern.RIB)
                            && LeggingsArmor.getTrim().getMaterial().equals(TrimMaterial.GOLD) && LeggingsArmor.getTrim().getPattern().equals(TrimPattern.RIB)
                            && BootsArmor.getTrim().getMaterial().equals(TrimMaterial.GOLD) && BootsArmor.getTrim().getPattern().equals(TrimPattern.RIB)) {
                        if (player_level == 2) {
                            Protection(p);
                        } else if (player_level == 3) {
                            Protection(p);
                            PotionEffect stengthEffect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, true, false);
                            p.addPotionEffect(stengthEffect);
                        } else if (player_level == 4) {
                            Protection(p);
                            PotionEffect stengthEffect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, true, false);
                            p.addPotionEffect(stengthEffect);
                            MoreHearts(p);
                        } else if (player_level == 5) {
                            Protection(p);
                            PotionEffect stengthEffect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, true, false);
                            p.addPotionEffect(stengthEffect);
                            MoreHearts(p);
                            PotionEffect regenerationEffect = new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0);
                            p.addPotionEffect(regenerationEffect);
                        } else if (player_level == 6) {
                            Protection(p);
                            PotionEffect stengthEffect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, true, false);
                            p.addPotionEffect(stengthEffect);
                            MoreHearts(p);
                            PotionEffect regenerationEffect = new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0);
                            p.addPotionEffect(regenerationEffect);
                            MakeExplosion(p);
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "You are not wearing Power trim armor");
                    }
                }
            }
        }
    }

    private void Levitaion(Player p) {
        PotionEffect levitationEffect = new PotionEffect(PotionEffectType.LEVITATION, 200, 0);
        if (p.hasPotionEffect(levitationEffect.getType())) {
            p.sendMessage("Levitation effect is or already active.");
            return;
        }
        if (!levitating_cooldown.get(p.getUniqueId())) {
            p.addPotionEffect(levitationEffect);
            levitating_cooldown.put(p.getUniqueId(), true);
        } else {
            return;
        }
        new BukkitRunnable() {
            int cooldownTimer = 20;

            @Override
            public void run() {
                if (cooldownTimer == 0) {
                    levitating_cooldown.put(p.getUniqueId(), false);
                    p.sendMessage("Levitation effect cooldown complete.");
                    cancel();
                } else {
                    cooldownTimer--;
                }
            }
        }.runTaskTimer(plugin, 200L, 20L);
    }

    private void SlowFalling(Player p) {
        double radius = 5.0;
        for (Entity entity : p.getWorld().getNearbyEntities(p.getLocation(), radius, radius, radius)) {
            if (entity instanceof Player) {
                Player nearbyPlayer = (Player) entity;
                if (nearbyPlayer != p) {
                    PotionEffect levitationEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 200, 0);
                    if (nearbyPlayer.hasPotionEffect(levitationEffect.getType())) {
                        return;
                    }
                    if (!slowfalling.get(p.getUniqueId())) {
                        nearbyPlayer.addPotionEffect(levitationEffect);
                        slowfalling.put(p.getUniqueId(), true);
                    } else {
                        return;
                    }
                    new BukkitRunnable() {
                        int cooldownTimer = 25;

                        @Override
                        public void run() {
                            if (cooldownTimer == 0) {
                                slowfalling.put(p.getUniqueId(), false);
                                cancel();
                            } else {
                                cooldownTimer--;
                            }
                        }
                    }.runTaskTimer(plugin, 200L, 20L);
                }
            }
        }
    }

    public void Launch(Player p) {
        double launchHeight = 150.0;
        double radius = 10.0;
        if (!launch_cooldown.get(p.getUniqueId())) {
            for (Entity entity : p.getWorld().getNearbyEntities(p.getLocation(), radius, radius, radius)) {
                if (entity != p) {
                    entity.setVelocity(entity.getVelocity().setY(launchHeight / 20.0));
                }
            }
            launch_cooldown.put(p.getUniqueId(), true);
        }
        if (launch_cooldown.get(p.getUniqueId())) {
            p.sendMessage("Launch is on Cooldown!");
        }

        new BukkitRunnable() {
            int cooldownTimer = 35;

            @Override
            public void run() {
                if (cooldownTimer == 0) {
                    launch_cooldown.put(p.getUniqueId(), false);
                    cancel();
                } else {
                    cooldownTimer--;
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public void DoubleJump(Player p) {
        if (!Jump_cooldown.get(p.getUniqueId())) {
            p.setVelocity(p.getVelocity().setY(0.9));
            Jump_cooldown.put(p.getUniqueId(), true);
        } else {
            p.sendMessage(ChatColor.RED + "Double Jump is on Cooldown!");
        }
        p.getServer().getScheduler().runTaskLater(plugin, () -> Jump_cooldown.put(p.getUniqueId(), false), 7 * 20);
    }

    //Location playerLocation;

//    public void suffocation(Player p) {
//        double radius = 5.0;
//        for (Entity entity : p.getWorld().getNearbyEntities(p.getLocation(), radius, radius, radius)) {
//            if (entity instanceof Player) {
//                Player nearbyPlayer = (Player) entity;
//                playerLocation = nearbyPlayer.getLocation();
//                if (nearbyPlayer != p) {
//                    if (suffo.get(p.getUniqueId())) {
//                        return;
//                    }
//
//                    if (!suffo.get(p.getUniqueId())) {
//                        for (int x = -1; x <= 1; x++) {
//                            for (int y = -1; y <= 1; y++) {
//                                for (int z = -1; z <= 1; z++) {
//                                    Location blockLocation = new Location(playerLocation.getWorld(), playerLocation.getX() + x, playerLocation.getY() + y, playerLocation.getZ() + z);
//                                    Block block = blockLocation.getBlock();
//                                    if (block.getType() == Material.AIR || block.getType() == Material.GRASS) {
//                                        block.setType(Material.BEDROCK);
//                                    }
//                                    suffo.put(p.getUniqueId(), true);
//                                }
//                            }
//                        }
//                    } else {
//                        return;
//                    }
//                    nearbyPlayer.sendMessage(String.valueOf(suffo));
//                    new BukkitRunnable() {
//                        @Override
//                        public void run() {
//                            if (suffo.get(p.getUniqueId())) {
//                                for (int x = -1; x <= 1; x++) {
//                                    for (int y = -1; y <= 1; y++) {
//                                        for (int z = -1; z <= 1; z++) {
//                                            Location blockLocation = new Location(playerLocation.getWorld(), playerLocation.getX() + x, playerLocation.getY() + y, playerLocation.getZ() + z);
//                                            Block block = blockLocation.getBlock();
//                                            if (block.getType() == Material.BEDROCK || block.getType() == Material.GRASS) {
//                                                block.setType(Material.AIR);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                            cancel();
//                        }
//                    }.runTaskTimer(plugin, 160L, 0L);
//
//                    new BukkitRunnable() {
//                        @Override
//                        public void run() {
//                            suffo.put(p.getUniqueId(), false);
//                            cancel();
//                        }
//                    }.runTaskTimer(plugin, 400L, 20L);
//                }
//            }
//        }
//    }

    public void placewater(Player player) {
        new BukkitRunnable() {
            int countdown = 40;
            int sccountdown = 20;
            boolean one_time_Water = false;
            boolean dispear = false;
            Location new_player_loc;
            Location waterLocation;

            @Override
            public void run() {
                if (countdown == 0) {
                    if (!dispear) {
                        for (int x = -5; x <= 5; x++) {
                            for (int z = -5; z <= 5; z++) {
                                dispear = true;
                                waterLocation = new_player_loc.clone().add(x, 0, z);
                                if (waterLocation.getBlock().getType() == Material.WATER || waterLocation.getBlock().getType() == Material.GRASS) {
                                    waterLocation.getBlock().setType(Material.AIR);
                                }
                            }
                        }
                        cancel();
                    }
                    sccountdown--;
                    if (sccountdown == 0) {
                        countdown = 40;
                    }
                } else {
                    dispear = false;
                    if (!one_time_Water) {
                        for (int x = -5; x <= 5; x++) {
                            for (int z = -5; z <= 5; z++) {
                                waterLocation = player.getLocation().clone().add(x, 0, z);
                                if (waterLocation.getBlock().getType() == Material.AIR || waterLocation.getBlock().getType() == Material.GRASS) {
                                    waterLocation.getBlock().setType(Material.WATER);
                                }
                            }
                        }
                        one_time_Water = true;
                        new_player_loc = player.getLocation();
                    }
                    double radius = 5.0;
                    for (Entity entity : player.getWorld().getNearbyEntities(new_player_loc, radius, radius, radius)) {
                        if (entity instanceof Player) {
                            Player nearbyPlayer = (Player) entity;
                            if (nearbyPlayer != player) {
                                nearbyPlayer.damage(3.0);
                            }
                        }
                    }
                    countdown--;
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void createFireCircle(Player player, int radius) {
        Location playerLocation = player.getLocation();
        for (double theta = 0; theta < 2 * Math.PI; theta += Math.PI / 16) {
            double x = radius * Math.cos(theta);
            double z = radius * Math.sin(theta);
            Location circleLocation = playerLocation.clone().add(x, 0, z);
            if (circleLocation.getBlock().getType() == Material.AIR || circleLocation.getBlock().getType() == Material.GRASS) {
                circleLocation.getBlock().setType(Material.FIRE);
            }
        }
    }

    public void Protection(Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                boolean hasProtection = true;
                for (ItemStack armorPiece : p.getInventory().getArmorContents()) {
                    if (armorPiece == null || armorPiece.getType() == Material.AIR || !armorPiece.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)) {
                        hasProtection = false;
                        break;
                    }
                }

                if (!hasProtection) {
                    // Apply Protection III to all armor pieces.
                    for (ItemStack armorPiece : p.getInventory().getArmorContents()) {
                        if (armorPiece != null && armorPiece.getType() != Material.AIR) {
                            armorPiece.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 10);
    }

    public void MoreHearts(Player p) {
        ItemStack[] armorContents = p.getInventory().getArmorContents();
        for (ItemStack item : armorContents) {
            if (item != null) {
                // The player is wearing armor, so grant 3 extra hearts.
                p.setMaxHealth(20.0 + 3.0);
                return;
            }
        }

        // The player is not wearing armor, set their health back to the default (20 hearts).
        p.setMaxHealth(20.0);
    }

    public void MakeExplosion(Player p) {
        if (moreHearts.get(p.getUniqueId())) {
            return;
        }
        Location explosionLocation = p.getLocation().clone();
        explosionLocation.add(0, 0.5, 0); // Move the explosion slightly upwards to avoid affecting the player
        p.getLocation().getWorld().createExplosion(explosionLocation, 7.0f, true, true);
        for (Entity nearbyEntity : p.getNearbyEntities(7, 7, 7)) {
            if (nearbyEntity != null) {
                if (nearbyEntity instanceof Player) {
                    Player targetPlayer = (Player) nearbyEntity;
                    targetPlayer.damage(30);
                }
            }
        }

        moreHearts.put(p.getUniqueId(), true);

        Bukkit.getScheduler().runTaskLater(plugin, () -> moreHearts.put(p.getUniqueId(), false), 300 * 20);
    }

    private void Slowness(Player p) {
        double radius = 5.0;
        for (Entity entity : p.getWorld().getNearbyEntities(p.getLocation(), radius, radius, radius)) {
            if (entity instanceof Player) {
                Player nearbyPlayer = (Player) entity;
                if (nearbyPlayer != p) {
                    PotionEffect levitationEffect = new PotionEffect(PotionEffectType.SLOW, 20 * 20, 0);
                    if (nearbyPlayer.hasPotionEffect(levitationEffect.getType())) {
                        return;
                    }

                    if (!slowness.get(p.getUniqueId())) {
                        nearbyPlayer.addPotionEffect(levitationEffect);
                        slowness.put(p.getUniqueId(), true);
                    } else {
                        return;
                    }
                    new BukkitRunnable() {
                        int cooldownTimer = 35;

                        @Override
                        public void run() {
                            if (cooldownTimer == 0) {
                                slowness.put(p.getUniqueId(), false);
                                cancel();
                            } else {
                                cooldownTimer--;
                            }
                        }
                    }.runTaskTimer(plugin, 20 * 20, 20L);
                }
            }
        }
    }

    private void Mining_Fatigue(Player p) {
        double radius = 5.0;
        for (Entity entity : p.getWorld().getNearbyEntities(p.getLocation(), radius, radius, radius)) {
            if (entity instanceof Player) {
                Player nearbyPlayer = (Player) entity;
                if (nearbyPlayer != p) {
                    PotionEffect levitationEffect = new PotionEffect(PotionEffectType.SLOW_DIGGING, 30 * 20, 0);
                    if (nearbyPlayer.hasPotionEffect(levitationEffect.getType())) {
                        return;
                    }
                    if (!mining_fatigue.get(p.getUniqueId())) {
                        nearbyPlayer.addPotionEffect(levitationEffect);
                        mining_fatigue.put(p.getUniqueId(), true);
                    } else {
                        return;
                    }
                    new BukkitRunnable() {
                        int cooldownTimer = 60;

                        @Override
                        public void run() {
                            if (cooldownTimer == 0) {
                                mining_fatigue.put(p.getUniqueId(), false);
                                p.sendMessage("Mining Fatigue effect cooldown complete.");
                                cancel();
                            } else {
                                cooldownTimer--;
                            }
                        }
                    }.runTaskTimer(plugin, 30 * 20, 20L);
                }
            }
        }
    }

    private void SlowTime(Player p) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players != p) {
                PotionEffect Slow_Dig = new PotionEffect(PotionEffectType.SLOW_DIGGING, 15 * 20, 0);
                PotionEffect Slow_effect = new PotionEffect(PotionEffectType.SLOW, 15 * 20, 0);
                PotionEffect Slow_fall = new PotionEffect(PotionEffectType.SLOW_FALLING, 15 * 20, 0);
                if (players.hasPotionEffect(Slow_Dig.getType())) {
                    return;
                }
                if (players.hasPotionEffect(Slow_effect.getType())) {
                    return;
                }
                if (players.hasPotionEffect(Slow_fall.getType())) {
                    return;
                }
                if (!slowtime.get(p.getUniqueId())) {
                    players.addPotionEffect(Slow_Dig);
                    players.addPotionEffect(Slow_effect);
                    players.addPotionEffect(Slow_fall);
                    slowtime.put(p.getUniqueId(), true);
                } else {
                    return;
                }
                new BukkitRunnable() {
                    int cooldownTimer = 60 * 5;

                    @Override
                    public void run() {
                        if (cooldownTimer == 0) {
                            slowtime.put(p.getUniqueId(), false);
                            p.sendMessage("Time is already slowed.");
                            cancel();
                        } else {
                            cooldownTimer--;
                        }
                    }
                }.runTaskTimer(plugin, 15 * 20, 20L);
            }
        }
    }

    @EventHandler
    private void onPlayerDamage(EntityDamageByEntityEvent e) {
        Player p = (Player) e.getEntity();
        if (!deflect.containsValue(p)) return;
        if (deflect.get(p.getUniqueId()).equals(p)) {
            Player damager = (Player) e.getDamager();
            double radius = 3.0;
            for (Entity entity : p.getWorld().getNearbyEntities(p.getLocation(), radius, radius, radius)) {
                if (entity instanceof Player) {
                    Player nearbyPlayer = (Player) entity;
                    if (nearbyPlayer != p) {
                        if (damager.equals(nearbyPlayer)) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
        delfect_cooldown.put(p.getUniqueId(), true);
        Bukkit.getScheduler().runTaskLater(plugin, () -> deflect.remove(p.getUniqueId()), 15 * 20);
    }

    private void FireCircleDamage(Player p, int radius) {
        if (!Circle_Damage.get(p.getUniqueId())) {
            Location playerLocation = p.getLocation();
            for (double theta = 0; theta < 2 * Math.PI; theta += Math.PI / 16) {
                double x = radius * Math.cos(theta);
                double z = radius * Math.sin(theta);
                Location circleLocation = playerLocation.clone().add(x, 0, z);
                if (circleLocation.getBlock().getType() == Material.AIR || circleLocation.getBlock().getType() == Material.GRASS) {
                    circleLocation.getBlock().setType(Material.FIRE);
                }
            }
        }
        if (!Circle_Damage.get(p.getUniqueId())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Entity entity : p.getWorld().getNearbyEntities(p.getLocation(), radius, radius, radius)) {
                        if (entity instanceof Player) {
                            Player nearbyPlayer = (Player) entity;
                            if (nearbyPlayer != p) {
                                nearbyPlayer.damage(2);
                                if (!Circle_Damage.get(p.getUniqueId())) {
                                    cancel();
                                }
                            }
                        }
                    }
                }
            }.runTaskTimer(plugin, 0, 20);
        }
        Circle_Damage.put(p.getUniqueId(), true);
        Bukkit.getScheduler().runTaskLater(plugin, () -> Circle_Damage.put(p.getUniqueId(), true), 120 * 20);
    }

    private void dash(Player player) {
        Location playerLocation = player.getLocation();
        Vector playerDirection = player.getEyeLocation().getDirection().normalize();
        Location newLocation = playerLocation.clone().add(playerDirection.multiply(6));
        newLocation.add(0, 1, 0);

        player.teleport(newLocation);

        Location currentLocation = playerLocation.clone();
        currentLocation.add(0, 1, 0);
        Block currentBlock;

        while (!currentLocation.getBlock().equals(newLocation.getBlock())) {
            currentBlock = currentLocation.getBlock();

            if (currentBlock.getType() == Material.AIR) {
                currentBlock.setType(Material.FIRE);
            }

            playerDirection = playerLocation.getDirection().normalize();
            currentLocation.add(playerDirection);
        }

    }

    private void Jerk_around(Player p) {
        Water_place_center = p.getLocation();
        placeWaterAroundPlayer(Water_place_center,6, Material.WATER);
        double radius = 6.0;
        for (Entity entity : p.getWorld().getNearbyEntities(p.getLocation(), radius, radius, radius)) {
            if (entity instanceof Player) {
                Player nearbyPlayer = (Player) entity;
                if (nearbyPlayer != p) {
                    if (!Jerk_Radius.contains(nearbyPlayer)){
                        Jerk_Radius.add(nearbyPlayer);
                    }
                }
            }
        }
        startCircleMovement(p, 6.0);
        new BukkitRunnable() {
            @Override
            public void run(){
                for (Player player : Jerk_Radius) {
                    player.damage(7);
                }
                if (Jerk_Radius.isEmpty()){
                    placeWaterAroundPlayer(Water_place_center,9, Material.AIR);
                    cancel();
                }
            }
        }.runTaskTimer(plugin,0,20);
    }

    private void Fireball(Player p) {
        if (!fireball_cooldown.get(p.getUniqueId())) {
            Location playerLocation = p.getLocation();
            Vector facingDirection = playerLocation.getDirection();
            facingDirection.normalize();
            World world = playerLocation.getWorld();

            SmallFireball fireball = (SmallFireball) world.spawnEntity(playerLocation.add(facingDirection), EntityType.SMALL_FIREBALL);
            fireball.setCustomName("Explosive Fireball");
            fireball.setVelocity(facingDirection.multiply(3.5));

            fireball_cooldown.put(p.getUniqueId(), true);
        }
        Bukkit.getScheduler().runTaskLater(plugin, () -> fireball_cooldown.put(p.getUniqueId(), false), 40 * 20);
    }


    @EventHandler
    public void onProjectHit(ProjectileHitEvent e) {
        Entity entity = e.getEntity();
        if (entity.getCustomName() != null && entity.getCustomName().equals("Explosive Fireball")) {
            e.setCancelled(true);

            entity.getWorld().createExplosion(e.getHitBlock().getLocation(), 6, true, true);

            entity.remove();
        }
    }

    private ItemStack createPlayerHead(String playerName) {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();

        skullMeta.setOwner(playerName);
        skullMeta.setDisplayName(playerName);

        playerHead.setItemMeta(skullMeta);
        return playerHead;
    }

    private void startCircleMovement(Player centerPlayer, double radius) {
        new BukkitRunnable() {
            double angle = 0;

            @Override
            public void run() {
                angle += Math.PI / 30;

                double centerX = centerPlayer.getLocation().getX();
                double centerZ = centerPlayer.getLocation().getZ();

                double newX = centerX + radius * Math.cos(angle);
                double newZ = centerZ + radius * Math.sin(angle);

                Location newLocation = new Location(centerPlayer.getWorld(), newX, centerPlayer.getLocation().getY(), newZ);
                for (Player player : Jerk_Radius) {
                    player.teleport(newLocation);
                }

                if (angle >= 2 * Math.PI * 15) {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L); // Run the task every tick
    }

    private void placeWaterAroundPlayer(Location centerLocation, double radius, Material place_mat) {
        double centerX = centerLocation.getX();
        double centerZ = centerLocation.getZ();

        for (double x = centerX - radius; x <= centerX + radius; x++) {
            for (double z = centerZ - radius; z <= centerZ + radius; z++) {
                double distanceSquared = (x - centerX) * (x - centerX) + (z - centerZ) * (z - centerZ);

                if (distanceSquared <= radius * radius) {
                    Location waterLocation = new Location(centerLocation.getWorld(), x, centerLocation.getY(), z);
                    centerLocation.getWorld().getBlockAt(waterLocation).setType(place_mat);
                }
            }
        }
    }

    private Map<Player, List<Location>> bedrockLocations = new HashMap<>();
    private Map<Player, Boolean> suffocationStatus = new HashMap<>();
    private int duration = 160; // Duration in ticks

    public void suffocation(Player p) {
        double radius = 5.0;

        for (Entity entity : p.getWorld().getNearbyEntities(p.getLocation(), radius, radius, radius)) {
            if (entity instanceof Player) {
                Player nearbyPlayer = (Player) entity;

                if (nearbyPlayer != p) {
                    if (suffocationStatus.get(p) != null && suffocationStatus.get(p)) {
                        return;
                    }

                    if (suffocationStatus.get(p) == null || !suffocationStatus.get(p)) {
                        List<Location> playerBedrockLocations = new ArrayList<>();
                        for (int x = -1; x <= 1; x++) {
                            for (int y = -1; y <= 1; y++) {
                                for (int z = -1; z <= 1; z++) {
                                    Location bedrockLocation = new Location(nearbyPlayer.getWorld(),
                                            nearbyPlayer.getLocation().getX() + x,
                                            nearbyPlayer.getLocation().getY() + y,
                                            nearbyPlayer.getLocation().getZ() + z);
                                    Block block = bedrockLocation.getBlock();

                                    if (block.getType() == Material.AIR || block.getType() == Material.GRASS) {
                                        block.setType(Material.BEDROCK);
                                        playerBedrockLocations.add(bedrockLocation);
                                    }
                                }
                            }
                        }

                        bedrockLocations.put(p, playerBedrockLocations);
                        suffocationStatus.put(p, true);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (suffocationStatus.get(p)) {
                                    removeBedrockBlocks(p);

                                }
                                cancel();
                            }
                        }.runTaskTimer(plugin, duration, 0L);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                suffocationStatus.put(p, false);
                                cancel();
                            }
                        }.runTaskLater(plugin, 400L);
                    }
                }
            }
        }
    }

    private void removeBedrockBlocks(Player p) {
        List<Location> locations = bedrockLocations.get(p);
        if (locations != null) {
            for (Location loc : locations) {
                Block block = loc.getBlock();
                if (block.getType() == Material.BEDROCK || block.getType() == Material.GRASS) {
                    block.setType(Material.AIR);
                }
            }
            bedrockLocations.remove(p);
        }
    }

    private void updateBannedPlayersGUI(Player player) {
        int slots;
        if (playerSet.size() < 9){
            slots = 9;
        }else{
            slots = playerSet.size() * 2;
        }
        Inventory gui = Bukkit.createInventory(player, slots, "Banned Players");

        // Populate the GUI with the updated set of banned players
        for (Player bannedPlayer : playerSet) {
            ItemStack playerHead = createPlayerHead(bannedPlayer.getName());
            gui.addItem(playerHead);
        }

        // Open the updated GUI
        player.openInventory(gui);
    }

    private void BlackHole(Player p) {
        int radius = 10;
        //if (!Black_Hole.containsKey(p)) {
            if (!Black_Hole_Cooldown.get(p.getUniqueId())) {
                Black_Hole.put(p, p.getLocation());
                Location BlackHole_Loc = Black_Hole.get(p).add(0, 10, 0);
                Location playerLocaation = p.getLocation().add(0, 12, 0);
                World world = BlackHole_Loc.getWorld();

                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        // Check if the block is within the circle using the circle equation
                        if (x * x + z * z <= radius * radius) {
                            Location blockLocation = BlackHole_Loc.clone().add(x, 0, z);
                            world.getBlockAt(blockLocation).setType(Material.BLACK_CONCRETE);
                        }
                    }
                }
                double p_radius = 10.0;
                for (Entity entity : p.getWorld().getNearbyEntities(Black_Hole.get(p), p_radius, p_radius, p_radius)) {
                    if (entity instanceof Player) {
                        Player nearbyPlayer = (Player) entity;
                        if (nearbyPlayer != p) {
                            nearbyPlayer.teleport(playerLocaation);
                        }
                    }
                }
                new BukkitRunnable() {
                    int countdown = 15;

                    @Override
                    public void run() {
                        if (countdown == 0) {
                            for (int x = -radius; x <= radius; x++) {
                                for (int z = -radius; z <= radius; z++) {
                                    // Check if the block is within the circle using the circle equation
                                    if (x * x + z * z <= radius * radius) {
                                        Location blockLocation = BlackHole_Loc.clone().add(x, 0, z);
                                        world.getBlockAt(blockLocation).setType(Material.AIR);
                                    }
                                }
                            }
                            cancel();
                        } else {
                            countdown--;
                            double radius = 10.0;
                            for (Entity entity : p.getWorld().getNearbyEntities(BlackHole_Loc, radius, radius, radius)) {
                                if (entity instanceof Player) {
                                    Player nearbyPlayer = (Player) entity;
                                    if (nearbyPlayer != p) {
                                        nearbyPlayer.damage(7);
                                    }
                                }
                            }
                        }
                    }
                }.runTaskTimer(plugin, 0, 20);
                p.sendMessage(ChatColor.GREEN+"Black Hole Created!");
                Black_Hole_Cooldown.put(p.getUniqueId(),true);
            }
            Bukkit.getScheduler().runTaskLater(plugin, () -> Black_Hole_Cooldown.put(p.getUniqueId(), false), 60 * 20);
        //}
    }

    public void updateArmorEnchantments(Player p) {
        HashMap<UUID, String> trims = plugin.trims;

        ItemStack helmet = new ItemStack(p.getInventory().getHelmet().getType());
        ItemStack chestplate = new ItemStack(p.getInventory().getChestplate().getType());
        ItemStack leggings = new ItemStack(p.getInventory().getLeggings().getType());
        ItemStack boots = new ItemStack(p.getInventory().getBoots().getType());

        ArmorMeta helmet_meta = (ArmorMeta) helmet.getItemMeta();
        ArmorMeta chestplate_meta = (ArmorMeta) chestplate.getItemMeta();
        ArmorMeta leggings_meta = (ArmorMeta) leggings.getItemMeta();
        ArmorMeta boots_meta = (ArmorMeta) boots.getItemMeta();

        if (trims.get(p.getUniqueId()).equals("Wind Trim")) {
            ArmorTrim trim1 = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.VEX);
            helmet_meta.setTrim(trim1);
            chestplate_meta.setTrim(trim1);
            leggings_meta.setTrim(trim1);
            boots_meta.setTrim(trim1);
        } else if (trims.get(p.getUniqueId()).equals("Water Trim")) {
            ArmorTrim trim2 = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.EYE);
            helmet_meta.setTrim(trim2);
            chestplate_meta.setTrim(trim2);
            leggings_meta.setTrim(trim2);
            boots_meta.setTrim(trim2);
        } else if (trims.get(p.getUniqueId()).equals("Fire Trim")) {
            ArmorTrim trim3 = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.WILD);
            helmet_meta.setTrim(trim3);
            chestplate_meta.setTrim(trim3);
            leggings_meta.setTrim(trim3);
            boots_meta.setTrim(trim3);
        } else if (trims.get(p.getUniqueId()).equals("Time Trim")) {
            ArmorTrim trim4 = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.DUNE);
            helmet_meta.setTrim(trim4);
            chestplate_meta.setTrim(trim4);
            leggings_meta.setTrim(trim4);
            boots_meta.setTrim(trim4);
        } else if (trims.get(p.getUniqueId()).equals("Power Trim")) {
            ArmorTrim trim5 = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.RIB);
            helmet_meta.setTrim(trim5);
            chestplate_meta.setTrim(trim5);
            leggings_meta.setTrim(trim5);
            boots_meta.setTrim(trim5);
        }

        helmet.setItemMeta(helmet_meta);
        chestplate.setItemMeta(chestplate_meta);
        leggings.setItemMeta(leggings_meta);
        boots.setItemMeta(boots_meta);
        p.getInventory().setChestplate(chestplate);

        ItemStack newHelmet = helmet != null ? helmet.clone() : null;
        ItemStack newChestplate = chestplate != null ? chestplate.clone() : null;
        ItemStack newLeggings = leggings != null ? leggings.clone() : null;
        ItemStack newBoots = boots != null ? boots.clone() : null;

        if (helmet != null && !helmet.getEnchantments().isEmpty()) {
            newHelmet.addEnchantments(helmet.getEnchantments());
        }

        if (chestplate != null && !chestplate.getEnchantments().isEmpty()) {
            newChestplate.addEnchantments(chestplate.getEnchantments());
        }

        if (leggings != null && !leggings.getEnchantments().isEmpty()) {
            newLeggings.addEnchantments(leggings.getEnchantments());
        }

        if (boots != null && !boots.getEnchantments().isEmpty()) {
            newBoots.addEnchantments(boots.getEnchantments());
        }
        p.getInventory().setHelmet(newHelmet);
        p.getInventory().setChestplate(newChestplate);
        p.getInventory().setLeggings(newLeggings);
        p.getInventory().setBoots(newBoots);
    }
}
package anderman.nova.nova;

import anderman.nova.nova.Commands.Select;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class Nova extends JavaPlugin {

    public HashMap<UUID, Integer> levels = new HashMap<UUID, Integer>();
    public HashMap<UUID, String> trims = new HashMap<UUID, String>();
    public HashMap<UUID, String> active_abilities = new HashMap<UUID, String>();
    @Override
    public void onEnable() {
        new Listeners(this);
        new Select(this);
        addRecipes();
        System.out.println("Nova Plugin Has Been Started!");
    }

    @Override
    public void onDisable() {
        getServer().clearRecipes();
        System.out.println("Nova Plugin Has Been Stopped!");
    }

    private void addRecipes(){
        ItemStack itemPortato = new ItemStack(Material.ECHO_SHARD);
        ItemMeta itemMeta = itemPortato.getItemMeta();
        itemMeta.setDisplayName("Level Upgrader"); // Set custom name
        itemMeta.addEnchant(Enchantment.BINDING_CURSE,1,true);
        itemPortato.setItemMeta(itemMeta);
        ShapedRecipe recipe = new ShapedRecipe(itemPortato);
        recipe.shape("NDN", "DHD", "NDN");
        recipe.setIngredient('N', Material.NETHERITE_INGOT);
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('H', Material.HEART_OF_THE_SEA);
        getServer().addRecipe(recipe);

        ItemStack trimswapper = new ItemStack(Material.PURPLE_GLAZED_TERRACOTTA);
        ItemMeta trimswapper_meta = trimswapper.getItemMeta();
        trimswapper_meta.setDisplayName("Trim Swapper");
        trimswapper.setItemMeta(trimswapper_meta);
        ShapedRecipe trimswapper_recipie = new ShapedRecipe(trimswapper);
        trimswapper_recipie.shape("NAN", "HGH", "NAN");
        trimswapper_recipie.setIngredient('N', Material.NETHERITE_INGOT);
        trimswapper_recipie.setIngredient('A', Material.AMETHYST_SHARD);
        trimswapper_recipie.setIngredient('H', Material.NETHER_STAR);
        trimswapper_recipie.setIngredient('G', Material.GHAST_TEAR);
        getServer().addRecipe(trimswapper_recipie);

        ItemStack ReviveBeacon = new ItemStack(Material.BEACON);
        ItemMeta ReviveBeacon_meta = trimswapper.getItemMeta();
        ReviveBeacon_meta.setDisplayName("Revive Beacon");
        ReviveBeacon.setItemMeta(ReviveBeacon_meta);
        ShapedRecipe ReviveBeacon_recipie = new ShapedRecipe(ReviveBeacon);
        ReviveBeacon_recipie.shape("NRN", "RBR", "NRN");
        ReviveBeacon_recipie.setIngredient('N', Material.NETHER_STAR);
        ReviveBeacon_recipie.setIngredient('B', Material.BEACON);
        ReviveBeacon_recipie.setIngredient('R', Material.REDSTONE);
        getServer().addRecipe(ReviveBeacon_recipie);
    }
}

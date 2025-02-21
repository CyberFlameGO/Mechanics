package dev._2lstudios.mechanics.listeners;

import dev._2lstudios.mechanics.managers.BlockManager;
import dev._2lstudios.mechanics.managers.GameMechanicsManager;
import dev._2lstudios.mechanics.managers.MaterialManager;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class BlockBreakListener implements Listener {
  private final BlockManager blockManager;
  private final MaterialManager materialManager;

  public BlockBreakListener(GameMechanicsManager gameMechanicsManager) {
    this.blockManager = gameMechanicsManager.getBlockManager();
    this.materialManager = gameMechanicsManager.getMaterialManager();
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onBlockBreak(BlockBreakEvent event) {
    Player player = event.getPlayer();
    PlayerInventory playerInventory = player.getInventory();
    Block block = event.getBlock();
    Material material = block.getType();
    Material material1 = this.materialManager.getMaterial(material);
    String materialName = material.name();
    ItemStack heldItem = playerInventory.getItem(playerInventory.getHeldItemSlot());

    this.blockManager.addBreaker(block, player);

    if (material1 != null) {
      block.setType(material1);
    }

    int expToDrop = event.getExpToDrop();

    if ((materialName.endsWith("ORE") || materialName.endsWith("STONE")) && Math.random() <= 0.5D) {
      event.setExpToDrop(expToDrop + 1);
    }

    if (heldItem != null && expToDrop > 0) {
      Map<Enchantment, Integer> enchantments = heldItem.getEnchantments();

      if (enchantments.containsKey(Enchantment.LOOT_BONUS_BLOCKS))
        event.setExpToDrop(expToDrop * (((Integer) enchantments.get(Enchantment.LOOT_BONUS_BLOCKS)).intValue() + 1));
    }
  }
}

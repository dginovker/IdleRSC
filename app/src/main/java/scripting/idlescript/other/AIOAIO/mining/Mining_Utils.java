package scripting.idlescript.other.AIOAIO.mining;

import bot.Main;
import models.entities.ItemId;
import models.entities.SceneryId;
import scripting.idlescript.other.AIOAIO.AIOAIO;

public class Mining_Utils {
  public static final int[] picks = {
    ItemId.BRONZE_PICKAXE.getId(),
    ItemId.IRON_PICKAXE.getId(),
    ItemId.STEEL_PICKAXE.getId(),
    ItemId.MITHRIL_PICKAXE.getId(),
    ItemId.ADAMANTITE_PICKAXE.getId(),
    ItemId.RUNE_PICKAXE.getId()
  };

  public static int getBestPick() {
    Main.getController().log("Todo");
    return picks[0];
  }

  public static boolean hasPickInInventory() {
    // Check if the best pick we can use is in the inventory
    if (Main.getController().isItemInInventory(getBestPick())
        || Main.getController().isItemIdEquipped(getBestPick())) {
      return true;
    }
    return false;
  }

  public static boolean hasPickInBank() {
    if (!Main.getController().isInBank()) throw new IllegalStateException("Not in bank");
    // Check if the best pick we can use is in the bank
    if (Main.getController().isItemInBank(getBestPick())) {
      return true;
    }
    return false;
  }

  public static void withdrawPickaxeFromBank() {
    if (Main.getController().isItemInBank(getBestPick())) {
      Main.getController().withdrawItem(getBestPick());
      return;
    }
  }

  public static boolean isAxe(int itemId) {
    if (itemId == getBestPick()) {
      return true;
    }
    return false;
  }

  public static boolean meetsReqs() {
    switch (AIOAIO.state.currentTask.getName()) {
      case "normal":
        return true;
      case "oak":
        return Main.getController().getCurrentStat(Main.getController().getStatId("Woodcutting"))
            >= 15;
      case "willow":
        return Main.getController().getCurrentStat(Main.getController().getStatId("Woodcutting"))
            >= 30;
    }
    throw new IllegalStateException("Unknown tree type: " + AIOAIO.state.currentTask.getName());
  }

  public static int getRockId() {
    switch (AIOAIO.state.currentTask.getName()) {
      case "normal":
        return SceneryId.LEAFY_TREE.getId();
      case "oak":
        return SceneryId.TREE_OAK.getId();
      case "willow":
        return SceneryId.TREE_WILLOW.getId();
    }
    throw new IllegalStateException("Unknown tree type: " + AIOAIO.state.currentTask.getName());
  }
}

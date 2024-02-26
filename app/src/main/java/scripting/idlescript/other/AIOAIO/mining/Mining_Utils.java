package scripting.idlescript.other.AIOAIO.mining;

import bot.Main;
import models.entities.ItemId;
import models.entities.SceneryId;
import scripting.idlescript.other.AIOAIO.AIOAIO;

public class Mining_Utils {
  public static int getBestPick() {
    if (Main.getController().getCurrentStat(Main.getController().getStatId("Mining")) < 6) {
      return ItemId.IRON_PICKAXE.getId();
    } else if (Main.getController().getCurrentStat(Main.getController().getStatId("Mining")) < 21) {
      return ItemId.STEEL_PICKAXE.getId();
    } else if (Main.getController().getCurrentStat(Main.getController().getStatId("Mining")) < 31) {
      return ItemId.MITHRIL_PICKAXE.getId();
    } else if (Main.getController().getCurrentStat(Main.getController().getStatId("Mining")) < 41) {
      return ItemId.ADAMANTITE_PICKAXE.getId();
    }
    return ItemId.RUNE_PICKAXE.getId();
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

  public static int getPickCost() {
    if (getBestPick() == ItemId.IRON_PICKAXE.getId()) return 140;
    else if (getBestPick() == ItemId.STEEL_PICKAXE.getId()) return 500;
    else if (getBestPick() == ItemId.MITHRIL_PICKAXE.getId()) return 1300;
    else if (getBestPick() == ItemId.ADAMANTITE_PICKAXE.getId()) return 3200;
    return 32000;
  }

  public static boolean meetsReqs() {
    switch (AIOAIO.state.currentTask.getName()) {
      case "Clay":
      case "Copper ore":
      case "Tin ore":
        return true;
      case "Iron ore":
        return Main.getController().getCurrentStat(Main.getController().getStatId("Mining")) >= 15;
      case "Silver ore":
        return Main.getController().getCurrentStat(Main.getController().getStatId("Mining")) >= 20;
      case "Coal ore":
        return Main.getController().getCurrentStat(Main.getController().getStatId("Mining")) >= 30;
      case "Gold ore":
        Main.getController().log(AIOAIO.state.currentTask.getName() + " is not yet implemented!");
        return false;
        // return Main.getController().getCurrentStat(Main.getController().getStatId("Mining")) >=
        // 40;
      case "Gem":
        Main.getController()
            .log("TODO - Check if Shilo village is complete.. (steal the check from AIO Quester?)");
        return Main.getController().getCurrentStat(Main.getController().getStatId("Mining")) >= 40;
      case "Mithril ore":
        return Main.getController().getCurrentStat(Main.getController().getStatId("Mining")) >= 55;
      case "Adamantite ore":
        Main.getController().log(AIOAIO.state.currentTask.getName() + " is not yet implemented!");
        return false;
        // return Main.getController().getCurrentStat(Main.getController().getStatId("Mining")) >=
        // 70;
      case "Runite ore":
        Main.getController().log(AIOAIO.state.currentTask.getName() + " is not yet implemented!");
        return false;
        // return Main.getController().getCurrentStat(Main.getController().getStatId("Mining")) >=
        // 85;
    }
    throw new IllegalStateException("Unknown rock type: " + AIOAIO.state.currentTask.getName());
  }

  public static int getRockId() {
    switch (AIOAIO.state.currentTask.getName()) {
      case "Clay":
        return SceneryId.ROCK_CLAY.getId(); // TODO - VERIFY??
      case "Copper ore":
        return SceneryId.ROCK_COPPER.getId(); // TODDO - VERIFY??
      case "Tin ore":
        return SceneryId.ROCK_TIN.getId(); // TODO - VERIFY??
      case "Iron ore":
        return SceneryId.ROCK_IRON.getId(); // You get the idea
      case "Silver ore":
        return SceneryId.ROCK_SILVER.getId();
      case "Coal ore":
        return SceneryId.ROCK_COAL.getId();
      case "Gold ore":
        return SceneryId.ROCK_GOLD.getId();
      case "Gem":
        return SceneryId.GEM_ROCKS.getId();
      case "Mithril ore":
        return SceneryId.ROCK_MITHRIL.getId();
      case "Adamantite ore":
        return SceneryId.ROCK_ADAMITE.getId();
      case "Runite ore":
        return SceneryId.ROCK_RUNITE.getId();
    }
    throw new IllegalStateException("Unknown rock type: " + AIOAIO.state.currentTask.getName());
  }

  public static void findRocks() {
    // If lots of people use this script, add more locs and a Random
    switch (AIOAIO.state.currentTask.getName()) {
      case "Clay":
        Main.getController().walkTowards(160, 543); // Varrock Southwest Mine
      case "Copper ore":
        Main.getController().walkTowards(73, 546); // Varrock Southeast Mine
      case "Tin ore":
        Main.getController().walkTowards(160, 543); // Varrock Southwest Mine
      case "Iron ore":
        Main.getController().walkTowards(160, 543); // Varrock Southwest Mine
      case "Silver ore":
        Main.getController().walkTowards(160, 543); // Varrock Southwest Mine
      case "Coal ore":
        // if mining level then ...
        Main.getController().walkTowards(228, 515); // Barbarian Village
        // Gold is skipped, we want to do Tree Gnome Stronghold mine tho
      case "Gem":
        Main.getController().walkTowards(424, 825); // Shilo
      case "Mithril ore":
        Main.getController().walkTowards(116, 711); // Lumbridge Swamp Mine
        // Adamantite is skipped, we want to do Tree Gnome Stronghold mine tho
        // Runite is skipped, we wanna have logic for banking our stuff first
    }
    throw new IllegalStateException("Unknown rock location: " + AIOAIO.state.currentTask.getName());
  }
}

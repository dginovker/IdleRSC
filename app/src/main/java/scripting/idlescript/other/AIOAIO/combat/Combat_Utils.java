package scripting.idlescript.other.AIOAIO.combat;

import bot.Main;
import controller.Controller;
import models.entities.ItemId;
import scripting.idlescript.other.AIOAIO.AIOAIO;

public class Combat_Utils {
  // Melee weapons in Runescape Classic ordered from best -> worst
  static ItemId[] meleeWeapons = {
    ItemId.DRAGON_SWORD,
    ItemId.DRAGON_AXE,
    ItemId.RUNE_2_HANDED_SWORD,
    ItemId.RUNE_LONG_SWORD,
    ItemId.RUNE_SHORT_SWORD,
    ItemId.RUNE_BATTLE_AXE,
    ItemId.ADAMANTITE_2_HANDED_SWORD,
    ItemId.ADAMANTITE_LONG_SWORD,
    ItemId.ADAMANTITE_SHORT_SWORD,
    ItemId.ADAMANTITE_BATTLE_AXE,
    ItemId.MITHRIL_2_HANDED_SWORD,
    ItemId.MITHRIL_LONG_SWORD,
    ItemId.MITHRIL_SHORT_SWORD,
    ItemId.MITHRIL_BATTLE_AXE,
    ItemId.BLACK_2_HANDED_SWORD,
    ItemId.BLACK_LONG_SWORD,
    ItemId.BLACK_SHORT_SWORD,
    ItemId.BLACK_BATTLE_AXE,
    ItemId.STEEL_2_HANDED_SWORD,
    ItemId.STEEL_LONG_SWORD,
    ItemId.STEEL_SHORT_SWORD,
    ItemId.STEEL_BATTLE_AXE,
    ItemId.IRON_2_HANDED_SWORD,
    ItemId.IRON_LONG_SWORD,
    ItemId.IRON_SHORT_SWORD,
    ItemId.IRON_BATTLE_AXE,
    ItemId.BRONZE_2_HANDED_SWORD,
    ItemId.BRONZE_LONG_SWORD,
    ItemId.BRONZE_SHORT_SWORD,
    ItemId.BRONZE_BATTLE_AXE,
    ItemId.BRONZE_AXE
  };

  public static int getFightingGear() {
    Controller c = Main.getController();
    if (c.getNearestNpcById(95, false) == null) {
      c.setStatus("Walking to Bank");
      c.walkTowards(c.getNearestBank()[0], c.getNearestBank()[1]);
      return 100;
    }
    c.setStatus("Opening bank");
    c.openBank();
    c.setStatus("Depositing Everything");
    for (int itemId : c.getInventoryItemIds()) {
      Main.getController().depositItem(itemId, Main.getController().getInventoryItemCount(itemId));
      Main.getController().sleep(100);
    }
    c.setStatus("Withdrawing best melee weapon");
    for (ItemId weapon : meleeWeapons) {
      if (c.getBankItemCount(weapon.getId()) > 0) {
        System.out.println("Found " + weapon.name() + " as my best weapon Uwu");
        c.withdrawItem(weapon.getId());
        c.closeBank();
        c.sleep(750);
        c.equipItemById(weapon.getId());
        break;
      }
    }
    c.closeBank();
    AIOAIO.state.methodStartup = false;
    System.out.println("Combat Setup complete");
    return 680;
  }
}

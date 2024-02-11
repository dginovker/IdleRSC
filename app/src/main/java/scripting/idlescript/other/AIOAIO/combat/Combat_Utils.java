package scripting.idlescript.other.AIOAIO.combat;

import bot.Main;
import controller.Controller;
import java.util.LinkedHashMap;
import java.util.Map;
import models.entities.ItemId;
import models.entities.NpcId;
import orsc.ORSCharacter;
import scripting.idlescript.other.AIOAIO.AIOAIO;

public class Combat_Utils {
  // Melee weapons in Runescape Classic ordered from best -> worst with their
  // required attack levels
  static final Map<ItemId, Integer> meleeWeaponsWithLevels = new LinkedHashMap<>();

  static {
    meleeWeaponsWithLevels.put(ItemId.DRAGON_SWORD, 60);
    meleeWeaponsWithLevels.put(ItemId.DRAGON_AXE, 60);
    meleeWeaponsWithLevels.put(ItemId.RUNE_2_HANDED_SWORD, 40);
    meleeWeaponsWithLevels.put(ItemId.RUNE_LONG_SWORD, 40);
    meleeWeaponsWithLevels.put(ItemId.RUNE_SHORT_SWORD, 40);
    meleeWeaponsWithLevels.put(ItemId.RUNE_BATTLE_AXE, 40);
    meleeWeaponsWithLevels.put(ItemId.ADAMANTITE_2_HANDED_SWORD, 30);
    meleeWeaponsWithLevels.put(ItemId.ADAMANTITE_LONG_SWORD, 30);
    meleeWeaponsWithLevels.put(ItemId.ADAMANTITE_SHORT_SWORD, 30);
    meleeWeaponsWithLevels.put(ItemId.ADAMANTITE_BATTLE_AXE, 30);
    meleeWeaponsWithLevels.put(ItemId.MITHRIL_2_HANDED_SWORD, 20);
    meleeWeaponsWithLevels.put(ItemId.MITHRIL_LONG_SWORD, 20);
    meleeWeaponsWithLevels.put(ItemId.MITHRIL_SHORT_SWORD, 20);
    meleeWeaponsWithLevels.put(ItemId.MITHRIL_BATTLE_AXE, 20);
    meleeWeaponsWithLevels.put(ItemId.BLACK_2_HANDED_SWORD, 10);
    meleeWeaponsWithLevels.put(ItemId.BLACK_LONG_SWORD, 10);
    meleeWeaponsWithLevels.put(ItemId.BLACK_SHORT_SWORD, 10);
    meleeWeaponsWithLevels.put(ItemId.BLACK_BATTLE_AXE, 10);
    meleeWeaponsWithLevels.put(ItemId.STEEL_2_HANDED_SWORD, 5);
    meleeWeaponsWithLevels.put(ItemId.STEEL_LONG_SWORD, 5);
    meleeWeaponsWithLevels.put(ItemId.STEEL_SHORT_SWORD, 5);
    meleeWeaponsWithLevels.put(ItemId.STEEL_BATTLE_AXE, 5);
    meleeWeaponsWithLevels.put(ItemId.IRON_2_HANDED_SWORD, 1);
    meleeWeaponsWithLevels.put(ItemId.IRON_LONG_SWORD, 1);
    meleeWeaponsWithLevels.put(ItemId.IRON_SHORT_SWORD, 1);
    meleeWeaponsWithLevels.put(ItemId.IRON_BATTLE_AXE, 1);
    meleeWeaponsWithLevels.put(ItemId.BRONZE_2_HANDED_SWORD, 1);
    meleeWeaponsWithLevels.put(ItemId.BRONZE_LONG_SWORD, 1);
    meleeWeaponsWithLevels.put(ItemId.BRONZE_SHORT_SWORD, 1);
    meleeWeaponsWithLevels.put(ItemId.BRONZE_BATTLE_AXE, 1);
    meleeWeaponsWithLevels.put(ItemId.BRONZE_AXE, 1);
  }

  public static int getFightingGear() {
    Controller c = Main.getController();
    if (c.getNearestNpcById(NpcId.BANKER.getId(), false) == null) {
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
    int playerAttackLevel = c.getCurrentStat(c.getStatId("Attack"));
    for (Map.Entry<ItemId, Integer> entry : meleeWeaponsWithLevels.entrySet()) {
      if (playerAttackLevel >= entry.getValue() && c.getBankItemCount(entry.getKey().getId()) > 0) {
        c.log("Found " + entry.getKey().name() + " as my best weapon Uwu");
        c.withdrawItem(entry.getKey().getId());
        c.closeBank();
        c.sleep(750);
        c.equipItemById(entry.getKey().getId());
        break;
      }
    }
    c.closeBank();
    AIOAIO.state.methodStartup = false;
    System.out.println("Combat Setup complete");
    return 680;
  }

  /**
   * Attacks an NPC. Sleeps until the NPC is in combat
   *
   * @param npcId
   */
  public static void attackNpc(NpcId npcId) {
    Main.getController().setStatus("@yel@Attacking " + npcId.name());
    ORSCharacter npc = Main.getController().getNearestNpcById(npcId.getId(), false);
    if (npc == null) return;
    Main.getController().attackNpc(npc.serverIndex);
    Main.getController().sleepUntil(() -> Main.getController().isNpcInCombat(npc.serverIndex));
  }

  public static void buryBones() {
    Main.getController().itemCommand(ItemId.BONES.getId());
  }

  public static void lootBones() {
    int[] lootCoord = Main.getController().getNearestItemById(ItemId.BONES.getId());
    Main.getController().setStatus("@red@Picking bones");
    Main.getController().pickupItem(lootCoord[0], lootCoord[1], ItemId.BONES.getId(), true, false);
    Main.getController()
        .sleepUntil(() -> Main.getController().getInventoryItemCount(ItemId.BONES.getId()) > 0);
  }
}

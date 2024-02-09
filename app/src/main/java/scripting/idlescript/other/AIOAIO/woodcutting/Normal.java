package scripting.idlescript.other.AIOAIO.woodcutting;

import bot.Main;
import controller.Controller;
import models.entities.SceneryId;
import scripting.idlescript.other.AIOAIO.AIOAIO;
import scripting.idlescript.other.AIOAIO.TriState;

public class Normal {
  private static Controller c;
  public static int run() {
    c = Main.getController();
    c.setBatchBarsOn();

    if (!Woodcutting_Utils.hasAxeInInventory())
      return getAxe();
    if (c.getInventoryItemCount() >= 30)
      return bankLogs();
    if (c.isBatching())
      return 250; // Wait to finish chopping
    if (c.getNearestObjectById(SceneryId.LEAFY_TREE.getId()) != null)
      return chopTree();
    else
      return findTrees();
  }

  private static int findTrees() {
    c.walkTo(500, 453); // Go to Seers :)
    return 50;
  }

  private static int chopTree() {
    int[] treeCoords = c.getNearestObjectById(SceneryId.LEAFY_TREE.getId());
    c.atObject(treeCoords[0], treeCoords[1]);
    return 1200;
  }

  private static int bankLogs() {
    if (c.getNearestNpcById(95, false) == null) {
      c.setStatus("Walking to Bank");
      c.walkTo(c.getNearestBank()[0], c.getNearestBank()[1]);
    }
    c.setStatus("Opening bank");
    c.openBank();
    Woodcutting_Utils.depositAllExceptAxe();
    c.closeBank();
    return 680;
  }

  private static int getAxe() {
    if (AIOAIO.state.hasAxeInBank == TriState.FALSE)
      return getAxeFromFaladorSpawn();
    return getAxeFromBank();
  }

  private static int getAxeFromBank() {
    if (c.getNearestNpcById(95, false) == null) {
      c.setStatus("Walking to Bank");
      c.walkTo(c.getNearestBank()[0], c.getNearestBank()[1]);
    }
    c.setStatus("Opening bank");
    c.openBank();
    if (!Woodcutting_Utils.hasAxeInBank()) {
      // Next loop will grab it from Falador Spawn
      AIOAIO.state.hasAxeInBank = TriState.FALSE;
      return 100;
    }
    c.setStatus("Withdrawing axe");
    Woodcutting_Utils.withdrawAxeFromBank();
    c.closeBank();
    return 680;
  }

  private static int getAxeFromFaladorSpawn() {
    c.walkTo(330, 555);
    System.out.println(
        "TODO - Climb the ladder, wait for Bronze axe spawn, pick it up, go back down the ladder, then update state to say we have an axe");
    return 50;
  }
}
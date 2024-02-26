package scripting.idlescript.other.AIOAIO.mining;

import bot.Main;
import controller.Controller;
import models.entities.ItemId;
import models.entities.NpcId;
import scripting.idlescript.other.AIOAIO.AIOAIO;
import scripting.idlescript.other.AIOAIO.AIOAIO_Script_Utils;

public class Mine {
  private static Controller c;

  public static int run() {
    c = Main.getController();
    c.setBatchBarsOn();

    if (!Mining_Utils.meetsReqs()) {
      Main.getController()
          .log(
              "Aborted " + AIOAIO.state.currentTask.getName() + " task because we don't meet reqs");
      AIOAIO.state.endTime = System.currentTimeMillis();
      return 0;
    }
    if (!Mining_Utils.hasPickInInventory()) return getPick();
    else if (c.getInventoryItemCount() >= 30)
      AIOAIO_Script_Utils.towardsDepositAll(Mining_Utils.getBestPick());
    else if (c.isBatching()) return 250; // Wait to finish mining
    else if (c.getNearestReachableObjectById(Mining_Utils.getRockId(), true) != null)
      return mineRock();
    else return findRocks();
    return 250;
  }

  private static int findRocks() {
    switch (AIOAIO.state.currentTask.getName()) {
      case "normal":
      case "oak":
        c.setStatus("Walking to Seers");
        c.walkTowards(500, 453);
        return 50;
      case "willow":
        c.setStatus("Walking to Seers");
        c.walkTowards(509, 442);
        return 50;
    }
    throw new IllegalStateException("Unknown tree type: " + AIOAIO.state.currentTask.getName());
  }

  private static int mineRock() {
    c.setStatus("Mining rock tree");
    int[] rockCoords = c.getNearestReachableObjectById(Mining_Utils.getRockId(), true);
    c.atObject(rockCoords[0], rockCoords[1]);
    return 1200;
  }

  private static int getPick() {
    if (!AIOAIO.state.hasPickInBank) return buyPickFromDwarvenMines();
    return getPickFromBank();
  }

  private static int getPickFromBank() {
    if (c.getNearestNpcById(95, false) == null) {
      c.walkTowardsBank();
      return 100;
    }
    c.setStatus("Opening bank");
    c.openBank();
    if (!Mining_Utils.hasPickInBank()) {
      System.out.println("No pick in bank, gotta get one..");
      // Next loop will grab it from Dwarven Mines
      AIOAIO.state.hasPickInBank = false;
      c.closeBank();
      return 100;
    }
    c.setStatus("Withdrawing axe");
    Mining_Utils.withdrawPickaxeFromBank();
    c.closeBank();
    return 680;
  }

  private static int buyPickFromDwarvenMines() {
    if (c.isInBank()) c.closeBank();
    if (c.isInShop()) {
      c.log("Buying " + ItemId.getById(Mining_Utils.getBestPick()).name());
      c.shopBuy(Mining_Utils.getBestPick());
      c.closeShop();
      c.sleep(1200);
      if (c.getInventoryItemCount(Mining_Utils.getBestPick()) >= 1) {
        c.log("Got " + ItemId.getById(Mining_Utils.getBestPick()).name());
      } else {
        c.log("Failed to get " + ItemId.getById(Mining_Utils.getBestPick()).name());
      }
      return 50;
    }
    if (c.distanceTo(0, 0) < 5) {
      c.openShop(new int[] {NpcId.NURMOF.getId()});
    }
    c.walkTowards(0, 0);
    return 0;
  }
}

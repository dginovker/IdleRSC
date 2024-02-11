package scripting.idlescript.other.AIOAIO.combat;

import bot.Main;
import controller.Controller;
import models.entities.ItemId;
import models.entities.NpcId;
import scripting.idlescript.other.AIOAIO.AIOAIO;

public class Cow {
  private static Controller c;

  public static int strength() {
    Main.getController().setFightMode(1);
    return run();
  }

  public static int attack() {
    Main.getController().setFightMode(2);
    return run();
  }

  public static int defense() {
    Main.getController().setFightMode(3);
    return run();
  }

  public static int run() {
    c = Main.getController();
    c.setBatchBarsOn();

    if (AIOAIO.state.methodStartup) return Combat_Utils.getFightingGear();
    else if (c.getInventoryItemCount(ItemId.BONES.getId()) > 0) Combat_Utils.buryBones();
    else if (c.isInCombat()) c.sleepUntilGainedXp();
    else if (c.getNearestNpcById(NpcId.COW_ATTACKABLE.getId(), false) == null) findCows();
    // The NPC we want to bop is found at this point
    else if (c.getNearestItemById(ItemId.BONES.getId()) != null) Combat_Utils.lootBones();
    else Combat_Utils.attackNpc(NpcId.COW_ATTACKABLE);
    return 50;
  }

  private static void findCows() {
    c.walkTowards(99, 617);
  }
}

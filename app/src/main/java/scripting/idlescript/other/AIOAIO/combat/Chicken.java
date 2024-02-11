package scripting.idlescript.other.AIOAIO.combat;

import bot.Main;
import controller.Controller;
import models.entities.NpcId;
import orsc.ORSCharacter;
import scripting.idlescript.other.AIOAIO.AIOAIO;

public class Chicken {
  private static Controller c;

  public static int attack() {
    Main.getController().setFightMode(1);
    return run();
  }

  public static int run() {
    c = Main.getController();
    c.setBatchBarsOn();

    if (AIOAIO.state.methodStartup) return Combat_Utils.getFightingGear();
    else if (c.isInCombat()) c.sleepUntilGainedXp();
    else if (c.getNearestNpcById(NpcId.CHICKEN.getId(), false) != null) attackChicken();
    else findChickens();
    return 50;
  }

  private static void attackChicken() {
    c.setStatus("@yel@Attacking Bwuk");
    ORSCharacter npc = c.getNearestNpcById(NpcId.CHICKEN.getId(), false);
    if (npc == null) return;
    c.attackNpc(npc.serverIndex);
  }

  private static void findChickens() {
    c.walkTowards(119, 604);
  }
}

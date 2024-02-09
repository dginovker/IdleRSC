package scripting.idlescript.other.AIOAIO;

import bot.Main;
import scripting.idlescript.IdleScript;

public class AIOAIO extends IdleScript {
  static AIOAIOConfig botConfig = new AIOAIOConfig();
  static boolean guiSetup = false;
  static boolean guiDone = false;
  private AIOAIOSkill currentSkill;
  private AIOAIO_Method currentMethod;
  private long endTime = System.currentTimeMillis();

  public int start(String[] parameters) {
    if (!guiSetup) {
      guiSetup = true;
      AIOAIO_GUI.setupGUI();
    }
    if (guiDone) {
      return loop();
    }
    return 50;
  }

  private int loop() {
    if (System.currentTimeMillis() > endTime) {
      currentSkill = botConfig.getRandomEnabledSkill();
      currentMethod = currentSkill.getRandomEnabledMethod();
      endTime = System.currentTimeMillis() + 6_000;
    }
    System.out.println("Looping!");
    return 100;
  }

  @Override
  public void paintInterrupt() {
    Main.getController().drawString("@red@AIOAIO", 6, 21, 0xFFFFFF, 1);
    String currentSkillText =
        "Current Skill: " + (currentSkill != null ? currentSkill.getName() : "None");
    String currentMethodText =
        "Current Method: " + (currentMethod != null ? currentMethod.getName() : "None");
    Main.getController().drawString("@red@" + currentSkillText, 6, 35, 0xFFFFFF, 1);
    Main.getController().drawString("@red@" + currentMethodText, 6, 49, 0xFFFFFF, 1);
    long timeRemaining = endTime - System.currentTimeMillis();
    String timeRemainingText =
        "Time remaining: " + (timeRemaining > 0 ? timeRemaining / 1000 + " seconds" : "None");
    Main.getController().drawString("@red@" + timeRemainingText, 6, 63, 0xFFFFFF, 1);
  }
}

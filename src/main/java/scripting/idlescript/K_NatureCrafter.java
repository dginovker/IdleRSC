package scripting.idlescript;

import bot.Main;
import controller.Controller;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import orsc.ORSCharacter;

/**
 * Crafts Nature runes on Karamja.
 *
 * <p>Start in Karamja Shop or Inside/Outside Nature Alter. Start with Coins, Noted Ess, and Nat
 * Talisman. Need 79+ combat so tribesmen don't poison you. Unnotes ess in jungle shop, walks to
 * alter to craft, repeat.
 *
 * <p>Does NOT work for offical coleslaw irons (cannot purchase from overstock shops)
 *
 * <p>This bot supports the "autostart" parameter/CLI to automatiically start the bot without gui -
 * Will automatically start without GUI.
 *
 * <p>@Author - Kaila
 */
public class K_NatureCrafter extends IdleScript {
  private static final Controller c = Main.getController();
  private static JFrame scriptFrame = null;
  private static boolean guiSetup = false;
  private static boolean scriptStarted = false;
  private static boolean lowLevel = false;
  private static int totalNatz = 0;
  private static int totalTrips = 0;
  private static long startTime;
  private static final long startTimestamp = System.currentTimeMillis() / 1000L;

  private void startSequence() {
    c.displayMessage("@red@Nature Rune Crafter - By Kaila");
    c.displayMessage("@red@Start in Tai Bwo Wannai General Store or Inside/Outside Nature Alter");
    if (c.isInBank()) {
      c.closeBank();
    }
    if (c.isInShop()) {
      c.closeShop();
    }
    if (c.currentY() < 770 && c.currentY() > 500) {
      bank();
      BankToNat();
      c.sleep(100);
    }
    if (c.currentY() > 790) {
      c.walkTo(392, 803);
      if (c.currentX() == 392 && c.currentY() == 803) {
        c.atObject(392, 804);
        c.sleep(340);
      }
      c.walkTo(787, 23);
    }
  }

  public int start(String[] parameters) {
    if (parameters.length > 0 && !parameters[0].equals("")) {
      if (parameters[0].toLowerCase().startsWith("auto")) {
        c.displayMessage("Auto-starting, Crafting Nature Runes", 0);
        System.out.println("Auto-starting, Crafting Nature Runes");
        lowLevel = false;
        parseVariables();
        startSequence();
        scriptStart();
      }
    }
    if (!guiSetup) {
      setupGUI();
      guiSetup = true;
    }
    if (scriptStarted) {
      startSequence();
      scriptStart();
    }
    return 1000; // start() must return an int value now.
  }

  // if low hp log out for nature rc

  private void scriptStart() {
    while (c.isRunning()) {

      if (c.currentY() < 50) {
        c.setStatus("@red@Crafting..");
        c.atObject(787, 21);
        if (c.getBaseStat(18) < 91) {
          totalNatz = totalNatz + 26;
        }
        if (c.getBaseStat(18) > 90) {
          totalNatz = totalNatz + 52;
        }
        totalTrips = totalTrips + 1;
        c.sleep(618);
        c.setStatus("@red@Getting more Ess..");
        NatToBank();
        bank();
        BankToNat();
        c.sleep(618);
      }

      int eatLvl = c.getBaseStat(c.getStatId("Hits")) - 20;

      if (c.getCurrentStat(c.getStatId("Hits")) < eatLvl) {

        leaveCombat();
        c.setStatus("@red@We've ran out of Food! Running Away/Logging Out.");
        c.sleep(308);
        c.setAutoLogin(false);
        c.logout();
        BankToNat();
        c.setAutoLogin(false);
        c.logout();
        c.stop();
        c.logout();
      }
      leaveCombat();
    }
  }

  private void bank() {

    c.setStatus("@yel@Buying Runes..");
    if (c.getInventoryItemCount(10) == 0 || c.getInventoryItemCount(1299) == 0) {
      c.setStatus("@red@NO Coins or Ess in Inventory, Logging Out!.");
      c.setAutoLogin(false);
      c.logout();
      if (!c.isLoggedIn()) {
        c.stop();
      }
    }
    if (!c.isInShop() && c.getInventoryItemCount() != 30) {
      ORSCharacter npc = c.getNearestNpcById(522, false);
      if (npc != null && c.getInventoryItemCount() != 30 && c.currentY() < 760) {
        c.walktoNPC(
            npc.serverIndex,
            0); // added, bot doesn't always get runes if npc moves >2 or 3 tiles away
        c.npcCommand1(npc.serverIndex);
        c.sleep(4000); // need LONG sleep or it breaks npccommand1
      } else {
        c.sleep(1000);
      }
    }
    if (c.isInShop() && c.getInventoryItemCount() != 30) {
      c.shopSell(1299, 27);
      c.sleep(800);
      c.shopBuy(1299, 27);
      c.sleep(340);
      c.closeShop();
      c.sleep(340);
    }
  }

  private void NatToBank() { // replace
    c.setStatus("@gre@Walking to Shop..");
    if (!lowLevel) {
      c.walkTo(787, 25);
      c.walkTo(785, 26);
      if (c.currentX() == 785 && c.currentY() == 26) {
        c.atObject(783, 26);
        c.sleep(340);
      }
      c.walkTo(392, 803);
      c.walkTo(398, 791);
      // c.walkTo(555,555);
      c.walkTo(397, 782); // fix pathing ERROR here
      c.walkTo(402, 779);
      c.walkTo(409, 779);
      c.walkTo(414, 779);
      c.walkTo(422, 782);
      c.walkTo(436, 773);
      c.walkTo(456, 773);
      c.walkTo(457, 772);
      c.walkTo(457, 767);
      c.walkTo(459, 765);
      c.walkTo(458, 757);
      // in jungle shop
    }
    if (lowLevel) {
      c.walkTo(787, 25);
      c.walkTo(785, 26);
      if (c.currentX() == 785 && c.currentY() == 26) {
        c.atObject(783, 26);
        c.sleep(340);
      }
      c.walkTo(401, 797);
      c.walkTo(401, 793);
      c.walkTo(407, 787);
      c.walkTo(417, 786);
      c.walkTo(428, 786);
      c.walkTo(433, 789);
      c.walkTo(440, 793);
      c.walkTo(448, 793);
      c.walkTo(463, 793);
      c.walkTo(469, 789);
      c.walkTo(470, 782);
      c.walkTo(473, 776);
      c.walkTo(473, 769);
      c.walkTo(473, 756);
      c.walkTo(460, 756);
      // in jungle shop
    }
    c.setStatus("@gre@Done Walking..");
  }

  private void BankToNat() {
    if (!lowLevel) {
      c.setStatus("@gre@Walking to Nature Alter..");
      c.walkTo(459, 757);
      c.walkTo(459, 765);
      c.walkTo(457, 767);
      c.walkTo(457, 772);
      c.walkTo(456, 773);
      c.walkTo(436, 773);
      c.walkTo(424, 783);
      c.walkTo(422, 782);
      c.walkTo(414, 779); // pathing brokme and landed here
      c.walkTo(408, 779); // added
      c.walkTo(403, 779);
      c.walkTo(399, 780);
      c.walkTo(397, 783);
      c.walkTo(396, 786);
      c.walkTo(396, 795);
      c.walkTo(393, 800);
      c.walkTo(392, 803);
      if (c.currentX() < 400 && c.currentX() > 385 && c.currentY() > 800 && c.currentY() < 810) {
        c.atObject(392, 804);
        c.sleep(2000); // was 3k
      }
      if (c.currentY() < 50) {
        c.walkTo(787, 26);
        c.walkTo(787, 23);
        // next to alter now
      }
      c.setStatus("@gre@Done Walking..");
    }
    if (lowLevel) {
      c.setStatus("@gre@Walking to Nature Alter..");
      c.walkTo(460, 756);
      c.walkTo(473, 756);
      c.walkTo(473, 769);
      c.walkTo(473, 776);
      c.walkTo(470, 782);
      c.walkTo(469, 789);
      c.walkTo(463, 793);
      c.walkTo(448, 793);
      c.walkTo(440, 793);
      c.walkTo(433, 789);
      c.walkTo(428, 786);
      c.walkTo(417, 786);
      c.walkTo(407, 787);
      c.walkTo(401, 793);
      c.walkTo(401, 797);
      c.walkTo(394, 803);
      if (c.currentX() == 394 && c.currentY() == 803) {
        c.atObject(392, 804);
        c.sleep(340);
      }
      c.walkTo(787, 23);
    }
  }

  private void leaveCombat() {
    for (int i = 1; i <= 15; i++) {
      if (c.isInCombat()) {
        c.setStatus("@red@Leaving combat..");
        c.walkTo(c.currentX(), c.currentY(), 0, true);
        c.sleep(600);
      } else {
        c.setStatus("@red@Done Leaving combat..");
        break;
      }
      c.sleep(10);
    }
  }

  // GUI stuff below (icky)
  private void setValuesFromGUI(JCheckBox lowLevelCheckbox) {
    if (lowLevelCheckbox.isSelected()) {
      lowLevel = true;
    }
  }

  private void parseVariables() {
    startTime = System.currentTimeMillis();
  }

  private void setupGUI() {
    JLabel header = new JLabel("Nature Rune Crafter - By Kaila");
    JLabel label1 = new JLabel("Start in Tai Bwo Wannai General Store");
    JLabel label2 = new JLabel("or start Inside/Outside Nature Alter");
    JLabel label3 = new JLabel("Start with Coins, Noted Ess, and Nat Talisman");
    JLabel label4 = new JLabel("Need 79+ combat so tribesmen don't poison you");
    JLabel label5 = new JLabel("This bot supports the \"autostart\" parameter");
    JLabel label6 = new JLabel("Will automatically start without GUI");
    JCheckBox lowLevelCheckbox = new JCheckBox("Check This If Below 79 Combat");
    JButton startScriptButton = new JButton("Start");

    startScriptButton.addActionListener(
        e -> {
          setValuesFromGUI(lowLevelCheckbox);
          scriptFrame.setVisible(false);
          scriptFrame.dispose();
          parseVariables();
          scriptStarted = true;
        });

    scriptFrame = new JFrame(c.getPlayerName() + " - options");

    scriptFrame.setLayout(new GridLayout(0, 1));
    scriptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    scriptFrame.add(header);
    scriptFrame.add(label1);
    scriptFrame.add(label2);
    scriptFrame.add(label3);
    scriptFrame.add(label4);
    scriptFrame.add(label5);
    scriptFrame.add(label6);
    scriptFrame.add(lowLevelCheckbox);
    scriptFrame.add(startScriptButton);
    scriptFrame.pack();
    scriptFrame.setLocationRelativeTo(null);
    scriptFrame.setVisible(true);
    scriptFrame.requestFocusInWindow();
  }

  @Override
  public void paintInterrupt() {
    if (c != null) {

      String runTime = c.msToString(System.currentTimeMillis() - startTime);
      int NatzSuccessPerHr = 0;
      int TripSuccessPerHr = 0;
      long currentTimeInSeconds = System.currentTimeMillis() / 1000L;

      try {
        float timeRan = currentTimeInSeconds - startTimestamp;
        float scale = (60 * 60) / timeRan;
        NatzSuccessPerHr = (int) (totalNatz * scale);
        TripSuccessPerHr = (int) (totalTrips * scale);

      } catch (Exception e) {
        // divide by zero
      }
      int x = 6;
      int y = 21;
      c.drawString("@red@Nature Rune Crafter@gre@by Kaila", x, y - 3, 0xFFFFFF, 1);
      c.drawString("@whi@____________________", x, y, 0xFFFFFF, 1);
      c.drawString(
          "@whi@Natures Crafted: @gre@"
              + totalNatz
              + "@yel@ (@whi@"
              + String.format("%,d", NatzSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          x,
          y + 14,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Total Trips: @gre@"
              + totalTrips
              + "@yel@ (@whi@"
              + String.format("%,d", TripSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          x,
          y + (14 * 2),
          0xFFFFFF,
          1);
      c.drawString("@whi@Runtime: " + runTime, x, y + (14 * 3), 0xFFFFFF, 1);
      c.drawString("@whi@____________________", x, y + 3 + (14 * 3), 0xFFFFFF, 1);
    }
  }
}

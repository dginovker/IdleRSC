package scripting.idlescript.other;

import bot.Main;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import scripting.idlescript.IdleScript;

public class AIOAIO extends IdleScript {
  private static JFrame scriptFrame = null;
  private static JList<Skill> skillList;
  private static DefaultListModel<Skill> skillListModel;
  private static JPanel methodsPanel;
  private static JButton startButton, enableDisableSkillButton;
  private Skill currentSkill;
  private Method currentMethod;
  private long endTime = System.currentTimeMillis();
  private AIOAIOConfig botConfig = new AIOAIOConfig();
  private boolean guiSetup = false;
  private boolean guiDone = false;

  public int start(String[] parameters) {
    if (!guiSetup) {
      guiSetup = true;
      setupGUI();
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

  private void setupGUI() {
    scriptFrame = new JFrame("Runescape Classic AIO Bot");
    scriptFrame.setLayout(new BorderLayout());
    skillListModel = new DefaultListModel<>();
    botConfig.skills.forEach(skillListModel::addElement);
    skillList = new JList<>(skillListModel);
    skillList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    skillList.addListSelectionListener(this::skillSelected);
    methodsPanel = new JPanel();
    methodsPanel.setLayout(new BoxLayout(methodsPanel, BoxLayout.Y_AXIS));
    enableDisableSkillButton = new JButton("Enable/Disable Skill");
    enableDisableSkillButton.addActionListener(this::toggleSkillEnabled);
    startButton = new JButton("Start");
    startButton.addActionListener(this::startButtonClicked);
    JPanel skillsPanel = createSectionWithTitle("Skills", new JScrollPane(skillList));
    Dimension preferredSize = new Dimension(200, 100);
    methodsPanel.setPreferredSize(preferredSize);
    scriptFrame.add(skillsPanel, BorderLayout.WEST);
    scriptFrame.add(createSectionWithTitle("Methods", methodsPanel), BorderLayout.CENTER);
    scriptFrame.add(startButton, BorderLayout.SOUTH);
    scriptFrame.pack();
    scriptFrame.setLocationRelativeTo(null);
    scriptFrame.setVisible(true);
    scriptFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    updateMethodsPanel();
  }

  private JPanel createSectionWithTitle(String title, Component component) {
    JPanel sectionPanel = new JPanel(new BorderLayout());
    JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
    sectionPanel.add(titleLabel, BorderLayout.NORTH);
    sectionPanel.add(component, BorderLayout.CENTER);
    return sectionPanel;
  }

  private void skillSelected(ListSelectionEvent e) {
    if (!e.getValueIsAdjusting()) {
      updateMethodsPanel();
    }
  }

  private void toggleSkillEnabled(ActionEvent e) {
    Skill selectedSkill = skillList.getSelectedValue();
    if (selectedSkill != null) {
      selectedSkill.setEnabled(!selectedSkill.isEnabled());
      skillListModel.setElementAt(selectedSkill, skillList.getSelectedIndex());
      updateMethodsPanel();
    }
  }

  private void updateMethodsPanel() {
    methodsPanel.removeAll();
    Skill selectedSkill = skillList.getSelectedValue();
    if (selectedSkill != null) {
      for (Method method : selectedSkill.getMethods()) {
        JCheckBox checkBox = new JCheckBox(method.getName(), method.isEnabled());
        checkBox.addActionListener(e -> method.setEnabled(checkBox.isSelected()));
        methodsPanel.add(checkBox);
      }
    }
    methodsPanel.revalidate();
    methodsPanel.repaint();
  }

  private void startButtonClicked(ActionEvent e) {
    // Example of finalizing botConfig based on GUI selections. This step might
    // involve:
    // 1. Updating botConfig with the selected skills and methods.
    // 2. Any other pre-start configurations.

    // Since your structure doesn't directly link skills in botConfig with GUI, you
    // might need to:
    // - Iterate over skills and methods in GUI.
    // - Update the corresponding objects in botConfig accordingly.

    // This is a placeholder for how you might begin such a process. You'll need to
    // tailor it to your actual data structures.
    for (int i = 0; i < skillListModel.size(); i++) {
      Skill skill = skillListModel.getElementAt(i);
      // Assuming botConfig.skillMap should be populated or updated based on GUI.
      botConfig.skillMap.put(
          skill.getName(), skill); // Ensure skillMap is being used correctly in your logic.
    }

    // Assuming there's logic here to ensure botConfig accurately reflects the state
    // of the GUI selections.

    scriptFrame.dispose(); // Close the GUI window.
    guiDone = true; // Mark the GUI setup as complete to allow the bot loop to start.

    // Optionally, log or print a message indicating the bot is starting.
    System.out.println("Bot starting with the current configuration...");
  }

  class Skill {
    private String name;
    private boolean enabled;
    private Method[] methods;

    public Skill(String name, boolean enabled, Method[] methods) {
      this.name = name;
      this.enabled = enabled;
      this.methods = methods;
    }

    public String getName() {
      return name;
    }

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public Method[] getMethods() {
      return methods;
    }

    public Method getRandomEnabledMethod() {
      List<Method> enabledMethods =
          Arrays.stream(methods).filter(Method::isEnabled).collect(Collectors.toList());
      if (enabledMethods.isEmpty()) {
        return null;
      }
      int index = ThreadLocalRandom.current().nextInt(enabledMethods.size());
      return enabledMethods.get(index);
    }

    @Override
    public String toString() {
      return (enabled ? "✓ " : "✗ ") + name;
    }
  }

  class Method {
    private String name;
    private boolean enabled;
    private Runnable action;

    public Method(String name, boolean enabled, Runnable action) {
      this.name = name;
      this.enabled = enabled;
      this.action = action;
    }

    public String getName() {
      return name;
    }

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public void performAction() {
      if (action != null) {
        action.run();
      }
    }
  }

  class AIOAIOConfig {
    private final Map<String, Skill> skillMap = new HashMap<>();

    public List<Skill> skills = new ArrayList<>();

    public AIOAIOConfig() {
      skills.add(
          new Skill(
              "Woodcutting",
              true,
              new Method[] {
                new Method("normal", true, this::dummyAction),
                new Method("oak", true, this::dummyAction),
                new Method("willow", true, this::dummyAction)
              }));
      skills.add(
          new Skill("Fishing", true, new Method[] {new Method("Shrimp", true, this::dummyAction)}));
      skills.add(
          new Skill(
              "Agility",
              true,
              new Method[] {new Method("Tree Gnome Village", true, this::dummyAction)}));
    }

    private void dummyAction() {
      System.out.println("Performing method action");
    }

    public Skill getRandomEnabledSkill() {
      List<Skill> enabledSkills =
          skillMap.values().stream().filter(Skill::isEnabled).collect(Collectors.toList());
      int index = ThreadLocalRandom.current().nextInt(enabledSkills.size());
      return enabledSkills.get(index);
    }

    public void toggleSkillEnabled(String skillName) {
      Skill skill = skillMap.get(skillName);
      if (skill != null) {
        skill.setEnabled(!skill.isEnabled());
      }
    }

    public boolean isSkillEnabled(String skillName) {
      Skill skill = skillMap.get(skillName);
      return skill != null && skill.isEnabled();
    }

    public void toggleMethodEnabled(Skill skill, String methodName, boolean enabled) {
      for (Method method : skill.getMethods()) {
        if (method.getName().equals(methodName)) {
          method.setEnabled(enabled);
          break;
        }
      }
    }

    public boolean isMethodEnabled(Skill skill, String methodName) {
      for (Method method : skill.getMethods()) {
        if (method.getName().equals(methodName)) {
          return method.isEnabled();
        }
      }
      return false;
    }
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

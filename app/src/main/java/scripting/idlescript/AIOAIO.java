package scripting.idlescript;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

public class AIOAIO extends IdleScript {
  private static JFrame scriptFrame = null;
  private static JList<String> skillList;
  private static DefaultListModel<String> skillListModel;
  private static JPanel methodsPanel;
  private static JButton startButton, enableDisableSkillButton;
  private static JTextArea parameterTextArea;
  private static final String[] skills = {"Woodcutting", "Fishing", "Agility"};
  private static final String[][] methods = {
    {"normal", "oak", "willow"}, // Woodcutting methods
    {"Shrimp"}, // Fishing methods
    {"Tree Gnome Village"} // Agility methods
  };
  private AIOAIOConfig botConfig = new AIOAIOConfig();

  private boolean guiSetup = false;

  public int start(String[] parameters) {
    if (!guiSetup) {
      guiSetup = true;
      setupGUI();
    }
    return 1000;
  }

  private void setupGUI() {
    scriptFrame = new JFrame("Runescape Classic AIO Bot");
    scriptFrame.setLayout(new BorderLayout());

    skillListModel = new DefaultListModel<>();
    updateSkillListModel();
    skillList = new JList<>(skillListModel);
    skillList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    skillList.addListSelectionListener(this::skillSelected);

    methodsPanel = new JPanel();
    methodsPanel.setLayout(new BoxLayout(methodsPanel, BoxLayout.Y_AXIS));

    enableDisableSkillButton = new JButton();
    enableDisableSkillButton.addActionListener(this::toggleSkillEnabled);

    startButton = new JButton("Start");
    startButton.addActionListener(this::startButtonClicked);

    scriptFrame.add(new JScrollPane(skillList), BorderLayout.WEST);
    scriptFrame.add(methodsPanel, BorderLayout.CENTER);
    scriptFrame.add(startButton, BorderLayout.SOUTH);

    scriptFrame.pack();
    scriptFrame.setLocationRelativeTo(null);
    scriptFrame.setVisible(true);
    scriptFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    updateCommandText(); // Initially display the config in the parameterTextArea
    updateMethodsPanel(); // Initial update to reflect default state
  }

  private void skillSelected(ListSelectionEvent e) {
    if (!e.getValueIsAdjusting()) {
      updateMethodsPanel();
      updateEnableDisableButtonText();
    }
  }

  private void toggleSkillEnabled(ActionEvent e) {
    int selectedIndex = skillList.getSelectedIndex(); // Store selected index
    String selectedSkill = skillList.getSelectedValue().replaceAll("✓ |✗ ", "");
    botConfig.toggleSkillEnabled(selectedSkill);
    updateSkillListModel();
    skillList.setSelectedIndex(selectedIndex); // Set selection back to stored index
    updateMethodsPanel();
    updateEnableDisableButtonText();
    updateCommandText();
  }

  private void updateMethodsPanel() {
    methodsPanel.removeAll();
    final String[] selectedSkill = {skillList.getSelectedValue()};
    if (selectedSkill[0] != null) {
      selectedSkill[0] = selectedSkill[0].replaceAll("✓ |✗ ", "");
      int skillIndex = List.of(skills).indexOf(selectedSkill[0]);
      JLabel skillTitleLabel = new JLabel("Methods for " + selectedSkill[0]);
      methodsPanel.add(skillTitleLabel); // Adding title for skill
      for (String method : methods[skillIndex]) {
        JCheckBox checkBox =
            new JCheckBox(method, botConfig.isMethodEnabled(selectedSkill[0], method));
        checkBox.addActionListener(
            e -> {
              botConfig.toggleMethodEnabled(selectedSkill[0], method, checkBox.isSelected());
              SwingUtilities.invokeLater(
                  this::updateCommandText); // Update command text after toggle
            });
        methodsPanel.add(checkBox);
      }
    }
    methodsPanel.revalidate();
    methodsPanel.repaint();
  }

  private void updateSkillListModel() {
    skillListModel.clear();
    JLabel skillsTitleLabel = new JLabel("Skills");
    skillListModel.addElement(skillsTitleLabel.getText()); // Adding title for skills
    for (String skill : skills) {
      boolean isEnabled = botConfig.isSkillEnabled(skill);
      skillListModel.addElement((isEnabled ? "✓ " : "✗ ") + skill);
    }
  }

  private void updateEnableDisableButtonText() {
    if (!skillList.isSelectionEmpty()) {
      String selectedSkill = skillList.getSelectedValue().replaceAll("✓ |✗ ", "");
      boolean isEnabled = botConfig.isSkillEnabled(selectedSkill);
      enableDisableSkillButton.setText(isEnabled ? "Disable" : "Enable");
      methodsPanel.add(enableDisableSkillButton);
    }
  }

  private void startButtonClicked(ActionEvent e) {
    scriptFrame.dispose();
    // Start bot task based on botConfig
  }

  private void updateCommandText() {
    parameterTextArea.setText(botConfig.toString());
    System.out.println("Set the text to " + botConfig.toString());
  }

  static class AIOAIOConfig {
    private final Map<String, Boolean> skillEnabled = new HashMap<>();
    private final Map<String, List<String>> enabledMethods = new HashMap<>();

    public AIOAIOConfig() {
      for (String skill : skills) {
        skillEnabled.put(skill, true); // Initialize all skills as enabled
        enabledMethods.put(skill, new ArrayList<>()); // Initialize all methods as disabled
      }
    }

    void toggleSkillEnabled(String skill) {
      skillEnabled.put(skill, !skillEnabled.getOrDefault(skill, true));
    }

    boolean isSkillEnabled(String skill) {
      return skillEnabled.getOrDefault(skill, true);
    }

    void toggleMethodEnabled(String skill, String method, boolean enabled) {
      List<String> methods = enabledMethods.get(skill);
      if (enabled) {
        methods.add(method);
      } else {
        methods.remove(method);
      }
    }

    boolean isMethodEnabled(String skill, String method) {
      return enabledMethods.get(skill).contains(method);
    }
  }
}

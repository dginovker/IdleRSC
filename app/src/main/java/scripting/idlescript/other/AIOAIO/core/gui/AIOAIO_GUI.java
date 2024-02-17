package scripting.idlescript.other.AIOAIO.core.gui;

import bot.Main;
import java.awt.*;
import javax.swing.*;
import scripting.idlescript.other.AIOAIO.AIOAIO;
import scripting.idlescript.other.AIOAIO.core.AIOAIO_Skill;
import scripting.idlescript.other.AIOAIO.core.AIOAIO_Task;

public class AIOAIO_GUI {
  static JFrame scriptFrame = null;
  static JList<AIOAIO_Skill> skillList;
  static DefaultListModel<AIOAIO_Skill> skillListModel;
  static JPanel methodsPanel;
  static JButton startButton, enableDisableSkillButton;

  public static void setupGUI() {
    scriptFrame = new JFrame("Runescape Classic AIO Bot v" + AIOAIO.VERSION);
    scriptFrame.setLayout(new BorderLayout());
    skillListModel = new DefaultListModel<>();
    AIOAIO.state.botConfig.skills.forEach(skillListModel::addElement);
    skillList = new JList<>(skillListModel);
    skillList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    skillList.addListSelectionListener(
        e -> {
          if (!e.getValueIsAdjusting()) {
            updateMethodsPanel();
          }
        });
    methodsPanel = new JPanel();
    methodsPanel.setLayout(new BoxLayout(methodsPanel, BoxLayout.Y_AXIS));
    startButton = new JButton("Start");
    startButton.addActionListener(
        e -> {
          scriptFrame.dispose();
          AIOAIO.state.botConfig.saveConfig();
          AIOAIO.state.startPressed = true;
          Main.getController().log("AIO AIO starting!");
        });
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

  private static JPanel createSectionWithTitle(String title, Component component) {
    JPanel sectionPanel = new JPanel(new BorderLayout());
    JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
    sectionPanel.add(titleLabel, BorderLayout.NORTH);
    sectionPanel.add(component, BorderLayout.CENTER);
    return sectionPanel;
  }

  static void updateMethodsPanel() {
    methodsPanel.removeAll();
    AIOAIO_Skill selectedSkill = skillList.getSelectedValue();
    if (selectedSkill == null) return;

    for (AIOAIO_Task method : selectedSkill.getTasks()) {
      JCheckBox checkBox = new JCheckBox(method.getName(), method.isEnabled());
      checkBox.addActionListener(e -> method.setEnabled(checkBox.isSelected()));
      methodsPanel.add(checkBox);
    }

    enableDisableSkillButton = new JButton(selectedSkill.isEnabled() ? "Disable" : "Enable");
    enableDisableSkillButton.addActionListener(
        e -> {
          if (selectedSkill != null) {
            selectedSkill.setEnabled(!selectedSkill.isEnabled());
            skillListModel.setElementAt(selectedSkill, skillList.getSelectedIndex());
            updateMethodsPanel();
          }
        });
    methodsPanel.add(enableDisableSkillButton);
    methodsPanel.revalidate();
    methodsPanel.repaint();
  }
}

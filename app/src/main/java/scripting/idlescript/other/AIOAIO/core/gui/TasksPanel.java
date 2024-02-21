package scripting.idlescript.other.AIOAIO.core.gui;

import java.awt.Dimension;
import javax.swing.*;
import scripting.idlescript.other.AIOAIO.core.AIOAIO_Skill;
import scripting.idlescript.other.AIOAIO.core.AIOAIO_Task;

public class TasksPanel extends JPanel {
  private JList<AIOAIO_Skill> skillList;
  private DefaultListModel<AIOAIO_Skill> skillListModel;
  private JButton enableDisableSkillButton;
  private JPanel scrollablePanel; // Panel to hold tasks, making them scrollable

  public TasksPanel(JList<AIOAIO_Skill> skillList, DefaultListModel<AIOAIO_Skill> skillListModel) {
    this.skillList = skillList;
    this.skillListModel = skillListModel;
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setPreferredSize(new Dimension(200, 100));

    enableDisableSkillButton = new JButton();
    enableDisableSkillButton.setAlignmentX(LEFT_ALIGNMENT);
    add(enableDisableSkillButton);

    // Initialize scrollablePanel and wrap it in a JScrollPane
    scrollablePanel = new JPanel();
    scrollablePanel.setLayout(new BoxLayout(scrollablePanel, BoxLayout.Y_AXIS));
    JScrollPane scrollPane = new JScrollPane(scrollablePanel);
    scrollPane.setPreferredSize(new Dimension(200, 100));
    scrollPane.setAlignmentX(LEFT_ALIGNMENT);
    add(scrollPane); // Add the JScrollPane to the TasksPanel

    updatePanel();
  }

  public void updatePanel() {
    scrollablePanel.removeAll(); // Clear previous task checkboxes

    AIOAIO_Skill selectedSkill = skillList.getSelectedValue();
    if (selectedSkill != null) {
      // Update the button text and action listener
      enableDisableSkillButton.setText(selectedSkill.isEnabled() ? "Disable" : "Enable");
      enableDisableSkillButton.addActionListener(
          e -> {
            if (selectedSkill != null) {
              selectedSkill.setEnabled(!selectedSkill.isEnabled());
              skillListModel.setElementAt(selectedSkill, skillList.getSelectedIndex());
              updatePanel(); // Recursively update the panel to reflect changes
            }
          });

      for (AIOAIO_Task task : selectedSkill.getTasks()) {
        JCheckBox checkBox = new JCheckBox(task.getName(), task.isEnabled());
        checkBox.setAlignmentX(LEFT_ALIGNMENT);
        checkBox.addActionListener(
            e -> {
              task.setEnabled(checkBox.isSelected());
              updatePanel(); // Optionally update the panel if you want immediate visual feedback
            });
        scrollablePanel.add(checkBox); // Add checkboxes to scrollablePanel instead
      }
    }

    scrollablePanel.revalidate();
    scrollablePanel.repaint();
  }
}

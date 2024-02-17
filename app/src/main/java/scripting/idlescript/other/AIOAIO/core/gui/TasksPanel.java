package scripting.idlescript.other.AIOAIO.core.gui;

import java.awt.Dimension;
import javax.swing.*;
import scripting.idlescript.other.AIOAIO.core.AIOAIO_Skill;
import scripting.idlescript.other.AIOAIO.core.AIOAIO_Task;

public class TasksPanel extends JPanel {
  private JList<AIOAIO_Skill> skillList;
  private DefaultListModel<AIOAIO_Skill> skillListModel;
  private JButton enableDisableSkillButton;

  public TasksPanel(JList<AIOAIO_Skill> skillList, DefaultListModel<AIOAIO_Skill> skillListModel) {
    this.skillList = skillList;
    this.skillListModel = skillListModel;
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setPreferredSize(new Dimension(200, 100));
    updatePanel();
  }

  public void updatePanel() {
    removeAll();
    AIOAIO_Skill selectedSkill = skillList.getSelectedValue();
    if (selectedSkill == null) return;

    for (AIOAIO_Task task : selectedSkill.getTasks()) {
      JCheckBox checkBox = new JCheckBox(task.getName(), task.isEnabled());
      checkBox.addActionListener(e -> task.setEnabled(checkBox.isSelected()));
      add(checkBox);
    }

    enableDisableSkillButton = new JButton(selectedSkill.isEnabled() ? "Disable" : "Enable");
    enableDisableSkillButton.addActionListener(
        e -> {
          if (selectedSkill != null) {
            selectedSkill.setEnabled(!selectedSkill.isEnabled());
            skillListModel.setElementAt(selectedSkill, skillList.getSelectedIndex());
            updatePanel();
          }
        });
    add(enableDisableSkillButton);
    revalidate();
    repaint();
  }
}

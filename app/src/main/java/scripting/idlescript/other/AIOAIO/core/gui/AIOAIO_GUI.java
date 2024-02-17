package scripting.idlescript.other.AIOAIO.core.gui;

import bot.Main;
import java.awt.*;
import javax.swing.*;
import scripting.idlescript.other.AIOAIO.AIOAIO;
import scripting.idlescript.other.AIOAIO.core.AIOAIO_Skill;

public class AIOAIO_GUI {
  static JFrame scriptFrame = null;
  static JList<AIOAIO_Skill> skillList;
  static DefaultListModel<AIOAIO_Skill> skillListModel;
  static TasksPanel tasksPanel; // Use the new TasksPanel class
  static JButton startButton;

  public static void setupGUI() {
    scriptFrame = new JFrame("AIO AIO v" + AIOAIO.VERSION);
    scriptFrame.setLayout(new BorderLayout());
    skillListModel = new DefaultListModel<>();
    AIOAIO.state.botConfig.skills.forEach(skillListModel::addElement);
    skillList = new JList<>(skillListModel);
    skillList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    skillList.addListSelectionListener(
        e -> {
          if (!e.getValueIsAdjusting()) {
            tasksPanel.updatePanel(); // Use the updatePanel method from TasksPanel
          }
        });
    tasksPanel = new TasksPanel(skillList, skillListModel);
    startButton = new JButton("Start");
    startButton.addActionListener(
        e -> {
          scriptFrame.dispose();
          AIOAIO.state.botConfig.saveConfig();
          AIOAIO.state.startPressed = true;
          Main.getController().log("AIO AIO starting!");
        });
    JPanel skillsPanel = createSectionWithTitle("Skills", new JScrollPane(skillList));
    scriptFrame.add(skillsPanel, BorderLayout.WEST);
    scriptFrame.add(createSectionWithTitle("Tasks", tasksPanel), BorderLayout.CENTER);
    scriptFrame.add(startButton, BorderLayout.SOUTH);
    scriptFrame.pack();
    scriptFrame.setLocationRelativeTo(null);
    scriptFrame.setVisible(true);
    scriptFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  private static JPanel createSectionWithTitle(String title, Component component) {
    JPanel sectionPanel = new JPanel(new BorderLayout());
    JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
    sectionPanel.add(titleLabel, BorderLayout.NORTH);
    sectionPanel.add(component, BorderLayout.CENTER);
    return sectionPanel;
  }
}

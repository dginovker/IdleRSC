package scripting.idlescript.other.AIOAIO;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

class AIOAIO_Skill {
  private String name;
  private boolean enabled;
  private List<AIOAIO_Task> tasks;

  public AIOAIO_Skill(String name, boolean enabled, List<AIOAIO_Task> tasks) {
    this.name = name;
    this.enabled = enabled;
    this.tasks = tasks;
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

  public List<AIOAIO_Task> getTasks() {
    return tasks;
  }

  public void setTasks(List<AIOAIO_Task> methods) {
    this.tasks = methods;
  }

  public AIOAIO_Task getRandomEnabledMethod() {
    List<AIOAIO_Task> enabledMethods =
        tasks.stream().filter(AIOAIO_Task::isEnabled).collect(Collectors.toList());
    int index = ThreadLocalRandom.current().nextInt(enabledMethods.size());
    return enabledMethods.get(index);
  }

  @Override
  public String toString() {
    return (enabled ? "✓ " : "✗ ") + name;
  }
}

package scripting.idlescript.other.AIOAIO;

import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

class AIOAIOConfig {
  public List<AIOAIOSkill> skills = new ArrayList<>();

  public AIOAIOConfig() {
    skills.add(
        new AIOAIOSkill(
            "Woodcutting",
            true,
            Arrays.asList(
                new AIOAIO_Method("normal", true, this::dummyAction),
                new AIOAIO_Method("oak", true, this::dummyAction),
                new AIOAIO_Method("willow", true, this::dummyAction))));
    skills.add(
        new AIOAIOSkill(
            "Fishing",
            true,
            Collections.singletonList(new AIOAIO_Method("Shrimp", true, this::dummyAction))));
    skills.add(
        new AIOAIOSkill(
            "Agility",
            true,
            Collections.singletonList(
                new AIOAIO_Method("Tree Gnome Village", true, this::dummyAction))));
  }

  private void dummyAction() {
    System.out.println("Performing method action");
  }

  public AIOAIOSkill getRandomEnabledSkill() {
    List<AIOAIOSkill> enabledSkills =
        skills.stream().filter(AIOAIOSkill::isEnabled).collect(Collectors.toList());
    if (enabledSkills.isEmpty()) {
      return null;
    }
    int index = ThreadLocalRandom.current().nextInt(enabledSkills.size());
    return enabledSkills.get(index);
  }
}

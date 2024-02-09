package scripting.idlescript.other.AIOAIO;

import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import scripting.idlescript.other.AIOAIO.woodcutting.Normal;

class AIOAIOConfig {
  public List<AIOAIO_Skill> skills = new ArrayList<>();

  public AIOAIOConfig() {
    skills.add(
        new AIOAIO_Skill(
            "Woodcutting",
            true,
            Arrays.asList(
                new AIOAIO_Method("normal", true, Normal::run),
                new AIOAIO_Method("oak", true, this::dummyAction),
                new AIOAIO_Method("willow", true, this::dummyAction))));
    skills.add(
        new AIOAIO_Skill(
            "Fishing",
            true,
            Collections.singletonList(new AIOAIO_Method("Shrimp", true, this::dummyAction))));
    skills.add(
        new AIOAIO_Skill(
            "Agility",
            true,
            Collections.singletonList(
                new AIOAIO_Method("Tree Gnome Village", true, this::dummyAction))));
  }

  private int dummyAction(AIOAIO_State state) {
    System.out.println("Todo - Dummy action!");
    return 100;
  }

  public AIOAIO_Skill getRandomEnabledSkill() {
    List<AIOAIO_Skill> enabledSkills =
        skills.stream().filter(AIOAIO_Skill::isEnabled).collect(Collectors.toList());
    if (enabledSkills.isEmpty()) {
      return null;
    }
    int index = ThreadLocalRandom.current().nextInt(enabledSkills.size());
    return enabledSkills.get(index);
  }
}

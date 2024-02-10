package scripting.idlescript.other.AIOAIO;

import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import scripting.idlescript.other.AIOAIO.fishing.Fish;
import scripting.idlescript.other.AIOAIO.woodcutting.Woodcut;

class AIOAIOConfig {
  public List<AIOAIO_Skill> skills = new ArrayList<>();

  public AIOAIOConfig() {
    skills.add(
        new AIOAIO_Skill(
            "Woodcutting",
            false,
            Arrays.asList(
                new AIOAIO_Method("normal", true, Woodcut::run),
                new AIOAIO_Method("oak", true, Woodcut::run),
                new AIOAIO_Method("willow", true, Woodcut::run))));
    skills.add(
        new AIOAIO_Skill(
            "Fishing",
            true,
            Collections.singletonList(new AIOAIO_Method("Shrimp", true, Fish::run))));
    skills.add(
        new AIOAIO_Skill(
            "Agility",
            false,
            Collections.singletonList(
                new AIOAIO_Method("Tree Gnome Village", true, this::dummyAction))));
  }

  private int dummyAction() {
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
